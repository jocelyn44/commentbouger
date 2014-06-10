package com.nantes.commentbouger;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Parking {
	private String chemin = "parking/parking.xml";
	private Document doc;
	
	public Parking (){
		doc = Commun.getDocument(chemin);
		
	}
	
	// cette fonction permet de trouver la station bicloo la plus proche.
	public String findParking(double coordX, double coordY){
		double minLat=0,minLong=0;
		boolean first = true;
		
		//on prend le noeud racine
		Element racineElement;
	    racineElement = doc.getDocumentElement();
	    
	    //on recupere le noeud carto
	    NodeList markersList = racineElement.getChildNodes();
	    
	    // on récupere toutes les stations
	    //NodeList stationsList = markersList.item(0).getChildNodes();
		
	    //pour chaque station on recherche la plus proche
	    for(int i=0;i<markersList.getLength();i++){
	    	double lat,lng;
	    	lat=Double.valueOf(markersList.item(i).getAttributes().getNamedItem("lat").getNodeValue());
	    	lng=Double.valueOf(markersList.item(i).getAttributes().getNamedItem("lng").getNodeValue());
	    	//si on est sur le premier noeud, on initialise les minis
	    	if(first==true){
	    		minLat=lat;
	    		minLong=lng;
	    		first=false;
	    	}
	    	//si c'est plus pres on change le plus pres
	    	if(Commun.plusPres(coordX, coordY, lat, lng, minLat, minLong)){
	    		minLat=lat;
	    		minLong=lng;
	    	}
	    }
		return (minLat+","+minLong);
	}
	
}
