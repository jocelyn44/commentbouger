package tanResponse;


import java.util.ArrayList;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class ResponseItineraire {
	private List<Itineraire> lItis = new ArrayList<Itineraire>();
	
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
	
	public Itineraire get(int i){
		return lItis.get(i);
	}

}
