package com.webapp.api;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.webapp.PMF;
import com.webapp.model.Task;
import com.webapp.model.User;
import org.datanucleus.util.StringUtils;


/**
 * This is the servlet that handles all HTTP requests concerning Task class
 * @author Pujita
 *
 */
@SuppressWarnings("serial")
public class TaskServlet extends HttpServlet {
	PersistenceManager mgr = PMF.get().getPersistenceManager();
	final Gson gson = new Gson();

	/**
	 * This uses the HTTP GET request to list out all the tasks (in a JSON format) associated with a user
	 * it uses the userid or username parameter in the url. 
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
			
		mgr = PMF.get().getPersistenceManager();
		
		// return task if task's encodedKey is provided
		String taskId = req.getParameter("encodedkey");
		
		try{
			if ( !StringUtils.isEmpty(taskId)) {
				try {
					Task task = mgr.getObjectById(Task.class, taskId);
					resp.getWriter().println(gson.toJson(task)); 
					mgr.close();
					return;
				}
				catch (javax.jdo.JDOObjectNotFoundException e) {					
					resp.getWriter().println(gson.toJson("Invalid Task encodedKey"));
					mgr.close();
					return;					
				}						
			}			
			// return all tasks for username or userid provided
			String userId = req.getParameter("userid");
			if ( StringUtils.isEmpty(userId)) {
				final String userName = req.getParameter("username");
				
				if (StringUtils.isEmpty(userName)) {
					resp.setContentType("application/json");
			    	resp.getWriter().println(gson.toJson("Provide TaskID, UserID or Username to get a task"));
			    	mgr.close();
					return;
				}			
				User user = ModelController.getUserByName( mgr, userName);
				if(user == null) {
			        resp.setContentType("application/json");
			    	resp.getWriter().println(gson.toJson("User Does Not Exist!"));
			    	mgr.close();
			    	return;
				}
				userId = user.getEncodedKey();						
			}
			// Get the checked attribute and return checked, unchecked or all tasks.
			String isChecked = req.getParameter("checked");
			List<Task> result = null;
			if(isChecked == null) {
				result = ModelController.getTasksByUserId( mgr, userId);   
			} else {
				result = ModelController.getCheckedTasksByUserId( mgr, userId, Boolean.parseBoolean(isChecked));   
			}
		    mgr.close();
	        resp.setContentType("application/json");
	    	resp.getWriter().println(gson.toJson(result));   
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    }
	
	
	/**
	 * This uses the HTTP POST request to add a new task. It uses the task name and userid parameters.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		
		mgr = PMF.get().getPersistenceManager();
		Task taskToAdd = null;
					
		try {
			taskToAdd = gson.fromJson(req.getReader(), Task.class);
			
			if(taskToAdd == null) {
				resp.getWriter().println(gson.toJson("invalid syntax"));
				mgr.close();
				return;
			}
			// Check if userId or task name is provided
			if ( StringUtils.isEmpty(taskToAdd.getUserId()) || StringUtils.isEmpty(taskToAdd.getName())) {		    	
				resp.getWriter().println(gson.toJson("Provide userID and task name to create a task"));
				mgr.close();
				return;				    	
			}	
			taskToAdd.setCreationDate((new Date()).getTime());
			
			// Add task
			mgr.makePersistent(taskToAdd);			
	        resp.setContentType("application/json");
			resp.getWriter().println(gson.toJson(taskToAdd));
			mgr.close();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * This uses the HTTP PUT request to add a new task. It uses the task name and userid parameters.
	 */
	public void doPut(HttpServletRequest req, HttpServletResponse resp) {
		
		mgr = PMF.get().getPersistenceManager();
		Task taskToUpdate = null;
					
		try {
			taskToUpdate = gson.fromJson(req.getReader(), Task.class);
			
			if(taskToUpdate == null) {
				resp.getWriter().println(gson.toJson("invalid syntax"));
				mgr.close();
				return;
			}
			// Check if userId or task name or task encoded Key is provided
			if ( StringUtils.isEmpty(taskToUpdate.getUserId()) || StringUtils.isEmpty(taskToUpdate.getEncodedKey()) || StringUtils.isEmpty(taskToUpdate.getName())) {		    	
				resp.getWriter().println(gson.toJson("Provide userID and task name to create a task"));
				mgr.close();
				return;				    	
			}
			// Check if task exists in database		
			try {
				@SuppressWarnings("unused")
				Task task = mgr.getObjectById(Task.class, taskToUpdate.getEncodedKey());
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				resp.getWriter().println(gson.toJson("Invalid task encodedKey "));
				mgr.close();
				return;
			}
			taskToUpdate.setCreationDate((new Date()).getTime());
			
			// Update task
			mgr.makePersistent(taskToUpdate);			
	        resp.setContentType("application/json");
			resp.getWriter().println(gson.toJson(taskToUpdate));
			mgr.close();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * This uses the HTTP DELETE request to delete a task if its encoded key is provided. 
	 */
	public void doDelete(HttpServletRequest req, HttpServletResponse resp){
		
		mgr = PMF.get().getPersistenceManager();
		String key = req.getParameter("encodedkey");

	    //Task taskToDelete  = null;
	    try {
	    	//taskToDelete = gson.fromJson(req.getReader(), Task.class);
			
			if( StringUtils.isEmpty(key)) {
				resp.getWriter().println(gson.toJson("Invalid task"));
				mgr.close();
				return;
			}
			try {
				// Delete task
				Task task = mgr.getObjectById(Task.class, key);
				mgr.deletePersistent(task);
				
				resp.setContentType("application/json");
				resp.getWriter().println(gson.toJson(task));
				mgr.close();
				
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				resp.getWriter().println(gson.toJson("Invalid task encodedKey "));
				mgr.close();
				return;
			}
	    } catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	
	
}
