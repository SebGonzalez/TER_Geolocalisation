package fr.ufr.science.geolocalisation;

import java.util.ArrayList;

public class Personne 
{
	private long numClient;
	
	private String Nom;
	private String Prenom;
	
	private String ville;
	private String pays;
	
	ArrayList<String> infoComplementaireNom;
	ArrayList<String> infoComplementaire;

	public long getNumClient() {
		return numClient;
	}

	public void setNumClient(long numClient) {
		this.numClient = numClient;
	}

	public String getNom() {
		return Nom;
	}

	public void setNom(String nom) {
		Nom = nom;
	}

	public String getPrenom() {
		return Prenom;
	}

	public void setPrenom(String prenom) {
		Prenom = prenom;
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
