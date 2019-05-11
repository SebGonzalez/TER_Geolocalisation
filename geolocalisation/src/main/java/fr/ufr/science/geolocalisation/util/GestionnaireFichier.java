package fr.ufr.science.geolocalisation.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

public class GestionnaireFichier implements Serializable {
	private static final long serialVersionUID = -1445172941609914879L;
	
	private transient GestionnaireMarker gestionnaireMarker;
	
	private Map<String, Boolean> dictionnaireFichier;
	
	public GestionnaireFichier() {
		dictionnaireFichier = new HashMap<>();
	}
	
	public void loadMarker() {
		gestionnaireMarker = new GestionnaireMarker();
		for ( String key : dictionnaireFichier.keySet() ) {
		   gestionnaireMarker.ajoutFichier(key);
		}
	}
	
	public void ajoutFichier(String nomFichier) {
		this.dictionnaireFichier.put(nomFichier, true);
		gestionnaireMarker.ajoutFichier(nomFichier);
		System.out.println("Ajout fichier");
	}
	
	public void setVisibilityFile(String nomFichier, boolean visible) {
		dictionnaireFichier.put(nomFichier, visible);
	}
	
	public boolean getVisibilityFile(String nomFichier) {
		return dictionnaireFichier.get(nomFichier);
	}

	public Map<String, Boolean> getDictionnaire() {
		return dictionnaireFichier;
	}
	
	public Icon getIcon(String nomFichier) {
		return gestionnaireMarker.getIcon(nomFichier);
	}
}
