package fr.ufr.science.geolocalisation.model;

public class Route {
	private final double distance;
	private final long temps;
	
	public Route(double d, long t) {
		distance = d; temps = t;
	}

	public double getDistance() {
		return distance;
	}

	public long getTemps() {
		return temps;
	}
	
	public String toString() {
		return "Distance: " + distance + "m ; Durée: " + temps + "ms.";
	}
	
	public String toStringFormat() {
		long heures, min;
		heures = (temps/1000)/3600;
		min = ((temps/1000)/60)%60;
		
		return "Distance: " + distance/1000 + " km ; Durée: " + heures + "h" + min + "m."; 
	}
}
