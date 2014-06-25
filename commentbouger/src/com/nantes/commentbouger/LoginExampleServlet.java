package com.nantes.commentbouger;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LoginExampleServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		resp.setContentType("text/html");
		resp.getWriter().println("<h2>Nantes-CommentBouger.com</h2>");
		
		if (user != null) {
			
			resp.getWriter().println("Salut " + user.getNickname() +" !");
			resp.getWriter().println(
					"<a href='"
							+ userService.createLogoutURL(req.getRequestURI())
							+ "'> Déconnexion </a>");

		} else {

			resp.getWriter().println(
					"N'hésite pas à te <a href='"
							+ userService.createLoginURL(req.getRequestURI())
							+ "'> connecter ! </a>");

		}
	}
}
