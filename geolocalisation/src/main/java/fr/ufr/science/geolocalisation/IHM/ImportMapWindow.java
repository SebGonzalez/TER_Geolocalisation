package fr.ufr.science.geolocalisation.IHM;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.SwingConstants;

public class ImportMapWindow extends JFrame{
	private MainWindow mainWindow;

	private JPanel mainPanel;
	private JLabel mainText1;
	private JLabel mainText2;

	private JButton buttonExit;
	private JButton buttonDownload;
	private JButton buttonContinue;

	final String[] regions = {"PACA-LanguedocRoussillon","RhoneAlpes-Auvergne","MidiPyrenees-Aquitaine"};
	final String[] regionsID =  {"PACA-LR", "RA-A", "MP-A"};
	private ArrayList<JRadioButton> radioButtons;
	private ButtonGroup buttonGroup;

	String path = "/Users/gonzo/Desktop/test/";

	private double progress, size;
	private JProgressBar pb;
	private BufferedInputStream bis;
	private BufferedOutputStream baos;

	public ImportMapWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		this.setPreferredSize(new Dimension(600,300));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		init();
	}

	public void init() {

		GridBagConstraints gridBagConstraints;

		mainPanel = new JPanel();
		radioButtons = new ArrayList<JRadioButton>();
		buttonGroup = new ButtonGroup();
		mainText1 = new JLabel();
		mainText2 = new JLabel();

		buttonExit = new JButton("Quitter");
		buttonDownload = new JButton("Télécharger");
		buttonContinue = new JButton("Continuer sans");


		for(int i = 0; i < regions.length; i++) {
			JRadioButton radioButton = new JRadioButton(regions[i]);
			radioButton.setActionCommand(regionsID[i]);
			radioButtons.add(radioButton);
		}

		mainPanel.setLayout(new GridBagLayout());

		for(JRadioButton button : radioButtons) {
			buttonGroup.add(button);
		}
		mainText1.setHorizontalTextPosition(SwingConstants.CENTER);
		mainText1.setText("Pas de cartes importée");
		mainText2.setText("Choisissez le pack de cartes à télécharger");


		/*
		 * "Mise en page"
		 */

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.ABOVE_BASELINE;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		mainPanel.add(mainText1, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		mainPanel.add(mainText2, gridBagConstraints);


		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;

		for(int i = 0; i < radioButtons.size(); i ++) {
			gridBagConstraints.gridx = 0 + i;
			gridBagConstraints.gridy = 3;
			mainPanel.add(radioButtons.get(i), gridBagConstraints);
		}

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		mainPanel.add(buttonExit, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		mainPanel.add(buttonDownload, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		mainPanel.add(buttonContinue, gridBagConstraints);

		this.add(mainPanel);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);


		buttonExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == buttonExit) {
					setVisible(false);
					dispose();
					System.exit(0);
				}
			}

		});

		buttonContinue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == buttonContinue) {
					Object[] options = {"Continuer", "Retour"};
					setAlwaysOnTop(false);
					int selected = JOptionPane.showOptionDialog(null, "Le routing offline sera indisponible", "Attention", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
					if(selected == 0) {
						JOptionPane.getRootFrame().dispose();
						onlineMode();
					}
				}
			}	
		});

		buttonDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == buttonDownload) {
					downloadMap(buttonGroup.getSelection().getActionCommand());
					dispose();
					mainWindow.setMapImported(true);
					mainWindow.setEnabled(true);
				}
			}
		});
	}

	private void onlineMode() {
		mainWindow.setEnabled(true);
		dispose();
	}

	private void downloadMap(String mapID) {
		pb = new JProgressBar();
		pb.setStringPainted(true);

		if(mapID == "PACA-LR") {
			size = 363353;
			pb.setMaximum((int)size);
			pb.setValue(0);
			Thread thread = new Thread() {
				public void run() {
					try {
						bis = new BufferedInputStream(new URL("https://drive.google.com/uc?export=download&id=1dAy_ZDsuwRbNK95usf5qKVGbFocIArsl").openStream());
						ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(mainPanel, "Téléchargement...", bis);
						
						System.out.println("Début DL");

						pmis.getProgressMonitor().setMillisToPopup(10);
						baos = new BufferedOutputStream(new FileOutputStream(path + "PACA-LR.zip"));

						byte[] buffer = new byte[2048];
						int nRead = 0;

						while((nRead = pmis.read(buffer)) != -1) {
							progress+=nRead;
							actualizeProgressBar();
							System.out.println("Progress: " + Math.round(progress/size * 100));
							baos.write(buffer, 0, nRead);
						}

						pmis.close();
						baos.flush();
						baos.close();
 
						System.out.println("Fin DL");
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			};
			thread.start();
			
			//JOptionPane.showMessageDialog(mainPanel, pb, "Téléchargement...", JOptionPane.PLAIN_MESSAGE);

			 JOptionPane.showOptionDialog(mainPanel, pb,"Téléchargement...", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);

			
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else return;

		FileInputStream fis;
		File dir = new File(path);
		if(!dir.exists()) dir.mkdirs();

		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(path + "PACA-LR.zip");
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while(ze != null) {
				String name = ze.getName();
				File newFile = new File(path + File.separator + name);

				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e){
			e.printStackTrace();
		}

		File file = new File(path + "PACA-LR.zip");
		file.delete();
		
		dispose();
	}

	private synchronized void actualizeProgressBar() {
		//System.out.println("Progress...: " + Math.round(progress/size * 100));
		pb.setValue((int) Math.round(progress/size * 100));
	}
}
