package fr.ufr.science.geolocalisation.util;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class GestionnaireMarker {
	Icon icon1;
	Icon icon2;
	Icon iconFiltre;
	
	List<Icon> listeIcon;
	List<String> listeFichier;
	public GestionnaireMarker() {
		try {
			Image img1 = ImageIO.read(getClass().getResource("/fr/ufr/science/geolocalisation/marker2.png"));
			Image img2 = ImageIO.read(getClass().getResource("/fr/ufr/science/geolocalisation/marker3.png"));
			Image imgFiltre = ImageIO.read(getClass().getResource("/fr/ufr/science/geolocalisation/marker4.png"));
			
			icon1 = new ImageIcon(img1);
			icon2 = new ImageIcon(img2);
			iconFiltre = new ImageIcon(imgFiltre);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		listeIcon = new ArrayList<>();
		listeIcon.add(icon1);
		listeIcon.add(icon2);
		
		listeFichier = new ArrayList<>();
	}
	
	public void ajoutFichier(String nomFichier) {
		this.listeFichier.add(nomFichier);
	}
	
	public Icon getIcon(String nomFichier) {
		int id = listeFichier.indexOf(nomFichier);
		return listeIcon.get(id);
	}

	public Icon getIconFiltre() {
		return iconFiltre;
	}
	
	
}
