package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape;

import java.util.List;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class RectangularWindow extends WindowShape{

	private int width;
	
	private int height; 

	public RectangularWindow(int w, int h){
		this.width = w;
		this.height = h;
	}
	
	@Override
	public int width() {
		return width;
	}

	@Override
	public int height() {
		return height;
	}

	@Override
	public List<Pixel> removeDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> addDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> removeHorizontalDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> addHorizontalDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> removeVerticalDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> addVerticalDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void export(Pixel p, Matrix m, String path) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void infos() {
		// TODO Auto-generated method stub
		
	}

}
