package fr.ufr.science.geolocalisation.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Fichier implements Serializable {

	private static final long serialVersionUID = -6996640519456668314L;

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
		if(dictionnaireInfos.get(nom) == null) {
			Set<String> listeInfos = new HashSet<>();
			listeInfos.add(valeur);
			dictionnaireInfos.put(nom, listeInfos);
		} else {
			dictionnaireInfos.get(nom).add(valeur);
		}
	}
	
	public Set<String> listeInfos(String nom) {
		return dictionnaireInfos.get(nom);
	}
	
	public Set<String> getAllTypeInfos() {
		Set<String> typeInfos = new HashSet<>();
		
		System.out.println("ZZZZ");
		for(String s : dictionnaireInfos.keySet()) {
			System.out.println("ZZZZZ : " + s);
			typeInfos.add(s);
		}
		
		return typeInfos;
	}

}
