package fr.ufr.science.geolocalisation.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Filtre implements Serializable {
	
	private static final long serialVersionUID = 2396783057182306173L;
	
	private String nom;
	private boolean afficher;
	private Map<String, Boolean> dictionnaireInfos;
	
	public Filtre(String nom) {
		super();
		this.nom = nom;
		this.afficher = true;
		dictionnaireInfos = new HashMap<>();
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public boolean isAfficher() {
		return afficher;
	}

	public void setAfficher(boolean afficher) {
		this.afficher = afficher;
	}
	
	public Map<String, Boolean> getDictionnaireInfos() {
		return dictionnaireInfos;
	}

	public void addInfos(String valeurInfo) {
		dictionnaireInfos.put(valeurInfo, false);
	}
	
	public void setVisibilityInfo(String valeurInfo, boolean visible) {
		dictionnaireInfos.put(valeurInfo, visible);
	}
	
	public boolean getVisibilitInfo(String valeurInfo) {
		return dictionnaireInfos.get(valeurInfo);
	}
	
	
}
