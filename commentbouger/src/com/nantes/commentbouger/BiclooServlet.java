package com.nantes.commentbouger;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BiclooServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String dep= req.getParameter("dep");
		String arr= req.getParameter("arr");
	
		resp.setContentType("text/plain");
		
		//dans le cas ou on veut trouver les stations de bicloo proche de depart arrivee
		String rep="bicloo;";
		String[] tmp;
		Bicloo b = new Bicloo();
		//pour le point de depart
		rep+=dep+",checkPied,Depart;";
		tmp=dep.split(",");
		rep+=b.findBiclooo(Double.parseDouble(tmp[0]), Double.parseDouble(tmp[1]))+",checkBicloo,Prendre un bicloo";
		//pour le point d'arrivee
		tmp=arr.split(",");
		rep+=";"+b.findBiclooo(Double.parseDouble(tmp[0]), Double.parseDouble(tmp[1]))+",checkPied,Rendre le bicloo;";
		
		rep+=arr+",checkPied,Arrivee";
		resp.getWriter().write(rep);
	}
}
