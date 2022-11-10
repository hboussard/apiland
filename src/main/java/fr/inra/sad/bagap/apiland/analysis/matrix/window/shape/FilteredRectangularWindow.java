package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape;

import java.util.List;

import fr.inra.sad.bagap.apiland.cluster.Cluster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class FilteredRectangularWindow extends WindowShape{

	private Cluster cluster;
	
	public FilteredRectangularWindow(Cluster cluster){
		this.cluster = cluster;
	}
	
	public int getValue(){
		return cluster.getValue();
	}
	
	@Override
	public int theoreticalSize(){
		return cluster.theoriticalSize();
	}
	
	@Override
	public int filter(int wx, int wy){
		return cluster.filter(wx, wy);
	}

	@Override
	public int width() {
		return cluster.width();
	}

	@Override
	public int height() {
		return cluster.height();
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
