package fr.ufr.science.geolocalisation.gestionDonnee;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.ufr.science.geolocalisation.App;
import fr.ufr.science.geolocalisation.model.Coordonnee;
import fr.ufr.science.geolocalisation.model.Personne;
import fr.ufr.science.geolocalisation.util.GestionnaireFichier;
import fr.ufr.science.geolocalisation.util.GestionnaireFiltre;
import fr.ufr.science.geolocalisation.util.GestionnairePersonne;

public class ExportExcel 
{
	public static void exportation(File file, GestionnairePersonne g)
	{
		int cptLigne=1;
		int cptColonne=5;
		boolean init=false;

		if(!file.exists())	//VERIF EXISTANCE FILE
		{
			try
			{
				file.createNewFile();
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
		XSSFWorkbook workbook = new XSSFWorkbook();

		//Create a blank spreadsheet
		XSSFSheet spreadsheet = workbook.createSheet("test");

		//j=0;
		//INITIALISATION CNOM COLONNES
		Row initRow = spreadsheet.createRow(0);
		Cell initCell = initRow.createCell(0);
		initCell.setCellValue("NUMCLI");

		initCell = initRow.createCell(1);
		initCell.setCellValue("NOM");

		initCell = initRow.createCell(2);
		initCell.setCellValue("PRENOM");

		initCell = initRow.createCell(3);
		initCell.setCellValue("VILLE");

		initCell = initRow.createCell(4);
		initCell.setCellValue("PAYS");




		for (Entry<String, List<Personne>> entry : g.getGestionnairePersonne().entrySet()) 
		{
			

			for (Personne p : entry.getValue()) 
			{
				cptColonne=5;
				//
				//INITIALISATION NOM COLONNES INFO COMP
				if(init==false)
				{
					Iterator it2 = p.getInfoComplementaires().entrySet().iterator();
					while (it2.hasNext()) 
					{
						Map.Entry pair = (Map.Entry)it2.next();

						initCell = initRow.createCell(cptColonne);
						initCell.setCellValue(pair.getKey().toString());

						cptColonne++;
						//it2.remove(); // avoids a ConcurrentModificationException
					}
					cptColonne=5;
					init=true;
					//cptLigne--;
				}
				else
				{
					cptLigne++;
				}

				Row row = spreadsheet.createRow(cptLigne);

				//GESTION ATTRIBUT FIXE
				Cell cell = row.createCell(0);
				cell.setCellValue(p.getNumClient());

				cell = row.createCell(1);
				cell.setCellValue(p.getNom());

				cell = row.createCell(2);
				cell.setCellValue(p.getPrenom());

				cell = row.createCell(3);
				cell.setCellValue(p.getVille());

				cell = row.createCell(4);
				cell.setCellValue(p.getCP());

				Iterator it2 = p.getInfoComplementaires().entrySet().iterator();
				while (it2.hasNext()) 
				{
					Map.Entry pair = (Map.Entry)it2.next();

					cell = row.createCell(cptColonne);
					cell.setCellValue(pair.getValue().toString());
					cptColonne++;

					//it2.remove(); // avoids a ConcurrentModificationException
				}

				//GESTION INFO COMPLEMENTAIRES
				//System.out.println("A");
				//System.out.println("Ajout de : " + p.getNom() + " " + p.getPrenom() + " " + p.getVille() +" " + p.getInfoComplementaires().get("CP"));
			}

			//System.out.println("A");
		}
		try {
			FileOutputStream out = new FileOutputStream(file);

			workbook.write(out);
			out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void exportationFiltre(File file, GestionnairePersonne g, GestionnaireFiltre gestionnaireFiltre, GestionnaireFichier gestionnaireFichier)
	{
		int cptLigne=1;
		int cptColonne=5;
		boolean init=false;

		if(!file.exists())	//VERIF EXISTANCE FILE
		{
			try
			{
				file.createNewFile();
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
		XSSFWorkbook workbook = new XSSFWorkbook();

		//Create a blank spreadsheet
		XSSFSheet spreadsheet = workbook.createSheet("test");

		//j=0;
		//INITIALISATION CNOM COLONNES
		Row initRow = spreadsheet.createRow(0);
		Cell initCell = initRow.createCell(0);
		initCell.setCellValue("NUMCLI");

		initCell = initRow.createCell(1);
		initCell.setCellValue("NOM");

		initCell = initRow.createCell(2);
		initCell.setCellValue("PRENOM");

		initCell = initRow.createCell(3);
		initCell.setCellValue("VILLE");

		initCell = initRow.createCell(4);
		initCell.setCellValue("PAYS");




		for (Entry<String, List<Personne>> entry : g.getGestionnairePersonne().entrySet()) 
		{
			String fichier = entry.getValue().get(0).getFichier();
			String valeur = gestionnaireFiltre.showPersonne(entry.getValue().get(0));
			
			for (Personne p : entry.getValue()) 
			{
				cptColonne=5;
				//
				//INITIALISATION NOM COLONNES INFO COMP
				if(init==false)
				{
					Iterator it2 = p.getInfoComplementaires().entrySet().iterator();
					while (it2.hasNext()) 
					{
						Map.Entry pair = (Map.Entry)it2.next();

						initCell = initRow.createCell(cptColonne);
						initCell.setCellValue(pair.getKey().toString());

						cptColonne++;
						//it2.remove(); // avoids a ConcurrentModificationException
					}
					cptColonne=5;
					init=true;
					//cptLigne--;
				}
				else
				{
					cptLigne++;
				}

				Row row = spreadsheet.createRow(cptLigne);

				if ( gestionnaireFichier.getVisibilityFile(fichier) && !valeur.equals("false")) 
				{
				//GESTION ATTRIBUT FIXE
				Cell cell = row.createCell(0);
				cell.setCellValue(p.getNumClient());

				cell = row.createCell(1);
				cell.setCellValue(p.getNom());

				cell = row.createCell(2);
				cell.setCellValue(p.getPrenom());

				cell = row.createCell(3);
				cell.setCellValue(p.getVille());

				cell = row.createCell(4);
				cell.setCellValue(p.getCP());

				Iterator it2 = p.getInfoComplementaires().entrySet().iterator();
				while (it2.hasNext()) 
				{
					Map.Entry pair = (Map.Entry)it2.next();

					cell = row.createCell(cptColonne);
					cell.setCellValue(pair.getValue().toString());
					cptColonne++;

					//it2.remove(); // avoids a ConcurrentModificationException
				}
				}

				//GESTION INFO COMPLEMENTAIRES
				//System.out.println("A");
				//System.out.println("Ajout de : " + p.getNom() + " " + p.getPrenom() + " " + p.getVille() +" " + p.getInfoComplementaires().get("CP"));
			}

			//System.out.println("A");
		}
		try {
			FileOutputStream out = new FileOutputStream(file);

			workbook.write(out);
			out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, InvalidFormatException
	{
		GestionnairePersonne g = new  GestionnairePersonne();
		ExportExcel ec = new ExportExcel();

		File f = new File("test.xslx");
	}

}








