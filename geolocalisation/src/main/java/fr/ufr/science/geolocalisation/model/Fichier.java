package fr.ufr.science.geolocalisation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Fichier {

	private static int compteurId = 1;
	
	private int id;
	private String nom;
	
	private boolean afficher;
	private Map<String, Set<String>> dictionnaireInfos;
	
	public Fichier(String nom) {
		id = compteurId++;
		this.nom = nom;
		afficher = true;
		dictionnaireInfos = new HashMap<>();
	}
	
	public int getId() {
		return id;
	}
	
	public String getNom() {
		return nom;
	}

	public boolean isAfficher() {
		return afficher;
	}

	public void setAfficher(boolean afficher) {
		this.afficher = afficher;
	}
	
	public void ajouterInfo(String nom, String valeur) {
		if(dictionnaireInfos.get(nom) != null) {
			Set<String> listeInfos = new HashSet<>();
			listeInfos.add(valeur);
			dictionnaireInfos.put(nom, listeInfos);
		} else {
			dictionnaireInfos.get(nom).add(valeur);
		}
	}

}
