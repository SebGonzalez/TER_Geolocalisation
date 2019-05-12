package fr.ufr.science.geolocalisation;

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


	public static void main(String[] args) {
	
		try {
			RestaurationCSV rest = new RestaurationCSV();
			rest.restauration(gestionnairePersonne);
			
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		gestionnaireFiltre = new GestionnaireFiltre(gestionnairePersonne);
		gestionnaireCoordonne = loadCoordonne();
		gestionnaireFichier = loadFichier();
		gestionnaireFichier.loadMarker();
		System.out.println(gestionnaireFichier.getDictionnaire().size());
		MainWindow mainWindow = new MainWindow(gestionnairePersonne, gestionnaireCoordonne, gestionnaireFichier, gestionnaireFiltre);
	}
	
	private static GestionnaireCoordonnee loadCoordonne() {
		GestionnaireCoordonnee gestionnaireCoordonnee = (GestionnaireCoordonnee) Memoire.read("coordonnee.cfg");
		if(gestionnaireCoordonnee != null)
			return gestionnaireCoordonnee;
		return new GestionnaireCoordonnee();
	}
	
	private static GestionnaireFichier loadFichier() {
		GestionnaireFichier gestionnaireFichier = (GestionnaireFichier) Memoire.read("fichiers.cfg");
		if(gestionnaireFichier != null)
			return gestionnaireFichier;
		return new GestionnaireFichier();
	}
}
