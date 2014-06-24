package tanResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.mortbay.log.Log;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class Itineraire {
	  String arretDepart;
	  String adresseDepart;
	  String strEtapes;
	  List<Etape> etapes;
	  String adresseArrivee;
	  String horraireDep;
	  int duree;
	  double prix;
	  final Logger log = Logger.getLogger(Itineraire.class.getName());
	  
	  public Itineraire(String param) {
		  try {
			JSONObject j = new JSONObject(param.substring(1, param.length()-1));
			j.toString();
			adresseDepart=j.getString("adresseDepart");
			arretDepart=j.getString("arretDepart").split(":")[1];
			arretDepart=arretDepart.substring(1,arretDepart.length()-2);
			adresseArrivee=j.getString("adresseArrivee");
			duree=Integer.parseInt(j.getString("duree").substring(0, 2));
			//etapes=creerEtape(j.getString("etapes"));
			horraireDep=j.getString("heureDepart");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  param= param.substring(1, param.length()-1);
		  String[] tparam = param.split("\\[");
		  strEtapes= tparam[1].split("\\]")[0];
		 /* String finparam=tparam[1].split("\\]")[1];
		  String vrai;
		  
		// TODO Auto-generated constructor stub
		  String[] tab = tparam[0].substring(0, tparam[0].length()-10).concat(finparam).split(",");
		  
		  for(int i=0;i<tab.length;i++){
			  if(tab[i].split(":")[0].contains("arretDepart")){
				  arretDepart=tab[i].split(":")[2].substring(1, tab[i].split(":")[2].length()-2);
			  }
			  if(tab[i].replaceAll("\"", "").contains("adresseArrivee")){
				  adresseArrivee=tab[i].split(":")[1].substring(1, tab[i].split(":")[1].length());
			  }
			  if(tab[i].contains("duree")){
				  duree=Integer.parseInt(tab[i].split(":")[1].substring(1, tab[i].split(":")[1].length()).substring(0, 2));
			  }
		  }*/
		  etapes=creerEtape(strEtapes);
		  prix=((duree/60)+1)*1.5;
	  }
	  
	  public String getArretDepart() {
		return arretDepart;
	}

	public List<Etape> creerEtape(String e){
		  List<Etape> res = new ArrayList<Etape>();
		  try {
			JSONArray etapes = new JSONArray("["+e+"]");
			for(int i=0;i<etapes.length();i++){
				res.add(new Etape(etapes.get(i).toString()));
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  
		  return res;
	  }

	public String getAdresseDepart() {
		return arretDepart;
	}

	public String getStrEtapes() {
		return strEtapes;
	}

	public List<Etape> getEtapes() {
		return etapes;
	}

	public String getAdresseArrivee() {
		return adresseArrivee;
	}

	public int getDuree() {
		return duree;
	}

	public double getPrix() {
		return prix;
	}
	  
	public String toString(){
		String res="depart a "+horraireDep+" nombre d'etapes : "+this.etapes.size()+" duree : "+duree+" prix : "+prix+"/";
		for(Etape e: etapes){
			if(e.getType()!="bus")res+=",marche jusqu'a "+e.getArretDest()+",";
			else res+=",ligne "+e.getLigne()+" vers "+e.getDirection()+" jusqu'a "+e.getArretDest()+",";
		}
		return res.substring(0, res.length()-1).replace("ê", "e").replace("ë", "e").replace("ç", "c");
	}
	  
}
