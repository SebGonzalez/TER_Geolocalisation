package fr.ufr.science.geolocalisation.IHM;

import java.awt.Dialog;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.Icon;
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

	public SwingWaypoint(JFrame frame, GeoPosition coord, Icon icon) {
		super(coord);
		this.frame = frame;
		button = new JButton();
		button.setSize(64, 64);
		button.setContentAreaFilled(false);
		button.setBorder(null);
		button.addMouseListener(new SwingWaypointMouseListener());
		button.setIcon(icon);
		button.setVisible(true);
	}

	public SwingWaypoint(JFrame frame, GeoPosition coord, List<Personne> listePersonnes, Icon icon) { // Constructeur avec num Personne
		this(frame, coord, icon);
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
