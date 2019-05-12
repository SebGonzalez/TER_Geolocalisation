package fr.ufr.science.geolocalisation.util;

import java.util.Locale;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;

import fr.ufr.science.geolocalisation.model.Route;

public class RoutingOffline {
	
	private static GraphHopper hopper;
	
	public static void init(String graphHopperPath) {
		hopper = new GraphHopperOSM().forDesktop();
		hopper.setGraphHopperLocation(graphHopperPath);
		hopper.setEncodingManager(EncodingManager.create("car"));
		
		hopper.importOrLoad();
		System.out.println("Routing Initialisé");
		/*System.out.print("Petit test: ");
		Route r = RoutingOffline.getRoute(43.2961743,5.3699525, 43.5283, 5.44973);
		System.out.println(r.toStringFormat());*/
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
