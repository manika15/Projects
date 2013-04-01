package edu.gatech.arktos.testcases;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gdata.util.AuthenticationException;

import edu.gatech.arktos.Constants;
import edu.gatech.arktos.Session;
import junit.framework.TestCase;

public class SessionTest extends TestCase {

	Session session;
	@Before
	protected void setUp() throws Exception {
		session = new Session();
		super.setUp();
	}
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	@Test
	public void testLogin() throws AuthenticationException {
		assertEquals(0, session.login(Constants.USERNAME, Constants.PASSWORD));
		assertEquals(1, session.login(Constants.USERNAME, "wrong password"));
	}
	@Test
	public void testLogout() {
		assertEquals(0, session.logout());
	}

}
