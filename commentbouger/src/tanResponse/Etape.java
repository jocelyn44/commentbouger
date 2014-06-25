package tanResponse;

import java.util.logging.Logger;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class Etape {
	private String type; // marche / conduite ...
	private String ligne;
	private String arretDest;
	private String arretDep;
	private String direction;
	
	public Etape(String params){
		  final Logger log = Logger.getLogger(Etape.class.getName());

			//dans le cas ou on est sur un trajet en bus / tram
			if(params.contains("ligne")){
				try {
					JSONObject j;
					if(params.startsWith("{"))
						j= new JSONObject(params);
					else
						j= new JSONObject(params.substring(1, params.length()-1));
					if(j.get("marche").equals(false)){
						type="bus";
						JSONObject d = new JSONObject(j.getString("arretStop"));
						JSONObject l = new JSONObject(j.getString("ligne"));
						arretDest=com.nantes.commentbouger.Commun.sansAccents(d.getString("libelle"));
						ligne=l.getString("numLigne");
						direction=l.getString("terminus");
						j.toString();
					}else{
						type="marche";
						JSONObject d = new JSONObject(j.getString("arretStop"));
						arretDest=d.getString("libelle");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//dans le cas ou on est dans un trajet a pied
			else{
				try {
					JSONObject j = new JSONObject(params);
					JSONObject d = new JSONObject(j.getString("arretStop"));
					arretDest=d.getString("libelle");
					j.toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		
	}

	public String getType() {
		return type;
	}

	public String getLigne() {
		return ligne;
	}

	public String getArretDest() {
		return arretDest;
	}

	public String getDirection() {
		return direction;
	}
	
	public String toString(){
		if(type=="bus")
			return "Prendre le "+ligne+" vers "+direction+" jusqu'a "+arretDest;
		return "Marcher jusqu'a "+arretDest;
	}
	
}
