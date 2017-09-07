package fr.inra.sad.bagap.apiland.analysis.matrix;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelComposite;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RasterComposite;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixFactory;

public class RCMDistanceCalculation extends MatrixCalculation {

	private RasterComposite sources;
	
	private int[] cat;
	
	private Friction friction;
	
	public RCMDistanceCalculation(Matrix m, RasterComposite rc, Friction f, int... cat) {
		super(m);
		this.sources = rc;
		this.friction = f;
		this.cat = cat;
	}

	@Override
	public void doInit() {}

	@Override
	public void doRun() {
		Matrix dist = MatrixFactory.get(matrix().getType()).create(matrix());
		dist.init(Integer.MAX_VALUE);
		int v;
		RasterComposite rc = new RasterComposite();
		for(Raster r : sources.getRasters()){
			v = (int)matrix().get(r.getOne());
			for(int c : cat){
				if(v == c){
					for(Pixel p : r){
						dist.put(p, 0);
					}
					rc.addSimplePixelComposite((PixelComposite)r);
					break;
				}
			}
		}
		
		// pour la gestion des pixels à traiter en ordre croissant de distance
		Map<Pixel, Double> waits = new HashMap<Pixel, Double>();
		GComparator<Pixel> pComparator = new GComparator(waits);
		LinkedList<Pixel> wp = new LinkedList<Pixel>();
		Iterator<Pixel> ite;
		Pixel p;
		for(Raster r : rc){
			ite = r.getBoundaries();
			while(ite.hasNext()){
				p = ite.next();
				waits.put(p, 0.0);
				wp.add(p);
			}
		}
		
		while(waits.size() > 0){
			diffusion(matrix(), dist, waits, pComparator, wp);
		}
		
		setResult(dist);
	}
	
	private void diffusion(Matrix m, Matrix dist, Map<Pixel, Double> waits, GComparator pComparator, LinkedList<Pixel> wp){
		Collections.sort(wp, pComparator);
		Pixel p = wp.poll(); // recuperation du prochain point de diffusion
		double dd = waits.remove(p); // distance cumulee au point de diffusion
		double vd = m.get(p); // valeur au point de diffusion
		//System.out.println("diffusion au pixel "+p+" avec une distance cumulee de "+dd);
		Pixel np;
		double v, d;
		Iterator<Pixel> ite = p.getCardinalMargins(); // pour chaque pixel cardinal (4) 
		while(ite.hasNext()){
			np = ite.next();
			v = m.get(np); // valeur au point cardinal
			if( v != Raster.getNoDataValue()){
				d = dd + (m.cellsize()/2) * friction.get(vd) + (m.cellsize()/2) * friction.get(v); // distance au point cardinal
				if(d < dist.get(np)){ // MAJ ?
					//System.out.println("deplacement cardinal vers le pixel "+np+" avec une distance cumulee de "+d);
					dist.put(np, d);
					if(!waits.containsKey(np)){
						wp.addLast(np);
					}
					waits.put(np, d);
				}
			}
		}
		
		ite = p.getDiagonalMargins(); // pour chaque pixel diagonal (4) 
		while(ite.hasNext()){
			np = ite.next();
			v = m.get(np); // valeur au point diagonal
			if( v != Raster.getNoDataValue()){
				d = dd + (m.cellsize()*Math.sqrt(2)/2) * friction.get(vd) + (m.cellsize()*Math.sqrt(2)/2) * friction.get(v); // distance au point diagonal
				if(d < dist.get(np)){ // MAJ ?
					//System.out.println("déplacement diagonal vers le pixel "+np+" avec une distance cumulée de "+d);
					dist.put(np, d);
					if(!waits.containsKey(np)){
						wp.addLast(np);
					}
					waits.put(np, d);	
				}
			}
		}
	}

	@Override
	public void doClose() {
		sources = null;
	}
	
	private class GComparator<G> implements Comparator<G> {

		private Map<G, Double> w;

		public GComparator(Map<G, Double> w) {
			this.w = w;
		}

		@Override
		public int compare(G g1, G g2) {
			return w.get(g1).compareTo(w.get(g2));
		}
	}

	
}
