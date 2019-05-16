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
	RestaurationCSV rest = new RestaurationCSV();
	
	
	public GestionnairePersonne() {
		this.dictionnairePersonne = new HashMap<>();
	}
	
	public void addPersonne(Personne p) {
		if(dictionnairePersonne.get(p.getVille()) != null)
			dictionnairePersonne.get(p.getVille()).add(p);
		else {
			List<Personne> listePersonnes = new ArrayList<>();
			listePersonnes.add(p);
			
			dictionnairePersonne.put(p.getVille(), listePersonnes);
		}
		nbPersonne++;
	}
	
	public Map<String, List<Personne>> getGestionnairePersonne() {
		return dictionnairePersonne;
	}
	
	public Personne getPersonneByID(String id) {		// Pour trouver une personne avec son ID (utile pour afficher les infos)
		/*for(Personne p : listePersonne) {
			if(p.getNumClient() == id)
				return p;
		}*/
		return null;
	}

	public int getNbPersonne() {
		return nbPersonne;
	}

	public void setNbPersonne(int nbPersonne) {
		this.nbPersonne = nbPersonne;
	}
	
	
	
}
