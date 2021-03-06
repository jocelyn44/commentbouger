package com.nantes.commentbouger;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String reponse="";
		resp.setContentType("text/html");
		//reponse+="<h2>Nantes-CommentBouger.com</h2>";
		
		if (user != null) {
			
			resp.getWriter().println("Salut " + user.getNickname() +" !");
			resp.getWriter().println(
					"<a href='"
							+ userService.createLogoutURL(req.getRequestURI())
							+ "'> D�connexion </a>");

		} else {

			reponse+=(
					"N'h�site pas � te <a href='"
							+ userService.createLoginURL(req.getRequestURI())
							+ "'> connecter ! </a>");

		}
		
		resp.getWriter().write("google;"+reponse);
	}
}
