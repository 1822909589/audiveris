//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                                F i l t e r D e s c r i p t o r                                 //
//                                                                                                //
//------------------------------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//  Copyright © Hervé Bitteur and others 2000-2016. All rights reserved.
//  This software is released under the GNU General Public License.
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.
//------------------------------------------------------------------------------------------------//
// </editor-fold>
package omr.image;

import omr.constant.ConstantSet;

import omr.util.Param;

import ij.process.ByteProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Management data meant to describe an implementation instance of a PixelFilter.
 * (kind of filter + related parameters)
 */
public abstract class FilterDescriptor
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Constants constants = new Constants();

    private static final Logger logger = LoggerFactory.getLogger(
            FilterDescriptor.class);

    /** Default param. */
    public static final Param<FilterDescriptor> defaultFilter = new Default();

    //~ Methods ------------------------------------------------------------------------------------
    //--------//
    // equals //
    //--------//
    @Override
    public boolean equals (Object obj)
    {
        return (obj instanceof FilterDescriptor);
    }

    //-----------//
    // getFilter //
    //-----------//
    /**
     * Create a filter instance compatible with the descriptor and
     * the underlying pixel source.
     *
     * @param source the underlying pixel source
     * @return the filter instance, ready to use
     */
    public abstract PixelFilter getFilter (ByteProcessor source);

    //
    //---------//
    // getKind //
    //---------//
    /**
     * Report the kind of filter used.
     *
     * @return the filter kind
     */
    public abstract FilterKind getKind ();

    //----------------//
    // getDefaultKind //
    //----------------//
    public static FilterKind getDefaultKind ()
    {
        return constants.defaultKind.getValue();
    }

    //----------//
    // hashCode //
    //----------//
    @Override
    public int hashCode ()
    {
        int hash = 5;

        return hash;
    }

    //----------------//
    // setDefaultKind //
    //----------------//
    public static void setDefaultKind (FilterKind kind)
    {
        constants.defaultKind.setValue(kind);
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("{");
        sb.append(internalsString());
        sb.append('}');

        return sb.toString();
    }

    //-----------------//
    // internalsString //
    //-----------------//
    protected String internalsString ()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getKind());

        return sb.toString();
    }

    //~ Inner Classes ------------------------------------------------------------------------------
    //
    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
            extends ConstantSet
    {
        //~ Instance fields ------------------------------------------------------------------------

        private final FilterKind.Constant defaultKind = new FilterKind.Constant(
                FilterKind.ADAPTIVE,
                "Default kind of PixelFilter");
    }

    //---------//
    // Default //
    //---------//
    private static class Default
            extends Param<FilterDescriptor>
    {
        //~ Methods --------------------------------------------------------------------------------

        @Override
        public FilterDescriptor getSpecific ()
        {
            final String method = "getDefaultDescriptor";

            try {
                FilterKind kind = getDefaultKind();

                // Access the underlying class
                Method getDesc = kind.classe.getMethod(method, (Class[]) null);

                if (Modifier.isStatic(getDesc.getModifiers())) {
                    return (FilterDescriptor) getDesc.invoke(null);
                } else {
                    logger.error(method + " must be static");
                }
            } catch (Throwable ex) {
                logger.warn("Could not call " + method, ex);
            }

            return null;
        }

        @Override
        public boolean setSpecific (FilterDescriptor specific)
        {
            if (!getSpecific().equals(specific)) {
                FilterKind kind = specific.getKind();
                FilterDescriptor.setDefaultKind(kind);

                switch (kind) {
                case GLOBAL:

                    if (specific instanceof GlobalDescriptor) {
                        GlobalDescriptor gDesc = (GlobalDescriptor) specific;
                        GlobalFilter.setDefaultThreshold(gDesc.threshold);
                    } else {
                        logger.error("Wrong class for {} find {}", specific, kind);
                    }

                    break;

                case ADAPTIVE:

                    if (specific instanceof AdaptiveDescriptor) {
                        AdaptiveDescriptor aDesc = (AdaptiveDescriptor) specific;
                        AdaptiveFilter.setDefaultMeanCoeff(aDesc.meanCoeff);
                        AdaptiveFilter.setDefaultStdDevCoeff(aDesc.stdDevCoeff);
                    } else {
                        logger.error("Wrong class for {} find {}", specific, kind);
                    }

                    break;
                }

                logger.info("Default filter is now ''{}''", specific);

                return true;
            }

            return false;
        }
    }
}
