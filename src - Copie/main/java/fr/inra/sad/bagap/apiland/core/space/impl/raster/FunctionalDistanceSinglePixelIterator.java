package fr.inra.sad.bagap.apiland.core.space.impl.raster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class FunctionalDistanceSinglePixelIterator implements Iterator<Pixel>{
	
	private Pixel pixel;
	
	private double distance;
	
	private Set<Pixel> around, ever, treat;
	
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
		around = new HashSet<Pixel>();
		treat = new HashSet<Pixel>();
		ever = new HashSet<Pixel>();
		Map<Pixel, Double> rcm = new HashMap<Pixel, Double>();
		rcm.put(pixel, 0.0);
		TreeSet<Pixel> waits = new TreeSet<Pixel>();
		waits.add(pixel);
		//System.out.println("ici");
		//StringBuffer sb = new StringBuffer("");
		//int g = 0;
		while(!waits.isEmpty()){
			//g++;
			//sb.append(waits.size()+" ");
			diffuse(rcm, waits);
		}
		//System.out.println(g);
		//System.out.println(sb.toString());
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
			
			Pixel pn = new Pixel(p.x(), p.y()-1);
			Pixel pe = new Pixel(p.x()+1, p.y());
			Pixel ps = new Pixel(p.x(), p.y()+1);
			Pixel po = new Pixel(p.x()-1, p.y());
			Pixel pne = new Pixel(p.x()+1, p.y()-1);
			Pixel pse = new Pixel(p.x()+1, p.y()+1);
			Pixel pso = new Pixel(p.x()-1, p.y()+1);
			Pixel pno = new Pixel(p.x()-1, p.y()-1);
			
			//if(!(treat.contains(pn)
			//		&& treat.contains(pe)
			//		&& treat.contains(ps)
			//		&& treat.contains(po)
			//		&& treat.contains(pne)
			//		&& treat.contains(pse)
			//		&& treat.contains(pso)
			//		&& treat.contains(pno))){
			
			double v = rcm.get(p);
			//System.out.println("1");
			diffuseRook(p, pn, v, f, rcm, waits); // nord
			//System.out.println("2");
			diffuseRook(p, pe, v, f, rcm, waits); // est
			//System.out.println("3");
			diffuseRook(p, ps, v, f, rcm, waits); // sud
			//System.out.println("4");
			diffuseRook(p, po, v, f, rcm, waits); // ouest
			//System.out.println("5");
			diffuseQueen(p, pne, v, f, rcm, waits); // nord est
			//System.out.println("6");
			diffuseQueen(p, pse, v, f, rcm, waits); // sud est
			//System.out.println("7");
			diffuseQueen(p, pso, v, f, rcm, waits); // sud ouest
			//System.out.println("8");
			diffuseQueen(p, pno, v, f, rcm, waits); // nord ouest
			//System.out.println("9");
			ever.add(p);
			//}
		}
	}
	
	private void diffuseRook(Pixel pc, Pixel op, double v, double f, Map<Pixel, Double> rcm, TreeSet<Pixel> waits){
		double ov, of;
		if(matrix.contains(op) && !treat.contains(op)){
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
					if(!ever.contains(op)){
						waits.add(op);
						treat.add(op);
					}
				}
			}
		}
	}
	
	private void diffuseQueen(Pixel pc, Pixel op, double v, double f, Map<Pixel, Double> rcm, TreeSet<Pixel> waits){
		double ov, of;
		if(matrix.contains(op) && !treat.contains(op)){
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
					if(!ever.contains(op)){
						waits.add(op);
						treat.add(op);
					}
				}
			}
		}
	}
	
	@Override
	public boolean hasNext() {
		return ite.hasNext();
	}

	@Override
	public Pixel next() {
		return ite.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}

