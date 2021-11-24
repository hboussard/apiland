package fr.inra.sad.bagap.apiland.analysis.matrix.output;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

/**
 * Interpolation par Spline Cubique
 * 
 * @author Xavier Philippeau
 *
 */
public class Interpolate {

	// spline factor in the range [-1,0]
	private double a = -0.5;
	
	// original image
	private Matrix channel = null;
	
	public Interpolate(Matrix c) {
		this.channel = c;
	}
	
	// --- Spline coefficients ----------------------------------------------------
	
	// C0(t) = -at3 + at2 
	private double C0(double t) {
		return -a*t*t*t + a*t*t;
	}
	
	// C1(t) = -(a+2)t3 + (2a+3)t2 - at 
	private double C1(double t) {
		return -(a+2)*t*t*t + (2*a+3)*t*t - a*t;
	}
	
	// C2(t) = (a+2)t3 - (a+3)t2 + 1 
	private double C2(double t) {
		return (a+2)*t*t*t - (a+3)*t*t + 1;
	}
	
	// C3(t) = at3 - 2at2 + at 
	private double C3(double t) {
		return a*t*t*t - 2*a*t*t + a*t;
	}

	// --- Continous interpolation from discrete values ---------------------------
	
	// discret value for pixel of coordinates: i (integer) , j (integer)
	private double f(int i, int j) {
		return channel.get(i, j);
	}
	
	// compute interpolated value for pixel of coordinates: x (real) , j (integer)
	private double H(int j, double x) {
		int i = (int)x;
		return f(i-1,j)*C3(x-i) + f(i,j)*C2(x-i) + f(i+1,j)* C1(x-i) + f(i+2,j)*C0(x-i);
	}

	// compute interpolated value for pixel of coordinates: x (real) , y (real)
	public double value(double x, double y) {
		int j = (int)y;
		return H(j-1,x)*C3(y-j) + H(j,x)*C2(y-j) + H(j+1,x)*C1(y-j) + H(j+2,x)*C0(y-j);
	}
}