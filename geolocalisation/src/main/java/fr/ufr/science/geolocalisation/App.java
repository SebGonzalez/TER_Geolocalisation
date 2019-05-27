package fr.ufr.science.geolocalisation;

import java.io.File;

import javax.swing.UIManager;

import fr.ufr.science.geolocalisation.IHM.MainWindow;
import fr.ufr.science.geolocalisation.gestionDonnee.Memoire;
import fr.ufr.science.geolocalisation.gestionDonnee.RestaurationCSV;
import fr.ufr.science.geolocalisation.util.GestionnaireCoordonnee;
import fr.ufr.science.geolocalisation.util.GestionnaireFichier;
import fr.ufr.science.geolocalisation.util.GestionnaireFiltre;
import fr.ufr.science.geolocalisation.util.GestionnairePersonne;
import fr.ufr.science.geolocalisation.util.RoutingOffline;

/**
 * Hello world!
 *
 */

//selection multiple de points
//bdd avec import/export
//Ajouter note sur donateur
//Gérer plusieurs filtres
//api route
//marqueur en fonction du type
//export en jar
public class App {
	public static GestionnairePersonne gestionnairePersonne = new GestionnairePersonne();
	public static GestionnaireFichier gestionnaireFichier;
	public static GestionnaireCoordonnee gestionnaireCoordonne;
	public static GestionnaireFiltre gestionnaireFiltre;
	
	private static MainWindow mainWindow;
	private static String path;
	
	public static void main(String[] args) {
	
		setPath();
		try {
			RestaurationCSV rest = new RestaurationCSV();
			rest.restauration(gestionnairePersonne);
			
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		gestionnaireFiltre = loadFiltre();
		gestionnaireCoordonne = loadCoordonne();
		gestionnaireFichier = loadFichier();
		gestionnaireFichier.loadMarker();

		System.out.println(gestionnairePersonne);
		 mainWindow = new MainWindow(gestionnairePersonne, gestionnaireCoordonne, gestionnaireFichier, gestionnaireFiltre);
	}
	
	private static void setPath() {
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("win")) {
			System.out.println("Windows");
			path = System.getProperty("user.home") + "\\AppData\\Roaming\\.UFRGeolocalisation\\";
			System.out.println(path);
		}else if(os.contains("mac")) {
			System.out.println("iOS");
			path = System.getProperty("user.home") + "/Library/Application Support/UFRGeolocalisation/";
			System.out.println(path);
		}
		else {
			System.out.println("Linux");
			path = System.getProperty("user.home") + "/.UFRGeolocalisation/";
			System.out.println(path);
		}

		File directory = new File(path);
		if(!directory.exists()) {
			System.out.println("Creating " + path);
			directory.mkdirs();
			directory = new File(path + "mapCache");
			directory.mkdirs();
			directory = new File(path + "config");
			directory.mkdirs();
		}
	}
	
	public static String getPath() {
		return path;
	}
	
	private static GestionnaireCoordonnee loadCoordonne() {
		GestionnaireCoordonnee gestionnaireCoordonnee = (GestionnaireCoordonnee) Memoire.read(path + "config/coordonnee.cfg");
		if(gestionnaireCoordonnee != null)
			return gestionnaireCoordonnee;
		return new GestionnaireCoordonnee();
	}
	
	private static GestionnaireFiltre loadFiltre() {
		GestionnaireFiltre gestionnaireFiltre = (GestionnaireFiltre) Memoire.read(path + "config/filtres.cfg");
		if(gestionnaireFiltre != null) {
			gestionnaireFiltre.setGestionnairePersonne(gestionnairePersonne);
			return gestionnaireFiltre;
		}
		return new GestionnaireFiltre(gestionnairePersonne);
	}
	
	private static GestionnaireFichier loadFichier() {
		GestionnaireFichier gestionnaireFichier = (GestionnaireFichier) Memoire.read(path + "config/fichiers.cfg");
		if(gestionnaireFichier != null)
			return gestionnaireFichier;
		return new GestionnaireFichier();
	}
}
