package fr.ufr.science.geolocalisation.util;

import java.io.File;
import java.io.IOException;

import com.graphhopper.GraphHopper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;

public class ImportOSMFile {
	
	/*
	 * Transforme un .osm, .osm.pbf ou .osm.bz2 en fichiers utilisables par GraphHopper
	 * Long et couteux en RAM
	 */
	
	public static void importFile(String OSMFile, String graphHopperLocation, String setName) throws IOException {
		
		if(!isOSMFile(OSMFile)) {
			throw new IOException("Fichier OSM invalide (" + OSMFile + ")");
		}
		GraphHopper hopper = new GraphHopperOSM().forDesktop();
		hopper.setDataReaderFile(OSMFile);
		hopper.setGraphHopperLocation(graphHopperLocation + setName);
		hopper.setEncodingManager(EncodingManager.create("car"));
		
		System.out.println("Début de l'import...");
		hopper.importOrLoad();
		System.out.println("Fin de l'import");	
	}
	
	
	public static boolean isOSMFile(String path) {
		return isOSMFile(new File(path));
	}
	
	public static boolean isOSMFile(File f) {
		if(!f.exists())
			return false;
		String ext = f.getPath().substring(f.getPath().indexOf("."));
		if(ext.compareTo(".osm.pbf") == 0 || ext.compareTo(".osm.bz2") == 0)
			return true;
		return false;
	}
	
	public static void main(String[] args) {
		if(args.length < 3) {
			throw new IllegalArgumentException("Pas de fichier passé en argument");
		}
		try {
			importFile(args[0], args[1], args[2]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
