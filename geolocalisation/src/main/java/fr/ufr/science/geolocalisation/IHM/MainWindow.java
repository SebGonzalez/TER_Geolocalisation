package fr.ufr.science.geolocalisation.IHM;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapKit;
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

import fr.ufr.science.geolocalisation.model.Personne;
import fr.ufr.science.geolocalisation.util.GestionnairePersonne;
import fr.ufr.science.geolocalisation.util.OpenStreetMapUtils;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private GestionnairePersonne gestionnairePersonne;
	
	final JXMapViewer mapViewer;
    private JPanel jPanel1;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JSlider zoomSlider;
    
	private boolean sliderReversed = false;
	private boolean zoomChanging = false;
	
	public MainWindow(GestionnairePersonne gestionnairePersonne) {
		this.gestionnairePersonne = gestionnairePersonne;
		
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

        //toni ?
        GeoPosition france = new GeoPosition(46.227638, 2.213749);

        // Set the focus
        mapViewer.setZoom(13);
        zoomSlider.setValue(13);
        mapViewer.setAddressLocation(france);

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
        
        Set<SwingWaypoint> waypoints = new HashSet<SwingWaypoint>();
        for(Personne p : gestionnairePersonne.getListePersonne()) {
        		Map<String, Double> coords;
            coords = OpenStreetMapUtils.getInstance().getCoordinates(p.getVille());
            GeoPosition geo = new GeoPosition(coords.get("lat"), coords.get("lon"));
            waypoints.add( new SwingWaypoint(geo));
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
        
	}
	
	private void initComponents()
    {
        GridBagConstraints gridBagConstraints;

        jPanel1 = new JPanel();
        zoomInButton = new JButton();
        zoomOutButton = new JButton();
        zoomSlider = new JSlider();

        setLayout(new GridBagLayout());

        mapViewer.setLayout(new GridBagLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new GridBagLayout());

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
    }
	
    public void setZoom(int zoom)
    {
        zoomChanging = true;
        mapViewer.setZoom(zoom);
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
}
