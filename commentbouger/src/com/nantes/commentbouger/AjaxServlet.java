package com.nantes.commentbouger;

import java.io.BufferedReader;

import java.net.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;




import nantes.tan.Stops;
import tanResponse.Adresse;
import tanResponse.Etape;
import tanResponse.Itineraire;
import tanResponse.ResponseItineraire;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;


public class AjaxServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final Logger log = Logger.getLogger(Itineraire.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String quoi = req.getParameter("quoi");
		String dep= req.getParameter("dep");
		String arr= req.getParameter("arr");

		resp.setContentType("text/plain");
		
		if(quoi.equals("bus")){
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
		else if(quoi.equals("adresse")){
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
			rep+=dep+",checkPied,Depart;";
			tmp=dep.split(",");
			rep+=b.findBiclooo(Double.parseDouble(tmp[0]), Double.parseDouble(tmp[1]))+",checkBicloo,Prendre un bicloo";
			//pour le point d'arrivee
			tmp=arr.split(",");
			rep+=";"+b.findBiclooo(Double.parseDouble(tmp[0]), Double.parseDouble(tmp[1]))+",checkPied,Rendre le bicloo;";
			
			rep+=arr+",checkPied,Arrivee";
			resp.getWriter().write(rep);
		}
		else if(quoi.equals("choixIti")){
			//on récupere le numero de l'itineraire
			int numIti= Integer.parseInt(req.getParameter("iti"));
			HttpSession s = req.getSession();
			String strItis = (String) s.getAttribute("itineraires");
			Itineraire iti = new tanResponse.ResponseItineraire(strItis).get(numIti-1);
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
			}
			
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
			log.severe(e.toString());
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




