package tanResponse;

import java.util.LinkedList;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;

public class Itineraire {
	  String adresseDepart;
	  String strEtapes;
	  List<Etape> etapes;
	  String adresseArrivee;
	  int duree;
	  double prix;
	  
	  public Itineraire(String param) {
		  param= param.substring(1, param.length()-1);
		  String[] tparam = param.split("\\[");
		  strEtapes= tparam[1].split("\\]")[0];
		  String finparam=tparam[1].split("\\]")[1];
		  
		// TODO Auto-generated constructor stub
		  String[] tab = tparam[0].substring(0, tparam[0].length()-10).concat(finparam).split(",");
		  for(int i=0;i<tab.length;i++){
			  if(tab[i].contains("adresseDepart")){
				  adresseDepart=tab[i].split(":")[1].substring(1, tab[i].split(":")[1].length()-1);
			  }
			  if(tab[i].contains("adresseArrivee")){
				  adresseArrivee=tab[i].split(":")[1].substring(1, tab[i].split(":")[1].length());
			  }
			  if(tab[i].contains("duree")){
				  duree=Integer.parseInt(tab[i].split(":")[1].substring(1, tab[i].split(":")[1].length()).substring(0, 2));
			  }
			  etapes=creerEtape(strEtapes);
		  }
		  
		  tab.toString();
	  }
	  
	  public List<Etape> creerEtape(String e){
		  List<Etape> res = new LinkedList<Etape>();
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
		return adresseDepart;
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
	  
	  
}
