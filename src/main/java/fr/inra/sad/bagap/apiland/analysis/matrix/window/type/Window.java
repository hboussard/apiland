package fr.inra.sad.bagap.apiland.analysis.matrix.window.type;

import java.util.List;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public abstract class Window implements Comparable<Window>{
	
	private Pixel pixel;
	
	public Window(){
		this.pixel = null;
	}
	
	public Pixel pixel(){
		return pixel;
	}
	
	protected void setPixel(Pixel p){
		this.pixel = p;
	}
	
	public double[][] weighted(){
		throw new UnsupportedOperationException();
	}
	
	public double[][] weightedH(){
		throw new UnsupportedOperationException();
	}
	
	public double[][] weightedV(){
		throw new UnsupportedOperationException();
	}

	public abstract int theoreticalSize();
	
	public abstract int size();
	
	public abstract int size(int width, int height);
	
	public abstract void locate(Pixel p);
	
	public abstract void locate(int x, int y);
	
	public abstract int filter(Pixel wp);
	
	public abstract int filter(int inpX, int inpY);
	
	public abstract int width();
	
	public abstract int height();
	
	public abstract int diameter();
	
	public abstract Pixel getPixel(int wx, int wy);
	
	public abstract Pixel getPixel(Pixel wp);
	
	public abstract int outXWindow(int wx);
	
	public abstract int outYWindow(int wy);
	
	public abstract boolean accept(int x, int y);
	
	public abstract boolean accept(Pixel p);
	
	public abstract Pixel toWindow(int x, int y);
	
	public abstract Pixel toWindow(Pixel p);
	
	public abstract int toXWindow(int x);
	
	public abstract int toYWindow(int y);
	
	@Override
	public int compareTo(Window other) { 
		if(((Window) other).width()*((Window) other).height() > this.width()*this.height()){
			return 1;
		}else if(((Window) other).width()*((Window) other).height() < this.width()*this.height()){
			return -1;
		}else{
			return 0;
		}
	} 
	
	public abstract double get(Matrix m, int wx, int wy);

	public abstract double get(Matrix m, Pixel wp);

	public abstract List<Pixel> removeDownList();
	
	public abstract List<Pixel> addDownList();

	public abstract List<Pixel> removeHorizontalDownList();
	
	public abstract List<Pixel> addHorizontalDownList();
	
	public abstract List<Pixel> removeVerticalDownList();

	public abstract List<Pixel> addVerticalDownList();

	public abstract void reinit();

	public void close() {
		// do nothing
	}
	
	public abstract void display();
	
	public abstract void export(Pixel p, Matrix m, String path);
	
	public abstract void infos();
}
