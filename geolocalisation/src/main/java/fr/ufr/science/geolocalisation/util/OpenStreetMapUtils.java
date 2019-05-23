package fr.ufr.science.geolocalisation.util;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import fr.ufr.science.geolocalisation.App;
import fr.ufr.science.geolocalisation.IHM.MainWindow;
import fr.ufr.science.geolocalisation.model.Coordonnee;
import fr.ufr.science.geolocalisation.model.Personne;


//Possibilité d'ajouter une hashmap qui stocke les adresse-->coord
public class OpenStreetMapUtils {

	private static OpenStreetMapUtils instance = null;
	private static boolean offlineRoutingInitialized = false;
	
	public static OpenStreetMapUtils getInstance() {
		if (instance == null) {
			instance = new OpenStreetMapUtils();
		}
		return instance;
	}

	public static void setOfflineRoutingInitialized(boolean offlineRoutingInitialized) {
		OpenStreetMapUtils.offlineRoutingInitialized = offlineRoutingInitialized;
	}

	private String getRequest(String url) throws Exception {

		final URL obj = new URL(url);
		final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		if (con.getResponseCode() != 200) {
			System.out.println("NON" + con.getResponseCode());
			System.out.println(url);
			return null;
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		if(response.toString().equals("[]")) {
			System.out.println(url);
			System.out.println("Reponse " + response);
		}

		return response.toString();
	}

	public Map<String, Double> getCoordinates(String address) {
		
		String address2 = address.replaceAll(" ", "+");
		if(address2.toUpperCase().contains("CEDEX"))
			address2 = address2.split("\\+")[0];
		
		Map<String, Double> res;
		StringBuffer query;

		String queryResult = null;

		query = new StringBuffer();
		res = new HashMap<String, Double>();

		query.append("https://nominatim.openstreetmap.org/search?q=");

		query.append(address2);

		query.append("&format=json&addressdetails=1");

		try {
			queryResult = getRequest(query.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (queryResult == null || queryResult.equals("[]")) {
			return null;
		}

		Object obj = JSONValue.parse(queryResult);

		if (obj instanceof JSONArray) {
			JSONArray array = (JSONArray) obj;
			if (array.size() > 0) {
				JSONObject jsonObject = (JSONObject) array.get(0);

				String lon = (String) jsonObject.get("lon");
				String lat = (String) jsonObject.get("lat");

				res.put("lon", Double.parseDouble(lon));
				res.put("lat", Double.parseDouble(lat));

			}
			else return null;
		}

		return res;
	}

	public static double distance(double lat1, double lon1, double lat2, double lon2) {

		final int R = 6371; // Radius of the earth

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c * 1000; // convert to meters

		distance = Math.pow(distance, 2);

		return Math.sqrt(distance) / 1000;
	}

	public int distanceRoute(double lat1, double lon1, double lat2, double lon2) {
		StringBuffer query = new StringBuffer();
		query.append(
				"https://api.openrouteservice.org/directions?api_key=5b3ce3597851110001cf62486e202f9025104ba4a1316768741fa03b&profile=driving-car&units=km&language=fr&geometry=false&instructions=false&roundabout_exits=false&maneuvers=false&optimized=false&continue_straight=false&suppress_warnings=true&format=json");
		query.append("&coordinates=" + lon1 + "," + lat1 + "|" + lon2 + "," + lat2);

		String result = null;
		try {
			result = getRequest(query.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (result != null) {
			Object obj = JSONValue.parse(result);

			Object distance;

			JSONObject object = (JSONObject) obj;

			JSONArray object2 = (JSONArray) object.get("routes");

			JSONObject object3 = (JSONObject) object2.get(0);
			JSONObject object4 = (JSONObject) object3.get("summary");

			distance = object4.get("distance");
			Object temps = object4.get("duration");

			Double valeur = new Double(distance.toString());

			return valeur.intValue();
		}
		return 999;
	}

	public void filtreDistance(JFrame frame, String adresse, int distance) {
		
		Task task = new Task(frame, adresse, distance);
		task.execute();
	}

	class Task extends SwingWorker<List<Personne>, Void> {
		ProgressMonitor progressMonitor;
		String adresse;
		int distance;
		int progress = 0;
		List<Personne> listePersonneFiltre = new ArrayList<>();
		
		MainWindow frame;

		public Task(JFrame frame, String adresse, int distance) {
			progressMonitor = new ProgressMonitor(frame, "Calcul en cours", "Initialisation...", 0,
					App.gestionnairePersonne.getGestionnairePersonne().size());
			this.adresse = adresse;
			this.distance = distance;
			this.frame = (MainWindow) frame;
		}

		@Override
		public List<Personne> doInBackground() { 

			Coordonnee c = App.gestionnaireCoordonne.getCoordonnee(adresse);

			for (Entry<String, List<Personne>> entry : App.gestionnairePersonne.getGestionnairePersonne().entrySet()) {
				Coordonnee c2 = App.gestionnaireCoordonne.getCoordonnee(entry.getKey());
				
				if(!offlineRoutingInitialized || RoutingOffline.getRoute(c.getLat(), c.getLon(), c2.getLat(), c2.getLon()).getDistance() == -1) {
					if (distance(c.getLat(), c.getLon(), c2.getLat(), c2.getLon()) <= distance && distanceRoute(c.getLat(), c.getLon(), c2.getLat(), c2.getLon()) <= distance) {
						for (Personne p : entry.getValue()) {
							System.out.println("Ajout de : " + p.getNom() + " " + p.getPrenom() + " " + p.getVille());
							listePersonneFiltre.add(p);
							p.addFiltre("distance_" + adresse + "_" + distance);
						}
					}
				}else if (RoutingOffline.getRoute(c.getLat(), c.getLon(), c2.getLat(), c2.getLon()).getDistance()/1000 <= distance) {
					for (Personne p : entry.getValue()) {
						System.out.println("Ajout de : " + p.getNom() + " " + p.getPrenom() + " " + p.getVille());
						listePersonneFiltre.add(p);
						p.addFiltre("distance_" + adresse + "_" + distance);
					}
				}
				
				progress++;
				progressMonitor.setProgress(progress);
				progressMonitor.setNote("Avancement de " + progress + "/" + progressMonitor.getMaximum());
			}
			
			return listePersonneFiltre;
		}

		@Override
		public void done() {
			frame.menu.removeAll();
			frame.initComponents();
			frame.printWaypoints();
			
			Personne[] array = new Personne[listePersonneFiltre.size()];
			listePersonneFiltre.toArray(array);
			Toolkit.getDefaultToolkit().beep();
			progressMonitor.close();
		}
	}
}
