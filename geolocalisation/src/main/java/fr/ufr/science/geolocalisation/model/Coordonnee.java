package fr.ufr.science.geolocalisation.model;

import java.io.Serializable;

public class Coordonnee implements Serializable {
	
	private static final long serialVersionUID = -1445172941609914879L;

    private double lat;
    private double lng;

    public Coordonnee(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public Coordonnee(){
        this(0.,0.);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double v) {
        this.lat = v;
    }

    public double getLon() {
        return lng;
    }

    public void setLon(double v) {
        this.lng = v;
    }

    @Override
    public String toString() {
        return "Coordonee{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
