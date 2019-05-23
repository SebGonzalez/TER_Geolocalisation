package fr.ufr.science.geolocalisation.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.ufr.science.geolocalisation.model.Filtre;
import fr.ufr.science.geolocalisation.model.Personne;

public class GestionnaireFiltre implements Serializable {

	private static final long serialVersionUID = 2396783057182306173L;

	private transient GestionnairePersonne gestionnairePersonne;
	
	private List<Filtre> listeFiltre;
	private boolean showOthers = true;

	public GestionnaireFiltre(GestionnairePersonne gestionnairePersonne) {
		this.gestionnairePersonne = gestionnairePersonne;
		
		listeFiltre = new ArrayList<>();
	}
	
	public void setGestionnairePersonne(GestionnairePersonne gestionnairePersonne) {
		this.gestionnairePersonne = gestionnairePersonne;
	}
	
	public void ajoutFiltre(String nomFiltre) {
		Filtre filtre = new Filtre(nomFiltre);
		listeFiltre.add(filtre);
	}
	
	public void removeFiltre(String nomFiltre) {
		for(Filtre f : listeFiltre) {
			if(f.getNom().equals(nomFiltre)) {
				listeFiltre.remove(f);
				break;
			}
		}
		
		for (Entry<String, List<Personne>> entry : gestionnairePersonne.getGestionnairePersonne().entrySet()) {
			for(Personne p : entry.getValue()) {
				p.removeFiltre(nomFiltre);
			}
		}
	}
	
	public void ajoutFiltrePersonne(String nomFiltre, String value, boolean ajout) {
		for(Entry<String, List<Personne>> entry : gestionnairePersonne.getGestionnairePersonne().entrySet()) {
			for(Personne p : entry.getValue()) {
				p.checkFiltre(nomFiltre, value, ajout);
			}
		}
	}
	
	public void ajoutInfosFiltre(String nomFiltre, List<String> infosFiltre) {
		for(Filtre f : listeFiltre) {
			if(f.getNom().equals(nomFiltre)) {
				for(String s : infosFiltre) {
					f.addInfos(s);
				}
			}
		}
	}
	
	public Map<String, Boolean> getInfosFiltre(String nomFiltre) {
		for(Filtre f : listeFiltre) {
			if(f.getNom().equals(nomFiltre)) {
				return f.getDictionnaireInfos();
			}
		}
		return null;
	}
	
	public int nbFiltres() {
		return listeFiltre.size();
	}
	
	public List<Filtre> getFiltre() {
		return listeFiltre;
	}

	public void setVisibilityFile(String nomFiltre, boolean visibility) {
		for(Filtre f : listeFiltre) {
			if(f.getNom().equals(nomFiltre)) {
				f.setAfficher(visibility);
				return;
			}
		}
	}

	public String showPersonne(Personne personne) {		
		for (Filtre f : listeFiltre) {
			if(f.isAfficher() && personne.containsFiltre(f.getNom())) return "filtre";
		}
		
		if(showOthers)
			return "others";
		
		return "false";
	}
	
	public void setShowOthers(boolean visibility) {
		showOthers = visibility;
	}
	
	public boolean showOthers() {
		return showOthers;
	}
	
	public void clearFiltre() {
		this.listeFiltre.clear();
	}
	
}
