package fr.ufr.science.geolocalisation.IHM;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FilenameUtils;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import fr.ufr.science.geolocalisation.App;
import fr.ufr.science.geolocalisation.gestionDonnee.ExportExcel;
import fr.ufr.science.geolocalisation.gestionDonnee.ExtractionExcel;
import fr.ufr.science.geolocalisation.gestionDonnee.Memoire;
import fr.ufr.science.geolocalisation.gestionDonnee.SauvegardeCSV;
import fr.ufr.science.geolocalisation.model.Coordonnee;
import fr.ufr.science.geolocalisation.model.Fichier;
import fr.ufr.science.geolocalisation.model.Filtre;
import fr.ufr.science.geolocalisation.model.Personne;
import fr.ufr.science.geolocalisation.util.AutoSuggestor;
import fr.ufr.science.geolocalisation.util.GestionnaireCoordonnee;
import fr.ufr.science.geolocalisation.util.GestionnaireFichier;
import fr.ufr.science.geolocalisation.util.GestionnaireFiltre;
import fr.ufr.science.geolocalisation.util.GestionnairePersonne;
import fr.ufr.science.geolocalisation.util.OpenStreetMapUtils;
import fr.ufr.science.geolocalisation.util.RoutingOffline;

public class MainWindow extends JFrame {

	private String graphHopperPath = "/Users/gonzo/Desktop/test/"; // DEFAULT
	
	private static final long serialVersionUID = 1L;
	private GestionnairePersonne gestionnairePersonne;
	private GestionnaireCoordonnee gestionnaireCoordonne;
	private GestionnaireFichier gestionnaireFichier;
	private GestionnaireFiltre gestionnaireFiltre;
	
	private String loadedMapPack;

	public void setLoadedMapPack(String loadedMapPack) {
		this.loadedMapPack = loadedMapPack;
	}

	final JXMapViewer mapViewer;

	private ImportMapWindow importMapWindow;
	
	/*
	 * Base IHM
	 */

	private JPanel jPanel1;
	public JPanel menu;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JSlider zoomSlider;
	private JTextField distanceCheckCityName;
	private JTextField distanceCheckRange;
	private GeoPosition currentPosition;
	private int currentZoom;
	//

	/*
	 * Barre de menu
	 */

	private JButton hideMenu;
	private JMenu menuFichier;
	private JMenu menuAffichage;
	private JMenuBar menuBar;
	private JMenuItem importExcel;
	private JMenuItem exportExcelFull;
	private JMenuItem exportExcelFilter;
	private JMenuItem clearMap;
	private JMenuItem exportImage;
	private JFileChooser chooseExcelImport;
	private JFileChooser chooseExcelExport;
	private JFileChooser chooseImageExport;

	private boolean sliderReversed = false;
	private boolean zoomChanging = false;
	private boolean menuShow = true;

	private boolean isMapImported = false;

	public void setMapImported(boolean isMapImported) {
		this.isMapImported = isMapImported;
	}

	DefaultTileFactory tileFactory;

	public MainWindow(GestionnairePersonne gestionnairePersonne, GestionnaireCoordonnee gestionnaireCoordonne,
			GestionnaireFichier gestionnaireFichier, GestionnaireFiltre gestionnaireFiltre) {
		this.gestionnairePersonne = gestionnairePersonne;
		this.gestionnaireCoordonne = gestionnaireCoordonne;
		this.gestionnaireFichier = gestionnaireFichier;
		this.gestionnaireFiltre = gestionnaireFiltre;

		this.setPreferredSize(new Dimension(1280,1024));
		this.pack();
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		//Initialisation GraphHopper pour le routing. 
		if(!RoutingOffline.init(graphHopperPath)) {
			importMapWindow = new ImportMapWindow(this);
			this.setEnabled(false);
			if(isMapImported)
				RoutingOffline.init(graphHopperPath);
		} else OpenStreetMapUtils.setOfflineRoutingInitialized(true);


		// Create a TileFactoryInfo for OpenStreetMap
		TileFactoryInfo info = new OSMTileFactoryInfo();
		tileFactory = new DefaultTileFactory(info);

		// Setup local file cache
		File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
		tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

		// Setup JXMapViewer
		mapViewer = new JXMapViewer();
		mapViewer.setTileFactory(tileFactory);

		initComponents();

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		printWaypoints();

		zoomSlider.setOpaque(false);

		loadSettings();

		// Zoom de focus initial
		mapViewer.setZoom(currentZoom);
		zoomSlider.setValue(currentZoom);
		mapViewer.setAddressLocation(currentPosition);

		/*
		 * Ajout des listeners
		 */

		MouseInputListener mia = new PanMouseInputListener(mapViewer);
		mapViewer.addMouseListener(mia);
		mapViewer.addMouseMotionListener(mia);

		mapViewer.addMouseListener(new CenterMapListener(mapViewer));

		mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

		mapViewer.addKeyListener(new PanKeyListener(mapViewer));

		mapViewer.addPropertyChangeListener("zoom", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				zoomSlider.setValue(mapViewer.getZoom());
			}
		});

		mapViewer.addMouseListener(new MouseListener() {
			
			public void mouseClicked(java.awt.event.MouseEvent e) {}
			public void mouseEntered(java.awt.event.MouseEvent e) {}
			public void mouseExited(java.awt.event.MouseEvent e) {}
			public void mousePressed(java.awt.event.MouseEvent e) {}

			public void mouseReleased(java.awt.event.MouseEvent e) {		// Pour sauver position approximative de la fenetre quand on relance l'app
				java.awt.Point p = e.getPoint();
				currentPosition = mapViewer.convertPointToGeoPosition(p);
			}
		});

		/*
		 * Pour sauvegarde des paramètres avant la fermeture
		 */

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				saveSettings();
				saveFile();
				try
				{
					SauvegardeCSV save= new SauvegardeCSV();
					save.sauvegardeAll(App.gestionnairePersonne);
				}
				catch (IOException e)
				{
					
				}
				System.exit(0);
			}
		});
	}

	/*
	 * Initialisation de l'interface principale (Panel de menu gauche + carte +
	 * zoom)
	 */

	private void initComponentsMap() {	

		jPanel1 = new JPanel();
		zoomInButton = new JButton();
		zoomOutButton = new JButton();
		hideMenu = new JButton();
		zoomSlider = new JSlider();

		GridBagConstraints gridBagConstraints;
		mapViewer.setLayout(new GridBagLayout());

		jPanel1.setOpaque(false);
		jPanel1.setLayout(new GridBagLayout());

		/*
		 * Boutons de zoom
		 */

		zoomInButton.setAction(getZoomOutAction());
		zoomInButton.setIcon(new ImageIcon(getClass().getResource("/fr/ufr/science/geolocalisation/plus.png")));
		zoomInButton.setMargin(new Insets(2, 2, 10, 2));
		zoomInButton.setMaximumSize(new Dimension(30, 30));
		zoomInButton.setMinimumSize(new Dimension(30, 30));
		zoomInButton.setOpaque(false);
		zoomInButton.setPreferredSize(new Dimension(30, 30));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.SOUTHEAST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		jPanel1.add(zoomInButton, gridBagConstraints);

		zoomOutButton.setAction(getZoomInAction());
		zoomOutButton.setIcon(new ImageIcon(getClass().getResource("/fr/ufr/science/geolocalisation/minus.png")));
		zoomOutButton.setMargin(new Insets(10, 10, 10, 10));
		zoomOutButton.setMaximumSize(new Dimension(30, 30));
		zoomOutButton.setMinimumSize(new Dimension(30, 30));
		zoomOutButton.setOpaque(false);
		zoomOutButton.setPreferredSize(new Dimension(30, 30));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
		gridBagConstraints.weighty = 1.0;
		jPanel1.add(zoomOutButton, gridBagConstraints);

		zoomSlider.setMajorTickSpacing(1);
		zoomSlider.setMinorTickSpacing(1);
		zoomSlider.setOrientation(SwingConstants.VERTICAL);
		zoomSlider.setInverted(true);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setSnapToTicks(true);
		zoomSlider.setMinimumSize(new Dimension(35, 100));
		zoomSlider.setPreferredSize(new Dimension(35, 190));
		zoomSlider.setMinimum(tileFactory.getInfo().getMinimumZoomLevel());
		zoomSlider.setMaximum(tileFactory.getInfo().getMaximumZoomLevel());
		zoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			@Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				zoomSliderStateChanged(evt);
			}
		});
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new Insets(0, 0, 35, 0);
		gridBagConstraints.anchor = GridBagConstraints.SOUTH;
		jPanel1.add(zoomSlider, gridBagConstraints);

		/*
		 * Cacher/afficher le menu de gauche
		 */

		hideMenu.setPreferredSize(new Dimension(32, 32));
		hideMenu.setIcon(new ImageIcon(getClass().getResource("/fr/ufr/science/geolocalisation/hideMenu.png")));
		hideMenu.setContentAreaFilled(false);
		hideMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				menuShow = !menuShow;
				menu.setVisible(menuShow);
				if (menuShow) {
					hideMenu.setIcon(
							new ImageIcon(getClass().getResource("/fr/ufr/science/geolocalisation/hideMenu.png")));
				} else {
					hideMenu.setIcon(
							new ImageIcon(getClass().getResource("/fr/ufr/science/geolocalisation/showMenu.png")));
				}
			}
		});
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		jPanel1.add(hideMenu, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(4, 4, 4, 4);
		mapViewer.add(jPanel1, gridBagConstraints);

		mapViewer.revalidate();
	}

	/*
	 * Initialisation de la barre de menu et des composants du menu de gauche
	 */

	public void initComponents() {
		GridBagConstraints gridBagConstraints;

		/*
		 * Composants menu gauche
		 */

		menu = new JPanel();
		distanceCheckCityName = new JTextField();
		distanceCheckRange = new JTextField();

		JLabel labelAdresse = new JLabel("Adresse : ");
		JLabel labelDistance = new JLabel("Distance (km) : ");
		JButton filtre = new JButton("Lancer le filtre distance");

		/*
		 * Composants menu de fichier
		 */

		menuBar = new JMenuBar();
		menuFichier = new JMenu("Fichier");
		menuAffichage = new JMenu("Affichage");
		importExcel = new JMenuItem("Importer Excel");
		exportExcelFull = new JMenuItem("Exporter");
		exportExcelFilter = new JMenuItem("Exporter filtre");
		clearMap = new JMenuItem("Nettoyer la carte");
		exportImage = new JMenuItem("Exporter en png");
		chooseExcelImport = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		chooseExcelExport = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		chooseImageExport = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		setLayout(new GridBagLayout());

		menu.setOpaque(true);
		menu.setLayout(new GridBagLayout());
		menuFichier.add(importExcel);
		menuFichier.add(exportExcelFull);
		menuFichier.add(exportExcelFilter);
		menuAffichage.add(clearMap);
		menuAffichage.add(exportImage);
		menuBar.add(menuFichier);
		menuBar.add(menuAffichage);
		this.setJMenuBar(menuBar);

		/*
		 * Listeners menu fichier
		 */

		// Fichiers
		importExcel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue;
				if (e.getSource() == importExcel) {
					chooseExcelImport.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooseExcelImport.setMultiSelectionEnabled(false);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier Excel", "xlsx");
					chooseExcelImport.setAcceptAllFileFilterUsed(false);
					chooseExcelImport.addChoosableFileFilter(filter);
					returnValue = chooseExcelImport.showOpenDialog(null);

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						File selectedFile = chooseExcelImport.getSelectedFile();
						gestionnaireFichier.ajoutFichier(selectedFile.getName());

						ExtractionExcel extracteur = new ExtractionExcel();
						try {
							extracteur.readFile(selectedFile);
						} catch (IOException e1) {
							e1.printStackTrace();
						}

						menu.removeAll();
						mapViewer.removeAll();
						initComponents();
						printWaypoints();
					}
				}
			}

		});

		exportExcelFull.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue;
				if (e.getSource() == exportExcelFull) {
					chooseExcelExport.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooseExcelExport.setMultiSelectionEnabled(false);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier Excel", "xlsx");
					chooseExcelExport.setAcceptAllFileFilterUsed(false);
					chooseExcelExport.addChoosableFileFilter(filter);
					chooseExcelExport.setMultiSelectionEnabled(false);
					returnValue = chooseExcelExport.showOpenDialog(null);

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						System.out.println(chooseExcelExport.getSelectedFile().getPath());
						String path = chooseExcelExport.getSelectedFile().getPath();
						if (FilenameUtils.getExtension(chooseExcelExport.getSelectedFile().getPath())
								.compareTo("xlsx") != 0)
							path += ".xlsx";
						File selectedFile = new File(path);

						menu.removeAll();
						mapViewer.removeAll();
						initComponents();
						ExportExcel.exportation(selectedFile, gestionnairePersonne);
					}
				}
			}

		});

		exportExcelFilter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if(e.getSource() == exportExcelFilter) {
					int returnValue;

					chooseExcelExport.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooseExcelExport.setMultiSelectionEnabled(false);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier Excel", "xlsx");
					chooseExcelExport.setAcceptAllFileFilterUsed(false);
					chooseExcelExport.addChoosableFileFilter(filter);
					chooseExcelExport.setMultiSelectionEnabled(false);
					returnValue = chooseExcelExport.showOpenDialog(null);

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						System.out.println(chooseExcelExport.getSelectedFile().getPath());
						String path = chooseExcelExport.getSelectedFile().getPath();
						if(FilenameUtils.getExtension(chooseExcelExport.getSelectedFile().getPath()).compareTo("xlsx") !=0)
							path += ".xlsx";
						File selectedFile = new File(path);


						menu.removeAll();
						mapViewer.removeAll();
						initComponents();
						ExportExcel.exportationFiltre(selectedFile, gestionnairePersonne, gestionnaireFiltre, gestionnaireFichier);
					}
				}
			}
		});
		
		exportImage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == exportImage) {
					int returnValue;
					
					chooseImageExport.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooseImageExport.setMultiSelectionEnabled(false);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier png", "png");
					chooseImageExport.setAcceptAllFileFilterUsed(false);
					chooseImageExport.addChoosableFileFilter(filter);
					returnValue = chooseImageExport.showOpenDialog(null);					
					
					if(returnValue == JFileChooser.APPROVE_OPTION) {
						String path = chooseImageExport.getSelectedFile().getPath();
						if(FilenameUtils.getExtension(chooseImageExport.getSelectedFile().getPath()).compareTo("png") !=0)
							path += ".png";
						BufferedImage img = new BufferedImage(mapViewer.getWidth(), mapViewer.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics2D g2 = img.createGraphics();
						mapViewer.update(g2);
						try {
							ImageIO.write(img, "png", new File(path));
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
				}
				
			}
			
		});

		// Affichage
		clearMap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == clearMap) {
					System.out.println("Reset CSV");
					SauvegardeCSV.resetSauvegarde();
				}
			}

		});

		/*
		 * Arrangement menu gauche + listeners
		 */

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		menu.add(labelAdresse, gridBagConstraints);

		distanceCheckCityName.setPreferredSize(new Dimension(150, 30));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		menu.add(distanceCheckCityName, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		menu.add(labelDistance, gridBagConstraints);

		distanceCheckRange.setPreferredSize(new Dimension(150, 30));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		menu.add(distanceCheckRange, gridBagConstraints);

		filtre.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				OpenStreetMapUtils.getInstance().filtreDistance(MainWindow.this, distanceCheckCityName.getText(),
						Integer.parseInt(distanceCheckRange.getText()));

				gestionnaireFiltre.ajoutFiltre("distance_" + distanceCheckCityName.getText() + "_" + distanceCheckRange.getText());
			}
		});
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		menu.add(filtre, gridBagConstraints);

		JSeparator sep1 = new JSeparator(JSeparator.HORIZONTAL);
		sep1.setPreferredSize(new Dimension(1, 5));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(10, 0, 10, 0);
		menu.add(sep1, gridBagConstraints);

		JLabel labelAjoutFiltre = new JLabel("Ajouter un filtre : ");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		menu.add(labelAjoutFiltre, gridBagConstraints);

		JTextField fieldAjout = new JTextField();
		fieldAjout.setPreferredSize(new Dimension(150, 30));

		AutoSuggestor as = new AutoSuggestor(fieldAjout, (Window)this, menu, gestionnaireFichier.getAllTypeInfos(), Color.WHITE.brighter(), Color.BLUE, Color.RED, 0.75f);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		menu.add(fieldAjout, gridBagConstraints);

		JButton buttonAjout = new JButton("Ajouter");
		buttonAjout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gestionnaireFiltre.ajoutFiltre(fieldAjout.getText());
				List<String> listeValue = gestionnaireFichier.getAllInfos(fieldAjout.getText());
				gestionnaireFiltre.ajoutInfosFiltre(fieldAjout.getText(), listeValue);
				fieldAjout.setText("");
				menu.removeAll();
				initComponents();
				System.out.println("Fin ajout");
			}
		});
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		menu.add(buttonAjout, gridBagConstraints);

		JSeparator sep2 = new JSeparator(JSeparator.HORIZONTAL);
		sep1.setPreferredSize(new Dimension(1, 5));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(10, 0, 10, 0);
		menu.add(sep2, gridBagConstraints);

		JLabel labelFichier = new JLabel("Fichier : ");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		menu.add(labelFichier, gridBagConstraints);

		int compteur = 0;
		for (Fichier f : gestionnaireFichier.getListFichier()) {
			JLabel label = new JLabel(f.getNom());
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 12 + compteur;
			gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			menu.add(label, gridBagConstraints);

			JCheckBox checkBox = new JCheckBox();
			checkBox.setSelected(f.isAfficher());
			checkBox.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					gestionnaireFichier.setVisibilityFile(label.getText(), checkBox.isSelected());
					printWaypoints();
				}
			});
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 12 + compteur;
			gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			menu.add(checkBox, gridBagConstraints);

			compteur++;
		}

		JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
		sep.setPreferredSize(new Dimension(1, 5));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 13 + compteur;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(10, 0, 10, 0);
		menu.add(sep, gridBagConstraints);

		// Liste des filtres

		int compteurFiltre2 = 0;
		for (Filtre f : gestionnaireFiltre.getFiltre()) {

			if (!f.getNom().contains("distance")) {
				JLabel labelFiltre = new JLabel("Filtre " + f.getNom() + " :");
				gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 14 + compteur + compteurFiltre2;
				gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
				gridBagConstraints.insets = new Insets(0, 0, 10, 0);
				gridBagConstraints.weightx = 1;
				gridBagConstraints.weighty = 1;
				menu.add(labelFiltre, gridBagConstraints);

				Map<String, Boolean> infos = f.getDictionnaireInfos();
				CheckListItem listItem[] = new CheckListItem[infos.size()];
				int i = 0;
				for(Entry<String, Boolean> entry : infos.entrySet()) {
					listItem[i] = new CheckListItem(entry.getKey(), entry.getValue());
					i++;
				}
				JList<CheckListItem> list = new JList<>(listItem);
				list.setCellRenderer(new CheckListRenderer());
			    list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			    list.addMouseListener(new MouseAdapter() {
			      @Override
			      public void mouseClicked(MouseEvent event) {
			        JList list = (JList) event.getSource();
			        int index = list.locationToIndex(event.getPoint());// Get index of item
			                                                           // clicked
			        CheckListItem item = (CheckListItem) list.getModel()
			            .getElementAt(index);
			        item.setSelected(!item.isSelected()); // Toggle selected state
			        list.repaint(list.getCellBounds(index, index));// Repaint cell
			        
			        gestionnaireFiltre.ajoutFiltrePersonne(f.getNom(), item.toString(), item.isSelected());
			        f.setVisibilityInfo(item.toString(), item.isSelected());
			        printWaypoints();
			      }
			    });
			    
			    JScrollPane scrollPane3 = new JScrollPane(list);
				gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 15 + compteur + compteurFiltre2;
				gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
				gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints.weightx = 1;
				gridBagConstraints.weighty = 1;
				menu.add(scrollPane3, gridBagConstraints);
				
				JSeparator sep4 = new JSeparator(JSeparator.HORIZONTAL);
				sep4.setPreferredSize(new Dimension(1, 5));
				gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 16 + compteur + compteurFiltre2;
				gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
				gridBagConstraints.weightx = 1;
				gridBagConstraints.weighty = 1;
				gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints.insets = new Insets(10, 0, 10, 0);
				menu.add(sep4, gridBagConstraints);

				compteurFiltre2+= 5;
			}
		}



		JLabel labelFiltre = new JLabel("Filtres : ");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 17 + compteur + compteurFiltre2;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		menu.add(labelFiltre, gridBagConstraints);

		int compteurFiltre = 0;
		for (Filtre f : gestionnaireFiltre.getFiltre()) {
			JLabel label = new JLabel(f.getNom());
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 18 + compteur + compteurFiltre + compteurFiltre2;
			gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			menu.add(label, gridBagConstraints);

			JCheckBox checkBox = new JCheckBox();
			checkBox.setSelected(f.isAfficher());
			checkBox.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					f.setAfficher(checkBox.isSelected());
					printWaypoints();
				}
			});
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 18 + compteur + compteurFiltre + compteurFiltre2;
			gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			menu.add(checkBox, gridBagConstraints);

			compteurFiltre++;
		}

		JLabel label = new JLabel("Autre");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 19 + compteur + compteurFiltre + compteurFiltre2;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		menu.add(label, gridBagConstraints);

		JCheckBox checkBox = new JCheckBox();
		checkBox.setSelected(gestionnaireFiltre.showOthers());
		checkBox.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				gestionnaireFiltre.setShowOthers(checkBox.isSelected());
				printWaypoints();
			}
		});
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 19 + compteur + compteurFiltre + compteurFiltre2;
		gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		menu.add(checkBox, gridBagConstraints);
		
		JScrollPane scrollPane2 = new JScrollPane(menu);
		//scrollPane2.setOpaque(false);
		//scrollPane2.setBorder(null);
		scrollPane2.getViewport().setOpaque(false);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.01;
		gridBagConstraints.weighty = 0.7;
		this.add(menu, gridBagConstraints);

		/*
		 * 
		 */

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		this.add(mapViewer, gridBagConstraints);
	}

	public void setZoom(int zoom) {
		zoomChanging = true;
		mapViewer.setZoom(zoom);
		currentZoom = zoom;
		if (sliderReversed) {
			zoomSlider.setValue(zoomSlider.getMaximum() - zoom);
		} else {
			zoomSlider.setValue(zoom);
		}
		zoomChanging = false;
	}

	private void zoomSliderStateChanged(javax.swing.event.ChangeEvent evt) {
		if (!zoomChanging) {
			setZoom(zoomSlider.getValue());
		}

	}

	public Action getZoomOutAction() {
		Action act = new AbstractAction() {
			private static final long serialVersionUID = 1;

			@Override
			public void actionPerformed(ActionEvent e) {
				setZoom(mapViewer.getZoom() - 1);
			}
		};
		return act;
	}

	public Action getZoomInAction() {
		Action act = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				setZoom(mapViewer.getZoom() + 1);
			}
		};
		return act;
	}

	private boolean settingsFileExists(String file) {
		File f = new File(file);

		if (f.isFile())
			return true;
		return false;
	}

	private void loadSettings() {
		Properties settings = new Properties();
		InputStream iS = null;

		if (!settingsFileExists("settings.cfg"))
			createSettingsFile();
		try {
			File f = new File("settings.cfg");
			iS = new FileInputStream(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			settings.load(iS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		double lat = Double.parseDouble(settings.getProperty("lat"));
		double lon = Double.parseDouble(settings.getProperty("lon"));
		currentPosition = new GeoPosition(lat, lon);
		currentZoom = Integer.parseInt(settings.getProperty("currentZoom"));

		if (settings.getProperty("isMapImported", "false").compareTo("false") == 0)

			isMapImported = false;
		else
			isMapImported = true;

		System.out.println("Paramètres chargés");
	}

	private void createSettingsFile() {
		System.out.println("Création des paramètres");
		File f = new File("settings.cfg");
		Properties settings = new Properties();

		settings.setProperty("currentZoom", "13");
		settings.setProperty("lat", "46.227638");
		settings.setProperty("lon", "2.213749");
		settings.setProperty("isMapImported", "false");

		try {
			OutputStream out = new FileOutputStream(f);
			try {
				settings.store(out, "Paramètres");
				System.out.println("Paramètres créés");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void saveSettings() {
		// Met à jour les variables avec les valeurs actuelles
		currentZoom = mapViewer.getZoom();
		initComponentsMap();

		if (!settingsFileExists("settings.cfg")) {// Ne devrait normalement jamais arriver...
			createSettingsFile();
		} else {
			Properties settings = new Properties();
			System.out.println("Saving: " + currentPosition.toString());
			settings.setProperty("currentZoom", Integer.toString(currentZoom));
			settings.setProperty("lat", Double.toString(currentPosition.getLatitude()));
			settings.setProperty("lon", Double.toString(currentPosition.getLongitude()));
			if (isMapImported)
				settings.setProperty("isMapImported", "true");
			else
				settings.setProperty("isMapImported", "false");

			File f = new File("settings.cfg");

			try {
				OutputStream out = new FileOutputStream(f);

				try {
					settings.store(out, "Paramètres");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Paramètres sauvegardés");
	}

	private void saveFile() {
		Memoire.save(gestionnaireCoordonne, "coordonnee.cfg");
		Memoire.save(gestionnaireFichier, "fichiers.cfg");
		Memoire.save(gestionnaireFiltre, "filtres.cfg");
		System.out.println("coordonnees sauvegardés");
	}

	public Set<GeoPosition> printWaypoints() {
		mapViewer.removeAll();
		initComponentsMap();
		Set<SwingWaypoint> waypoints = new HashSet<SwingWaypoint>();
		Set<GeoPosition> pos = new HashSet<>();
		for (Entry<String, List<Personne>> entry : gestionnairePersonne.getGestionnairePersonne().entrySet()) {
			String fichier = entry.getValue().get(0).getFichier();

			String valeur = gestionnaireFiltre.showPersonne(entry.getValue().get(0));
			if (gestionnaireFichier.getVisibilityFile(fichier) && !valeur.equals("false")) {
				Coordonnee c = gestionnaireCoordonne.getCoordonnee(entry.getKey());
				GeoPosition geo = new GeoPosition(c.getLat(), c.getLon());
				pos.add(geo);
				if (valeur.equals("others"))
					waypoints.add(new SwingWaypoint(this, geo, entry.getValue(), gestionnaireFichier.getIcon(fichier)));
				else {
					waypoints.add(new SwingWaypoint(this, geo, entry.getValue(), gestionnaireFichier.getIconFiltre()));
				}
			}
		}

		mapViewer.zoomToBestFit(pos, 0.7);
		
		waypoints.add(new SwingWaypoint(this, mapViewer.getCenterPosition(), gestionnaireFichier.getIconFiltre()));
		// Set the overlay painter
		WaypointPainter<SwingWaypoint> swingWaypointPainter = new SwingWaypointOverlayPainter();
		swingWaypointPainter.setWaypoints(waypoints);
		mapViewer.setOverlayPainter(swingWaypointPainter);

		// Add the JButtons to the map viewer
		for (SwingWaypoint w : waypoints) {
			mapViewer.add(w.getButton());
		}
		
		return pos;
	}
	
	public void autoZoom(Set<GeoPosition> pos) {
		 mapViewer.zoomToBestFit(pos, 0.7);
	}
}
