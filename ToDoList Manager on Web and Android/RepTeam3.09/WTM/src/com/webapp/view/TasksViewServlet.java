package com.webapp.view;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webapp.PMF;
import com.webapp.api.ModelController;
import com.webapp.model.Task;
import com.webapp.model.User;


@SuppressWarnings("serial")
public class TasksViewServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			
			Cookie[] cookies = req.getCookies();	
			String userName = null;
			for (Cookie cookie : cookies) {
				if("user".equals(cookie.getName())) {
					userName =cookie.getValue();
					break;
				}
			}
			PersistenceManager mgr = PMF.get().getPersistenceManager();

			final User user = ModelController.getUserByName(mgr, userName);
			if (user == null) {
				req.getRequestDispatcher("/404.jsp").forward(req, resp);
				mgr.close();
				return;
			}
			List<Task> tasks = ModelController.getTasksByUserId(mgr, user.getEncodedKey());
			req.setAttribute("tasks", tasks);
			req.setAttribute("user", user);				
			mgr.close();

			//TODO: tasks is empty
			req.getRequestDispatcher("/tasks.jsp").forward(req, resp);
			
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
