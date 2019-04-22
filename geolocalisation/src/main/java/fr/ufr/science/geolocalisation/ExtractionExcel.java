package fr.ufr.science.geolocalisation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class ExtractionExcel 
{
	public void readFile(File file) throws IOException
	{
		//FileReader fr = new FileReader(file);
		//BufferedReader br = new BufferedReader(fr);

		//File excelFile = new File("contacts.xlsx");
		FileInputStream fis = new FileInputStream(file);

		// we create an XSSF Workbook object for our XLSX Excel File
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		// we get first sheet
		XSSFSheet sheet = workbook.getSheetAt(0);

		// we iterate on rows
		Iterator<Row> rowIt = sheet.iterator();

		int i;
		int nbValeurs = 1;	//COMPTEUR NOMBRE DE COLONNES 
		//int cpt = 0;

		boolean prem = true;	//FLAG POUR LA PREMIERE CHAINE
		boolean nonCop = true;	//FLAG NON COPIE EN DOUBLE DES COLONNE DU INSERT

		//String line = br.readLine();	//INITIALISATION DE LECTURE FICHIER
		String[] tabChaine = new String[1000];

		int[] tab = new int[1000];
		int cptTab = 0;
		String infoColonne[] = new String[1000];

		for(i=0;i<tab.length-1;i++)
		{
			tab[i]=-1;
		}

		while(rowIt.hasNext())	//READLINE RENV NULL SI FICHIER LUE EN ENTIER
		{

			Personne p = new Personne(); //CREATION NOUVELLE PERSONNE
			Row row = rowIt.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			i=0;

			//tabChaine = line.split(",");	//SEPARATION STANDART EN CSV
			//System.out.println(line);



			if(prem==true)	//PREMIERE LIGNE -> INISTIALISATION
			{
				i=0;
				while (cellIterator.hasNext()) 	//PREMIERE LIGNE DOIT FAIRE CREATE ICI
				{
					Cell cell = cellIterator.next();
					//System.out.println(cell.toString());
					if(cell.toString().compareTo("NUMCLI")==0)
						//if(tabChaine[i]=="NUMCLI")
					{
						//System.out.println(tabChaine[i]);
						//p.setNumClient(Long.parseLong(tabChaine[i]));	//CONVERSION NUMCLIENT EN LONG
						tab[0]=i;
						//System.out.println("A");
						//infoColonne[cptTab]="NUMCLI";
					}
					else if(cell.toString().compareTo("NOM")==0)
					{	
						//p.setNom(tabChaine[i]);
						//System.out.println(tabChaine[i]);
						tab[1]=i;
						//System.out.println(i);
						//infoColonne[cptTab]="NOM";
					}
					else if(cell.toString().compareTo("PRENOM")==0)
					{
						//p.setPrenom(tabChaine[i]);
						tab[2]=i;
						//System.out.println(i);

						//infoColonne[cptTab]="PRENOM";
					}
					else if(cell.toString().compareTo("VILLE")==0)
					{
						//p.setVille(tabChaine[i]);
						tab[3]=i;
						System.out.println(tab[3]);

						//infoColonne[cptTab]="VILLE";
					}
					else if(cell.toString().compareTo("PAYS")==0)
					{
						//p.setPays(tabChaine[i]);
						tab[4]=i;
						//infoColonne[cptTab]="PAYS";
					}
					else
					{
						//p.infoComplementaireNom.add(tabChaine[i]);
					}


					i++;
				}
				prem=false;

			}



			else
			{
				i=0;
				/*while (cellIterator.hasNext()) 	//PREMIERE LIGNE DOIT FAIRE CREATE ICI
				{

					Cell cell = cellIterator.next();
					//CellType cellType = cell.getCellTypeEnum();
					//if(cell.getCellType()== cell.CELL_TYPE_BLANK)
					//{
					//i++;
					//}
					tabChaine[i]=cell.toString();
					i++;
					if(i==tab[1])
					{
						p.setNom(cell.toString());
					}
					if(i==tab[2])
					{
						p.setPrenom(cell.toString());
					}
					if(i==tab[3])
					{
						p.setVille(cell.toString());
					}
					if(i==tab[4])
					{
						p.setPays(cell.toString());
					}

					//cell.
				}*/


				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
					Cell cell = row.getCell(c);
					if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
					{
						if(i==tab[1])
						{
							p.setNom(cell.toString());
						}
						if(i==tab[2])
						{
							p.setPrenom(cell.toString());
						}
						if(i==tab[3])
						{
							p.setVille(cell.toString());
						}
						if(i==tab[4])
						{
							p.setPays(cell.toString());
						}
					}
					i++;

				}


				//System.out.println(sheet.getRow(1).toString());
				//System.out.println(tabChaine[2]);
				//if(tab[i]!=-1)
				//{
				//if(i==0)	//0 PLACE DE NUMCLI
				//{
				//p.setNumClient(Long.parseLong(tabChaine[0]));	//CONVERSION NUMCLIENT EN LONG
				//}
				//else if(i==1)
				//{
				//tab[1]++;
				//p.setNom(tabChaine[tab[1]]);
				//System.out.println(tab[3]);
				//}
				//else if(i==2)
				//{
				//p.setPrenom(tabChaine[tab[2]]);
				//System.out.println(tab[2]);
				//}
				//else if(i==3)
				//{
				//System.out.println(tab[i]);
				//p.setVille(tabChaine[tab[3]]);
				//}
				//else if(i==4)
				//{
				//p.setPays(tabChaine[tab[4]]);
				//}


				//}

				//else
				//{
				//if(tabChaine[i].compareTo("")!=0)
				//p.infoComplementaire.add(tabChaine[i]);
				//}

			}




			//line = br.readLine();
			//if(br.readLine==null) {break;}


			System.out.println(p.getNom()+ " " + p.getPays()+ " " + p.getPrenom()+ " " + p.getVille());
			//System.out.println(p.getPays());

		}
		//br.close();
		//fr.close();





	}

	public static void main(String[] args) throws IOException
	{
		ExtractionExcel ec = new ExtractionExcel();
		File f = new File("Exple-mouvements-BDD-Grands-Mécènes.xlsx");
		ec.readFile(f);
	}


}
