package fr.ufr.science.geolocalisation.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import fr.ufr.science.geolocalisation.App;
import fr.ufr.science.geolocalisation.model.Coordonnee;
import fr.ufr.science.geolocalisation.model.Personne;

//Possibilité d'ajouter une hashmap qui stocke les adresse-->coord
public class OpenStreetMapUtils {

	private static OpenStreetMapUtils instance = null;

	public static OpenStreetMapUtils getInstance() {
		if (instance == null) {
			instance = new OpenStreetMapUtils();
		}
		return instance;
	}

	private String getRequest(String url) throws Exception {

		System.out.println(url);

		final URL obj = new URL(url);
		final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		if (con.getResponseCode() != 200) {
			System.out.println("NON" + con.getResponseCode());
			//return null;
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		System.out.println("Reponse " + response);

		return response.toString();
	}

	public Map<String, Double> getCoordinates(String address) {
		Map<String, Double> res;
		StringBuffer query;
		String[] split = address.split(" ");
		String queryResult = null;

		query = new StringBuffer();
		res = new HashMap<String, Double>();

		query.append("https://nominatim.openstreetmap.org/search?q=");

		if (split.length == 0) {
			return null;
		}

		for (int i = 0; i < split.length; i++) {
			query.append(split[i]);
			if (i < (split.length - 1)) {
				query.append("+");
			}
		}
		query.append("&format=json&addressdetails=1");

		try {
			queryResult = getRequest(query.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (queryResult == null) {
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
		}

		return res;
	}
	
	public static double distance(double lat1, double lon1, double lat2,
	        double lon2) {

	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    distance = Math.pow(distance, 2);

	    return Math.sqrt(distance) / 1000;
	}
	
	public static List<Personne> filtreDistance(String adresse, int distance) {
		List<Personne> listePersonneFiltre = new ArrayList<>();
		
		Coordonnee c = App.gestionnaireCoordonne.getCoordonnee(adresse);
		
		for(Personne p : App.gestionnairePersonne.getListePersonne()) {
			Coordonnee c2 = App.gestionnaireCoordonne.getCoordonnee(p.getVille());
			if(distance(c.getLat(), c.getLon(), c2.getLat(), c2.getLon()) <= distance) {
				System.out.println("Ajout de : " + p.getNom() + " " + p.getPrenom() + " " + p.getVille());
				listePersonneFiltre.add(p);
			}
		}
		
		return listePersonneFiltre;
	}
}
