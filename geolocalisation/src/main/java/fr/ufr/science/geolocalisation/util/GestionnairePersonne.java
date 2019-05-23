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
		//System.out.println(p.getInfoComplementaires().get("Score ISF"));
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
