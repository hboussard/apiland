package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class FunctionalWindowWithMap extends FunctionalWindow {

	private Friction friction;
	
	public FunctionalWindowWithMap(Matrix m, double d, Friction f, DistanceFunction function){
		super(m, d, f.min(), function);
		this.friction = f;
		initTheoriticalSize();
	}
	
	public FunctionalWindowWithMap(Matrix m, double d, Friction f){
		super(m, d, f.min());
		this.friction = f;
		initTheoriticalSize();
	}
	
	public FunctionalWindowWithMap(Matrix m, double d, int displacement, Friction f, DistanceFunction function){
		super(m, d, displacement, f.min(), function);
		this.friction = f;
		initTheoriticalSize();
	}
	
	public FunctionalWindowWithMap(Matrix m, double d, int displacement, Friction f){
		super(m, d, displacement, f.min());
		this.friction = f;
		initTheoriticalSize();
	}
	
	@Override
	public int width() {
		int v = new Double(((2*dMax/matrix.cellsize())/*/friction.min()*/)+1).intValue();
		if(v % 2 == 0){
			return v - 1;
		}else{
			return v;
		}
	}

	@Override
	public int height() {
		int v = new Double(((2*dMax/matrix.cellsize())/*/friction.min()*/)+1).intValue();
		if(v % 2 == 0){
			return v - 1;
		}else{
			return v;
		}
	}
	
	@Override
	protected double friction(Matrix m, Pixel p){
		double c = window.get(matrix, p);
		if(c == Raster.getNoDataValue()){
			return c;
		}
		return friction.get(c);
	}
	
}
