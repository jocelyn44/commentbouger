package com.nantes.commentbouger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tanResponse.Adresse;
import tanResponse.Itineraire;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;

public class AdressServlet  extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final Logger log = Logger.getLogger(Itineraire.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {



		//dans le cas ou on veut trouver les arrets de bus
		String ou = req.getParameter("ou");
		ou.replaceAll(" ", "+");
		String surl="https://www.tan.fr/ewp/mhv.php/itineraire/address.json?&nom="+ou;
		URL url = new URL(surl);

		List<Adresse> listeAddresse = new ArrayList<Adresse>();
		URLConnection connection = url.openConnection();
		BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = Commun.sansAccents(r.readLine());
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
			res="Il n'y a pas de resultats, veuillez saisir une adresse plus precise ";
		resp.getWriter().write("adresse;"+Commun.sansAccents(res.substring(0, res.length()-1)+";"+req.getParameter("qui")));
	}
}