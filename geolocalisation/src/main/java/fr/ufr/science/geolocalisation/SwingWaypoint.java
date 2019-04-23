package fr.ufr.science.geolocalisation;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * A waypoint that is represented by a button on the map.
 *
 * @author Daniel Stahr
 */
public class SwingWaypoint extends DefaultWaypoint {
    private final JButton button;

    public SwingWaypoint(GeoPosition coord) {
        super(coord);
        
        button = new JButton();
        button.setSize(24, 24);
        button.setPreferredSize(new Dimension(64, 54));
        button.addMouseListener(new SwingWaypointMouseListener());
        try {
            Image img = ImageIO.read(getClass().getResource("src/main/resources/fr/ufr/science/geolocalisation/marker.png"));
            button.setIcon(new ImageIcon(img));
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        button.setVisible(true);
    }

    JButton getButton() {
        return button;
    }

    private class SwingWaypointMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JOptionPane.showMessageDialog(button, "You clicked on ");
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
