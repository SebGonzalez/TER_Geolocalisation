package fr.ufr.science.geolocalisation.IHM;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import fr.ufr.science.geolocalisation.App;
import fr.ufr.science.geolocalisation.model.Personne;

/**
 * A waypoint that is represented by a button on the map.
 *
 * @author Daniel Stahr
 */
public class SwingWaypoint extends DefaultWaypoint {
    private final JButton button;
    private String numPersonne = "";	// Pour afficher les infos de la personne

    public SwingWaypoint(GeoPosition coord) {
        super(coord);
        
        button = new JButton();
        button.setSize(64, 64);
        button.setContentAreaFilled(false);
        button.setBorder(null);
        button.addMouseListener(new SwingWaypointMouseListener());
        try {
            Image img = ImageIO.read(getClass().getResource("/fr/ufr/science/geolocalisation/marker2.png"));
            button.setIcon(new ImageIcon(img));
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        button.setVisible(true);
    }
    
    public SwingWaypoint(GeoPosition coord, String numPersonne) { // Constructeur avec num Personne
    	this(coord);
    	this.numPersonne = numPersonne;
    }

    JButton getButton() {
        return button;
    }
    
    public String getNumPersonne() {
    	return numPersonne;
    }

    private class SwingWaypointMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {		//Affichage des informations
        	if(numPersonne != "") {
	            Personne p = App.gestionnairePersonne.getPersonneByID(numPersonne);
	            String toPrint = "Num. Client: " + p.getNumClient() + "\nNom: " +  p.getNom() + "\nPrénom: " + p.getPrenom() + "\nVille: " + p.getVille() + "\nPays: " + p.getPays();
	            if(!p.getInfoComplementaires().isEmpty()) {
	            	for(Map.Entry<String, String> entry : p.getInfoComplementaires().entrySet()) {
	            		toPrint += "\n"+entry.getKey() + ": " + entry.getValue();
	            	}
	            }
	        	JOptionPane.showMessageDialog(button, toPrint);
        	}
        	else JOptionPane.showMessageDialog(button, "Erreur: Aucune donnée");
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
