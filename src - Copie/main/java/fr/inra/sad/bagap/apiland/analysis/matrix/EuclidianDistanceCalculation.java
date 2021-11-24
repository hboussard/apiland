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
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixFactory;

public class EuclidianDistanceCalculation extends MatrixCalculation {

	private RasterComposite sources;
	
	private int[] cat;
	
	public EuclidianDistanceCalculation(Matrix m, RasterComposite rc, int... cat) {
		super(m);
		sources = rc;
		this.cat = cat;
	}

	@Override
	public void doInit() {}

	@Override
	public void doRun() {
		Matrix dist = MatrixFactory.get(matrix().getType()).create(matrix());
		dist.init(999999);
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
		
		// pour la gestion des pixels a traiter en ordre croissant de distance
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
			euclidianDiffusion(matrix(), dist, waits, pComparator, wp);
		}
		
		setResult(dist);
	}
	
	private void euclidianDiffusion(Matrix m, Matrix dist, Map<Pixel, Double> waits, GComparator pComparator, LinkedList<Pixel> wp){
		Collections.sort(wp, pComparator);
		Pixel p = wp.poll();
		double d = waits.remove(p);
		Pixel np;
		double v;
		Iterator<Pixel> ite;
		ite = p.getCardinalMargins();
		while(ite.hasNext()){
			np = ite.next();
			v = d + m.cellsize(); 
			if(v < dist.get(np)){
				dist.put(np, v);
				if(!waits.containsKey(np)){
					wp.addLast(np);
				}
				waits.put(np, v);
			}
		}
		
		ite = p.getDiagonalMargins();
		while(ite.hasNext()){
			np = ite.next();
			v = d + m.cellsize()*Math.sqrt(2); 
			if(v < dist.get(np)){
				dist.put(np, v);
				if(!waits.containsKey(np)){
					wp.addLast(np);
				}
				waits.put(np, v);	
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
