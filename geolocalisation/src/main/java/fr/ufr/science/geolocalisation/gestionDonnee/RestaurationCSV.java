package fr.ufr.science.geolocalisation.gestionDonnee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import fr.ufr.science.geolocalisation.model.Personne;
import fr.ufr.science.geolocalisation.util.GestionnairePersonne;

public class RestaurationCSV {

	public void restauration(GestionnairePersonne g) throws IOException 
	{
		File f = new File("SauvegardePersonne.csv");
		boolean traitementFiltre = false;	//SERT GERER LES FILTRES

		if (!f.exists()) 
		{
			System.out.println("Pas de fichier CSV, donc pas de données a recuperer");
			return;
		}

		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		String[] tabChaine; // POUR SEPARATION DES LIGNES EN ELLEMENT SEPARE PARS ;

		while (line != null) // READLINE RENV NULL SI FICHIER LUE EN ENTIER
		{
			traitementFiltre=false; //INITIALISATION
			tabChaine = line.split(";"); // SEPARATION DANS NOTRE FICHIER

			if (tabChaine.length >= 6) {
				Personne p = new Personne(tabChaine[0], tabChaine[1], tabChaine[2], tabChaine[4], tabChaine[3], tabChaine[5]);

				for (int j = 6; j < tabChaine.length - 1; j+=2) //INFO COMP SE LISENT 2 PARS 2
				{
					if(tabChaine[j].compareTo("debFiltre")==0)	//LUE LE MOT CLE DE TRAITEMENT DES FILTRES
					{
						//System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
						//System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
						//System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
						//System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
						//System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
						traitementFiltre=true;

					}
					else if(traitementFiltre==true)
					{
						p.addFiltre(tabChaine[j]);
						p.addFiltre(tabChaine[j+1]);
					}
					else
					{
						//System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
						//System.out.println(tabChaine[j] + " : " + tabChaine[j + 1]);
						p.getInfoComplementaires().put(tabChaine[j], tabChaine[j + 1]);
					}

				}


				g.addPersonne(p);
				// System.out.println(p.getNom()+ " " + p.getPrenom()+ " " + p.getNumClient() +"
				// " + p.getPays()+ " " + p.getVille());
			}
			line = br.readLine();
		}

		br.close();
		fr.close();

	}

	public static void main(String[] args) throws IOException 
	{
		RestaurationCSV r = new RestaurationCSV();
		GestionnairePersonne gestP = new GestionnairePersonne();

		if (gestP.getGestionnairePersonne().isEmpty()) {
			System.out.println("vide");
		}
		r.restauration(gestP);
	}

}
