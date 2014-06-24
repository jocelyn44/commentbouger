package nantes.tan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.nantes.commentbouger.Commun;

public class Tan {

	/**
	 * 
	 * L'objectif est de retourner toutes les positions GPS des arrets de bus d'une ligne situé entre deux arrets
	 * 
	 * @param nomArretDep
	 * @param nomLigne
	 * @param nomTerminus
	 * @param nomArretArr
	 * @return 
	 */
	public static String coordTrajetTan (String nomArretDep, String nomLigne,  String nomArretArr){

		nomArretDep = Commun.transformTan(nomArretDep);
		nomArretArr = Commun.transformTan(nomArretArr);
		
		String typeCheck;
		//if(nomLigne.equals("1") || nomLigne.equals("2") || nomLigne.equals("3"))
			typeCheck="checkTram";
		/*else
			typeCheck="checkBus";*/
		// On génère les objets Shapes et Stops depuis les fichiers .txt
		List<Shapes> shapes = genererListShapes();
		List<Stops> stops = genererListStops();

		// On récupère les coordonnées GPS 
		List<String> lstCoordArr = new ArrayList<>();
		List<String> lstCoordDep = new ArrayList<>();
		for(int x = 1; x <= stops.size()-1; x++){
			if(nomArretArr.equals(stops.get(x).getStop_name())){
				lstCoordArr.add(stops.get(x).getStop_lat()+","+stops.get(x).getStop_lon());
			}

			if(nomArretDep.equals(stops.get(x).getStop_name())){
				lstCoordDep.add(stops.get(x).getStop_lat()+","+stops.get(x).getStop_lon());
			}
		}
		Boolean depTrouve =false;
		Boolean bonneLigne = false;
		List<String> lstCoord = new ArrayList<>();
		int sens=1;
		// Pour chaque ligne du fichier shapes.txt
		for(int i = 1; i <= shapes.size()-1; i=i+sens){
			String ligne = shapes.get(i).getShape_id().substring(0, shapes.get(i).getShape_id().length()-4);

			if (bonneLigne && !ligne.equals(nomLigne)){
				sens = -sens;
				i = i-1;
			}

			if(!shapes.get(i).getShape_id().equals(shapes.get(i-1).getShape_id())){
				depTrouve = false;
			}

			String coordTemp = shapes.get(i).getShape_pt_lat()+","+shapes.get(i).getShape_pt_lon()+";";
			if (!coordTemp.equals(shapes.get(i-1).getShape_pt_lat()+","+shapes.get(i-1).getShape_pt_lon()+";")){

				// Si on a trouvé les coordonnées de l'arret de départ
				if(lstCoordDep.contains(shapes.get(i).getShape_pt_lat()+","+shapes.get(i).getShape_pt_lon())){

					// Si on a trouve le bon numéro de ligne
					if (ligne.equals(nomLigne)){
						depTrouve =true;
						bonneLigne = true;
						lstCoord = new ArrayList<>();
					}
				}
				if(depTrouve){
					// Si on trouve une arret on le sauvegarde de la forme suivante : 47.21694925,-1.55679602,checkBus;47.22998380,-1.61681022,checkBus
					lstCoord.add(shapes.get(i).getShape_pt_lat()+","+shapes.get(i).getShape_pt_lon()+","+typeCheck+";");

					// Si on a trouvé les coordonnées de l'arret d'arrivé on Break;
					if(lstCoordArr.contains(shapes.get(i).getShape_pt_lat()+","+shapes.get(i).getShape_pt_lon())){
						break;
					}
				}	
			}
		}

		// On retourne une String contenant l'ensemble des positions GPS des arrets d'une ligne
		// Chaque arret de bus est représenté par sa latitude et sa longitude et le fait que se soit un arret de bus.
		// 47.21694925,-1.55679602,checkBus;
		String stringCoordGps="";
		for (String coord : lstCoord){
			stringCoordGps = stringCoordGps+coord;
		}
		return stringCoordGps;

	}



	public static List<Shapes> genererListShapes(){
		String filePath = "tan/shapes.txt";

		List<Shapes> lstShapes = new ArrayList<>(); 

		try{
			// Création du flux bufférisé sur un FileReader, immédiatement suivi par un 
			// try/finally, ce qui permet de ne fermer le flux QUE s'il le reader
			// est correctement instancié (évite les NullPointerException)
			BufferedReader buff = new BufferedReader(new FileReader(filePath));

			try {
				String line;
				// Lecture du fichier ligne par ligne. Cette boucle se termine
				// quand la méthode retourne la valeur null.
				int i = 0;
				String[] tabString;

				while ((line = buff.readLine()) != null) {
					line.trim();
					line = new String(line.getBytes(), "UTF8");
					line.trim();

					if (i == 0){
						i++;
					}else{
						tabString = line.split(",");
						Shapes shapes = new Shapes(tabString[0],tabString[1],tabString[2],tabString[3]);
						lstShapes.add(shapes);
					}
				}
			} finally {
				// dans tous les cas, on ferme nos flux
				buff.close();
			}
		} catch (IOException ioe) {
			// erreur de fermeture des flux
			System.out.println("Erreur --" + ioe.toString());
		}
		return lstShapes;

	}

	public static List<Stops> genererListStops(){
		String filePath = "tan/stops.txt";

		List<Stops> lstStops = new ArrayList<>(); 

		try{
			// Création du flux bufférisé sur un FileReader, immédiatement suivi par un 
			// try/finally, ce qui permet de ne fermer le flux QUE s'il le reader
			// est correctement instancié (évite les NullPointerException)
			BufferedReader buff = new BufferedReader(new FileReader(filePath));

			try {
				String line;
				// Lecture du fichier ligne par ligne. Cette boucle se termine
				// quand la méthode retourne la valeur null.
				int i = 0;
				String[] tabString;

				while ((line = buff.readLine()) != null) {
					line.trim();
					line = new String(line.getBytes(), "UTF8");
					line.trim();

					if (i == 0){
						i++;
					}else{
						tabString = line.split(",");

						String varStop_name =  tabString[1];
						String stop_name = (String) varStop_name.subSequence(1, varStop_name.length()-1);


						Stops stops = new Stops(tabString[0],stop_name,tabString[2],tabString[3],tabString[4],tabString[5],tabString[6],tabString[7],null);
						lstStops.add(stops);
					}
				}
			} finally {
				// dans tous les cas, on ferme nos flux
				buff.close();
			}
		} catch (IOException ioe) {
			// erreur de fermeture des flux
			System.out.println("Erreur --" + ioe.toString());
		}
		return lstStops;

	}
}
