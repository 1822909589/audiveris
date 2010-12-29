//----------------------------------------------------------------------------//
//                                                                            //
//                            S h e e t B e n c h                             //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright (C) Herve Bitteur 2000-2010. All rights reserved.               //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.sheet;

import omr.log.Logger;

import omr.score.Score;

import omr.step.Step;

import java.util.*;

/**
 * Class {@code SheetBench} records all important information related to the
 * processing of a music sheet.
 *
 * <p>It delegates the actual recording to the containing score bench,
 * just prefixing the records with the sheet index</p>
 *
 * @author Hervé Bitteur
 */
public class SheetBench
    extends Bench
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = Logger.getLogger(SheetBench.class);

    /** Special key which indicates that an interruption has occurred */
    private static final String INTERRUPTION_KEY = "whole.interrupted";

    //~ Instance fields --------------------------------------------------------

    /** The related sheet */
    private final Sheet sheet;

    /** The prefix to use for this sheet */
    private final String sheetPrefix;

    /** The related score */
    private final Score score;

    /** Time stamp when this instance was created */
    private final long startTime = System.currentTimeMillis();

    /** Time when we started current step */
    private long stepStartTime = startTime;

    /** Starting date */
    private final Date date = new Date(startTime);

    //~ Constructors -----------------------------------------------------------

    //------------//
    // SheetBench //
    //------------//
    /**
     * Creates a new SheetBench object.
     * @param sheet the related sheet
     */
    public SheetBench (Sheet sheet)
    {
        this.sheet = sheet;

        sheetPrefix = String.format("p%02d.", sheet.getPage().getIndex());
        score = sheet.getScore();

        addProp(
            "image",
            score.getImagePath() + "#" + sheet.getPage().getIndex());

        flushBench();
    }

    //~ Methods ----------------------------------------------------------------

    //----------//
    // getSheet //
    //----------//
    /**
     * @return the sheet
     */
    public Sheet getSheet ()
    {
        return sheet;
    }

    //--------------------//
    // recordCancellation //
    //--------------------//
    public void recordCancellation ()
    {
        addProp("whole.cancelled", "true");
    }

    //----------------------//
    // recordImageDimension //
    //----------------------//
    public void recordImageDimension (int width,
                                      int height)
    {
        addProp("image.width", "" + width);
        addProp("image.height", "" + height);
    }

    //-----------------//
    // recordPartCount //
    //-----------------//
    public void recordPartCount (int partCount)
    {
        addProp("parts", "" + partCount);
    }

    //-------------//
    // recordScale //
    //-------------//
    public void recordScale (Scale scale)
    {
        addProp("scale.mainBack", "" + scale.mainBack());
        addProp("scale.mainFore", "" + scale.mainFore());
        addProp("scale.interline", "" + scale.interline());
    }

    //------------//
    // recordSkew //
    //------------//
    public void recordSkew (double skew)
    {
        addProp("skew", "" + skew);
    }

    //------------------//
    // recordStaveCount //
    //------------------//
    public void recordStaveCount (int staveCount)
    {
        addProp("staves", "" + staveCount);
    }

    //------------//
    // recordStep //
    //------------//
    public void recordStep (Step step)
    {
        long now = System.currentTimeMillis();
        addProp(
            "step." + step.getName().toLowerCase() + ".duration",
            "" + (now - stepStartTime));
        stepStartTime = now;
        flushBench();
    }

    //-------------------//
    // recordSystemCount //
    //-------------------//
    public void recordSystemCount (int systemCount)
    {
        addProp("systems", "" + systemCount);
    }

    //---------//
    // addProp //
    //---------//
    /**
     * Redirect to the score bench, with the sheet prefix
     * @param radix the provided radix
     * @param value the property value
     */
    @Override
    protected final void addProp (String radix,
                                  String value)
    {
        score.getBench()
             .addProp(sheetPrefix + radix, value);
    }

    //------------//
    // flushBench //
    //------------//
    /**
     * Flush the score container
     */
    protected final void flushBench ()
    {
        score.getBench()
             .flushBench();
    }
}