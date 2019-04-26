package fr.ufr.science.geolocalisation.model;

import java.util.ArrayList;

//serialiser
public class Personne 
{
	private String numClient;
	private String nom;
	private String prenom;
	
	private String ville;
	private String pays;
	
	//hashmap
	ArrayList<String> infoComplementaireNom;
	ArrayList<String> infoComplementaire;
	
	public Personne() {
	}

	public Personne(String numClient, String nom, String prenom, String ville, String pays) {
		this.numClient = numClient;
		this.nom = nom;
		this.prenom = prenom;
		this.ville = ville;
		this.pays = pays;
	}

	public String getNumClient() {
		return numClient;
	}

	public void setNumClient(String numClient) {
		this.numClient = numClient;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public ArrayList<String> getInfoComplementaireNom() {
		return infoComplementaireNom;
	}

	public void setInfoComplementaireNom(ArrayList<String> infoComplementaireNom) {
		this.infoComplementaireNom = infoComplementaireNom;
	}

	public ArrayList<String> getInfoComplementaire() {
		return infoComplementaire;
	}

	public void setInfoComplementaire(ArrayList<String> infoComplementaire) {
		this.infoComplementaire = infoComplementaire;
	}	
}
