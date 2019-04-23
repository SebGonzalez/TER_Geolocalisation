package fr.ufr.science.geolocalisation;

/**
 * Hello world!
 *
 */
public class App 
{
	public static GestionnairePersonne gestionnairePersonne = new GestionnairePersonne();
	
    public static void main( String[] args )
    {
       MainWindow mainWindow = new MainWindow(gestionnairePersonne);
    }
}
