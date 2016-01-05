//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                                         D e b u g                                              //
//                                                                                                //
//------------------------------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//  Copyright © Hervé Bitteur and others 2000-2016. All rights reserved.
//  This software is released under the GNU General Public License.
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.
//------------------------------------------------------------------------------------------------//
// </editor-fold>
package omr;

import omr.classifier.Sample;
import omr.classifier.SampleRepository;
import omr.classifier.ShapeDescription;

import omr.glyph.Shape;
import omr.glyph.ShapeSet;

import omr.image.TemplateFactory;

import omr.sheet.Picture;
import omr.sheet.SheetStub;
import omr.sheet.ui.StubDependent;
import omr.sheet.ui.StubsController;

import org.jdesktop.application.Action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;

/**
 * Convenient class meant to temporarily inject some debugging.
 * To be used in sync with file user-actions.xml in config folder
 *
 * @author Hervé Bitteur
 */
public class Debug
        extends StubDependent
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Logger logger = LoggerFactory.getLogger(Debug.class);

    //~ Methods ------------------------------------------------------------------------------------
    //--------------//
    // checkSources //
    //--------------//
    /**
     * Check which sources are still in Picture cache.
     *
     * @param e unused
     */
    @Action
    public void checkSources (ActionEvent e)
    {
        SheetStub stub = StubsController.getCurrentStub();

        if (stub != null && stub.hasSheet()) {
            Picture picture = stub.getSheet().getPicture();

            if (picture != null) {
                picture.checkSources();
            }
        }
    }

    //    //------------------//
    //    // injectChordNames //
    //    //------------------//
    //    @Action(enabledProperty = SHEET_AVAILABLE)
    //    public void injectChordNames (ActionEvent e)
    //    {
    //        Score score = ScoreController.getCurrentScore();
    //
    //        if (score == null) {
    //            return;
    //        }
    //
    //        ScoreSystem system = score.getFirstPage()
    //                                  .getFirstSystem();
    //        system.acceptChildren(new ChordInjector());
    //    }
    //    //---------------//
    //    // ChordInjector //
    //    //---------------//
    //    private static class ChordInjector
    //        extends AbstractScoreVisitor
    //    {
    //        //~ Static fields/initializers -----------------------------------------
    //
    //        /** List of symbols to inject. */
    //        private static final String[] shelf = new String[] {
    //                                                  "BMaj7/D#", "BMaj7", "G#m9",
    //                                                  "F#", "C#7sus4", "F#"
    //                                              };
    //
    //        //~ Instance fields ----------------------------------------------------
    //
    //        /** Current index to symbol to inject. */
    //        private int symbolCount = 0;
    //
    //        //~ Methods ------------------------------------------------------------
    //
    //        @Override
    //        public boolean visit (ChordSymbol symbol)
    //        {
    //            // Replace chord info by one taken from the shelf
    //            if (symbolCount < shelf.length) {
    //                symbol.info = ChordInfo.create(shelf[symbolCount++]);
    //            }
    //
    //            return false;
    //        }
    //    }
    //----------------//
    // checkTemplates //
    //----------------//
    /**
     * Generate the templates for all relevant shapes for a range of interline values.
     *
     * @param e unused
     */
    @Action
    public void checkTemplates (ActionEvent e)
    {
        TemplateFactory factory = TemplateFactory.getInstance();

        for (int i = 10; i < 40; i++) {
            logger.info("Catalog for interline {}", i);
            factory.getCatalog(i);
        }

        logger.info("Done.");
    }

    //------------------//
    // saveTrainingData //
    //------------------//
    /**
     * Generate a file (format arff) to be used by Weka machine learning software,
     * with the training data.
     *
     * @param e unused
     */
    @Action
    public void saveTrainingData (ActionEvent e)
    {
        Path path = WellKnowns.EVAL_FOLDER.resolve(
                "samples-" + ShapeDescription.getName() + ".arff");
        final PrintWriter out = getPrintWriter(path);

        out.println("@relation " + "glyphs-" + ShapeDescription.getName());
        out.println();

        for (String label : ShapeDescription.getParameterLabels()) {
            out.println("@attribute " + label + " real");
        }

        // Last attribute: shape
        out.print("@attribute shape {");

        for (Shape shape : ShapeSet.allPhysicalShapes) {
            out.print(shape);

            if (shape != Shape.LAST_PHYSICAL_SHAPE) {
                out.print(", ");
            }
        }

        out.println("}");

        out.println();
        out.println("@data");

        SampleRepository repository = SampleRepository.getInstance();
        List<String> gNames = repository.getWholeBase(null);
        logger.info("Glyphs: {}", gNames.size());

        for (String gName : gNames) {
            Sample sample = repository.getSample(gName, null);

            if (sample != null) {
                double[] ins = ShapeDescription.features(sample, sample.getInterline());

                for (double in : ins) {
                    out.print((float) in);
                    out.print(",");
                }

                out.println(sample.getShape().getPhysicalShape());
            }
        }

        out.flush();
        out.close();
        logger.info("Classifier data saved in " + path.toAbsolutePath());
    }

    //----------------//
    // getPrintWriter //
    //----------------//
    private static PrintWriter getPrintWriter (Path path)
    {
        try {
            final BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(path.toFile()),
                            WellKnowns.FILE_ENCODING));

            return new PrintWriter(bw);
        } catch (Exception ex) {
            System.err.println("Error creating " + path + ex);

            return null;
        }
    }
}
