package edu.gatech.arktos.testcases;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gdata.util.ServiceException;

import edu.gatech.arktos.Constants;
import edu.gatech.arktos.GradesDB;
import edu.gatech.arktos.Session;
import edu.gatech.arktos.Student;
import edu.gatech.arktos.StudentAssignment;
import edu.gatech.arktos.Team;
import junit.framework.TestCase;

public class TeamTest extends TestCase {
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

	// Test team number is correct
	public void testTeamNum() {
		try {
			HashSet<Team> teams = db.getTeamsByProject("P1");
			assertEquals(3, teams.size());
			
			teams = db.getTeamsByProject("P2");
			assertEquals(3, teams.size());
			
			teams = db.getTeamsByProject("P3");
			assertEquals(3, teams.size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	// Test team is correct
	public void testStudentAverage() {
		HashSet<Team> teams;
		try {
			teams = db.getTeamsByProject("P1");
			assertEquals(3, teams.size());

			Team team = db.getTeam("P1", "Team 1");
			assertNotNull(team);
			assertEquals(93, team.getTeamScore());
			HashMap hm = team.getStudentList();
			assertEquals("9.25", hm.get("Freddie Catlay"));
			assertEquals("9.00", hm.get("Ernesta Anderson"));
			assertEquals("7.00", hm.get("Sheree Gadow"));
			
			team = db.getTeam("P1", "Team 2");
			assertNotNull(team);
			assertEquals(96, team.getTeamScore());
			hm = team.getStudentList();
			assertEquals("9.50", hm.get("Josepha Jube"));
			assertEquals("5.50", hm.get("Wilfrid Eastwood"));
			assertEquals("7.25", hm.get("Cynthia Faast"));
			
			team = db.getTeam("P1", "Team 3");
			assertEquals(90, team.getTeamScore());
			assertNotNull(team);
			hm = team.getStudentList();
			assertEquals("7.67", hm.get("Grier Nehling"));
			assertEquals("9.33", hm.get("Rastus Kight"));
		} catch (Exception e) {
			fail(e.toString());
		}

	}

}
