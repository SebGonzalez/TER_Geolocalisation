package fr.ufr.science.geolocalisation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class Extraction 
{
	public void readFile(File file) throws IOException
	{
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		int i;
		int nbValeurs = 1;	//COMPTEUR NOMBRE DE COLONNES 
		//int cpt = 0;

		boolean prem = true;	//FLAG POUR LA PREMIERE CHAINE
		boolean nonCop = true;	//FLAG NON COPIE EN DOUBLE DES COLONNE DU INSERT

		String line = br.readLine();	//INITIALISATION DE LECTURE FICHIER
		String[] tabChaine;

		int[] tab = new int[1000];
		int cptTab = 0;
		String infoColonne[] = new String[1000];

		for(i=0;i<tab.length-1;i++)
		{
			tab[i]=-1;
		}

		while(line!=null)	//READLINE RENV NULL SI FICHIER LUE EN ENTIER
		{

			Personne p = new Personne(); //CREATION NOUVELLE PERSONNE

			tabChaine = line.split(",");	//SEPARATION STANDART EN CSV
			//System.out.println(line);
			if(prem==true)	//PREMIERE LIGNE -> INISTIALISATION
			{
				for(i=0;i<tabChaine.length-1;i++)	//PREMIERE LIGNE DOIT FAIRE CREATE ICI
				{
					//System.out.println(tabChaine[i]);
					if(tabChaine[i].compareTo("NUMCLI")==0)
					//if(tabChaine[i]=="NUMCLI")
					{
						//System.out.println(tabChaine[i]);
						//p.setNumClient(Long.parseLong(tabChaine[i]));	//CONVERSION NUMCLIENT EN LONG
						tab[0]=i;
						infoColonne[cptTab]="NUMCLI";
					}
					else if(tabChaine[i].compareTo("NOM")==0)
					{	
						//p.setNom(tabChaine[i]);
						//System.out.println(tabChaine[i]);
						tab[1]=i;
						infoColonne[cptTab]="NOM";
					}
					else if(tabChaine[i].compareTo("PRENOM")==0)
					{
						//p.setPrenom(tabChaine[i]);
						tab[2]=i;
						infoColonne[cptTab]="PRENOM";
					}
					else if(tabChaine[i].compareTo("VILLE")==0)
					{
						//p.setVille(tabChaine[i]);
						tab[3]=i;
						infoColonne[cptTab]="VILLE";
					}
					else if(tabChaine[i].compareTo("PAYS")==0)
					{
						//p.setPays(tabChaine[i]);
						tab[4]=i;
						infoColonne[cptTab]="PAYS";
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
							//System.out.println(tab[i]);
							p.setVille(tabChaine[tab[3]]);
						}
						else if(i==4)
						{
							p.setPays(tabChaine[tab[4]]);
						}

					}

					else
					{
						//if(tabChaine[i].compareTo("")!=0)
						//p.infoComplementaire.add(tabChaine[i]);
					}
				}

			}
			line = br.readLine();
			//if(br.readLine==null) {break;}
			
			
		System.out.println(p.getNom()+ " " + p.getPays()+ " " + p.getPrenom()+ " " + p.getVille());
			//System.out.println(p.getPays());
		
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
