package com.nantes.commentbouger;

public class point {
	private double coordX;
	private double coordY;
	
	public point (double pcoordX, double pcoordY){
		coordX = pcoordX;
		coordY = pcoordY;
	}
	
	// clacul de la distance à vol d'oiseau entre 2 points GPS
	public double distFrom(point p){
		double rap=180/Math.PI;

	    double a1 = this.coordX / rap;
	    double a2 = this.coordY / rap;
	    double b1 = p.coordX / rap;
	    double b2 = p.coordY / rap;

	    double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
	    double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
	    double t3 = Math.sin(a1)*Math.sin(b1);
	    double tt = Math.acos(t1 + t2 + t3);

	    return Math.round(6366000*tt);
	}
}
