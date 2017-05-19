package fr.inra.sad_paysage.apiland.core.space.impl.raster;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class FunctionalDistanceSinglePixelIterator implements Iterator<Pixel>{
	
	private Pixel pixel;
	
	private double distance;
	
	private Set<Pixel> around;
	
	private Iterator<Pixel> ite;
	
	private Matrix matrix;
	
	private Matrix frictionMat;
	
	private Friction frictionMap;
	
	private boolean map;
	
	public FunctionalDistanceSinglePixelIterator(Pixel pixel, double distance, Matrix matrix, Matrix friction){
		//System.out.println(pixel);
		this.pixel = pixel;
		this.distance = distance;
		this.matrix = matrix;
		this.frictionMat = friction;
		map = false;
		init();
	}
	
	public FunctionalDistanceSinglePixelIterator(Pixel pixel, double distance, Matrix matrix, Friction friction){
		//System.out.println(pixel);
		this.pixel = pixel;
		this.distance = distance;
		this.matrix = matrix;
		this.frictionMap = friction;
		map = true;
		init();
	}

	private void init(){
		around = new TreeSet<Pixel>();
		Map<Pixel, Double> rcm = new HashMap<Pixel, Double>();
		rcm.put(pixel, 0.0);
		TreeSet<Pixel> waits = new TreeSet<Pixel>();
		waits.add(pixel);
		while(!waits.isEmpty()){
			diffuse(rcm, waits);
		}
		ite = around.iterator();
	}
	
	private void diffuse(Map<Pixel, Double> rcm, TreeSet<Pixel> waits){
		Pixel p = waits.pollFirst();
		double f;
		if(map){
			f = frictionMap.get(matrix.get(p));
		}else{
			f = frictionMat.get(p);
		}
		if(f != Raster.getNoDataValue()){
			
			double v = rcm.get(p);
			diffuseRook(p, new Pixel(p.x(), p.y()-1), v, f, rcm, waits); // nord
			diffuseRook(p, new Pixel(p.x()+1, p.y()), v, f, rcm, waits); // est
			diffuseRook(p, new Pixel(p.x(), p.y()+1), v, f, rcm, waits); // sud
			diffuseRook(p, new Pixel(p.x()-1, p.y()), v, f, rcm, waits); // ouest
			
			diffuseQueen(p, new Pixel(p.x()+1, p.y()-1), v, f, rcm, waits); // nord est
			diffuseQueen(p, new Pixel(p.x()+1, p.y()+1), v, f, rcm, waits); // sud est
			diffuseQueen(p, new Pixel(p.x()-1, p.y()+1), v, f, rcm, waits); // sud ouest
			diffuseQueen(p, new Pixel(p.x()-1, p.y()-1), v, f, rcm, waits); // nord ouest
			//ever.put(p, 1);
		}
	}
	
	private void diffuseRook(Pixel pc, Pixel op, double v, double f, Map<Pixel, Double> rcm, TreeSet<Pixel> waits){
		double ov, of;
		if(matrix.contains(op)){
			if(map){
				of = frictionMap.get(matrix.get(op));
			}else{
				of = frictionMat.get(op);
			}
			if(of != Raster.getNoDataValue()){
				ov = v + (matrix.cellsize()/2*f + matrix.cellsize()/2*of);
				if(ov <= distance){
					around.add(op);
					rcm.put(op, ov);
					waits.add(op);
				}
			}
		}
	}
	
	private void diffuseQueen(Pixel pc, Pixel op, double v, double f, Map<Pixel, Double> rcm, TreeSet<Pixel> waits){
		double ov, of;
		if(matrix.contains(op)){
			if(map){
				of = frictionMap.get(matrix.get(op));
			}else{
				of = frictionMat.get(op);
			}
			if(of != Raster.getNoDataValue()){
				ov = v + (Math.sqrt(2)*matrix.cellsize()/2*f + Math.sqrt(2)*matrix.cellsize()/2*of);
				if(ov <= distance){
					around.add(op);
					rcm.put(op, ov);
					waits.add(op);
				}
			}
		}
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

