package fr.ufr.science.geolocalisation;

import javax.swing.UIManager;

/**
 * Hello world!
 *
 */

// sauvegarde dernier lieu
// ajout menu à gauche
// distance par rapport à un lieu
public class App {
	public static GestionnairePersonne gestionnairePersonne = new GestionnairePersonne();

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		MainWindow mainWindow = new MainWindow(gestionnairePersonne);
	}
}
