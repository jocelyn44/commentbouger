package com.nantes.commentbouger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tanResponse.ResponseItineraire;

public class ReqItiTanServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String dep= req.getParameter("dep");
		String arr= req.getParameter("arr");
		
		//dans le cas ou on veut trouver les arrets de bus
		dep=dep.replaceAll("-", "+").replaceAll("\\|", "%7C").replace(" ", "+");
		arr=arr.replaceAll("-", "+").replaceAll("\\|", "%7C").replace(" ", "+");
		String heure;
		heure=(Calendar.getInstance().get(Calendar.YEAR))+"-";
		if(Integer.toString(Calendar.getInstance().get(Calendar.MONTH)).length()<2)
			heure+="0"+(Calendar.getInstance().get(Calendar.MONTH)+1)+"-";
		else
			heure+=(Calendar.getInstance().get(Calendar.MONTH)+1)+"-";
		if(Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).length()<2)
			heure+="0"+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"+";
		else
			heure+=Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"+";
		if(Integer.toString(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)).length()<2)
			heure+="0"+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+"%3A";
		else
			heure+=Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+"%3A";
		if(Integer.toString(Calendar.getInstance().get(Calendar.MINUTE)).length()<2)
			heure+="0"+Calendar.getInstance().get(Calendar.MINUTE);
		else
			heure+=Calendar.getInstance().get(Calendar.MINUTE);
		//heure=
		String surl="https://www.tan.fr/ewp/mhv.php/itineraire/resultat.json?depart="+ dep+"&arrive="+arr+"&type=0&accessible=0&temps="+heure+"&retour=0";
		URL url = new URL(surl);
		
		
		URLConnection connection = url.openConnection();
		BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		final Logger log = Logger.getLogger(AjaxServlet.class.getName());
		
		String line=r.readLine();
		line = Commun.sansAccents(line);
		if(line.contains("error")){
			resp.getWriter().write(line);
		}
		else{
			HttpSession s = req.getSession();
			ResponseItineraire response = new tanResponse.ResponseItineraire(line);
 			s.setAttribute("itineraires", line);	
			resp.getWriter().write("bus;"+response.toString());
		}
		
	}

}
