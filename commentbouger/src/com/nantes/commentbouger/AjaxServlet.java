package com.nantes.commentbouger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Element;

import tanResponse.Adresse;
import tanResponse.ResponseItineraire;

import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;


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
			dep=dep.replaceAll("-", "+").replaceAll("\\|", "%7C").replace(" ", "+");
			arr=arr.replaceAll("-", "+").replaceAll("\\|", "%7C").replace(" ", "+");
			String surl="https://www.tan.fr/ewp/mhv.php/itineraire/resultat.json?depart="+dep+"&arrive="+arr+"&type=0&accessible=0&temps=2014-06-26+14%3A55&retour=0";
			URL url = new URL(surl);
			
			
			URLConnection connection = url.openConnection();
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = r.readLine();
 			if(line.contains("error")){
				resp.getWriter().write(line);
			}
			else{
				ResponseItineraire response = new tanResponse.ResponseItineraire(line);	
				resp.getWriter().write("bus;"+response.toString());
			}
			//HashMap<String, Object> chemin;
			
			System.out.println("ok2");
			
			
			
		}
		else if(quoi.equals("adresse")){
			//dans le cas ou on veut trouver les arrets de bus
			String ou = req.getParameter("ou");
			ou.replaceAll(" ", "+");
			String surl="https://www.tan.fr/ewp/mhv.php/itineraire/address.json?&nom="+ou;
			URL url = new URL(surl);
			
			List<Adresse> listeAddresse = new ArrayList<Adresse>();
			URLConnection connection = url.openConnection();
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = r.readLine();
			String[] tab= line.substring(1, line.length()-1).split("\\[");
			for(int j=0;j<tab.length;j++){
				if(tab[j].contains("Addresses") || tab[j].contains("Points d'arr") || tab[j].contains("Stops") ){
					try {
						JSONArray add = new JSONArray("["+tab[j+1]+"]");
						for(int i=0;i<add.length();i++){
							listeAddresse.add(new Adresse(add.get(i).toString()));
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				j++;
			}
			String res="";
			for(Adresse a: listeAddresse){
				if(a.getNom().equalsIgnoreCase(a.getVille())!=true)
					res+=a.toString()+";";
			}
			if(res=="")
				res="adresse;Il n'y a pas de resultats, veuillez saisir une adresse plus precise ";
			resp.getWriter().write("adresse;"+res.substring(0, res.length()-1)+";"+req.getParameter("qui"));
		}
		else if(quoi.equals("park")){
			//dans le cas ou on veut trouver le parking le plus proche de l'arrivee
			resp.getWriter().write("[parking]");
		}
		else if(quoi.equals("bicloo")){
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


