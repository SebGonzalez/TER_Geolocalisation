package fr.ufr.science.geolocalisation.util;

import java.util.HashMap;
import java.util.Map;

import fr.ufr.science.geolocalisation.model.Coordonnee;

public class GestionnaireCoordonnee {
	
	Map<String, Coordonnee> coordonnee;
	
	public GestionnaireCoordonnee() {
		this.coordonnee = new HashMap<>();
	}
	
	public Coordonnee getCoordonnee(String adresse) {
		if(coordonnee.containsKey(adresse)) {
			return coordonnee.get(adresse);
		}
		
		Map<String, Double> coords;
        coords = OpenStreetMapUtils.getInstance().getCoordinates(adresse);
        
        Coordonnee c = new Coordonnee(coords.get("lat"), coords.get("lon"));
        coordonnee.put(adresse, c);
        
        return c;
	}
}
