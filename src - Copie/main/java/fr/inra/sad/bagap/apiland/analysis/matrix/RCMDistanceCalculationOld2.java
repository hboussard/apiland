package fr.inra.sad.bagap.apiland.analysis.matrix;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelComposite;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixFactory;

public class RCMDistanceCalculationOld2 extends MatrixCalculation {
	
	//private Set<Integer> values;
	private Collection<Integer> values;
	
	private boolean map;
	
	private Friction frictionMap;
	
	private Matrix frictionMat;
	
	private double threshold;
	
	//private long count;
	
	/*
	public RCMDistanceCalculation(Matrix m, Friction f, Set<Integer> values) {
		this(m, f, values, Raster.getNoDataValue());
	}
	
	public RCMDistanceCalculation(Matrix m, Friction f, Set<Integer> values, double threshold) {
		super(m);
		this.frictionMap = f;
		this.map = true;
		this.values = values;
		this.threshold = threshold;
		
	}
	
	public RCMDistanceCalculation(Matrix m, Matrix f, Set<Integer> values) {
		this(m, f, values, Raster.getNoDataValue());
	}
	
	public RCMDistanceCalculation(Matrix m, Matrix f, Set<Integer> values, double threshold) {
		super(m);
		this.frictionMat = f;
		this.map = false;
		this.values = values;
		this.threshold = threshold;
	}
	*/
	
	public RCMDistanceCalculationOld2(Matrix m, Friction f, Collection<Integer> values) {
		this(m, f, values, Raster.getNoDataValue());
	}
	
	public RCMDistanceCalculationOld2(Matrix m, Friction f, Collection<Integer> values, double threshold) {
		super(m);
		this.frictionMap = f;
		this.map = true;
		this.values = values;
		this.threshold = threshold;
		
	}
	
	public RCMDistanceCalculationOld2(Matrix m, Matrix f, Collection<Integer> values) {
		this(m, f, values, Raster.getNoDataValue());
	}
	
	public RCMDistanceCalculationOld2(Matrix m, Matrix f, Collection<Integer> values, double threshold) {
		super(m);
		this.frictionMat = f;
		this.map = false;
		this.values = values;
		this.threshold = threshold;
	}

	@Override
	public void doInit() {
		//count = 0l;
	}

	@Override
	public void doRun() {
		//Matrix dist = MatrixFactory.get(matrix().getType()).create(matrix());
		//System.out.println("création de la matrice");
		Matrix dist = MatrixFactory.get(matrix().getType()).create(matrix().width(), matrix().height(), matrix().cellsize(), matrix().minX(), matrix().maxX(), matrix().minY(), matrix().maxY(), matrix().noDataValue());
		
		//System.out.println("initialisation de la matrice");
		if(threshold != Raster.getNoDataValue()){
			dist.init(threshold);
		}else{
			dist.init(Integer.MAX_VALUE);
		}
		
		int v;
		int width = matrix().width();
		int height = matrix().height();
		
		int total = (width * height) * 2;
		
		
		/*
		 * PixelComposite pc = new PixelComposite(); 
		System.out.println("récupération des pixels sources");
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				v = matrix().get(x, y);
				for(int c : values){
					if(v == c){
						dist.put(x, y, 0);
						pc.addSimplePixel(new Pixel(x, y));
						break;
					}
				}
				updateProgression(total);
			}
		}
		//rc.addSimplePixelComposite(pc);
		*/
		
		LinkedList<Pixel> wp = new LinkedList<Pixel>();
		//Map<Pixel, Double> waits = new HashMap<Pixel, Double>();
		//GComparator<Pixel> pComparator = new GComparator(waits);
		//System.out.println("récupération des pixels sources");
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				v = new Double(matrix().get(x, y)).intValue();
				if(values.contains(v)){
					dist.put(x, y, 0);
					int v0 = new Double(matrix().get(x-1, y)).intValue();
					if(v0 == Raster.getNoDataValue() || values.contains(v0)){
						int v1 = new Double(matrix().get(x-1, y-1)).intValue();
						if(v1 == Raster.getNoDataValue() || values.contains(v1)){
							int v2 = new Double(matrix().get(x-1, y+1)).intValue();
							if(v2 == Raster.getNoDataValue() || values.contains(v2)){
								int v3 = new Double(matrix().get(x, y+1)).intValue();
								if(v3 == Raster.getNoDataValue() || values.contains(v3)){
									int v4 = new Double(matrix().get(x, y-1)).intValue();
									if(v4 == Raster.getNoDataValue() || values.contains(v4)){
										int v5 = new Double(matrix().get(x+1, y-1)).intValue();
										if(v5 == Raster.getNoDataValue() || values.contains(v5)){
											int v6 = new Double(matrix().get(x+1, y)).intValue();
											if(v6 == Raster.getNoDataValue() || values.contains(v6)){
												int v7 = new Double(matrix().get(x+1, y+1)).intValue();
												if(v7 == Raster.getNoDataValue() || values.contains(v7)){
													// do nothing
												}else{
													Pixel p = new Pixel(x, y);
													wp.add(p);
												}
											}else{
												Pixel p = new Pixel(x, y);
												wp.add(p);
											}
										}else{
											Pixel p = new Pixel(x, y);
											wp.add(p);
										}
									}else{
										Pixel p = new Pixel(x, y);
										wp.add(p);
									}
								}else{
									Pixel p = new Pixel(x, y);
									wp.add(p);
								}
							}else{
								Pixel p = new Pixel(x, y);
								wp.add(p);
							}
						}else{
							Pixel p = new Pixel(x, y);
							wp.add(p);
						}
					}else{
						Pixel p = new Pixel(x, y);
						wp.add(p);
					}
				}
				updateProgression(total);
			}
		}
		
		
		/*
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
		}*/
		
		//System.out.println("sélection des pixels sources");
		/*
		// pour la gestion des pixels a traiter en ordre croissant de distance
		Map<Pixel, Double> waits = new HashMap<Pixel, Double>();
		GComparator<Pixel> pComparator = new GComparator(waits);
		LinkedList<Pixel> wp = new LinkedList<Pixel>();
		Iterator<Pixel> ite;
		Pixel p;
		for(Raster r : pc){
			ite = r.getBoundaries();
			while(ite.hasNext()){
				p = ite.next();
				waits.put(p, 0.0);
				wp.add(p);
			}
		}
		*/
		//System.out.println("diffusion");
		Pixel lp = new Pixel(-1, -1);
		while(wp.size() > 0){
		//while(waits.size() > 0){
			//updateProgression(total);
			
			if(wp.size() % 10000 == 0){
				System.out.println(wp.size());
			}
			
			lp = diffusion(matrix(), dist, wp, lp);
			
		}
		//System.out.println("fin diffusion");
		
		//nettoyage
		for(int y=0; y<height; y++){
			//System.out.println("1 : "+y);
			for(int x=0; x<width; x++){
				v = new Double(matrix().get(x, y)).intValue();
				if(v == Raster.getNoDataValue()){
					dist.put(x, y, Raster.getNoDataValue());
				}
				updateProgression(total);
			}
		}
		
		setResult(dist);
	}
	
	private Pixel diffusion(Matrix m, Matrix dist, /*Map<Pixel, Double> waits, GComparator pComparator,*/ LinkedList<Pixel> wp, Pixel lastP){
		//System.out.println("d1");
		//Collections.sort(wp, pComparator);
		//System.out.println("d2");
		Pixel p = wp.poll(); // recuperation du prochain point de diffusion
		
		if(p != lastP){
			
			//double dd = waits.remove(p); // distance cumulee au point de diffusion
			double dd = dist.get(p);
			
			//System.out.println(p+" "+wp.size());
			
			if(threshold == Raster.getNoDataValue() || threshold > dd){
				double vd = m.get(p); // valeur au point de diffusion
				double fd = friction(vd, p.x(), p.y()); // friction au point de diffusion
				//System.out.println("diffusion au pixel "+p+" avec une distance cumulee de "+dd);
				Pixel np;
				double v, d;
				//System.out.println("d3");
				Iterator<Pixel> ite = p.getCardinalMargins(); // pour chaque pixel cardinal (4)
				//System.out.println("d4");
				while(ite.hasNext()){
					np = ite.next();
					v = m.get(np); // valeur au point cardinal
					if( v != Raster.getNoDataValue()){
						double fc = friction(v, np.x(), np.y());
						//d = dd + (m.cellsize()/2) * friction.get(vd) + (m.cellsize()/2) * friction.get(v); // distance au point cardinal
						d = dd + (m.cellsize()/2) * fd + (m.cellsize()/2) * fc; // distance au point cardinal
						double vdn = dist.get(np);
						if(d < vdn){ // MAJ ?
							//System.out.println("deplacement cardinal vers le pixel "+np+" avec une distance cumulee de "+d);
							dist.put(np, d);
							//if(vdn == 500){
							//if(!wp.contains(np)){
								wp.addLast(np);
							//}
							/*
							if(!waits.containsKey(np)){
								wp.addLast(np);
							}
							waits.put(np, d);
							*/
						}
					}
				}
				//System.out.println("d5");
				ite = p.getDiagonalMargins(); // pour chaque pixel diagonal (4)
				//System.out.println("d6");
				while(ite.hasNext()){
					np = ite.next();
					v = m.get(np); // valeur au point diagonal
					if( v != Raster.getNoDataValue()){
						double fc = friction(v, np.x(), np.y());
						//d = dd + (m.cellsize()*Math.sqrt(2)/2) * friction.get(vd) + (m.cellsize()*Math.sqrt(2)/2) * friction.get(v); // distance au point diagonal
						d = dd + (m.cellsize()*Math.sqrt(2)/2) * fd + (m.cellsize()*Math.sqrt(2)/2) * fc; // distance au point diagonal
						double vdn = dist.get(np);
						if(d < vdn){ // MAJ ?
							//System.out.println("deplacement diagonal vers le pixel "+np+" avec une distance cumulee de "+d);
							dist.put(np, d);
							//if(vdn == 500){
							//if(!wp.contains(np)){
								wp.addLast(np);
							//}
							/*
							if(!waits.containsKey(np)){
								wp.addLast(np);
							}
							waits.put(np, d);
							*/
						}
					}
				}
			}
			
			return p;
		}
		
		return lastP;
		//System.out.println("d7");
	}

	private double friction(double v, int x, int y){
		if(map){
			return frictionMap.get(v);
		}
		/*
		long lcount = System.currentTimeMillis();
		double fm = frictionMat.get(x, y);
		long lcount2 = System.currentTimeMillis();
		count += lcount2 - lcount;
		return fm;
		*/
		return frictionMat.get(x, y);
	}
	
	@Override
	public void doClose() {
		//sources = null;
		//System.out.println("temps "+count);
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
