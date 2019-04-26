package fr.ufr.science.geolocalisation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


//TODO : CHARGER LES FICHIER ET VERIF SI NUMCLIENT EXISTE
//TODO : SAUVEGARDE DE CHAQUE NUMCLIENT
public class SauvegardeCSV 
{
	public void sauvegarde(Personne p, ArrayList<String> numClient) throws IOException
	{
		//File f = new File("SauvegardePersonne.csv");
		Iterator<String> it = numClient.iterator();
		boolean dejaPresent=false;
		FileWriter fw = new FileWriter("SauvegardePersonne.csv",true);

		while (it.hasNext()) 
		{
			String s = it.next();
			if(p.getNumClient()==null)
			{
				dejaPresent=true;
			}
			else if(s.compareTo(p.getNumClient())==0)	//NUMCLIENT DEJA PRESENT
			{
				dejaPresent=true;
			}
		}

		if(dejaPresent==false)
		{
			numClient.add(p.getNumClient());	//AJOUTE NOUVEAU NUMCLIENT A LA LISTE DES DEJA PRESENT
			fw.write(p.getNumClient()+";"+p.getNom()+";"+p.getPrenom()+";"+p.getPays()+";"+p.getVille());
			//fw.write(p.getNumClient());
		}

		fw.write("\r");
		fw.close();
	}

	public void initListeNumclient(ArrayList<String> numclient) throws IOException
	{
		FileReader fr = new FileReader("SauvegardePersonne.csv");
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();

		String[] tabChaine;		//POUR SEPARATION DES LIGNES EN ELLEMENT SEPARE PARS ;
		
		//System.out.println("AA");
		
		while(line!=null)	//READLINE RENV NULL SI FICHIER LUE EN ENTIER
		{
			//System.out.println("AA");
			tabChaine = line.split(";");	//SEPARATION DANS NOTRE FICHIER

			//for(int i=0;i<tabChaine.length-1;i++)	
			//{
				numclient.add(tabChaine[0]);//PREMIER ELEM LUE EST LE NUMCLIENT
			//}
			line = br.readLine();
		}

		br.close();
		fr.close();
	}

	/*public int tailleTabNumclient() throws FileNotFoundException
	{
		File f = new File("SauvegardePersonne.csv");
		if(!f.isFile())
		{ 
			//System.out.println("Fichier ")
			return -1;
		}


		return -1;
	}*/

	public static void main(String[] args) throws IOException
	{
		SauvegardeCSV c = new SauvegardeCSV();
		ArrayList<String> tabNumClient = new ArrayList<String>();	//LISTE DE NUMCLIENT DEJA SAUVEGARDE
		c.initListeNumclient(tabNumClient);
		System.out.println( tabNumClient.get(0));	
	}
	

}


