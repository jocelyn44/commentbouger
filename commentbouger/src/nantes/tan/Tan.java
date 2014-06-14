package nantes.tan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Tan {

	/**
	 * @param nomArretDep
	 * @param nomLigne
	 * @param nomTerminus
	 * @param nomArretArr
	 * @return 
	 */
	public static String coordTrajetTan (String nomArretDep, String nomLigne, String nomTerminus, String nomArretArr){
		List<Shapes> shapes = genererListShapes();
		List<Stops> stops = genererListStops();

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

				if(lstCoordDep.contains(shapes.get(i).getShape_pt_lat()+","+shapes.get(i).getShape_pt_lon())){

					if (ligne.equals(nomLigne)){
						depTrouve =true;
						bonneLigne = true;
						lstCoord = new ArrayList<>();
					}
				}
				if(depTrouve){
					lstCoord.add(shapes.get(i).getShape_pt_lat()+","+shapes.get(i).getShape_pt_lon()+";");

					if(lstCoordArr.contains(shapes.get(i).getShape_pt_lat()+","+shapes.get(i).getShape_pt_lon())){
						break;
					}
				}	
			}
		}
		String stringCoordGps="";
		for (String coord : lstCoord){
			stringCoordGps = stringCoordGps+coord+"\n";
		}
		System.out.println(stringCoordGps);
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
