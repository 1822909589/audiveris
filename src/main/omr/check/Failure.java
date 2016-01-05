//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                                          F a i l u r e                                         //
//                                                                                                //
//------------------------------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//  Copyright © Hervé Bitteur and others 2000-2016. All rights reserved.
//  This software is released under the GNU General Public License.
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.
//------------------------------------------------------------------------------------------------//
// </editor-fold>
package omr.check;

/**
 * Class {@code Failure} is the root of all results that store a failure.
 *
 * @author Hervé Bitteur
 */
public class Failure
{
    //~ Instance fields ----------------------------------------------------------------------------

    /**
     * A readable comment about the failure.
     */
    public final String comment;

    //~ Constructors -------------------------------------------------------------------------------
    /**
     * Create a new Failure object.
     *
     * @param comment A comment that describe the failure reason
     */
    public Failure (String comment)
    {
        this.comment = comment;
    }

    //~ Methods ------------------------------------------------------------------------------------
    //----------//
    // toString //
    //----------//
    /**
     * Report a readable string about this failure instance
     *
     * @return a descriptive string
     */
    @Override
    public String toString ()
    {
        return "failure:" + comment;
    }
}
