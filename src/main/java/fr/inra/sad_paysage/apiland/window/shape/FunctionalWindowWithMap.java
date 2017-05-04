package fr.inra.sad_paysage.apiland.window.shape;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class FunctionalWindowWithMap extends FunctionalWindow {

	private Friction friction;
	
	public FunctionalWindowWithMap(Matrix m, double d, Friction f){
		super(m, d);
		this.friction = f;
	}
	
	public FunctionalWindowWithMap(Matrix m, double d, int displacement, Friction f){
		super(m, d, displacement);
		this.friction = f;
	}
	
	@Override
	public int width() {
		return new Double(((2*dMax/matrix.cellsize())/friction.min())+1).intValue()-1;
	}

	@Override
	public int height() {
		return new Double(((2*dMax/matrix.cellsize())/friction.min())+1).intValue()-1;
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
