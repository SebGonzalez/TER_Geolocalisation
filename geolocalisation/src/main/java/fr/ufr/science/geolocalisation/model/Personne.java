package fr.ufr.science.geolocalisation.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

//serialiser
public class Personne 
{
	private String numClient;
	private String nom;
	private String prenom;
	
	private String ville;
	private String pays;
	
	public HashMap<String, String> infoComplementaires;
	
	private boolean filtre = true;
	
	private String fichier;
	
	public Personne() 
	{
		this.infoComplementaires  = new HashMap<String, String>();
	}

	public Personne(String numClient, String nom, String prenom, String ville, String pays, String fichier) {
		this.numClient = numClient;
		this.nom = nom;
		this.prenom = prenom;
		this.ville = ville;
		this.pays = pays;
		this.fichier = fichier;
		this.infoComplementaires  = new HashMap<String, String>();
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

	public HashMap<String, String> getInfoComplementaires() {
		return infoComplementaires;
	}

	public void setInfoComplementaires(HashMap<String, String> infoComplementaires) {
		this.infoComplementaires = infoComplementaires;
	}
	
	public String getFichier() {
		return fichier;
	}

	public void setFichier(String fichier) {
		this.fichier = fichier;
	}

	@Override
	public String toString() {
		return this.getPrenom() + " " + this.getNom();
	}
	
	public String toStringInfoComplementaire() {
		String chaine = "";
		for (Entry<String, String> entry : infoComplementaires.entrySet()) {
			chaine += entry.getKey() + " : " + entry.getValue() + "\n";
		}
		return chaine;
	}

}
