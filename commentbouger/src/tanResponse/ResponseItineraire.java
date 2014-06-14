package tanResponse;


import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;

public class ResponseItineraire {
	

	public ResponseItineraire(String pmap) {
		try {
			JSONArray jsoniti = new JSONArray("["+pmap+"]");
			Itineraire iti = new Itineraire(jsoniti.toString());
			iti.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
