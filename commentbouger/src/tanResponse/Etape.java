package tanResponse;

public class Etape {
	private String type; // marche / conduite ...
	private String ligne;
	private String arretDest;
	private String direction;
	
	public Etape(String params){
			String[] tab = params.substring(1, params.length()-1).split("\\{")[1].split("\\}")[0].split(",");
			//dans le cas ou on est sur un trajet en bus / tram
			if(params.contains("\"ligne\":{")){
				type="bus";
				String[] tab2 = params.substring(1, params.length()-1).split("\\{")[2].split("\\}")[0].split(",");
				for(String str:tab){
					if(str.contains("numLigne")){
						ligne=str.split(":")[1].substring(1, str.split(":")[1].length()-1);
					}
					if(str.contains("terminus")){
						direction=str.split(":")[1].substring(1, str.split(":")[1].length()-1);
					}
				}
				for(String str:tab2){
					if(str.contains("libelle")){
						arretDest=str.split(":")[1].substring(1, str.split(":")[1].length()-1);
					}
				}
			}//dans le cas ou on est dans un trajet a pied
			else{
				type="marche";
				for(String str:tab){
					if(str.contains("libelle")){
						arretDest=str.split(":")[1].substring(1, str.split(":")[1].length()-1);
					}
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
	
	
}
