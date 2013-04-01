package edu.gatech.arktos.testcases;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for edu.gatech.cc.arktos.testcases");
		//$JUnit-BEGIN$
		suite.addTestSuite(SessionTest.class);
		suite.addTestSuite(GradesDBTest.class);
		suite.addTestSuite(AssignmentsTest.class);
		suite.addTestSuite(ProjectTest.class);
		suite.addTestSuite(TeamTest.class);
		suite.addTestSuite(StudentAssignmentTest.class);
		suite.addTestSuite(StudentProjectTest.class);
		suite.addTestSuite(GradesDBTest2.class);
		suite.addTestSuite(UITest.class);
		//$JUnit-END$
		return suite;
	}

}
