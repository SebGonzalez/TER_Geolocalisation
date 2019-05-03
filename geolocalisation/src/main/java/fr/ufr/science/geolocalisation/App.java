package fr.ufr.science.geolocalisation;

import javax.swing.UIManager;

import fr.ufr.science.geolocalisation.IHM.MainWindow;
import fr.ufr.science.geolocalisation.gestionDonnee.RestaurationCSV;
import fr.ufr.science.geolocalisation.util.GestionnaireCoordonnee;
import fr.ufr.science.geolocalisation.util.GestionnairePersonne;

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
	public static GestionnaireCoordonnee gestionnaireCoordonne = new GestionnaireCoordonnee();


	public static void main(String[] args) {
	
		try {
			RestaurationCSV rest = new RestaurationCSV();
			rest.restauration(gestionnairePersonne);
			
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		MainWindow mainWindow = new MainWindow(gestionnairePersonne, gestionnaireCoordonne);
	}
}
