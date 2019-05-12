package fr.ufr.science.geolocalisation.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.ufr.science.geolocalisation.model.Personne;

public class GestionnaireFiltre {

	GestionnairePersonne gestionnairePersonne;
	
	private Map<String, Boolean> dictionnaireFiltres;
	private boolean showOthers = true;

	public GestionnaireFiltre(GestionnairePersonne gestionnairePersonne) {
		this.gestionnairePersonne = gestionnairePersonne;
		
		dictionnaireFiltres = new HashMap<>();
	}
	
	public void ajoutFiltre(String nomFiltre) {
		dictionnaireFiltres.put(nomFiltre, true);
	}
	
	public void removeFiltre(String nomFiltre) {
		dictionnaireFiltres.remove(nomFiltre);
		
		for (Entry<String, List<Personne>> entry : gestionnairePersonne.getGestionnairePersonne().entrySet()) {
			for(Personne p : entry.getValue()) {
				p.removeFiltre(nomFiltre);
			}
		}
	}
	
	public int nbFiltres() {
		return dictionnaireFiltres.size();
	}
	
	public Map<String, Boolean> getFiltre() {
		return dictionnaireFiltres;
	}

	public void setVisibilityFile(String nomFiltre, boolean visibility) {
		dictionnaireFiltres.put(nomFiltre, visibility);
	}

	public boolean showPersonne(Personne personne) {
		if(showOthers)
			return true;
		
		for (Entry<String, Boolean> entry : dictionnaireFiltres.entrySet()) {
			if(entry.getValue() && personne.containsFiltre(entry.getKey())) return true;
		}
		
		return false;
	}
	
	public void setShowOthers(boolean visibility) {
		showOthers = visibility;
	}
	
	public boolean showOthers() {
		return showOthers;
	}
	
}
