package fr.inra.sad_paysage.apiland.window.shape;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class FunctionalWindowWithMatrix extends FunctionalWindow {
	
	private Matrix friction;
	
	private double min;
	
	public FunctionalWindowWithMatrix(Matrix m, double d, Matrix f){
		super(m, d);
		this.friction = f;
		this.min = f.minV();
	}
	
	public FunctionalWindowWithMatrix(Matrix m, double d, int displacement, Matrix f){
		super(m, d, displacement);
		this.friction = f;
		this.min = f.minV();
	}
	
	@Override
	public int width() {
		return new Double(((dMax/matrix.cellsize())*2)/min+1).intValue()-1;
	}

	@Override
	public int height() {
		return new Double(((dMax/matrix.cellsize())*2)/min+1).intValue()-1;
	}

	@Override
	protected double friction(Matrix m, Pixel p) {
		return window.get(friction, p);
	}

}

