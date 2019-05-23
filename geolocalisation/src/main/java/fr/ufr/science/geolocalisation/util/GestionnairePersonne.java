package fr.ufr.science.geolocalisation.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.ufr.science.geolocalisation.gestionDonnee.RestaurationCSV;
import fr.ufr.science.geolocalisation.model.Personne;

public class GestionnairePersonne {
	//private List<Personne> listePersonne;
	private Map<String, List<Personne>> dictionnairePersonne;
	private int nbPersonne = 0;
	
	public GestionnairePersonne() {
		this.dictionnairePersonne = new HashMap<>();
	}
	
	public void addPersonne(Personne p) {
		String adresse = p.getVille() + " " + p.getCP();
		//System.out.println(p.getInfoComplementaires().get("Score ISF"));
		if(dictionnairePersonne.get(adresse) != null)
			dictionnairePersonne.get(adresse).add(p);
		else {
			List<Personne> listePersonnes = new ArrayList<>();
			listePersonnes.add(p);
			
			dictionnairePersonne.put(adresse, listePersonnes);
		}
		nbPersonne++;
	}
	
	public Map<String, List<Personne>> getGestionnairePersonne() {
		return dictionnairePersonne;
	}
	
	public void clearPersonne() {
		dictionnairePersonne.clear();
	}

	public int getNbPersonne() {
		return nbPersonne;
	}

	public void setNbPersonne(int nbPersonne) {
		this.nbPersonne = nbPersonne;
	}
	
	
	
}
