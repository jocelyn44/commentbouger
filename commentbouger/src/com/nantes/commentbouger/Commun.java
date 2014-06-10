package com.nantes.commentbouger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Commun {

	public static double getDistance(double lat1, double lon1, double lat2, double lon2){
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
}
