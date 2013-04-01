package edu.gatech.arktos.testcases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.gatech.arktos.Assignment;
import edu.gatech.arktos.Constants;
import edu.gatech.arktos.GradesDB;
import edu.gatech.arktos.Session;
import edu.gatech.arktos.Student;
import edu.gatech.arktos.StudentAssignment;
import edu.gatech.arktos.Project;
import junit.framework.TestCase;

public class ProjectTest extends TestCase
{
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
	
	// Test project number is correct
	public void testProjecttNum() {
		ArrayList<Project> projects = null;
		try {
			projects = db.getProjects();
		} catch (Exception e) {
			fail(e.toString());
		}
		assertEquals(3, projects.size());
	}
	
	// Test project "P1" is in the projects list
	public void testProject1() {
		ArrayList<Project> projects = null;
		try {
			projects = db.getProjects();
			boolean found = false;
			for (Project project : projects) {
				if (project.getProjectName().equals("P1")) {
					found = true;
					break;
				}
			}
			assertTrue(found);

		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	// Test average grade for each project is correct
	public void testProjectAvgGrade()
	{
		ArrayList<Project> projects = null;
		try {
			projects = db.getProjects();
			assertNotNull(projects);
			for (Project project : projects) {
				if(project.getProjectName().equals("P1"))
				{assertEquals(93,project.getAverageGrade());}
				
				else 	if(project.getProjectName().equals("P2"))
				{assertEquals(92,project.getAverageGrade());}
				
				else	if(project.getProjectName().equals("P3"))
				{assertEquals(99,project.getAverageGrade());}
			}
		}catch(Exception e){
			fail(e.toString());
		}
	}
	
	//Test team list for each project is correct
	public void testProjectTeams()
	{	
		ArrayList<Project> projects = null;
	
	try {
		projects = db.getProjects();
		if(projects == null){
			fail("Can't get any project!");
		}
		for (Project project : projects) {
			HashMap hm = new HashMap();
		
			if(project.getProjectName().equals("P1"))
			{
				hm = project.getTeamList();
				assertEquals(93,hm.get("Team 1"));
				assertEquals(96,hm.get("Team 2"));
				assertEquals(90,hm.get("Team 3"));
			}
			
			else 	if(project.getProjectName().equals("P2"))
			{
				hm = project.getTeamList();
				assertEquals(95,hm.get("Team 1"));
				assertEquals(96,hm.get("Team 2"));
				assertEquals(86,hm.get("Team 3"));
			}
			
			else	if(project.getProjectName().equals("P3"))
			{
				hm = project.getTeamList();
				assertEquals(100,hm.get("Team 1"));
				assertEquals(96,hm.get("Team 2"));
				assertEquals(102,hm.get("Team 3"));
			}
		}
	}catch(Exception e){
		fail(e.toString());
	}
	}
	
	
}
