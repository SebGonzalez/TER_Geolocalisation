package fr.ufr.science.geolocalisation.IHM;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import fr.ufr.science.geolocalisation.App;
import fr.ufr.science.geolocalisation.util.RoutingOffline;

public class ImportMapWindow extends JFrame {
	private MainWindow mainWindow;

	private JPanel mainPanel;
	private JLabel mainText1;
	private JLabel mainText2;
	private JLabel telechargement;

	private JButton buttonExit;
	private JButton buttonDownload;
	private JButton buttonContinue;

	private HashMap<String, String> URLs;
	private ArrayList<JRadioButton> radioButtons;
	private ButtonGroup buttonGroup;

	String path = "/Users/gonzo/Desktop/test/";

	private double progress, size;
	private JProgressBar progressBar = new JProgressBar(0, 100);
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
		
		telechargement = new JLabel("Téléchargement en cours");
		telechargement.setVisible(false);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = y;
		gridBagConstraints.weightx = 4;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(telechargement, gridBagConstraints);
		
		progressBar.setStringPainted(true);
		progressBar.setVisible(false);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = y+1;
		gridBagConstraints.weightx = 4;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(progressBar, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = y+2;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		mainPanel.add(buttonExit, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = y+2;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		mainPanel.add(buttonDownload, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = y+2;
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
					progressBar.setVisible(true);
					telechargement.setVisible(true);
					try {
						downloadMap(buttonGroup.getSelection().getActionCommand());
						
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					} catch (NullPointerException e2) {
						e2.printStackTrace();
					}
				}
			}
		});
	}

	private void onlineMode() {
		mainWindow.setEnabled(true);
		dispose();
	}

	private void downloadMap(String mapID) throws MalformedURLException {

		final Object lock = new Object();

		
		new BackgroundWorker(mapID).execute();

		System.out.println("fini");
		
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
	
	public class BackgroundWorker extends SwingWorker<Void, Void> {

		String mapID;
		public BackgroundWorker(String mapId) {
			this.mapID = mapId;
			addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					progressBar.setValue(getProgress());
				}

			});
		}

		@Override
		protected void done() {
			FileInputStream fis;
			File dir = new File(path);
			if(!dir.exists()) dir.mkdirs();

			byte[] buffer = new byte[1024];
			try {
				fis = new FileInputStream(path + "/Region-"+mapID+".zip");
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

			File file = new File(path + "/Region-"+mapID+".zip");
			file.delete();
			
			RoutingOffline.init(App.getPath());
			dispose();
			mainWindow.setMapImported(true);
			mainWindow.setEnabled(true);
			mainWindow.setAlwaysOnTop(true);
			mainWindow.setAlwaysOnTop(false);
		}

		@Override
		protected Void doInBackground() throws Exception {

			try {
				URL url = new URL(URLs.get(mapID));
				
			    URLConnection conexion = url.openConnection();
			    conexion.connect();	    
			    int lenghtOfFile = 101349743; 
			    System.out.println("taille = " + lenghtOfFile);
			    InputStream input = new BufferedInputStream(url.openStream());
				
				// File Name
				

				// Copy file
				String saveFile = path + "/Region-"+mapID+".zip";
			
				OutputStream output = new FileOutputStream(saveFile);
				
			    byte data[] = new byte[1024];
			    int count;
			    
			    	long total = 0;
			
			        while ((count = input.read(data)) != -1) {
			        	total += count;
			        	setProgress((int)((total*100)/lenghtOfFile));
			            output.write(data, 0, count);
			        }
			
			        output.flush();
			        output.close();
			        input.close(); 
			        
			} catch (Exception ex) {
				System.err.println(ex);
			}
			
			return null;
			

		}
	}
	
}

