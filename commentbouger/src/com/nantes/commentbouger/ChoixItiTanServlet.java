package com.nantes.commentbouger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tanResponse.Etape;
import tanResponse.Itineraire;
import nantes.tan.Stops;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class ChoixItiTanServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final Logger log = Logger.getLogger(Itineraire.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {


		resp.setContentType("text/plain");

		//on récupere le numero de l'itineraire
		int numIti= Integer.parseInt(req.getParameter("iti"));
		HttpSession s = req.getSession();
		String strItis = (String) s.getAttribute("itineraires");
		Itineraire iti;
		iti= new tanResponse.ResponseItineraire(strItis).get(numIti-1);
		String res="";//getCoordinatesFromAdress(iti.getAdresseDepart());

		int i=0;
		String type="",nomArretPrecedent="";
		//pour chaque etape
		for(Etape e: iti.getEtapes()){
			//si on est dans le cas d'une marche, on se rend a une adresse ou on part d'une
			//adresse donc on cherche les coordonnees GPS de la ou les adresses
			if(e.getType()=="marche" || e.getType()==null)
				type="checkPied";
			else
				type="checkBus";
			if(nomArretPrecedent=="")
				nomArretPrecedent=iti.getArretDepart();

			nomArretPrecedent=Commun.sansAccents(nomArretPrecedent);
			boolean trouve=false;
			nantes.tan.Stops arret;
			if(type.equals("checkPied")){
				arret=getStop(nomArretPrecedent);
				if(null==arret)//c'est une adresse
					res+=getCoordinatesFromAdress(nomArretPrecedent+" 44")+","+type+","+e.toString()+";";
				else
					res+=arret.stop_lat+","+arret.stop_lon+","+type+","+e.toString()+";";
			}
			else{//si on est en bus
				String trajet;
				trajet=nantes.tan.Tan.coordTrajetTan(nomArretPrecedent, e.getLigne(), e.getArretDest());
				trajet=trajet.substring(0, trajet.indexOf(";"))+","+e.toString()+trajet.substring(trajet.indexOf(";"),trajet.length()-1);
				res+=trajet;
			}

			nomArretPrecedent=e.getArretDest();
			i++;

			resp.getWriter().write("iti;"+res.substring(0,res.length()));
		}
	}


	//cette fonction retourne l'arret dont le nom est egla a la string passee en parametre
	public nantes.tan.Stops getStop(String nom){
		List<Stops> arrets = nantes.tan.Tan.genererListStops();
		for(Stops stop : arrets){
			if(Commun.transformTan(stop.getStop_name()).equals(Commun.transformTan(nom))){
				return stop;
			}
		}
		return null;
	}

	public String getCoordinatesFromAdress(String adresse){
		String res="",strMap="",tmp="";
		String urlMap = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		URL urlDep;
		try {
			urlDep = new URL(urlMap+adresse.replace(" ", "+")+"&key=AIzaSyBfOwNhtQC9k5HGyUPtqlnCbzmNbgjeQ6o");
			URLConnection connection = urlDep.openConnection();
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((tmp=r.readLine())!=null){
				strMap+=tmp;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		//log.severe(strMap);
		try {
			JSONObject repMap = new JSONObject(strMap);
			JSONArray arrMap = new JSONArray(repMap.get("results").toString());
			repMap = new JSONObject(arrMap.get(0).toString());
			repMap = new JSONObject(repMap.get("geometry").toString());
			repMap = new JSONObject(repMap.get("location").toString());
			res+=repMap.get("lat").toString();
			res+=","+repMap.get("lng").toString();
			repMap.toString();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return res;
	}
}
