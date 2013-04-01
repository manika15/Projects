package edu.gatech.arktos.testcases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.gatech.arktos.Constants;
import edu.gatech.arktos.GradesDB;
import edu.gatech.arktos.Session;
import edu.gatech.arktos.Student;
import edu.gatech.arktos.StudentAssignment;
import edu.gatech.arktos.Assignment;
import junit.framework.TestCase;

public class AssignmentsTest extends TestCase {
	private Session session = null;
	GradesDB db = null;

	protected void setUp() throws Exception {
		session = new Session();
		session.login(Constants.USERNAME, Constants.PASSWORD);
		db = session.getDBByName(Constants.GRADES_DB);
		super.setUp();
	}

	protected void tearDown() throws Exception {
		session.logout();
		super.tearDown();
	}

	// Test assignment number is correct
	public void testAssignmentNum() {
		ArrayList<Assignment> assignments = null;
		try {
			assignments = db.getAssignments();
		} catch (Exception e) {
			fail(e.toString());
		}
		assertEquals(3, assignments.size());
	}

	// Test assignment "Assignment 1" is in the assignments list
	public void testAssinment1() {
		ArrayList<Assignment> assignments = null;
		try {
			assignments = db.getAssignments();
			boolean found = false;
			for (Assignment assignment : assignments) {
				if (assignment.getAssignmentNumber() == 1) {
					found = true;
					break;
				}
			}
			assertTrue(found);

		} catch (Exception e) {
			fail(e.toString());
		}
	}

	// Test average grade for each assignment is correct
	public void testAssignmentGrade() {
		try {
			Assignment assignment = db.getAssignmentByNumber(1);
			assertNotNull(assignment);
			assertEquals(99, assignment.getAverageGrade());
			
			assignment = db.getAssignmentByNumber(2);
			assertNotNull(assignment);
			assertEquals(100, assignment.getAverageGrade());
			
			assignment = db.getAssignmentByNumber(3);
			assertNotNull(assignment);
			assertEquals(77, assignment.getAverageGrade());
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	// Test student list for each assignment is correct.
	public void testStudents() {
		try {
			HashMap hm = new HashMap();
			Assignment assignment = db.getAssignmentByNumber(1);
			assertNotNull(assignment);
			hm = assignment.getStudentList();
			assertEquals(100, hm.get("Freddie Catlay"));
			assertEquals(100, hm.get("Grier Nehling"));
			assertEquals(90, hm.get("Christine Schaeffer"));
			
			assignment = db.getAssignmentByNumber(2);
			assertNotNull(assignment);
			hm = assignment.getStudentList();
			assertEquals(95, hm.get("Freddie Catlay"));
			assertEquals(95, hm.get("Grier Nehling"));
			assertEquals(90, hm.get("Christine Schaeffer"));
			
			assignment = db.getAssignmentByNumber(3);
			assertNotNull(assignment);
			hm = assignment.getStudentList();
			assertEquals(75, hm.get("Freddie Catlay"));
			assertEquals(85, hm.get("Grier Nehling"));
			assertEquals(100, hm.get("Christine Schaeffer"));
		} catch (Exception e) {
			fail(e.toString());
		}

	}
}
