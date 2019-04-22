package fr.ufr.science.geolocalisation;

public class Personne 
{
	private long numClient;
	
	private String Nom;
	private String Prenom;
	
	private String ville;
	private String pays;
	
	private String[][] infoComplementaire;

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

	public String[][] getInfoComplementaire() {
		return infoComplementaire;
	}

	public void setInfoComplementaire(String[][] infoComplementaire) {
		this.infoComplementaire = infoComplementaire;
	}

	
	
}
