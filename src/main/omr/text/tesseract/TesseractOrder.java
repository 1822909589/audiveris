//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                                  T e s s e r a c t O r d e r                                   //
//                                                                                                //
//------------------------------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//  Copyright © Hervé Bitteur and others 2000-2016. All rights reserved.
//  This software is released under the GNU General Public License.
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.
//------------------------------------------------------------------------------------------------//
// </editor-fold>
package omr.text.tesseract;

import omr.WellKnowns;

import omr.text.FontInfo;
import omr.text.TextChar;
import omr.text.TextLine;
import omr.text.TextWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tesseract.TessBridge;

import tesseract.TessBridge.PIX;
import tesseract.TessBridge.ResultIterator;
import tesseract.TessBridge.ResultIterator.Level;
import tesseract.TessBridge.TessBaseAPI;
import tesseract.TessBridge.TessBaseAPI.SegmentationMode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageOutputStream;

/**
 * Class {@code TesseractOrder} carries a processing order submitted to Tesseract OCR
 * program.
 *
 * @author Hervé Bitteur
 */
public class TesseractOrder
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Logger logger = LoggerFactory.getLogger(TesseractOrder.class);

    /** To avoid repetitive warnings if OCR binding failed */
    private static boolean userWarned;

    static {
        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.registerServiceProvider(
                new com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi());
        registry.registerServiceProvider(
                new com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi());
    }

    //~ Instance fields ----------------------------------------------------------------------------
    /** Serial number for this order. */
    private final int serial;

    /** Image label. */
    private final String label;

    /** Should we keep a disk copy of the image?. */
    private final boolean keepImage;

    /** Language specification. */
    private final String lang;

    /** Desired handling of layout. */
    private final SegmentationMode segMode;

    /** The dedicated API. */
    private TessBaseAPI api;

    /** The image being processed. */
    private final PIX image;

    //~ Constructors -------------------------------------------------------------------------------
    //
    //----------------//
    // TesseractOrder //
    //----------------//
    /**
     * Creates a new TesseractOrder object.
     *
     * @param label         A debugging label (such as sheet name or glyph id)
     * @param serial        A unique id for this order instance
     * @param keepImage     True to keep a disk copy of the image
     * @param lang          The language specification
     * @param segMode       The desired page segmentation mode
     * @param bufferedImage The image to process
     *
     * @throws UnsatisfiedLinkError When bridge to C++ could not be loaded
     * @throws IOException          When temporary Tiff buffer failed
     * @throws RuntimeException     When PIX image failed
     */
    public TesseractOrder (String label,
                           int serial,
                           boolean keepImage,
                           String lang,
                           SegmentationMode segMode,
                           BufferedImage bufferedImage)
            throws UnsatisfiedLinkError, IOException
    {
        this.label = label;
        this.serial = serial;
        this.keepImage = keepImage;
        this.lang = lang;
        this.segMode = segMode;

        // Build a PIX from the image provided
        ByteBuffer buf = toTiffBuffer(bufferedImage);
        image = PIX.readMemTiff(buf, buf.capacity(), 0);

        if (image == null) {
            logger.warn("Invalid image {}", label);
            throw new RuntimeException("Invalid image");
        }
    }

    //~ Methods ------------------------------------------------------------------------------------
    //
    //---------//
    // process //
    //---------//
    /**
     * Actually allocate a Tesseract API and recognize the image.
     *
     * @return the sequence of lines found
     */
    public List<TextLine> process ()
    {
        try {
            api = new TessBaseAPI(WellKnowns.OCR_FOLDER.toString());

            // Init API with proper language
            if (!api.Init(lang)) {
                logger.warn("Could not initialize Tesseract with lang {}", lang);

                return finish(null);
            }

            // Set API image
            api.SetImage(image);

            // Perform layout analysis according to segmentation mode
            api.SetPageSegMode(segMode);
            api.AnalyseLayout();

            // Perform image recognition
            if (api.Recognize() != 0) {
                logger.warn("Error in Tesseract recognize");

                return finish(null);
            }

            // Extract lines
            return finish(getLines());
        } catch (UnsatisfiedLinkError ex) {
            if (!userWarned) {
                logger.warn("Could not link Tesseract bridge", ex);
                logger.warn("java.library.path=" + System.getProperty("java.library.path"));
                userWarned = true;
            }

            throw new RuntimeException(ex);
        }
    }

    //--------//
    // finish //
    //--------//
    /**
     * Convenient way to cleanup Tesseract resources while ending the current processing
     *
     * @param lines the lines found, if any
     * @return the lines found, if nay
     */
    private List<TextLine> finish (List<TextLine> lines)
    {
        if (image != null) {
            PIX.freeData(image);
        }

        if (api != null) {
            api.End();
        }

        return lines;
    }

    //---------//
    // getFont //
    //---------//
    /**
     * Map Tesseract3 font attributes to our own FontInfo class.
     *
     * @param att Font attributes out of OCR, perhaps null
     * @return our FontInfo structure, or null
     */
    private FontInfo getFont (TessBridge.FontAttributes att)
    {
        if (att != null) {
            return new FontInfo(
                    att.isBold,
                    att.isItalic,
                    att.isUnderlined,
                    att.isMonospace,
                    att.isSerif,
                    att.isSmallcaps,
                    att.pointsize,
                    att.fontName);
        } else {
            return null;
        }
    }

    //----------//
    // getLines //
    //----------//
    /**
     * Build the hierarchy of TextLine / TextWord / TextChar instances out of the raw
     * results of OCR recognition.
     *
     * @return the sequence of lines
     */
    private List<TextLine> getLines ()
    {
        ResultIterator it = api.GetIterator();

        List<TextLine> lines = new ArrayList<TextLine>(); // All lines built so far
        TextLine line = null; // The line being built
        TextWord word = null; // The word being built

        try {
            do {
                // SKip empty stuff
                if (it.Empty(Level.SYMBOL)) {
                    continue;
                }

                // Start of line?
                if (it.IsAtBeginningOf(Level.TEXTLINE)) {
                    line = new TextLine();
                    logger.debug("{} {}", label, line);
                    lines.add(line);
                }

                // Start of word?
                if (it.IsAtBeginningOf(Level.WORD)) {
                    FontInfo fontInfo = getFont(it.WordFontAttributes());

                    if (fontInfo == null) {
                        logger.debug("No font info on {}", label);

                        return null;
                    }

                    word = new TextWord(
                            it.BoundingBox(Level.WORD),
                            it.GetUTF8Text(Level.WORD),
                            it.Baseline(Level.WORD),
                            it.Confidence(Level.WORD) / 100.0,
                            fontInfo,
                            line);
                    logger.debug("    {}", word);
                    line.appendWord(word);

                    // // Heuristic... (just to test)
                    // boolean isDict = it.WordIsFromDictionary();
                    // boolean isNumeric = it.WordIsNumeric();
                    // boolean isLatin = encoder.canEncode(wordContent);
                    // int conf = (int) Math.rint(it.Confidence(WORD));
                    // int len = wordContent.length();
                    // boolean isValid = isLatin
                    //         && (conf >= 80
                    //   || (conf >= 50 && ((isDict && len > 1) || isNumeric)));
                }

                // Char/symbol to be processed
                word.addChar(
                        new TextChar(it.BoundingBox(Level.SYMBOL), it.GetUTF8Text(Level.SYMBOL)));
            } while (it.Next(Level.SYMBOL));

            return lines;
        } catch (Exception ex) {
            logger.warn("Error decoding tesseract output", ex);

            return null;
        } finally {
            it.delete();
        }
    }

    //--------------//
    // toTiffBuffer //
    //--------------//
    /**
     * Convert the given image into a TIFF-formatted ByteBuffer for
     * passing it directly to Tesseract.
     * A copy of the tiff buffer can be saved on disk, if so desired.
     *
     * @param image the input image
     * @return a buffer in TIFF format
     */
    private ByteBuffer toTiffBuffer (BufferedImage image)
            throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (final ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("tiff").
                    next();
            writer.setOutput(ios);
            writer.write(image);
        }

        ByteBuffer buf = ByteBuffer.allocate(baos.size());
        byte[] bytes = baos.toByteArray();
        buf.put(bytes);

        // Should we keep a local copy of this buffer on disk?
        if (keepImage) {
            String name = String.format("%03d-", serial) + ((label != null) ? label : "");
            Path path = WellKnowns.TEMP_FOLDER.resolve(name + ".tif");

            // Make sure the TEMP directory exists
            if (!Files.exists(WellKnowns.TEMP_FOLDER)) {
                Files.createDirectories(WellKnowns.TEMP_FOLDER);
            }

            try (final FileOutputStream fos = new FileOutputStream(path.toFile())) {
                fos.write(bytes);
            }
        }

        return buf;
    }
}
