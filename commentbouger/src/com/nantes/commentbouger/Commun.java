package com.nantes.commentbouger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Commun {
	//cette fonction calcule la distance entre deux points GPS
	public static double getDistanceMap(double lat1, double lon1, double lat2, double lon2){
		String dep, arr;
		dep = Double.toString(lat1)+","+Double.toString(lon1);
		arr = Double.toString(lat2)+","+Double.toString(lon2);
		String req = "http://maps.googleapis.com/maps/api/distancematrix/xml?origins="+dep+"&destinations="+arr+"&mode=walk&language=fr-FR";
		double res=0;
		try {
			URL u = new URL(req);
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(u.openStream()));
				String line;

		        while ((line = reader.readLine()) != null) {
		            if(line.contains("<distance>")){
		            	line =reader.readLine();
		            	if(line.contains("<value>")){
		    				String[]vals=line.split("<value>");
		    				vals=vals[1].split("</value>");
		    				res=Double.parseDouble(vals[0]);
		            	}
		            }
		        }
		        reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public static double getDistanceOffline(double lat1, double lon1, double lat2, double lon2) {
        //code for Distance in Kilo Meter
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.abs(Math.round(rad2deg(Math.acos(dist)) * 60 * 1.1515 * 1.609344 * 1000));
        return (dist);
	} 
	private static double deg2rad(double deg) {
	        return (deg * Math.PI / 180.0); }
	private static double rad2deg(double rad) {
	        return (rad / Math.PI * 180.0); } 
	
	// cette fonction permet de récupérer le XML proprement
		public static Document getDocument(String chemin){
			Document documentXML = null;
		    
		    // on parse le fichier
		    try {
		    File file = new File(chemin);
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docBuilderFactory.newDocumentBuilder();
			documentXML = docBuilder.parse(file);
		    } 
			catch (SAXException e) {
				e.printStackTrace();
				System.exit(0);
			} 
			catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
			catch (ParserConfigurationException e) {
				e.printStackTrace();
				System.exit(0);
			}
		    return documentXML;
		}

		//cette fonction retourne vrai si la station 1 est plus proche que la station 2 du point de reference
		public static boolean plusPres(double Xref, double Yref, double X1, double Y1, double X2, double Y2){
			return Commun.getDistanceOffline(Xref, Yref, X1, Y1)<Commun.getDistanceOffline(Xref, Yref, X2, Y2);
		}
		
		public static String transformTan(String s) {
			s.trim();
			s.toLowerCase();
			return s.replace("ë", "e").replace("ê", "e").replace("é", "e").replace("è", "e").replace("à", "a").replace("ç", "c").replace("à", "a").replace("\\u00e8", "e").replace("\\u00e9", "e").replace("\u00e2", "a").replace("\u00e7", "c").replace("â", "a").replace("ô","a").replace(" ","").replace("-","").replace("'","");
		}
		
		public static String sansAccents(String s) {
			return s.replace("ë", "e").replace("ê", "e").replace("é", "e").replace("è", "e").replace("à", "a").replace("ç", "c").replace("à", "a").replace("\\u00e8", "e").replace("\\u00e9", "e").replace("\u00e2", "a").replace("\u00e7", "c").replace("â", "a");
		}
}
