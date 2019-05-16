package fr.ufr.science.geolocalisation.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;

import fr.ufr.science.geolocalisation.model.Fichier;

public class GestionnaireFichier implements Serializable {
	private static final long serialVersionUID = -1445172941609914879L;
	
	private transient GestionnaireMarker gestionnaireMarker;
	
	private List<Fichier> listeFichier;
	
	public GestionnaireFichier() {
		listeFichier = new ArrayList<>();
	}
	
	public void loadMarker() {
		gestionnaireMarker = new GestionnaireMarker();
		for ( Fichier f : listeFichier ) {
		   gestionnaireMarker.ajoutFichier(f.getNom());
		}
	}
	
	public void ajoutFichier(String nomFichier) {
		Fichier fichier = new Fichier(nomFichier);
		this.listeFichier.add(fichier);
		gestionnaireMarker.ajoutFichier(nomFichier);
	}
	
	public void setVisibilityFile(String nomFichier, boolean visible) {
		for(Fichier f : listeFichier) {
			if(f.getNom().equals(nomFichier)) {
				f.setAfficher(visible);
				return;
			}
		}
	}
	
	public boolean getVisibilityFile(String nomFichier) {
		for(Fichier f : listeFichier) {
			if(f.getNom().equals(nomFichier)) {
				return f.isAfficher();
			}
		}
		return false;
	}
	
	public List<Fichier> getListFichier() {
		return listeFichier;
	}
	
	public Icon getIcon(String nomFichier) {
		return gestionnaireMarker.getIcon(nomFichier);
	}

	public Icon getIconFiltre() {
		return gestionnaireMarker.getIconFiltre();
	}
	
	public void ajoutInfo(String nomFichier, String nomInfo, String valeurInfo) {
		for(Fichier f : listeFichier) {
			if(f.getNom().equals(nomFichier)) {
				f.ajouterInfo(nomInfo, valeurInfo);
				return;
			}
		}
	}
	
	public List<String> getAllTypeInfos() {
		Set<String> typeInfos = new HashSet<>();
		for(Fichier f : listeFichier) {
			typeInfos.addAll(f.getAllTypeInfos());
		}
		
		for(String s : typeInfos) {
			System.out.println("CCCC : " + s);
		}
		List<String> liste = new ArrayList<>(typeInfos);
		for(String s : liste) {
			System.out.println("BBBB : " + s);
		}
		
		return new ArrayList<>(typeInfos);
	}
}
