package fr.ufr.science.geolocalisation.IHM;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import java.util.Properties;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputListener;

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

import fr.ufr.science.geolocalisation.model.Coordonnee;
import fr.ufr.science.geolocalisation.model.Personne;
import fr.ufr.science.geolocalisation.util.GestionnaireCoordonnee;
import fr.ufr.science.geolocalisation.util.GestionnairePersonne;
import fr.ufr.science.geolocalisation.util.OpenStreetMapUtils;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private GestionnairePersonne gestionnairePersonne;
	private GestionnaireCoordonnee gestionnaireCoordonne;
	
	final JXMapViewer mapViewer;
    private JPanel jPanel1;
    private JPanel menu;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JSlider zoomSlider;
    private JTextField distanceCheckCityName;
    private JTextField distanceCheckRange;
    private JLabel distanceCheckResult;
    private GeoPosition currentPosition;
    private int currentZoom;
    
	private boolean sliderReversed = false;
	private boolean zoomChanging = false;
	
	public MainWindow(GestionnairePersonne gestionnairePersonne, GestionnaireCoordonnee gestionnaireCoordonne) {
		this.gestionnairePersonne = gestionnairePersonne;
		this.gestionnaireCoordonne = gestionnaireCoordonne;
		
		this.setSize(800, 600);
		
		// Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);

        // Setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

        // Setup JXMapViewer
        mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);
        
        initComponents();
        zoomSlider.setMinimum(tileFactory.getInfo().getMinimumZoomLevel());
        zoomSlider.setMaximum(tileFactory.getInfo().getMaximumZoomLevel());
        
        zoomSlider.setOpaque(false);
        
        loadSettings();

        // Set the focus
        mapViewer.setZoom(currentZoom);
        zoomSlider.setValue(currentZoom);
        mapViewer.setAddressLocation(currentPosition);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);

        mapViewer.addMouseListener(new CenterMapListener(mapViewer));

        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

        mapViewer.addKeyListener(new PanKeyListener(mapViewer));
        
        mapViewer.addPropertyChangeListener("zoom", new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                zoomSlider.setValue(mapViewer.getZoom());
            }
        });
        
        mapViewer.addMouseListener(new MouseListener() {				//C'est pas beau, mais?...

			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {

			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				java.awt.Point p = e.getPoint();
				currentPosition = mapViewer.convertPointToGeoPosition(p);
			}
        });
        
        Set<SwingWaypoint> waypoints = new HashSet<SwingWaypoint>();
        for(Personne p : gestionnairePersonne.getListePersonne()) {
        		
        	Coordonnee c = gestionnaireCoordonne.getCoordonnee(p.getVille());
            GeoPosition geo = new GeoPosition(c.getLat(), c.getLon());
            waypoints.add( new SwingWaypoint(geo, p.getNumClient()));
        }
        
     // Set the overlay painter
        WaypointPainter<SwingWaypoint> swingWaypointPainter = new SwingWaypointOverlayPainter();
        swingWaypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(swingWaypointPainter);

        // Add the JButtons to the map viewer
        for (SwingWaypoint w : waypoints) {
            mapViewer.add(w.getButton());
        }
        
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        OpenStreetMapUtils.filtreDistance("Marseille",19);
        
	}
	
	private void initComponents()
    {
        GridBagConstraints gridBagConstraints;

        jPanel1 = new JPanel();
        menu = new JPanel();
        zoomInButton = new JButton();
        zoomOutButton = new JButton();
        zoomSlider = new JSlider();
        distanceCheckCityName = new JTextField();
        distanceCheckRange = new JTextField();
        distanceCheckResult = new JLabel();
        
        
        setLayout(new GridBagLayout());

        mapViewer.setLayout(new GridBagLayout());
        
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new GridBagLayout());
        
        menu.setOpaque(true);
        menu.setLayout(new GridBagLayout());
        //menu.setMaximumSize(new Dimension(this.getHeight(), 200));

        /*
         * MENU
         */
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 0.7;
        this.add(menu, gridBagConstraints);
        
        distanceCheckCityName.setPreferredSize(new Dimension(150, 30));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        
        menu.add(distanceCheckCityName, gridBagConstraints);
        
        distanceCheckRange.setPreferredSize(new Dimension(150, 30));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        
        menu.add(distanceCheckRange, gridBagConstraints);
        
        
        menu.add(distanceCheckResult);
        
        /*
         * 
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
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
        zoomSlider.addChangeListener(new javax.swing.event.ChangeListener()
        {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                zoomSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        jPanel1.add(zoomSlider, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(4, 4, 4, 4);
        mapViewer.add(jPanel1, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(mapViewer, gridBagConstraints);
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                saveSettings();
                System.exit(0);
            }
        });
        
    }
	
    public void setZoom(int zoom)
    {
        zoomChanging = true;
        mapViewer.setZoom(zoom);
        currentZoom = zoom;
        if (sliderReversed)
        {
            zoomSlider.setValue(zoomSlider.getMaximum() - zoom);
        }
        else
        {
            zoomSlider.setValue(zoom);
        }
       zoomChanging = false;
    }
    
    private void zoomSliderStateChanged(javax.swing.event.ChangeEvent evt)
    {
        if (!zoomChanging)
        {
            setZoom(zoomSlider.getValue());
        }
       
    }
    
	public Action getZoomOutAction()
    {
        Action act = new AbstractAction()
        {
            private static final long serialVersionUID = 1;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                setZoom(mapViewer.getZoom() - 1);
            }
        };
        return act;
    }
    
    public Action getZoomInAction()
    {
        Action act = new AbstractAction()
        {
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e)
            {
                setZoom(mapViewer.getZoom() + 1);
            }
        };
        return act;
    }
    
    private boolean settingsFileExists(String file) {
    	File f = new File(file);
    	
    	if(f.isFile())
    		return true;
    	return false;
    }
    
    private void loadSettings() {
    	Properties settings = new Properties();
    	InputStream iS = null;
    	
    	if(settingsFileExists("settings.cfg")) {
    		try {
    			File f = new File("settings.cfg");
    			iS = new FileInputStream(f);
    		} catch(IOException e) {
    			e.printStackTrace();
    		}
    		try {
    			settings.load(iS);
    		} catch(IOException e) {
    			e.printStackTrace();
    		}
    	} else {
        	createSettingsFile();
        }
    	double lat = Double.parseDouble(settings.getProperty("lat"));
    	double lon = Double.parseDouble(settings.getProperty("lon"));
    	currentPosition = new GeoPosition(lat, lon);
    	currentZoom = Integer.parseInt(settings.getProperty("currentZoom"));
    	
    	System.out.println("Paramètres chargés");
    }
    
    private void createSettingsFile() {
    	File f = new File("settings.cfg");
    	Properties settings = new Properties();
    	
    	settings.setProperty("currentZoom", "13");
    	settings.setProperty("lat", "46.227638");
    	settings.setProperty("lon", "2.213749");
    	
    	try {
    		OutputStream out = new FileOutputStream(f);
    		try {
    			settings.store(out, "Paramètres");
    			System.out.println("Paramètres créés");
    		} catch(IOException e) {
    			e.printStackTrace();
    		}
    	}catch(FileNotFoundException e) {
    		e.printStackTrace();
    	}
    }
    
    private void saveSettings() {
    	//Met à jour les variables avec les valeurs actuelles
    	currentZoom = mapViewer.getZoom();
    	
    	if(!settingsFileExists("settings.cfg")) {// Ne devrait normalement jamais arriver...
    		createSettingsFile();
    	}else {
    		Properties settings = new Properties();
    		System.out.println("Saving: " + currentPosition.toString());
    		settings.setProperty("currentZoom", Integer.toString(currentZoom));
        	settings.setProperty("lat", Double.toString(currentPosition.getLatitude()));
        	settings.setProperty("lon", Double.toString(currentPosition.getLongitude()));
    		
    		File f = new File("settings.cfg");
    		
    		try {
    			OutputStream out = new FileOutputStream(f);
    			
    			try {
    				settings.store(out, "Paramètres");
    			} catch(IOException e) {
    				e.printStackTrace();
    			}
    		} catch(FileNotFoundException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	System.out.println("Paramètres sauvegardés");  	
    }
   
}
