package fr.ufr.science.geolocalisation.model;

import java.util.ArrayList;
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
	private String CP;
	
	private HashMap<String, String> infoComplementaires;
	
	private List<String> filtre;
	
	private String fichier;
	
	public Personne() 
	{
		this.infoComplementaires  = new HashMap<String, String>();
		filtre = new ArrayList<>();
	}

	public Personne(String numClient, String nom, String prenom, String ville, String cp, String fichier) {
		this.numClient = numClient;
		this.nom = nom;
		this.prenom = prenom;
		this.ville = ville;
		this.CP = cp;
		this.fichier = fichier;
		this.infoComplementaires  = new HashMap<String, String>();
		filtre = new ArrayList<>();
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

	public String getCP() {
		return CP;
	}

	public void setCP(String cp) {
		this.CP = cp;
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
	
	public void addFiltre(String nomFiltre) {
		this.filtre.add(nomFiltre);
	}
	
	public void removeFiltre(String nomFiltre) {
		this.filtre.remove(nomFiltre);
	}
	
	public boolean containsFiltre(String nomFiltre) {
		return filtre.contains(nomFiltre);
	}
	

	public List<String> getFiltre()
	{
		return this.filtre;
	}

	public void checkFiltre(String nomFiltre, String value, boolean ajout) {
		if(infoComplementaires.get(nomFiltre).equals(value)) {
			if(ajout) {
				this.addFiltre(nomFiltre);
			}
			else {
				this.removeFiltre(nomFiltre);
			}
		}

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
