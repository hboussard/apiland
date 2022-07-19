package fr.inra.sad.bagap.apiland.core.space.impl.raster;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class EuclidianDistanceSinglePixelIterator implements Iterator<Pixel>{
	
	private Pixel pixel;
	
	private double distance;
	
	private Set<Pixel> around;
	
	private Iterator<Pixel> ite;
	
	public EuclidianDistanceSinglePixelIterator(Pixel pixel, double distance){
		//System.out.println(pixel);
		this.pixel = pixel;
		this.distance = distance;
		init();
	}

	private void init(){
		double cellsize = Raster.getCellSize();
		double drayon = distance / cellsize;
		int rayon = (int) (distance / cellsize);
		around = new TreeSet<Pixel>();
		for(int y = pixel.y()-rayon; y <= pixel.y()+rayon; y++){
			for(int x = pixel.x()-rayon; x <= pixel.x()+rayon; x++){
				if(distance(pixel.x(), pixel.y(), x, y) <= drayon){
					around.add(new Pixel(x, y));
				}
			}
		}
		ite = around.iterator();
	}
	
	private double distance(int x1, int y1, int x2, int y2){
		return Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));
	}
	
	public boolean hasNext() {
		return ite.hasNext();
	}

	public Pixel next() {
		return ite.next();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
