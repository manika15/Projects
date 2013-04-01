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

public class StudentProjectTest extends TestCase{

	private Session session = null;
	GradesDB db = null;
	Student student = null;
	String studentName = "Shevon Wise";
	
	ArrayList<StudentProject> studentProjects= null;
	
	protected void setUp() throws Exception {
		session = new Session();
		session.login(Constants.USERNAME, Constants.PASSWORD);
		db = session.getDBByName(Constants.GRADES_DB);
		student = db.getStudentByName(studentName);
		studentProjects = student.getInvolvedProjects();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		session.logout();
		super.tearDown();
	}
	

	// Test student "Shevon Wise" have finished three projects
	public void testStudentInvolvedProjectsNum() {
		try {
			Student student = db.getStudentByName("Shevon Wise");
			ArrayList<StudentProject> studentProjects = student.getInvolvedProjects();
			assertEquals(3, studentProjects.size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
    // Test student "Shevon Wise"'s team grade for P1 is 93
	public void testStudentTeamGrade() {
		boolean found = false;
		StudentProject p = null;
		try{
			Student student = db.getStudentByName("Shevon Wise");
			ArrayList<StudentProject> studentProjects = student.getInvolvedProjects();
			for(StudentProject studentProject: studentProjects){
				if(studentProject.getProjectName().equals("P1") && studentProject.getTeamName().equals("Team 1"))
				{
					found = true;
					p = studentProject;
				}
			}
		}catch(Exception e){
			fail(e.toString());
		}
		
        assertTrue(found);
		assertEquals(93,p.getTeamGrade());
	}
	
	//Test project P1 average grade is 93
	public void testAverageProjectGrade(){
		boolean found = false;
		StudentProject p = null;
		try{
			Student student = db.getStudentByName("Shevon Wise");
			ArrayList<StudentProject> studentProjects = student.getInvolvedProjects();
			for(StudentProject studentProject: studentProjects){
				if(studentProject.getProjectName().equals("P1") && studentProject.getTeamName().equals("Team 1"))
				{
					found = true;
					p = studentProject;
				}
			}
		}catch(Exception e){
			fail(e.toString());
		}
		
        assertTrue(found);
		assertEquals(93,p.getAverageGrade());
	}
	
	//Test student's individual evaluation is correct
	public void testAverageContribution(){
		boolean found = false;
		StudentProject p = null;
		try{
			Student student = db.getStudentByName("Shevon Wise");
			ArrayList<StudentProject> studentProjects = student.getInvolvedProjects();
			for(StudentProject studentProject: studentProjects){
				if(studentProject.getProjectName().equals("P2") && studentProject.getTeamName().equals("Team 3"))
				{
					found = true;
					p = studentProject;
				}
			}
		}catch(Exception e){
			fail(e.toString());
		}
		
        assertTrue(found);
		assertEquals("7.50",p.getIndEvaluation());
	}	

	// Test getStudentByID can also get the correct project information for a
	// student
	public void testProjectByStudentID() {
		try {
			Student student = db.getStudentByID("901234504");
			ArrayList<StudentProject> studentProjects = student
					.getInvolvedProjects();

			StudentProject p = null;
			boolean found = false;
			for (StudentProject studentProject : studentProjects) {
				if (studentProject.getProjectName().equals("P3")) {
					found = true;
					p = studentProject;
					break;
				}

			}

			assertTrue(found);
			assertEquals(99, p.getAverageGrade());
			assertEquals(96, p.getTeamGrade());
			assertEquals("8.25", p.getIndEvaluation());
		} catch (Exception e) {
			fail(e.toString());
		}

	}
	
	// Test getStudents can also get the correct project information for a
	// student
	public void testProjectByStudents() {
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
			ArrayList<StudentProject> studentProjects = student
					.getInvolvedProjects();

			StudentProject p = null;
			found = false;
			for (StudentProject studentProject : studentProjects) {
				if (studentProject.getProjectName().equals("P3")) {
					found = true;
					p = studentProject;
					break;
				}

			}

			assertTrue(found);
			assertEquals(99, p.getAverageGrade());
			assertEquals(100, p.getTeamGrade());
			assertEquals("9.00", p.getIndEvaluation());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
}
