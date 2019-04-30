package fr.ufr.science.geolocalisation.gestionDonnee;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.ufr.science.geolocalisation.model.Personne;
import fr.ufr.science.geolocalisation.util.GestionnairePersonne;

public class ExtractionExcel 
{
	//TODO : EMPECHER LES DOUBLONS ?

	public void readFile(File file,GestionnairePersonne g) throws IOException
	{
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);	//ENTITE POUR LECTURE FICHIER EXCEL
		XSSFSheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rowIt = sheet.iterator();	//ITERATION SUR COLONNES

		SauvegardeCSV save= new SauvegardeCSV();	//FICHIER POUR SAUVEGARDE DES PERSONNES DANS UN CSV
		ArrayList<String> tabNumclient = new ArrayList<String>();	//LISTE DE NUMCLIENT DEJA SAUVEGARDE
		save.initListeNumclient(tabNumclient);

		int i;	//COMPTEUR POUR PLACEMENT DES INFORMATIONS
		boolean prem = true;	//FLAG POUR LA PREMIERE LIGNE

		int[] tab = new int[5];

		HashMap<Integer, String> idNomColonne = new HashMap<Integer, String>();		//STOCKE "ID DE LA COLONNE" "NOM DE LA COLONNE"
		//HashMap<String, String> infoComp = new HashMap<String, String>();

		while(rowIt.hasNext())	//TANT QUE LIGNES SUIVANTES EXISTE
		{

			Personne p = new Personne(); //CREATION NOUVELLE PERSONNE
			Row row = rowIt.next();

			Iterator<Cell> cellIterator = row.cellIterator();	//ITERATION SUR CELLULE DE LA LIGNE
			i=0;
			//j=0;

			if(prem==true)	//PREMIERE LIGNE -> INISTIALISATION
			{
				while (cellIterator.hasNext()) 	//TANT QUE CELLULE SUIVANTE EXISTE
				{
					Cell cell = cellIterator.next();
					if(cell.toString().compareTo("NUMCLI")==0)
					{
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
						idNomColonne.put(i, cell.toString().replaceAll("[\r\n]+", ""));	//STOCKE L'ID DE LA COLONNE AVEC LE NOM DE LA COLONNE
					}
					i++;
					
				}
				prem=false;

			}

			else	//TOUT AUTRES CAS
			{
				i=0;
				//j=0;
				for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++)	//PARCOURS DIFFERENT DE ITERATOR POUR DETECTER CELLULES VIDES 
				{
					Cell cell = row.getCell(c);
					if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
					{
						if(i==tab[0])
						{
							p.setNumClient(cell.toString());
						}
						else if(i==tab[1])
						{
							p.setNom(cell.toString());
						}
						else if(i==tab[2])
						{
							p.setPrenom(cell.toString());
						}
						else if(i==tab[3])
						{
							p.setVille(cell.toString());
						}
						else if(i==tab[4])
						{
							p.setPays(cell.toString());
						}
						else
						{
							if (idNomColonne.containsKey(i))
							{
								p.getInfoComplementaires().put(idNomColonne.get(i),cell.toString().replaceAll("[\r\n]+", ""));	//STOCKE LE NOM DE LA COLONNE AVEC SA VALEURS
							}
						}

					}
					i++;


				}
			}
			System.out.println(p.getNom()+  " " + p.getPrenom()+ " " + p.getNumClient() +" " + p.getPays()+ " " + p.getVille());
			//System.out.println(p.infoComplementaires.get("Score ISF"));

			if(p.getNumClient()!=null)
			{
				g.addPersonne(p);
				save.sauvegarde(p,tabNumclient);
			}

		}

	}

	public static void main(String[] args) throws IOException
	{		
		ExtractionExcel ec = new ExtractionExcel();
		File f = new File("/Users/sebgonzalez/Desktop/Exple-BDD-Grands-Mécènes-copie.xlsx");
		GestionnairePersonne gestP = new GestionnairePersonne();
		ec.readFile(f,gestP);
	}


}
