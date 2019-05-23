package fr.ufr.science.geolocalisation.IHM;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import fr.ufr.science.geolocalisation.model.Personne;

public class WaypointDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private List<Personne> listePersonnes;
	
	JTabbedPane tabbedPane;
	
	public WaypointDialog(JFrame jFrame, List<Personne> listePersonnes) {
		super(jFrame, true);
		this.setSize(600, 900);
		this.setLocationRelativeTo(null);
		this.listePersonnes = listePersonnes;
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		
		for(Personne p : listePersonnes) {
			JPanel panel = new JPanel();
			
			String toPrint = "Num. Client: " + p.getNumClient() + "\nNom: " + p.getNom() + "\nPrénom: "
					+ p.getPrenom() + "\nVille: " + p.getVille() + "\nPays: " + p.getCP();
			//if (!p.getInfoComplementaires().isEmpty()) {
				for (Map.Entry<String, String> entry : p.getInfoComplementaires().entrySet()) {
					toPrint += "\n" + entry.getKey() + ": " + entry.getValue();
				}
			//}

			JTextArea textArea = new JTextArea();
			textArea.setText(toPrint);
			textArea.setEditable(false);
			textArea.setCaretPosition(0);
			
			JScrollPane scrollPane = new JScrollPane(textArea);
			
			panel.add(scrollPane);
			
			tabbedPane.add(p.getNom() + " " + p.getPrenom(), scrollPane);
			//System.out.println(p.toStringInfoComplementaire());
			
		}
		this.add(tabbedPane);
	}
	
	

}
