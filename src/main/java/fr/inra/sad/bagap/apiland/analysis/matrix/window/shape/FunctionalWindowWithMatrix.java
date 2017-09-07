package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class FunctionalWindowWithMatrix extends FunctionalWindow {
	
	private Matrix friction;
	
	private double min;
	
	public FunctionalWindowWithMatrix(Matrix m, double d, Matrix f){
		super(m, d, f.minV());
		this.friction = f;
		this.min = f.minV();
		//this.min = 1;
	}
	
	public FunctionalWindowWithMatrix(Matrix m, double d, int displacement, Matrix f){
		super(m, d, displacement);
		this.friction = f;
		this.min = f.minV();
		//this.min = 1;
	}
	
	@Override
	public int width() {
		int v = new Double(((2*dMax/matrix.cellsize())/min)+1).intValue();
		if(v % 2 == 0){
			return v - 1;
		}else{
			return v;
		}
	}

	@Override
	public int height() {
		int v = new Double(((2*dMax/matrix.cellsize())/min)+1).intValue();
		if(v % 2 == 0){
			return v - 1;
		}else{
			return v;
		}
	}
	/*
	@Override
	public int width() {
		return new Double(((dMax/matrix.cellsize())*2)/min+1).intValue()-1;
	}

	@Override
	public int height() {
		return new Double(((dMax/matrix.cellsize())*2)/min+1).intValue()-1;
	}*/

	@Override
	protected double friction(Matrix m, Pixel p) {
		return window.get(friction, p);
	}

}

