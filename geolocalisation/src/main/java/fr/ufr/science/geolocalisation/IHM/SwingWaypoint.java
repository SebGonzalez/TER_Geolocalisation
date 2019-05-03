package fr.ufr.science.geolocalisation.IHM;

import java.awt.Dialog;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import fr.ufr.science.geolocalisation.model.Personne;

/**
 * A waypoint that is represented by a button on the map.
 *
 */
public class SwingWaypoint extends DefaultWaypoint {
	private final JButton button;
	private List<Personne> listePersonnes; // Pour afficher les infos de la personne
	private JFrame frame;

	public SwingWaypoint(JFrame frame, GeoPosition coord) {
		super(coord);
		this.frame = frame;
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

	public SwingWaypoint(JFrame frame, GeoPosition coord, List<Personne> listePersonnes) { // Constructeur avec num Personne
		this(frame, coord);
		this.listePersonnes = listePersonnes;
	}

	JButton getButton() {
		return button;
	}

	public List<Personne> getListPersonnes() {
		return listePersonnes;
	}

	private class SwingWaypointMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) { // Affichage des informations
			if (!listePersonnes.isEmpty()) {
				System.out.println("Affichage");
				Dialog wayPointDialog = new WaypointDialog(frame, listePersonnes);
				wayPointDialog.setVisible(true);
			} else
				JOptionPane.showMessageDialog(button, "Erreur: Aucune donnée");
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
