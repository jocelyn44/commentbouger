package com.nantes.commentbouger;

import java.io.IOException;

import javax.servlet.http.*;


@SuppressWarnings("serial")
public class CommentbougerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Bicloo rab=new Bicloo("bicloo/carto.xml");
		resp.setContentType("text/plain");
		resp.sendRedirect("index.html");
		resp.getWriter().println(req.getParameter("dep"));
		resp.getWriter().println(rab.findBiclooo(47.218371, -1.553621000000021));
	}
	
	
	
}
