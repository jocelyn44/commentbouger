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
				String rep = reader.toString();
				String[] vals = rep.split("value>");
				rep = vals[3];
				vals=rep.split("<value");
				res=Double.parseDouble(vals[0]);
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
