package com.webapp.api;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.datanucleus.util.StringUtils;

import com.webapp.model.Task;
import com.webapp.model.User;

public class ModelController {
	
	@SuppressWarnings("unchecked")
	public static User getUserByName(final PersistenceManager mgr, final String name) {
		if (StringUtils.isEmpty(name) || mgr == null) {
			return null;
		}
		Query query = mgr.newQuery(User.class,  "name =='" + name + "'");
		for (Object obj : (List<Object>) query.execute()) {
	        return (User) obj;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<User> getAllUsers(final PersistenceManager mgr) {
		if (mgr == null) {
			return null;
		}
		List<User> result = new ArrayList<User>();	
		Query query = mgr.newQuery(User.class);
		for (Object obj : (List<Object>) query.execute()) {
	        result.add((User) obj);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Task> getTasksByUserId(final PersistenceManager mgr, final String userId) {
		if (StringUtils.isEmpty(userId) || mgr == null) {
			return null;
		}
		List<Task> result = new ArrayList<Task>();	
		Query query = mgr.newQuery(Task.class,  "userId =='" + userId + "'");
		for (Object obj : (List<Object>) query.execute()) {
			result.add((Task)obj);	        
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Task> getCheckedTasksByUserId(final PersistenceManager mgr, final String userId, boolean checked) {
		if (StringUtils.isEmpty(userId) || mgr == null) {
			return null;
		}
		List<Task> result = new ArrayList<Task>();	
		Query query = mgr.newQuery(Task.class,  "userId =='" + userId + "'");
		for (Object obj : (List<Object>) query.execute()) {
			if(((Task)obj).isChecked() == checked){
				result.add((Task)obj);	
			}
		}
		return result;
	}
	
	public static List<Task> getTasksByUserName(final PersistenceManager mgr, final String name) {
		if (StringUtils.isEmpty(name) || mgr == null) {
			return null;
		}
		User user = getUserByName( mgr, name);
		if (user == null) {
			return null;
		}
		return getTasksByUserId( mgr, user.getEncodedKey());
		
	}
	
	@SuppressWarnings("unchecked")
	public static List<Task> deleteTasksByUserId(final PersistenceManager mgr, final String userId) {
		if (StringUtils.isEmpty(userId) || mgr == null) {
			return null;
		}
		List<Task> result = new ArrayList<Task>();	
		Query query = mgr.newQuery(Task.class,  "userId =='" + userId + "'");
		for (Object obj : (List<Object>) query.execute()) {
			result.add((Task)obj);	
			mgr.deletePersistent((Task)obj);
		}
		return result;
	}

}
