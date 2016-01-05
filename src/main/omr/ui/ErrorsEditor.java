//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                                    E r r o r s E d i t o r                                     //
//                                                                                                //
//------------------------------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//  Copyright © Hervé Bitteur and others 2000-2016. All rights reserved.
//  This software is released under the GNU General Public License.
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.
//------------------------------------------------------------------------------------------------//
// </editor-fold>
package omr.ui;

import omr.glyph.Glyph;

import omr.sheet.Sheet;

import omr.step.Step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Class {@code ErrorsEditor} handles the set of error messages
 * recorded during the translation from sheet to score, allowing the
 * user to interactively browse the errors and go to the related
 * location in the sheet view.
 *
 * @author Hervé Bitteur
 */
public class ErrorsEditor
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Logger logger = LoggerFactory.getLogger(ErrorsEditor.class);

    //~ Instance fields ----------------------------------------------------------------------------
    /** Related sheet */
    private final Sheet sheet;

    /** The list of displayed errors */
    private final JList<Record> list;

    /** The scrolling area */
    private final JScrollPane scrollPane;

    /** Selection listener */
    private final ListSelectionListener listener = new MyListener();

    /** Set of error records */
    private final SortedSet<Record> recordSet = new TreeSet<Record>();

    /** Facade model for the JList */
    private final DefaultListModel<Record> model = new DefaultListModel<Record>();

    //~ Constructors -------------------------------------------------------------------------------
    /**
     * Create an instance of ErrorsEditor (one per sheet / score).
     *
     * @param sheet the related sheet
     */
    public ErrorsEditor (Sheet sheet)
    {
        this.sheet = sheet;
        list = new JList<Record>(model);
        scrollPane = new JScrollPane(list);
        scrollPane.setBorder(null);
        list.addListSelectionListener(listener);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    //~ Methods ------------------------------------------------------------------------------------
    //    //----------//
    //    // addError //
    //    //----------//
    //    /**
    //     * Record an error within a SystemNode entity.
    //     *
    //     * @param node the node in the score hierarchy
    //     * @param text the message text
    //     */
    //    public void addError (OldSystemNode node,
    //                          String text)
    //    {
    //        addError(node, null, text);
    //    }
    //
    //    //----------//
    //    // addError //
    //    //----------//
    //    /**
    //     * Record an error within a SystemNode entity, and related to a
    //     * specific glyph.
    //     *
    //     * @param node  the containing node in score hierarchy
    //     * @param glyph the related glyph
    //     * @param text  the message text
    //     */
    //    public void addError (final OldSystemNode node,
    //                          final Glyph glyph,
    //                          final String text)
    //    {
    //        final Step step = getCurrentStep();
    //
    //        SwingUtilities.invokeLater(
    //                new Runnable()
    //                {
    //                    // This part is run on swing thread
    //                    @Override
    //                    public void run ()
    //                    {
    //                        if (recordSet.add(new Record(step, node, glyph, text))) {
    //                            // Update the model
    //                            model.removeAllElements();
    //
    //                            for (Record record : recordSet) {
    //                                model.addElement(record);
    //                            }
    //                        }
    //                    }
    //                });
    //    }
    //
    //    //-------//
    //    // clear //
    //    //-------//
    //    /**
    //     * Remove all errors from the editor. (Not used?)
    //     */
    //    public void clear ()
    //    {
    //        SwingUtilities.invokeLater(
    //                new Runnable()
    //                {
    //                    // This part is run on swing thread
    //                    @Override
    //                    public void run ()
    //                    {
    //                        recordSet.clear();
    //                        model.removeAllElements();
    //                    }
    //                });
    //    }
    //-----------//
    // clearStep //
    //-----------//
    /**
     * Clear all messages related to the provided step.
     *
     * @param step the step we are interested in
     */
    public void clearStep (final Step step)
    {
//            SwingUtilities.invokeLater(
//                    new Runnable()
//                    {
//                        // This part is run on swing thread
//                        @Override
//                        public void run ()
//                        {
//                            logger.debug("Clearing errors for {}", step);
//
//                            for (Iterator<Record> it = recordSet.iterator(); it.hasNext();) {
//                                Record record = it.next();
//
//                                if (record.step == step) {
//                                    it.remove();
//                                }
//                            }
//
//                            // Update the model
//                            model.removeAllElements();
//
//                            for (Record record : recordSet) {
//                                model.addElement(record);
//                            }
//                        }
//                    });
    }

        //-------------//
    // clearSystem //
    //-------------//
    /**
     * Clear all messages related to the provided system id.
     * (we use system id rather than system, since a system may be reallocated
     * by SystemsBuilder)
     *
     * @param step     the step we are interested in
     * @param systemId the id of system to clear
     */
    public void clearSystem (final Step step,
                             final int systemId)
    {
//            SwingUtilities.invokeLater(
//                    new Runnable()
//                    {
//                        // This part is run on swing thread
//                        @Override
//                        public void run ()
//                        {
//                            logger.debug("Clearing errors for {} system {}", step, systemId);
//
//                            for (Iterator<Record> it = recordSet.iterator(); it.hasNext();) {
//                                Record record = it.next();
//
//                                if ((record.step == step)
//                                    && (record.node.getSystem().getId() == systemId)) {
//                                    it.remove();
//                                }
//                            }
//
//                            // Update the model
//                            model.removeAllElements();
//
//                            for (Record record : recordSet) {
//                                model.addElement(record);
//                            }
//                        }
//                    });
    }

    //--------------//
    // getComponent //
    //--------------//
    /**
     * Give access to the real component.
     *
     * @return the concrete component
     */
    public JComponent getComponent ()
    {
        return scrollPane;
    }

    //~ Inner Classes ------------------------------------------------------------------------------
    //
    //    //----------------//
    //    // getCurrentStep //
    //    //----------------//
    //    /**
    //     * Retrieve the step being performed on the sheet.
    //     * Beware, during SCORE step and following steps, just the first sheet has a current step
    //     * assigned.
    //     *
    //     * @return the step being done
    //     */
    //    private Step getCurrentStep ()
    //    {
    //        return sheet.getCurrentStep();
    //    }
    //
    //------------//
    // MyListener //
    //------------//
    /**
     * A specific listener to handle user selection in the list of
     * errors.
     */
    private class MyListener
            implements ListSelectionListener
    {
        //~ Methods --------------------------------------------------------------------------------

        @Override
        public void valueChanged (ListSelectionEvent e)
        {
            //            if ((e.getSource() == list) && !e.getValueIsAdjusting()) {
            //                Record record = list.getSelectedValue();
            //
            //                if (record != null) {
            //                    logger.debug("value={}", record);
            //
            //                    // Use glyph location if available
            //                    if (record.glyph != null) {
            //                        sheet.getGlyphNest().getGlyphService().publish(
            //                                new GlyphEvent(this, SelectionHint.GLYPH_INIT, null, record.glyph));
            //                    } else {
            //                        // Otherwise use node location as possible
            //                        try {
            //                            Point pixPt = null;
            //
            //                            try {
            //                                pixPt = record.node.getCenter();
            //                            } catch (Exception ex) {
            //                            }
            //
            //                            if (pixPt == null) {
            //                                if (record.node instanceof OldMeasureNode) {
            //                                    OldMeasureNode mn = (OldMeasureNode) record.node;
            //                                    OldMeasure measure = mn.getMeasure();
            //
            //                                    if (measure != null) {
            //                                        pixPt = measure.getCenter();
            //                                    }
            //                                }
            //                            }
            //
            //                            sheet.getLocationService().publish(
            //                                    new LocationEvent(
            //                                            ErrorsEditor.this,
            //                                            SelectionHint.LOCATION_INIT,
            //                                            null,
            //                                            new Rectangle(pixPt)));
            //                        } catch (Exception ex) {
            //                            logger.warn("Failed pointing to " + record.node, ex);
            //                        }
            //                    }
            //                }
            //            }
        }
    }

    //--------//
    // Record //
    //--------//
    /**
     * A structure to hold the various pieces of an error message.
     */
    private static class Record
            implements Comparable<Record>
    {
        //~ Instance fields ------------------------------------------------------------------------

        final Step step;

        //
        //        final OldSystemNode node;
        //
        final Glyph glyph;

        final String text;

        //~ Constructors ---------------------------------------------------------------------------
        public Record (Step step,
                       //                       OldSystemNode node,
                       Glyph glyph,
                       String text)
        {
            this.step = step;
            //            this.node = node;
            this.glyph = glyph;
            this.text = text;
        }

        //~ Methods --------------------------------------------------------------------------------
        @Override
        public int compareTo (Record other)
        {
            // Very basic indeed !!!
            return toString().compareTo(other.toString());
        }

        @Override
        public String toString ()
        {
            StringBuilder sb = new StringBuilder();

            //            sb.append(node.getContextString());
            if (glyph != null) {
                sb.append(" [").append(glyph.idString()).append("]");
            }

            if (step != null) {
                sb.append(" ").append(step);
            }

            sb.append(" ").append(text);

            return sb.toString();
        }
    }
}
