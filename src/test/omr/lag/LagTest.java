//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                                         L a g T e s t                                          //
//                                                                                                //
//------------------------------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//  Copyright © Herve Bitteur and others 2000-2016. All rights reserved.
//  This software is released under the GNU General Public License.
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.
//------------------------------------------------------------------------------------------------//
// </editor-fold>
package omr.lag;

import omr.run.RunTable;

import omr.util.BaseTestCase;

/**
 * Class <code>LagTest</code> gathers some basic tests to exercise unitary
 * Lag features.
 */
public class LagTest
        extends BaseTestCase
{
    //~ Instance fields ----------------------------------------------------------------------------

    // Lags and RunTable instances
    Lag vLag;

    RunTable vTable;

    Lag hLag;

    RunTable hTable;

    //~ Methods ------------------------------------------------------------------------------------
//    //------------------------//
//    // testCreateSectionNoRun //
//    //------------------------//
//    public void testCreateSectionNoRun ()
//    {
//        try {
//            Section s = hLag.createSection(123, null);
//            fail(
//                    "IllegalArgumentException should be raised"
//                    + " when creating section with a null run");
//        } catch (IllegalArgumentException expected) {
//            checkException(expected);
//        }
//    }
//
//    //-------------------------------//
//    // testGetRectangleCendroidEmpty //
//    //-------------------------------//
//    public void testGetRectangleCendroidEmpty ()
//    {
//        Section s2 = hLag.createSection(180, createRun(hTable, 180, 100, 10));
//
//        Rectangle roi = new Rectangle(0, 0, 20, 20);
//        Point pt = s2.getRectangleCentroid(roi);
//        System.out.println("roi=" + roi + " pt=" + pt);
//        assertNull("External roi should give a null centroid", pt);
//    }
//
//    //------------------------------//
//    // testGetRectangleCendroidHori //
//    //------------------------------//
//    public void testGetRectangleCendroidHori ()
//    {
//        int p = 180;
//        Section s = hLag.createSection(180, createRun(hTable, p++, 100, 10));
//        s.append(createRun(hTable, p++, 102, 20));
//        s.append(createRun(hTable, p++, 102, 20));
//        s.append(createRun(hTable, p++, 102, 20));
//        s.drawAscii();
//
//        Rectangle roi = new Rectangle(100, 180, 1, 1);
//        Point pt = s.getRectangleCentroid(roi);
//        System.out.println("roi=" + roi + " pt=" + pt);
//
//        Point expected = new Point(100, 180);
//        assertEquals("Wrong pt", expected, pt);
//
//        roi = new Rectangle(102, 178, 3, 5);
//        pt = s.getRectangleCentroid(roi);
//        System.out.println("roi=" + roi + " pt=" + pt);
//
//        expected = new Point(103, 181);
//        assertEquals("Wrong pt", expected, pt);
//    }
//
//    //------------------------------//
//    // testGetRectangleCendroidNull //
//    //------------------------------//
//    public void testGetRectangleCendroidNull ()
//    {
//        Section s2 = hLag.createSection(180, createRun(hTable, 180, 100, 10));
//
//        try {
//            Rectangle roi = null;
//            System.out.println("roi=" + roi);
//
//            Point pt = s2.getRectangleCentroid(roi);
//            fail(
//                    "IllegalArgumentException should be raised"
//                    + " when rectangle of interest is null");
//        } catch (IllegalArgumentException expected) {
//            checkException(expected);
//        }
//    }
//
//    //------------------------------//
//    // testGetRectangleCendroidVert //
//    //------------------------------//
//    public void testGetRectangleCendroidVert ()
//    {
//        int p = 50;
//        Section s = vLag.createSection(p, createRun(vTable, p++, 100, 10));
//        s.append(createRun(vTable, p++, 102, 20));
//        s.append(createRun(vTable, p++, 102, 20));
//        s.append(createRun(vTable, p++, 102, 20));
//        s.drawAscii();
//
//        Rectangle roi = new Rectangle(48, 102, 5, 3);
//        Point pt = s.getRectangleCentroid(roi);
//        System.out.println("roi=" + roi + " pt=" + pt);
//
//        Point expected = new Point(51, 103);
//        assertEquals("Wrong pt", expected, pt);
//    }
//
//    //    /**
//    //     * Test of getRunAt method, of class Lag.
//    //     */
//    //    @Test
//    //    public void testGetRunAt ()
//    //    {
//    //        System.out.println("getRunAt");
//    //
//    //        int x = 0;
//    //        int y = 0;
//    //        Lag instance = new LagImpl();
//    //        Run expResult = null;
//    //        Run result = instance.getRunAt(x, y);
//    //        assertEquals(expResult, result);
//    //        fail("The test case is a prototype.");
//    //    }
//    //    /**
//    //     * Test of getRunService method, of class Lag.
//    //     */
//    //    @Test
//    //    public void testGetRunService ()
//    //    {
//    //        System.out.println("getRunService");
//    //
//    //        Lag              instance = new LagImpl();
//    //        SelectionService expResult = null;
//    //        SelectionService result = instance.getRunService();
//    //        assertEquals(expResult, result);
//    //        fail("The test case is a prototype.");
//    //    }
//    //
//    //    /**
//    //     * Test of getRunTable method, of class Lag.
//    //     */
//    //    @Test
//    //    public void testGetRuns ()
//    //    {
//    //        System.out.println("getRunTable");
//    //
//    //        Lag       instance = new LagImpl();
//    //        RunTable expResult = null;
//    //        RunTable result = instance.getRunTable();
//    //        assertEquals(expResult, result);
//    //        fail("The test case is a prototype.");
//    //    }
//    //
//    //    /**
//    //     * Test of getEntityService method, of class Lag.
//    //     */
//    //    @Test
//    //    public void testGetSectionService ()
//    //    {
//    //        System.out.println("getEntityService");
//    //
//    //        Lag              instance = new LagImpl();
//    //        SelectionService expResult = null;
//    //        SelectionService result = instance.getEntityService();
//    //        assertEquals(expResult, result);
//    //        fail("The test case is a prototype.");
//    //    }
//    //-----------------//
//    // testGetSections //
//    //-----------------//
//    public void testGetSections ()
//    {
//        Section s2 = vLag.createSection(180, new Run(100, 10));
//        s2.append(new Run(101, 20));
//
//        Section s3 = vLag.createSection(180, new Run(100, 10));
//        s3.append(new Run(101, 20));
//
//        List<Section> sections = new ArrayList<Section>();
//        sections.add(s2);
//        sections.add(s3);
//        Collections.sort(sections, Section.idComparator);
//
//        List<Section> lagSections = new ArrayList<Section>(vLag.getAllEntities());
//        Collections.sort(lagSections, Section.idComparator);
//        assertEquals("Retrieved sections.", sections, lagSections);
//    }
//
//    //    /**
//    //     * Test of getSelectedSection method, of class Lag.
//    //     */
//    //    @Test
//    //    public void testGetSelectedSection ()
//    //    {
//    //        System.out.println("getSelectedSection");
//    //
//    //        Lag     instance = new LagImpl();
//    //        Section expResult = null;
//    //        Section result = instance.getSelectedSection();
//    //        assertEquals(expResult, result);
//    //        fail("The test case is a prototype.");
//    //    }
//    //
//    //    /**
//    //     * Test of getSelectedSectionSet method, of class Lag.
//    //     */
//    //    @Test
//    //    public void testGetSelectedSectionSet ()
//    //    {
//    //        System.out.println("getSelectedSectionSet");
//    //
//    //        Lag instance = new LagImpl();
//    //        Set expResult = null;
//    //        Set result = instance.getSelectedSectionSet();
//    //        assertEquals(expResult, result);
//    //        fail("The test case is a prototype.");
//    //    }
//    //----------//
//    // testHLag //
//    //----------//
//    public void testHLag ()
//    {
//        assertNotNull("Lag hLag was not allocated", hLag);
//        assertFalse("hLag is not vertical", hLag.getOrientation().isVertical());
//    }
//
//    //--------------//
//    // testHSection //
//    //--------------//
//    public void testHSection ()
//    {
//        // Test of horizontal section
//        Section s1 = hLag.createSection(180, new Run(100, 10));
//        s1.append(new Run(101, 20));
//        hLag.dump(null);
//        dump("s1 dump:", s1);
//        commonAssertions(s1);
//        assertEquals("Bad ContourBox", s1.getBounds(), new Rectangle(100, 180, 21, 2));
//    }
//
//    //---------------//
//    // testHabsolute //
//    //---------------//
//    public void testHabsolute ()
//    {
//        Point cp = new Point(12, 34);
//        Point xy = hLag.getOrientation().absolute(cp);
//        assertEquals("Non expected switch.", cp, xy);
//    }
//
//    //---------------//
//    // testHoriented //
//    //---------------//
//    public void testHoriented ()
//    {
//        Point xy = new Point(12, 34);
//        Point cp = hLag.getOrientation().oriented(xy);
//        assertEquals("Non expected switch.", cp, xy);
//    }
//
//    //-----------------------------------//
//    // testLookupIntersectedSectionsHori //
//    //-----------------------------------//
//    public void testLookupIntersectedSectionsHori ()
//    {
//        Section s2 = hLag.createSection(180, new Run(100, 10));
//        s2.append(new Run(101, 20));
//
//        Section s3 = hLag.createSection(200, new Run(150, 10));
//        s3.append(new Run(161, 20));
//        s3.append(new Run(170, 15));
//
//        Set<Section> founds = null;
//
//        founds = hLag.intersectedSections(new Rectangle(0, 0, 0, 0));
//        assertEquals("No section.", 0, founds.size());
//
//        founds = hLag.intersectedSections(new Rectangle(100, 180, 1, 1));
//        assertEquals("One section.", 1, founds.size());
//
//        founds = hLag.intersectedSections(new Rectangle(0, 180, 200, 21));
//        assertEquals("Two sections.", 2, founds.size());
//    }
//
//    //------------------------//
//    // testLookupSectionsHori //
//    //------------------------//
//    public void testLookupSectionsHori ()
//    {
//        Section s2 = hLag.createSection(180, new Run(100, 10));
//        s2.append(new Run(101, 20));
//
//        Section s3 = hLag.createSection(200, new Run(150, 10));
//        s3.append(new Run(161, 20));
//        s3.append(new Run(170, 15));
//
//        Set<Section> founds = null;
//
//        founds = hLag.containedSections(new Rectangle(0, 0, 0, 0));
//        assertEquals("No section.", 0, founds.size());
//
//        founds = hLag.containedSections(new Rectangle(100, 180, 21, 2));
//        assertEquals("One section.", 1, founds.size());
//
//        founds = hLag.containedSections(new Rectangle(100, 180, 85, 23));
//        assertEquals("Two sections.", 2, founds.size());
//    }
//
//    //------------------------//
//    // testLookupSectionsVert //
//    //------------------------//
//    public void testLookupSectionsVert ()
//    {
//        Section s2 = vLag.createSection(180, new Run(100, 10));
//        s2.append(new Run(101, 20));
//
//        Section s3 = vLag.createSection(200, new Run(150, 10));
//        s3.append(new Run(161, 20));
//        s3.append(new Run(170, 15));
//
//        Set<Section> founds = null;
//
//        founds = vLag.containedSections(new Rectangle(0, 0, 0, 0));
//        assertEquals("No section.", 0, founds.size());
//
//        founds = vLag.containedSections(new Rectangle(180, 100, 2, 21));
//        assertEquals("One section.", 1, founds.size());
//
//        founds = vLag.containedSections(new Rectangle(180, 100, 23, 85));
//        assertEquals("Two sections.", 2, founds.size());
//    }
//
//    //    /**
//    //     * Test of purgeSections method, of class Lag.
//    //     */
//    //    @Test
//    //    public void testPurgeSections ()
//    //    {
//    //        System.out.println("purgeSections");
//    //
//    //        Predicate<Section> predicate = null;
//    //        Lag                instance = new LagImpl();
//    //        List               expResult = null;
//    //        List               result = instance.purgeSections(predicate);
//    //        assertEquals(expResult, result);
//    //        fail("The test case is a prototype.");
//    //    }
//    //
//    //    /**
//    //     * Test of setRuns method, of class Lag.
//    //     */
//    //    @Test
//    //    public void testSetRuns ()
//    //    {
//    //        System.out.println("setRuns");
//    //
//    //        RunTable runsTable = null;
//    //        Lag       instance = new LagImpl();
//    //        instance.setRuns(runsTable);
//    //        fail("The test case is a prototype.");
//    //    }
//    //
//    //    /**
//    //     * Test of setServices method, of class Lag.
//    //     */
//    //    @Test
//    //    public void testSetServices ()
//    //    {
//    //        System.out.println("setServices");
//    //
//    //        SelectionService locationService = null;
//    //        SelectionService sceneService = null;
//    //        Lag              instance = new LagImpl();
//    //        instance.setServices(locationService, sceneService);
//    //        fail("The test case is a prototype.");
//    //    }
//    //---------------//
//    // testTranslate //
//    //---------------//
//    public void testTranslate ()
//    {
//        Section s1 = vLag.createSection(1, createRun(vTable, 1, 2, 5));
//        s1.append(createRun(vTable, 2, 0, 3));
//        s1.append(createRun(vTable, 3, 1, 2));
//        s1.append(createRun(vTable, 4, 1, 1));
//        s1.append(createRun(vTable, 5, 1, 6));
//        dump("Before translation", s1);
//
//        Point vector = new Point(10, 2);
//        s1.translate(vector);
//        dump("After translation", s1);
//    }
//
//    //----------//
//    // testVLag //
//    //----------//
//    public void testVLag ()
//    {
//        assertNotNull("Lag vLag was not allocated", vLag);
//        assertTrue("vLag is vertical", vLag.getOrientation().isVertical());
//    }
//
//    //--------------//
//    // testVSection //
//    //--------------//
//    public void testVSection ()
//    {
//        // Test of vertical sections
//        Section s2 = vLag.createSection(180, new Run(100, 10));
//        s2.append(new Run(101, 20));
//        vLag.dump(null);
//        dump("s2 dump:", s2);
//        commonAssertions(s2);
//        assertEquals("Bad ContourBox", s2.getBounds(), new Rectangle(180, 100, 2, 21));
//
//        Section s3 = vLag.createSection(180, new Run(100, 10));
//        s3.append(new Run(101, 20));
//
//        // And an edge between 2 sections (of the same lag)
//        s2.addTarget(s3);
//    }
//
//    //---------------//
//    // testVabsolute //
//    //---------------//
//    public void testVabsolute ()
//    {
//        Point cp = new Point(12, 34);
//        Point xy = vLag.getOrientation().absolute(cp);
//        assertEquals("Expected switch.", new Point(cp.y, cp.x), xy);
//    }
//
//    //---------------//
//    // testVoriented //
//    //---------------//
//    public void testVoriented ()
//    {
//        Point xy = new Point(12, 34);
//        Point cp = vLag.getOrientation().absolute(xy);
//        assertEquals("Expected switch.", new Point(cp.y, cp.x), xy);
//    }
//
//    //-------//
//    // setUp //
//    //-------//
//    @Override
//    protected void setUp ()
//    {
//        vLag = new BasicLag("My Vertical Lag", Orientation.VERTICAL);
//        vTable = new RunTable(Orientation.VERTICAL, 100, 200); // Absolute
//        vLag.setRuns(vTable);
//
//        hLag = new BasicLag("My Horizontal Lag", Orientation.HORIZONTAL);
//        hTable = new RunTable(Orientation.HORIZONTAL, 100, 200); // Absolute
//        hLag.setRuns(hTable);
//    }
//
//    //------------------//
//    // commonAssertions //
//    //------------------//
//    private void commonAssertions (Section s)
//    {
//        Orientation ori = s.getGraph().getOrientation();
//        assertEquals("Bad Bounds", s.getOrientedBounds(), new Rectangle(100, 180, 21, 2));
//        assertEquals("Bad Centroid", s.getCentroid(), ori.absolute(new Point(109, 180)));
//
//        //        assertTrue("Bad Containment", s.contains(100, 180));
//        //        assertFalse("Bad Containment", s.contains(100, 181));
//        //        assertTrue("Bad Containment", s.contains(101, 181));
//        //        assertFalse("Bad Containment", s.contains(110, 180));
//        //        assertFalse("Bad Containment", s.contains(121, 181));
//        Point pt;
//        pt = ori.absolute(new Point(100, 180));
//        assertTrue("Bad Containment", s.contains(pt.x, pt.y));
//
//        pt = ori.absolute(new Point(100, 181));
//        assertFalse("Bad Containment", s.contains(pt.x, pt.y));
//
//        pt = ori.absolute(new Point(101, 181));
//        assertTrue("Bad Containment", s.contains(pt.x, pt.y));
//
//        pt = ori.absolute(new Point(110, 180));
//        assertFalse("Bad Containment", s.contains(pt.x, pt.y));
//
//        pt = ori.absolute(new Point(121, 181));
//        assertFalse("Bad Containment", s.contains(pt.x, pt.y));
//
//        assertEquals("Bad FirstPos", s.getFirstPos(), 180);
//        assertEquals("Bad LastPos", s.getLastPos(), 181);
//        assertEquals("Bad MaxRunLength", s.getMaxRunLength(), 20);
//        assertEquals("Bad MeanRunLength", s.getMeanRunLength(), 15);
//        assertEquals("Bad RunNb", s.getRunCount(), 2);
//        assertEquals("Bad Start", s.getStartCoord(), 100);
//        assertEquals("Bad Stop", s.getStopCoord(), 120);
//        assertEquals("Bad Weight", s.getWeight(), 30);
//    }
//
//    //-----------//
//    // createRun //
//    //-----------//
//    private Run createRun (RunTable table,
//                           int alignment,
//                           int start,
//                           int length)
//    {
//        Run run = new Run(start, length);
//
//        table.addRun(alignment, run);
//
//        return run;
//    }
//
//    //------//
//    // dump //
//    //------//
//    private void dump (String title,
//                       Section section)
//    {
//        if (title != null) {
//            System.out.println();
//            System.out.println(title);
//        }
//
//        System.out.println(section.toString());
//
//        //        System.out.println ("getRunAtPos(0)=" + section.getRunAtPos(0));
//        System.out.println("getOrientedBounds=" + section.getOrientedBounds());
//        System.out.println("getCentroid=" + section.getCentroid());
//        System.out.println("getContourBox=" + section.getBounds());
//        System.out.println("getFirstPos=" + section.getFirstPos());
//        System.out.println("getFirstRun=" + section.getFirstRun());
//        System.out.println("getLastPos=" + section.getLastPos());
//        System.out.println("getLastRun=" + section.getLastRun());
//        System.out.println("getMaxRunLength=" + section.getMaxRunLength());
//        System.out.println("getMeanRunLength=" + section.getMeanRunLength());
//        System.out.println("getRunNb=" + section.getRunCount());
//        System.out.println("getStart=" + section.getStartCoord());
//        System.out.println("getStop=" + section.getStopCoord());
//        System.out.println("getWeight=" + section.getWeight());
//        System.out.println("getContour=");
//        dump(section.getPolygon());
//        //section.getLag().dump("\nLag dump:");
//        section.drawAscii();
//    }
//
//    //------//
//    // dump //
//    //------//
//    private void dump (Polygon polygon)
//    {
//        for (int i = 0; i < polygon.npoints; i++) {
//            System.out.println(i + ": x" + polygon.xpoints[i] + ",y" + polygon.ypoints[i]);
//        }
//    }
//
//    //------//
//    // dump //
//    //------//
//    private void dump (int[] ints,
//                       String title)
//    {
//        System.out.println("\n" + title);
//
//        for (int i = 0; i < ints.length; i++) {
//            System.out.print(i + ":");
//
//            for (int k = 0; k < ints[i]; k++) {
//                System.out.print("x");
//            }
//
//            System.out.println();
//        }
//    }
}
