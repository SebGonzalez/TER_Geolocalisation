package fr.ufr.science.geolocalisation.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ufr.science.geolocalisation.gestionDonnee.RestaurationCSV;
import fr.ufr.science.geolocalisation.model.Personne;

public class GestionnairePersonne {
	//private List<Personne> listePersonne;
	private Map<String, List<Personne>> dictionnairePersonne;
	RestaurationCSV rest = new RestaurationCSV();
	
	
	public GestionnairePersonne() {
		this.dictionnairePersonne = new HashMap<>();
		
		Personne p = new Personne("123", "Pierre", "Sbroggio", "Marseille", "France");
		Personne p2 = new Personne("124", "Alexandre", "Biemar", "Paris", "France");
		Personne p3 = new Personne("125", "Amine", "Boudraa", "Madrid", "Espagne");
		/*addPersonne(p);
		addPersonne(p2);
		addPersonne(p3);*/
	}
	
	public void addPersonne(Personne p) {
		if(dictionnairePersonne.get(p.getVille()) != null)
			dictionnairePersonne.get(p.getVille()).add(p);
		else {
			List<Personne> listePersonnes = new ArrayList<>();
			listePersonnes.add(p);
			
			dictionnairePersonne.put(p.getVille(), listePersonnes);
		}
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
	
}
