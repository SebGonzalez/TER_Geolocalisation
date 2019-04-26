package fr.ufr.science.geolocalisation.gestionDonnee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import fr.ufr.science.geolocalisation.model.Personne;

public class Extraction 
{
	public void readFile(File file) throws IOException
	{
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		int i;

		boolean prem = true;	//FLAG POUR LA PREMIERE LIGNE

		String line = br.readLine();	//INITIALISATION DE LECTURE FICHIER
		String[] tabChaine;

		int[] tab = new int[1000];

		for(i=0;i<tab.length-1;i++)	//INIT SECURITE
		{
			tab[i]=-1;
		}

		while(line!=null)	//READLINE RENV NULL SI FICHIER LUE EN ENTIER
		{

			Personne p = new Personne(); //CREATION NOUVELLE PERSONNE

			tabChaine = line.split(",");	//SEPARATION STANDART EN CSV CONVERTIT DE XLSX
			if(prem==true)	//PREMIERE LIGNE -> INISTIALISATION
			{
				for(i=0;i<tabChaine.length-1;i++)	//PREMIERE LIGNE DOIT FAIRE CREATE ICI
				{
					if(tabChaine[i].compareTo("NUMCLI")==0)
					{
						//p.setNumClient(Long.parseLong(tabChaine[i]));	//CONVERSION NUMCLIENT EN LONG
						tab[0]=i;
					}
					else if(tabChaine[i].compareTo("NOM")==0)
					{	
						tab[1]=i;
					}
					else if(tabChaine[i].compareTo("PRENOM")==0)
					{
						tab[2]=i;
					}
					else if(tabChaine[i].compareTo("VILLE")==0)
					{
						tab[3]=i;
					}
					else if(tabChaine[i].compareTo("PAYS")==0)
					{
						tab[4]=i;
					}
					else
					{
						//p.infoComplementaireNom.add(tabChaine[i]);
					}

				}
				prem=false;
			}
			else
			{
				for(i=0;i<tabChaine.length-1;i++)	//PREMIERE LIGNE DOIT FAIRE CREATE ICI
				{
					if(tab[i]!=-1)
					{
						if(i==0)	//0 PLACE DE NUMCLI
						{
							//p.setNumClient(Long.parseLong(tabChaine[0]));	//CONVERSION NUMCLIENT EN LONG
						}
						else if(i==1)
						{
							p.setNom(tabChaine[tab[1]]);
						}
						else if(i==2)
						{
							p.setPrenom(tabChaine[tab[2]]);
						}
						else if(i==3)
						{
							p.setVille(tabChaine[tab[3]]);
						}
						else if(i==4)
						{
							p.setPays(tabChaine[tab[4]]);
						}

					}

					else
					{
						//p.infoComplementaire.add(tabChaine[i]);
					}
				}

			}
			line = br.readLine();

			System.out.println(p.getNom()+ " " + p.getPays()+ " " + p.getPrenom()+ " " + p.getVille());

		}
		br.close();
		fr.close();
	}

	public static void main(String[] args) throws IOException
	{
		Extraction ec = new Extraction();
		File f = new File("Exple-mouvements-BDD-Grands-Mécènes.csv");
		ec.readFile(f);
	}


}
