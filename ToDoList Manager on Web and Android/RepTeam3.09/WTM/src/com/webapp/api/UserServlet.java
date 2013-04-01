package com.webapp.api;

import java.io.IOException;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.datanucleus.util.StringUtils;

import com.google.gson.Gson;
import com.webapp.PMF;
import com.webapp.model.Task;
import com.webapp.model.User;

/**
 * This is the servlet that handles all HTTP requests concerning User class
 * @author Pujita
 *
 */
@SuppressWarnings("serial")
public class UserServlet extends HttpServlet{

	PersistenceManager mgr;// = PMF.get().getPersistenceManager();
	final Gson gson = new Gson();


	/**
	 * This uses the HTTP GET request to list out all the users (in a JSON format)  
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		mgr = PMF.get().getPersistenceManager();
			
		String name = req.getParameter("name");
		String password = req.getParameter("encryptedPwd");
		// If both username and password are provided
		if ( !StringUtils.isEmpty(name) && !StringUtils.isEmpty(password) ) {
			User user = ModelController.getUserByName(mgr, name);
			if (user == null) {
				resp.getWriter().println(gson.toJson("User Does Not Exist!"));		    	
				mgr.close();
				return;
			}
			
			if (user.getEncryptedPwd().equals(password)) {
				resp.getWriter().println(gson.toJson(user));
			} else {
				resp.getWriter().println(gson.toJson("Password Does Not Match!"));		    	
			}
			mgr.close();
			return;
		}
		
		// otherwise list all users
		//TODO: Remove this, only for testing purposes 
	    List<User> result = ModelController.getAllUsers(mgr);
        resp.setContentType("application/json");
    	resp.getWriter().println(gson.toJson(result)); 
    	mgr.close();
    }
	
	
	/**
	 * This uses the HTTP POST request to add a new User  
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
				
		mgr = PMF.get().getPersistenceManager();
		User userToAdd = null;
					
		try {
			userToAdd = gson.fromJson(req.getReader(), User.class);
			
			if(userToAdd == null) {
				resp.getWriter().println(gson.toJson("invalid syntax"));
				mgr.close();
				return;
			}
			// Check if user already exists
			User user = ModelController.getUserByName(mgr, userToAdd.getName());
			if (user != null) {
				resp.getWriter().println(gson.toJson("Username already exists!"));
				mgr.close();
				return;
			}
			// If username is available
			mgr.makePersistent(userToAdd);			
	        resp.setContentType("application/json");
			resp.getWriter().println(gson.toJson(userToAdd));
			mgr.close();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	

	/**
	 * This uses the HTTP PUT request to add a new User  
	 */
	public void doPut(HttpServletRequest req, HttpServletResponse resp) {
				
		mgr = PMF.get().getPersistenceManager();
		User userToUpdate = null;
		
		try {
			userToUpdate = gson.fromJson(req.getReader(), User.class);
			
			if(userToUpdate == null) {
				resp.getWriter().println(gson.toJson("invalid syntax"));
				mgr.close();
				return;
			}
			// check if a User Id is provided
			if ( StringUtils.isEmpty(userToUpdate.getEncodedKey()) ) {
				resp.getWriter().println(gson.toJson("Provide userID to update a user"));
				mgr.close();
				return;	
			}
			// get user that is already stored
			try {
				@SuppressWarnings("unused")
				User user = mgr.getObjectById(User.class, userToUpdate.getEncodedKey());
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				resp.getWriter().println(gson.toJson("Invalid user encodedKey "));
				mgr.close();
				return;
			} 
										
			// update User
			mgr.makePersistent(userToUpdate);			
	        resp.setContentType("application/json");
			resp.getWriter().println(gson.toJson(userToUpdate));
			mgr.close();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * This uses the HTTP DELETE request to delete a user and the tasks associated with it
	 */
	public void doDelete(HttpServletRequest req, HttpServletResponse resp){
				
	    mgr = PMF.get().getPersistenceManager();
	    String key = req.getParameter("encodedKey");
	    //User userToDelete  = null;
	    try {
	    	//userToDelete = gson.fromJson(req.getReader(), User.class);
			
			if(StringUtils.isEmpty(key)) {
				resp.getWriter().println(gson.toJson("Invalid user"));
				mgr.close();
				return;
			}
			try {
				// Delete user and tasks
				User user = mgr.getObjectById(User.class, key);
				List<Task> deletedTasks = ModelController.deleteTasksByUserId(mgr, key);
				mgr.deletePersistent(user);
				
				resp.setContentType("application/json");
				resp.getWriter().println(gson.toJson(user));
				//resp.getWriter().println(gson.toJson(deletedTasks));
				mgr.close();
				
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				resp.getWriter().println(gson.toJson("Invalid user encodedKey "));
				mgr.close();
				return;
			}
	    } catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	
	
}
