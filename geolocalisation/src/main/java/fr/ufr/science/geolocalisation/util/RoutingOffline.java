package fr.ufr.science.geolocalisation.util;

import java.io.File;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;

import fr.ufr.science.geolocalisation.model.Route;

public class RoutingOffline {
	
	private static GraphHopper hopper;
	static final String[] cacheFileNames = {"edges", "geometry", "location_index", "names", "nodes", "nodes_ch_fastest_car_node", "properties", "shortcuts_fastest_car_node"} ; 
	
	public static boolean init(String graphHopperPath) {
		if(checkIfCached(graphHopperPath)) {
			hopper = new GraphHopperOSM().forDesktop();
			hopper.setGraphHopperLocation(graphHopperPath);
			hopper.setEncodingManager(EncodingManager.create("car"));
			
			hopper.importOrLoad();
			System.out.println("Routing offline Initialisé");
			return true;
		} else return false;
	}
	
	public static boolean checkIfCached(String path) {
		for(String fileName : cacheFileNames) {
			if(!new File(path + fileName).exists()) {
				System.out.println("Fichiers de carte en cache manquant(s)");
				return false;
			}
		}
		return true;
	}
	
	public static boolean isOsmFile(String path) {
		return isOsmFile(new File(path));
	}
	
	public static boolean isOsmFile(File f) {
		if(!f.exists())
			return false;
		String ext = f.getPath().substring(f.getPath().indexOf("."));
		if(ext.compareTo(".osm.pbf") == 0 || ext.compareTo(".osm.bz2") == 0)
			return true;
		return false;
	}
	
	public static Route getRoute(double fromLat, double fromLon, double toLat, double toLon) {
		GHRequest req = new GHRequest(fromLat, fromLon, toLat, toLon).
				setWeighting("fastest").
				setVehicle("car").
				setLocale(Locale.FRENCH);
		GHResponse rsp = hopper.route(req);
		
		if(rsp.hasErrors()) {
				System.out.println("Erreur: Impossible de trouver un chemin");
			   	return new Route(-1, -1);
			}
		
		PathWrapper path = rsp.getBest();
		
		return new Route(path.getDistance(), path.getTime());
	}	
}
