package com.nantes.commentbouger;

import java.io.IOException;

import javax.servlet.http.*;


@SuppressWarnings("serial")
public class CommentbougerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Bicloo rab=new Bicloo();
		Parking rabe=new Parking();
		resp.setContentType("text/plain");
		resp.sendRedirect("index.html");
		resp.getWriter().println(req.getParameter("dep"));
		resp.getWriter().println(rab.findBiclooo(47.23168880000001, -1.5594246000000567));
		resp.getWriter().println(rabe.findParking(47.23168880000001, -1.5594246000000567));
		
//		System.out.println("C5");
//		Tan.coordTrajetTan("Ile de Nantes", "C5", "Quai des Antilles", "Prairie au Duc");
//		System.out.println("C5");
//		
//		Tan.coordTrajetTan("Prairie au Duc", "C5", "Gare SNCF Sud", "Ile de Nantes");
//		System.out.println("C5");
//		
//		Tan.coordTrajetTan("Pompidou", "C5", "Gare SNCF Sud", "Picasso");
//		System.out.println("C5");
//		Tan.coordTrajetTan("Picasso", "C5", "Quai des Antilles", "Pompidou");
//		System.out.println("C5");
//		Tan.coordTrajetTan("Pompidou", "C5", "Gare SNCF Sud", "Picasso");
//		System.out.println("C6");
//		Tan.coordTrajetTan("Koufra", "C6", "Chantrerie Grandes Ecoles", "Batignolles");
//		System.out.println("C6");
//		Tan.coordTrajetTan("Coudray", "C6", "Chantrerie", "Keren");
//		System.out.println("C6");
//		Tan.coordTrajetTan("Keren", "C6", "Hermeland", "Coudray");
//		System.out.println("C7");
//		Tan.coordTrajetTan("Coty", "C7", "Trianon", "Minais");
//		System.out.println("C7");
//		Tan.coordTrajetTan("Minais", "C7", "Souillarderie", "Coty");
	}
	
	
	
}
