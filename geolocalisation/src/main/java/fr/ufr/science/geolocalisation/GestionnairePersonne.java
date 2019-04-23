package fr.ufr.science.geolocalisation;

import java.util.ArrayList;
import java.util.List;

public class GestionnairePersonne {
	private List<Personne> listePersonne;
	
	public GestionnairePersonne() {
		this.listePersonne = new ArrayList<Personne>();
		
		Personne p = new Personne("123", "Pierre", "Sbroggio", "Marseille", "France");
		Personne p2 = new Personne("124", "Alexandre", "Biemar", "Paris", "France");
		addPersonne(p);
		addPersonne(p2);
	}
	
	public void addPersonne(Personne p) {
		listePersonne.add(p);
	}
	
	public List<Personne> getListePersonne() {
		return listePersonne;
	}
	
}
