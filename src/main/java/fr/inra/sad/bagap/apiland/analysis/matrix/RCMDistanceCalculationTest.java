package fr.inra.sad.bagap.apiland.analysis.matrix;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixFactory;

public class RCMDistanceCalculationTest extends MatrixCalculation {
	
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
	
	public RCMDistanceCalculationTest(Matrix m, Friction f, Collection<Integer> values) {
		this(m, f, values, Raster.getNoDataValue());
	}
	
	public RCMDistanceCalculationTest(Matrix m, Friction f, Collection<Integer> values, double threshold) {
		super(m);
		this.frictionMap = f;
		this.map = true;
		this.values = values;
		this.threshold = threshold;
		
	}
	
	public RCMDistanceCalculationTest(Matrix m, Matrix f, Collection<Integer> values) {
		this(m, f, values, Raster.getNoDataValue());
	}
	
	public RCMDistanceCalculationTest(Matrix m, Matrix f, Collection<Integer> values, double threshold) {
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
		
		System.out.println("création de la matrice");
		Matrix dist = MatrixFactory.get(matrix().getType()).create(matrix().width(), matrix().height(), matrix().cellsize(), matrix().minX(), matrix().maxX(), matrix().minY(), matrix().maxY(), matrix().noDataValue());
		
		System.out.println("initialisation de la matrice");
		if(threshold != Raster.getNoDataValue()){
			dist.init(threshold);
		}else{
			dist.init(Integer.MAX_VALUE);
		}
		
		int v;
		int width = matrix().width();
		int height = matrix().height();
		
		int total = (width * height) * 2;
		
		// pour la gestion des pixels a traiter en ordre croissant de distance
		Map<Pixel, Double> waits = new HashMap<Pixel, Double>();
		PixelWithValueComparator pComparator = new PixelWithValueComparator(waits);
		Set<Pixel> sortedWaits = new TreeSet<Pixel>(pComparator);
		
		System.out.println("récupération des pixels sources");
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
													waits.put(new Pixel(x, y), 0.0);
												}
											}else{
												waits.put(new Pixel(x, y), 0.0);
											}
										}else{
											waits.put(new Pixel(x, y), 0.0);
										}
									}else{
										waits.put(new Pixel(x, y), 0.0);
									}
								}else{
									waits.put(new Pixel(x, y), 0.0);
								}
							}else{
								waits.put(new Pixel(x, y), 0.0);
							}
						}else{
							waits.put(new Pixel(x, y), 0.0);
						}
					}else{
						waits.put(new Pixel(x, y), 0.0);
					}
				}
				updateProgression(total);
			}
		}
		sortedWaits.addAll(waits.keySet());
		System.out.println("début "+waits.size()+" "+sortedWaits.size());
		System.out.println("sélection des pixels sources");
		
		System.out.println("diffusion");
		int treat = 0;
		while(waits.size() > 0){
			
			if(waits.size() % 10000 == 0){
				System.out.println(treat+" "+waits.size()+" "+sortedWaits.size());
			}
			
			treat = diffusion(treat, matrix(), dist, waits, sortedWaits);
			//System.out.println(treat);
		}
		System.out.println("fin diffusion");
		
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
	
	private int  diffusion(int treat, Matrix m, Matrix dist, Map<Pixel, Double> waits, Set<Pixel> sortedWaits){
		
		Pixel p = sortedWaits.iterator().next(); // recuperation du prochain point de diffusion
		double dd = waits.get(p);
		
		//System.out.println("traitement de pixel "+p);
		
		//System.out.println("avant remove "+sortedWaits.size()+" "+waits.size());
		sortedWaits.remove(p);
		waits.remove(p);
		//System.out.println("après remove "+sortedWaits.size()+" "+waits.size());
		
		if(threshold == Raster.getNoDataValue() || threshold > dd){
			double vd = m.get(p); // valeur au point de diffusion
			double fd = friction(vd, p.x(), p.y()); // friction au point de diffusion
			
			Pixel np;
			double v, d;
			Iterator<Pixel> ite = p.getCardinalMargins(); // pour chaque pixel cardinal (4)
			while(ite.hasNext()){
				np = ite.next();
				v = m.get(np); // valeur au point cardinal
				if( v != Raster.getNoDataValue()){
					double fc = friction(v, np.x(), np.y());
					d = dd + (m.cellsize()/2) * fd + (m.cellsize()/2) * fc; // distance au point cardinal
					double vdn = dist.get(np);
					if(d < vdn){ // MAJ ?
						dist.put(np, d);
						
						//System.out.println("test maj de pixel "+np);
						
						if(waits.keySet().contains(np)){
							//System.out.println("1 avant maj "+sortedWaits.size()+" "+waits.size());
							if(!sortedWaits.remove(np)){
								System.out.println();
								for(Pixel px : sortedWaits){
									System.out.println(np+" "+px+" "+waits.get(px));
								}
								//System.out.println(sortedWaits);
								System.out.println();
							}
							//System.out.println(sortedWaits.remove(np));
							waits.replace(np, vdn, d);
							sortedWaits.add(np);
							//System.out.println("1 après maj "+sortedWaits.size()+" "+waits.size());
						}else{
							//System.out.println("add "+np+" size "+waits.size());
							//System.out.println("2 avant ajout "+sortedWaits.size()+" "+waits.size());
							waits.put(np, d);
							sortedWaits.add(np);
							//System.out.println("2 après ajout "+sortedWaits.size()+" "+waits.size());
						}
					}
				}
			}
			ite = p.getDiagonalMargins(); // pour chaque pixel diagonal (4)
			while(ite.hasNext()){
				np = ite.next();
				v = m.get(np); // valeur au point diagonal
				if( v != Raster.getNoDataValue()){
					double fc = friction(v, np.x(), np.y());
					d = dd + (m.cellsize()*Math.sqrt(2)/2) * fd + (m.cellsize()*Math.sqrt(2)/2) * fc; // distance au point diagonal
					double vdn = dist.get(np);
					if(d < vdn){ // MAJ ?
						dist.put(np, d);
						
						//System.out.println("test maj de pixel "+np);
						
						if(waits.keySet().contains(np)){
							//System.out.println("3 avant maj "+sortedWaits.size()+" "+waits.size());
							System.out.println(sortedWaits.remove(np));
							waits.replace(np, vdn, d);
							sortedWaits.add(np);
							//System.out.println("3 après maj "+sortedWaits.size()+" "+waits.size());
						}else{
							//System.out.println("add "+np+" size "+waits.size());
							//System.out.println("4 avant ajout "+sortedWaits.size()+" "+waits.size());
							waits.put(np, d);
							sortedWaits.add(np);
							//System.out.println("4 après ajout "+sortedWaits.size()+" "+waits.size());
						}
					}
				}
			}
		}
		
		return treat+1;
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
	
	private class PixelWithValueComparator implements Comparator<Pixel> {

		private Map<Pixel, Double> map;

		public PixelWithValueComparator(Map<Pixel, Double> map) {
			this.map = map;
		}

		@Override
		public int compare(Pixel p1, Pixel p2) {
			if(p1.equals(p2)){
				return 0;
			}

			int value = map.get(p1).compareTo(map.get(p2));
			if(value < 1){
				return -1;
			}/*else if(value > 1){
				return 1;	
			}
			if((p1.x() * 152 + p1.y() * 7 + 1) > (p2.x() * 152 + p2.y() * 7 + 1)){
				return -1;
			}*/
			return 1;
		}
		
	}

	
}
