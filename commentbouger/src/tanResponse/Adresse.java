package tanResponse;

public class Adresse {
	private String type="";
	private String nom="";
	private String id="";
	private String ville="";
	private String cp="";
	
	public Adresse(String params){
		String[] tab=params.split(",");
		for(String s: tab){
			if(s.contains("ville")){
				ville=s.split(":")[1].substring(1, s.split(":")[1].length()-1);
			}
			if(s.contains("nom")){
				nom=s.split(":")[1].substring(1, s.split(":")[1].length()-1);
			}
			if(s.contains("cp")){
				cp=s.split(":")[1].substring(1, s.split(":")[1].length()-2);
			}
			if(s.contains("id")){
				id=s.split(":")[1].substring(1, s.split(":")[1].length()-1);
			}
		}
	}
	
	public String toString(){
		return ville+" "+cp+": "+nom+"["+id;
	}

	public String getType() {
		return type;
	}

	public String getNom() {
		return nom;
	}

	public String getId() {
		return id;
	}

	public String getVille() {
		return ville;
	}

	public String getCp() {
		return cp;
	}
	
	
}
