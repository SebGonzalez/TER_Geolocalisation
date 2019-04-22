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
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);	//ENTITE POUR LECTURE FICHIER EXCEL
		XSSFSheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rowIt = sheet.iterator();	//ITERATION SUR COLONNES

		int i;	//COMPTEUR POUR PLACEMENT DES INFORMATIONS
		boolean prem = true;	//FLAG POUR LA PREMIERE LIGNE

		int[] tab = new int[1000];
		for(i=0;i<tab.length-1;i++)	//INITIALISATION, SECURITE
		{
			tab[i]=-1;
		}

		while(rowIt.hasNext())	//TANT QUE LIGNES SUIVANTES EXISTE
		{

			Personne p = new Personne(); //CREATION NOUVELLE PERSONNE
			Row row = rowIt.next();

			Iterator<Cell> cellIterator = row.cellIterator();	//ITERATION SUR CELLULE DE LA LIGNE
			i=0;

			if(prem==true)	//PREMIERE LIGNE -> INISTIALISATION
			{
				while (cellIterator.hasNext()) 	//TANT QUE CELLULE SUIVANTE EXISTE
				{
					Cell cell = cellIterator.next();
					if(cell.toString().compareTo("NUMCLI")==0)
					{
						//p.setNumClient(Long.parseLong(tabChaine[i]));	//CONVERSION NUMCLIENT EN LONG
						tab[0]=i;
					}
					else if(cell.toString().compareTo("NOM")==0)
					{	
						tab[1]=i;
					}
					else if(cell.toString().compareTo("PRENOM")==0)
					{
						tab[2]=i;
					}
					else if(cell.toString().compareTo("VILLE")==0)
					{
						tab[3]=i;
					}
					else if(cell.toString().compareTo("PAYS")==0)
					{
						tab[4]=i;
					}
					else
					{
						//p.infoComplementaireNom.add(tabChaine[i]);
					}
					i++;
				}
				prem=false;

			}

			else	//TOUT AUTRES CAS
			{
				i=0;
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++)	//PARCOURS DIFFERENT DE ITERATOR POUR DETECTER CELLULES VIDES 
				{
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
			}
			System.out.println(p.getNom()+ " " + p.getPays()+ " " + p.getPrenom()+ " " + p.getVille());


		}

	}

	public static void main(String[] args) throws IOException
	{
		ExtractionExcel ec = new ExtractionExcel();
		File f = new File("Exple-mouvements-BDD-Grands-Mécènes.xlsx");
		ec.readFile(f);
	}


}
