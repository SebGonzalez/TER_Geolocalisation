package fr.ufr.science.geolocalisation;

/**
 * Hello world!
 *
 */

//sauvegarde dernier lieu
//ajout menu à gauche
//distance par rapport à un lieu
public class App 
{
	public static GestionnairePersonne gestionnairePersonne = new GestionnairePersonne();
	
    public static void main( String[] args )
    {
       MainWindow mainWindow = new MainWindow(gestionnairePersonne);
    }
}
