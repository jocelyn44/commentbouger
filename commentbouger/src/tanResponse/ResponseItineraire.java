package tanResponse;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;

public class ResponseItineraire {
	List<Itineraire> lItis = new ArrayList<Itineraire>();
	
	public ResponseItineraire(String pmap) {
		try {
			JSONArray jsonitis = new JSONArray(pmap);
			for(int i=0;i<jsonitis.length();i++){
				JSONArray jsoniti= new JSONArray("["+jsonitis.get(i)+"]");
				lItis.add(new Itineraire(jsoniti.toString()));				
			}
			jsonitis.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String toString(){
		String res="";
		for(Itineraire i: lItis){
			res+=i.toString()+";";
		}
		return res;
	}

}
