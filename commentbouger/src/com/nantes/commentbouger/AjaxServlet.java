package com.nantes.commentbouger;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AjaxServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String quoi = req.getParameter("quoi");
		String dep= req.getParameter("dep");
		String arr= req.getParameter("arr");

		resp.setContentType("text/plain");
		if(quoi.equals("bus")){
			//dans le cas ou on veut trouver les arrets de bus
			resp.getWriter().write("reponse serveur  : [bus]");
		}
		if(quoi.equals("park")){
			//dans le cas ou on veut trouver le parking le plus proche de l'arrivee
			resp.getWriter().write("[parking]");
		}
		if(quoi.equals("bicloo")){
			//dans le cas ou on veut trouver les stations de bicloo proche de depart arrivee
			String rep="bicloo;";
			String[] tmp;
			Bicloo b = new Bicloo();
			//pour le point de depart
			tmp=dep.split(",");
			rep+=b.findBiclooo(Double.parseDouble(tmp[0]), Double.parseDouble(tmp[1]));
			//pour le point d'arrivee
			tmp=arr.split(",");
			rep+=";"+b.findBiclooo(Double.parseDouble(tmp[0]), Double.parseDouble(tmp[1]));
			
			resp.getWriter().write(rep);
		}
	}
}
