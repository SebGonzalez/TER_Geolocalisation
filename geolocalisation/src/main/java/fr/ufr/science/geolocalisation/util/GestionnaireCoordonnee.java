package fr.ufr.science.geolocalisation.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import fr.ufr.science.geolocalisation.model.Coordonnee;

public class GestionnaireCoordonnee implements Serializable {
	
	private static final long serialVersionUID = -1445172941609914879L;
	
	private Map<String, Coordonnee> coordonnee;
	
	public GestionnaireCoordonnee() {
		this.coordonnee = new HashMap<>();
	}
	
	public Coordonnee getCoordonnee(String adresse) {
		if(coordonnee.containsKey(adresse)) {
			return coordonnee.get(adresse);
		}
		
		Map<String, Double> coords;
        coords = OpenStreetMapUtils.getInstance().getCoordinates(adresse);
        
        if(coords == null) return null;
        
        Coordonnee c = new Coordonnee(coords.get("lat"), coords.get("lon"));
        coordonnee.put(adresse, c);
        
        return c;
	}
}
