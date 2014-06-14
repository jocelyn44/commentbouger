package tanResponse;

public class Adresse {
	String type="";
	String libelle="";
	String id="";
	String ville="";
	String cp="";
	
	public Adresse(String params){
		String[] tab=params.split(",");
		for(String s: tab){
			if(s.contains("ville")){
				ville=s.split(":")[1].substring(1, s.split(":")[1].length()-1);
			}
			if(s.contains("nom")){
				libelle=s.split(":")[1].substring(1, s.split(":")[1].length()-1);
			}
			if(s.contains("cp")){
				cp=s.split(":")[1].substring(1, s.split(":")[1].length()-2);
			}
		}
		libelle=ville+" "+cp+": "+libelle;
	}
	
	public String toString(){
		return libelle;
	}
}
