package fr.ufr.science.geolocalisation.gestionDonnee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.ufr.science.geolocalisation.model.Personne;
import fr.ufr.science.geolocalisation.util.GestionnairePersonne;


//TODO : MISE A JOUR DES PERSONNES SAUVEGARDÉE
public class SauvegardeCSV 
{
	public void sauvegarde(Personne p, ArrayList<String> numClient) throws IOException
	{
		Iterator<String> it = numClient.iterator();
		boolean dejaPresent=false;
		FileWriter fw = new FileWriter("SauvegardePersonne.csv",true);

		//REGARDE SI LA PERSONNE EST DEJA STOCKÉ DANS LE FICHIER CSV
		while (it.hasNext()) 
		{
			String s = it.next();
			if(p.getNumClient()==null)	//NUMCLIENT DOIT EXISTER, JUSTE UNE SECURITÉE
			{
				dejaPresent=true;
			}
			else if(s.compareTo(p.getNumClient())==0)	//NUMCLIENT DEJA PRESENT
			{
				dejaPresent=true;
			}
		}

		//System.out.println("A");
		if(dejaPresent==false)
		{
			//SAUV INFO BASE
			numClient.add(p.getNumClient());	//AJOUTE NOUVEAU NUMCLIENT A LA LISTE DES DEJA PRESENT
			fw.write(p.getNumClient()+";"+p.getNom()+";"+p.getPrenom()+";"+p.getPays()+";"+p.getVille() + ";" + p.getFichier());

			//SAUV INFO COMP
			Iterator it2 = p.getInfoComplementaires().entrySet().iterator();
			while (it2.hasNext()) 
			{
				Map.Entry pair = (Map.Entry)it2.next();
				fw.write(";");
				fw.write(pair.getKey()+";"+pair.getValue());	//ECRIS NOM INFO COMPLEMENTAIRE PUIS VALEURS DE L'INFO
				//it2.remove(); // avoids a ConcurrentModificationException
			}
		}

		fw.write("\r");
		fw.close();
	}

	public void sauvegardeAll(GestionnairePersonne g) throws IOException
	{
		SauvegardeCSV.resetSauvegarde();
		
		FileWriter fw = new FileWriter("SauvegardePersonne.csv",true);
		for (Entry<String, List<Personne>> entry : g.getGestionnairePersonne().entrySet()) 
		{

			for (Personne p : entry.getValue()) 
			{
				
				fw.write(p.getNumClient()+";"+p.getNom()+";"+p.getPrenom()+";"+p.getPays()+";"+p.getVille() + ";" + p.getFichier());

				//SAUV INFO COMP
				Iterator it2 = p.getInfoComplementaires().entrySet().iterator();
				
				while (it2.hasNext()) 
				{
					Map.Entry pair = (Map.Entry)it2.next();
					fw.write(";");
					fw.write(pair.getKey()+";"+pair.getValue());	//ECRIS NOM INFO COMPLEMENTAIRE PUIS VALEURS DE L'INFO
				}
			}
			fw.write("\r");
		}


		fw.close();
	}

	public void initListeNumclient(ArrayList<String> numclient) throws IOException
	{
		File f = new File("SauvegardePersonne.csv");
		if(!f.exists())
		{
			f.createNewFile();
		}

		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();

		String[] tabChaine;		//POUR SEPARATION DES LIGNES EN ELLEMENT SEPARE PARS ;


		while(line!=null)	//READLINE RENV NULL SI FICHIER LUE EN ENTIER
		{
			tabChaine = line.split(";");	//SEPARATION DANS NOTRE FICHIER
			numclient.add(tabChaine[0]);//PREMIER ELEM LUE EST LE NUMCLIENT

			line = br.readLine();
		}

		br.close();
		fr.close();
	}

	public static void resetSauvegarde()	//RESET DU CSV 
	{
		File f = new File("SauvegardePersonne.csv");
		if(!f.exists())
		{
			return;
		}
		f.delete();
	}

	public static void main(String[] args) throws IOException
	{
		SauvegardeCSV c = new SauvegardeCSV();
		ArrayList<String> tabNumClient = new ArrayList<String>();	//LISTE DE NUMCLIENT DEJA SAUVEGARDE
		c.initListeNumclient(tabNumClient);
		//c.sauvegarde(p, numClient);
		//System.out.println( tabNumClient.get(0));
	}


}


