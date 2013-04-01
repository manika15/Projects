package edu.gatech.arktos.testcases;

import java.util.ArrayList;
import java.util.HashSet;

import edu.gatech.arktos.Constants;
import edu.gatech.arktos.GradesDB;
import edu.gatech.arktos.Session;
import edu.gatech.arktos.Student;
import edu.gatech.arktos.StudentAssignment;
import edu.gatech.arktos.StudentProject;
import junit.framework.TestCase;

public class StudentAssignmentTest extends TestCase {

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

	// Test student "Freddie Catlay" have finished three assignments
	public void testStudentInvolvedProjectsNum() {
		try {
			Student student = db.getStudentByName("Freddie Catlay");
			ArrayList<StudentAssignment> studentAssignments = student
					.getInvolvedAssignments();
			assertEquals(3, studentAssignments.size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	// Test student "Freddie Catlay"'s grade for Assignment1 is 100
	public void testStudentGrade() {
		boolean found = false;
		StudentAssignment a = null;
		
		try {
			Student student = db.getStudentByName("Freddie Catlay");
			ArrayList<StudentAssignment> studentAssignments = student
					.getInvolvedAssignments();


			for (StudentAssignment studentAssignment : studentAssignments) {
				if (studentAssignment.getAssignmentName()
						.equals("Assignment 1")) {
					found = true; 
					a = studentAssignment;
				}
			}
		} catch (Exception e) {
			fail(e.toString());
		}
		
		assertTrue(found);
		assertEquals(100, a.getGrade());
	}

	// Test assignment2's average grade is 100
	public void testAssignmentAverageGrade() {
		boolean found = false;
		StudentAssignment a = null;
		try {
			Student student = db.getStudentByName("Freddie Catlay");
			ArrayList<StudentAssignment> studentAssignments = student
					.getInvolvedAssignments();

			for (StudentAssignment studentAssignment : studentAssignments) {
				if (studentAssignment.getAssignmentName()
						.equals("Assignment 2")) {
					found = true; 
					a = studentAssignment;
				}
			}
		} catch (Exception e) {
			fail(e.toString());
		}
		assertTrue(found);
		assertEquals(100, a.getAverageGrade());

	}

	// Test getStudentByID can also get the correct assignment information for a
	// student
	public void testAssignmentByStudentID() {
		try {
			Student student = db.getStudentByID("901234504");
			ArrayList<StudentAssignment> studentAssignments = student
					.getInvolvedAssignments();

			StudentAssignment ass3 = null;
			boolean found = false;
			for (StudentAssignment studentAssignment : studentAssignments) {
				if (studentAssignment.getAssignmentName()
						.equals("Assignment 3")) {
					found = true;
					ass3 = studentAssignment;
					break;
				}

			}

			assertTrue(found);
			assertEquals(77, ass3.getAverageGrade());
			assertEquals(40, ass3.getGrade());
		} catch (Exception e) {
			fail(e.toString());
		}

	}

	// Test getStudents can also get the correct assignment information for a
	// student
	public void testAssignmentByStudents() {
		try {
			HashSet<Student> students = null;
			try {
				students = db.getStudents();
			} catch (Exception e) {
				fail("Exception");
			}
			boolean found = false;
			Student student = null;
			for (Student s : students) {
				if ((s.getName().compareTo("Cynthia Faast") == 0)
						&& (s.getGtid().compareTo("901234514") == 0)) {
					found = true;
					student = s;
					break;
				}
			}
			assertTrue(found);
			ArrayList<StudentAssignment> studentAssignments = student
					.getInvolvedAssignments();
			StudentAssignment ass3 = null;
			found = false;
			for (StudentAssignment studentAssignment : studentAssignments) {
				if (studentAssignment.getAssignmentName()
						.equals("Assignment 3")) {
					found = true;
					ass3 = studentAssignment;
					break;
				}

			}

			assertTrue(found);
			assertEquals(77, ass3.getAverageGrade());
			assertEquals(85, ass3.getGrade());
		} catch (Exception e) {
			fail(e.toString());
		}

	}
}
