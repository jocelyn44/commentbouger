package com.nantes.commentbouger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

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
}
