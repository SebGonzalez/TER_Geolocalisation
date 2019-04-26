package fr.ufr.science.geolocalisation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
			//numclient.add(tabChaine[0]);	//PREMIER ELEM LUE EST LE NUMCLIENT
			if(tabChaine.length>=4)
			{
				Personne p = new Personne(tabChaine[0],tabChaine[1],tabChaine[2],tabChaine[3],tabChaine[4]);
				System.out.println(p.getNom()+  " " + p.getPrenom()+ " " + p.getNumClient() +" " + p.getPays()+ " " + p.getVille());
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

		r.restauration(gestP);
	}

}
