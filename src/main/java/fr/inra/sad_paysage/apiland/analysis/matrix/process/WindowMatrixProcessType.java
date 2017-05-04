package fr.inra.sad_paysage.apiland.analysis.matrix.process;

import fr.inra.sad_paysage.apiland.window.Window;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.core.util.Couple;

/**
 * modeling class of a window process type
 * @author H.Boussard
 */
public class WindowMatrixProcessType extends MatrixProcessType {
	
	/** vertical values */
	private double[] verticals;
		
	/** horizontal values */
	private double[] horizontals;
	
	/** horizontal couple */
	private double hc;
	
	/** vertical couple */
	private double vc;
	
	/** constructor */
	public WindowMatrixProcessType(Matrix m){
		super(m);
		
		horizontals = new double[m.width()];
		for(int x=0; x<m.width(); x++){
			horizontals[x] = Raster.getNoDataValue();
		}

		verticals = new double[m.height()];
		for(int y=0; y<m.height(); y++){
			verticals[y] = Raster.getNoDataValue();
		}
	}
	
	/**
	 * to create a window process with a specific window 
	 * at a specific position pixel
	 * @param w the specific window
	 * @param p the position pixel
	 * @return a window process
	 */
	public WindowMatrixProcess create(Window w, Pixel p){
		return new SimpleWindowMatrixProcess(w, p, this);
	}

	/**
	 * to set a value at a specific location
	 * @param x x position
	 * @param y y position
	 * @param v the value
	 * @param m 
	 */
	public void setValue(int x, int y, double v, Matrix m){
		vc = Couple.get(v, horizontals[x]);
		horizontals[x] = v;
		hc = Couple.get(v, verticals[y]);
		verticals[y] = v;
	}
	
	/** @return the horizontal couple */
	public double getHorizontal(Matrix m){
		return hc;
	}
	
	/** @return the vertical couple */
	public double getVertical(Matrix m){
		return vc;
	}
	
}
