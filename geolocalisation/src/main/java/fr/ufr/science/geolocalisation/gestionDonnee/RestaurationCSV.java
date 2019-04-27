package fr.ufr.science.geolocalisation.gestionDonnee;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import fr.ufr.science.geolocalisation.model.Personne;
import fr.ufr.science.geolocalisation.util.GestionnairePersonne;

public class RestaurationCSV 
{
	public void restauration(GestionnairePersonne g) throws IOException
	{
		FileReader fr = new FileReader("SauvegardePersonne.csv");
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		String[] tabChaine;		//POUR SEPARATION DES LIGNES EN ELLEMENT SEPARE PARS ;

		while(line!=null)	//READLINE RENV NULL SI FICHIER LUE EN ENTIER
		{
			tabChaine = line.split(";");	//SEPARATION DANS NOTRE FICHIER
			if(tabChaine.length>=5)
			{
				Personne p = new Personne(tabChaine[0],tabChaine[1],tabChaine[2],tabChaine[4],tabChaine[3]);
				for(int j=5;j<tabChaine.length-1;j++)
				{
					p.getInfoComplementaires().put(tabChaine[j],tabChaine[j+1]);
				}
				g.addPersonne(p);
				//System.out.println(p.getNom()+  " " + p.getPrenom()+ " " + p.getNumClient() +" " + p.getPays()+ " " + p.getVille());
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
		if(gestP.getListePersonne().isEmpty())
		{
		System.out.println("vide");
		}
		r.restauration(gestP);
	}

}
