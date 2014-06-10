package com.nantes.commentbouger;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Bicloo {
	private String chemin = "";
	private Document doc;
	
	public Bicloo (String ch){
		chemin = ch;
		doc = getDocument();
		
	}
	
	// cette fonction permet de r�cup�rer le XML proprement
	private Document getDocument(){
		Document documentXML = null;
	    
	    // on parse le fichier de configuration
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
	public boolean plusPret(double Xref, double Yref, double X1, double Y1, double X2, double Y2){
		return Commun.getDistance(Xref, Yref, X1, Y1)<Commun.getDistance(Xref, Yref, X2, Y2);
	}
	
	// cette fonction permet de trouver la station bicloo la plus proche.
	public String findBiclooo(double coordX, double coordY){
		//on prend le noeud racine
		Element racineElement;
	    racineElement = doc.getDocumentElement();
	    
	    //on recupere le noeud carto
	    NodeList cartoList = racineElement.getElementsByTagName("carto");
	    
	    // on r�cupere toutes les stations
	    NodeList stationsList = cartoList.item(0).getChildNodes();
		
	    //pour chaque station on recherche la plus proche
	    for(int i=0;i<stationsList.getLength();i++){
	    	
	    }
		return "";
	}
	
}
