package edu.gatech.arktos.testcases;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;


import edu.gatech.arktos.Constants;
import edu.gatech.arktos.GradesDB;
import edu.gatech.arktos.Session;
import edu.gatech.arktos.Student;
import edu.gatech.arktos.Assignment;

public class GradesDBTest2 extends TestCase {
    private Session session = null;

    GradesDB db = null;

    @Override
    protected void setUp() throws Exception {
        session = new Session();
        session.login(Constants.USERNAME, Constants.PASSWORD);
        db = session.getDBByName(Constants.GRADES_DB);
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        session.logout();
        super.tearDown();
    }

    // ============================================================

    // Assignment Tests

    public void testGetAssignmentByNumber() {
        Assignment assignment = null;
        try {
            assignment = db.getAssignmentByNumber(1);
        } catch (Exception e) {
            fail("Exception");
        }
        assertNotNull(assignment);
        assertTrue(assignment.getAssignmentDescription()
                .compareTo("swiki page") == 0);
    }

    public void testGetAssignments() {
        List<Assignment> assignments = null;
        try {
            assignments = db.getAssignments();
        } catch (Exception e) {
            fail("Exception");
        }
        assertNotNull(assignments);
        assertEquals(3, assignments.size());
        boolean found = true;
        for (Assignment a : assignments) {
            if (a.getAssignmentDescription().compareTo("junit and coverage") == 0
                    && a.getAssignmentNumber() == 3) {
                found = true;
                break;
            }
        }
        assertTrue(found);
        assertEquals(3, assignments.size());
    }

    public void testAssignmentScore() {
        try {
            Assignment a = db.getAssignmentByNumber(3);
            Map<String, Integer> marks = a.getStudentList();
            assertEquals(40, marks.get("Shevon Wise").intValue());
        } catch (Exception e) {
            fail("Exception while getting assignment number 3");
        }
    }


    // Student Tests

    public void testAttendance() {
        try {
            Student student = db.getStudentByName("Grier Nehling");
            // student.getAttendance() should return a rounded of integer
            // that gives the percentage of attendance.
            assertEquals(96, student.getAttendance());
        } catch (Exception e) {
            fail("Exception");
        }
    }

    public void testName() {
        try {
            Student student = db.getStudentByID("901234503");
            assertEquals("Grier Nehling", student.getName());
        } catch (Exception e) {
            fail("Exception");
        }
    }

    public void testGtid() {
        try {
            Student student = db.getStudentByName("Grier Nehling");
            assertEquals("901234503", student.getGtid());
        } catch (Exception e) {
            fail("Exception");
        }
    }

    public void testEmail() {
        try {
            Student student = db.getStudentByName("Grier Nehling");
            assertEquals("gn@gatech.edu", student.getEmail());
        } catch (Exception e) {
            fail("Exception");
        }
    }

    public void testCheckAll() {
        try {
            Student student = db.getStudentByName("Grier Nehling");
            assertEquals("901234503", student.getGtid());
            assertEquals(96, student.getAttendance());
            assertEquals("Grier Nehling", student.getName());
            assertEquals("gn@gatech.edu", student.getEmail());
        } catch (Exception e) {
            fail("Exception");
        }
    }

    // FileSave Tests

    public void testFileSave() {
        try {
            Student student = db.getStudentByID("901234510");
            student.saveInfoToFile();
            String text = student.readInfoFromFile();
            assertTrue(text.contains("Ernesta Anderson")
                    && text.contains("901234510")
                    && text.contains("ea@gatech.edu") && text.contains("86")
                    && text.contains("102") && text.contains("96")
                    && text.contains("93"));
        } catch (Exception e) {
            fail("Exception");
        }
    }

    // ============================================================

}
