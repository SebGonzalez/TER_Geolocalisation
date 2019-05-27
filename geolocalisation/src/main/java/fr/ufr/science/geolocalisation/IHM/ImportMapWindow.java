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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.SwingConstants;

import fr.ufr.science.geolocalisation.App;
import fr.ufr.science.geolocalisation.util.RoutingOffline;

public class ImportMapWindow extends JFrame{
	private MainWindow mainWindow;

	private JPanel mainPanel;
	private JLabel mainText1;
	private JLabel mainText2;

	private JButton buttonExit;
	private JButton buttonDownload;
	private JButton buttonContinue;

	private HashMap<String, String> URLs;
	private ArrayList<JRadioButton> radioButtons;
	private ButtonGroup buttonGroup;

	String path;

	private double progress, size;
	private JProgressBar pb;
	private BufferedInputStream bis;
	private BufferedOutputStream baos;

	private int nbRegions;

	public ImportMapWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		URLs = new HashMap<String, String>();
		path = App.getPath() + "mapCache";
		this.setPreferredSize(new Dimension(600,300));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		init();
	}

	public void init() {

		try {
			readURLFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		GridBagConstraints gridBagConstraints;

		mainPanel = new JPanel();
		radioButtons = new ArrayList<JRadioButton>();
		buttonGroup = new ButtonGroup();
		mainText1 = new JLabel();
		mainText2 = new JLabel();

		buttonExit = new JButton("Quitter");
		buttonDownload = new JButton("Télécharger");
		buttonContinue = new JButton("Continuer sans");


		Object[] regions = URLs.keySet().toArray();
		for(int i = 0; i < regions.length; i++) {
			String key = (String) regions[i];
			JRadioButton radioButton = new JRadioButton("Région-" + key);
			radioButton.setActionCommand(key);
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

		for(int i = 0; i < radioButtons.size(); i = i) {
			gridBagConstraints.gridy = (int) (3 + Math.floor(i/3));
			for(int k = 0; k < 3; k ++, i++) {
				if(i < radioButtons.size()) {
					gridBagConstraints.gridx = k;
					mainPanel.add(radioButtons.get(i), gridBagConstraints);
				}
			}
		}

		int y = (int) Math.floor(radioButtons.size()/3) + 4;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = y;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		mainPanel.add(buttonExit, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = y;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		mainPanel.add(buttonDownload, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = y;
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
					try {
						downloadMap(buttonGroup.getSelection().getActionCommand());
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
					RoutingOffline.init(App.getPath());
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

	private void downloadMap(String mapID) throws MalformedURLException {
		this.setAlwaysOnTop(false);
		pb = new JProgressBar();
		pb.setValue(0);
		pb.setMaximum(100);
		pb.setStringPainted(true);
		pb.setBorder(BorderFactory.createTitledBorder("Téléchargement en cours"));
		final Object lock = new Object();

		URL url = new URL(URLs.get(mapID));
		/*Thread progressBar = new Thread() {
			public void run() {
				JOptionPane.showMessageDialog(mainPanel, pb, "Téléchargement...", JOptionPane.PLAIN_MESSAGE);		
			}
		};*/

			//size = 47500;
			Thread thread = new Thread() {
				public void run() {
					try {
						bis = new BufferedInputStream(url.openStream());
						ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(mainPanel, "Téléchargement...", bis);
						System.out.println("Début DL");

						pmis.getProgressMonitor().setMillisToPopup(10);
						baos = new BufferedOutputStream(new FileOutputStream(path + "Région-"+mapID+".zip"));

						byte[] buffer = new byte[2048];
						int nRead = 0;

						while((nRead = pmis.read(buffer)) != -1) {
							synchronized(lock) {
								progress+=1;
								actualizeProgressBar();
							}
							//System.out.println("Progress: " + Math.round(progress/size * 100));
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

			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		FileInputStream fis;
		File dir = new File(path);
		if(!dir.exists()) dir.mkdirs();

		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(path + "Région-"+mapID+".zip");
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

		File file = new File(path + "Région-"+mapID+".zip");
		file.delete();
	}

	private synchronized void actualizeProgressBar() {
		System.out.println("Progress...: " + Math.round(progress/size * 100));
		pb.setValue((int) Math.round(progress/size * 100));
	}

	private void readURLFile() throws IOException{
		File f = new File(App.getPath() + "config\\download.cfg");
		if(!f.exists())
			throw new IOException("URL File missing");
		Properties settings = new Properties();
		FileInputStream is = new FileInputStream(f);
		settings.load(is);

		Enumeration<Object> keys = settings.keys();

		while(keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			URLs.put(key, (String) settings.getProperty(key));
		}

		nbRegions = URLs.size();
	}
}
