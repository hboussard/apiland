package fr.inra.sad.bagap.apiland.analysis.matrix;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixFactory;

public class RCMDistanceCalculationTest3 extends MatrixCalculation {

	private Collection<Integer> values;

	private boolean map;

	private Friction frictionMap;

	private Matrix frictionMat;

	private double threshold;

	public RCMDistanceCalculationTest3(Matrix m, Friction f, Collection<Integer> values) {
		this(m, f, values, Raster.getNoDataValue());
	}

	public RCMDistanceCalculationTest3(Matrix m, Friction f, Collection<Integer> values, double threshold) {
		super(m);
		this.frictionMap = f;
		this.map = true;
		this.values = values;
		if(threshold == Raster.getNoDataValue()){
			this.threshold = Integer.MAX_VALUE;
		}else{
			this.threshold = threshold;
		}
	}

	public RCMDistanceCalculationTest3(Matrix m, Matrix f, Collection<Integer> values) {
		this(m, f, values, Raster.getNoDataValue());
	}

	public RCMDistanceCalculationTest3(Matrix m, Matrix f, Collection<Integer> values, double threshold) {
		super(m);
		this.frictionMat = f;
		this.map = false;
		this.values = values;
		if(threshold == Raster.getNoDataValue()){
			this.threshold = Integer.MAX_VALUE;
		}else{
			this.threshold = threshold;
		}
	}

	@Override
	public void doInit() {
	}

	@Override
	public void doRun() {

		System.out.println("création de la matrice");
		Matrix dist = MatrixFactory.get(matrix().getType()).create(matrix().width(), matrix().height(),
				matrix().cellsize(), matrix().minX(), matrix().maxX(), matrix().minY(), matrix().maxY(),
				matrix().noDataValue());

		System.out.println("initialisation de la matrice");
		dist.init(threshold);

		int v;
		int width = matrix().width();
		int height = matrix().height();

		int total = (width * height) * 2;

		// pour la gestion des pixels a traiter en ordre croissant de distance
		Map<Double, Set<Pixel>> waits = new TreeMap<Double, Set<Pixel>>();

		System.out.println("récupération des pixels sources");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				v = new Double(matrix().get(x, y)).intValue();
				if (values.contains(v)) {
					dist.put(x, y, 0);
					int v0 = new Double(matrix().get(x - 1, y)).intValue();
					if (v0 == Raster.getNoDataValue() || values.contains(v0)) {
						int v1 = new Double(matrix().get(x - 1, y - 1)).intValue();
						if (v1 == Raster.getNoDataValue() || values.contains(v1)) {
							int v2 = new Double(matrix().get(x - 1, y + 1)).intValue();
							if (v2 == Raster.getNoDataValue() || values.contains(v2)) {
								int v3 = new Double(matrix().get(x, y + 1)).intValue();
								if (v3 == Raster.getNoDataValue() || values.contains(v3)) {
									int v4 = new Double(matrix().get(x, y - 1)).intValue();
									if (v4 == Raster.getNoDataValue() || values.contains(v4)) {
										int v5 = new Double(matrix().get(x + 1, y - 1)).intValue();
										if (v5 == Raster.getNoDataValue() || values.contains(v5)) {
											int v6 = new Double(matrix().get(x + 1, y)).intValue();
											if (v6 == Raster.getNoDataValue() || values.contains(v6)) {
												int v7 = new Double(matrix().get(x + 1, y + 1)).intValue();
												if (v7 == Raster.getNoDataValue() || values.contains(v7)) {
													// do nothing
												} else {
													setPixelAndValue(waits, new Pixel(x, y), 0.0);
												}
											} else {
												setPixelAndValue(waits, new Pixel(x, y), 0.0);
											}
										} else {
											setPixelAndValue(waits, new Pixel(x, y), 0.0);
										}
									} else {
										setPixelAndValue(waits, new Pixel(x, y), 0.0);
									}
								} else {
									setPixelAndValue(waits, new Pixel(x, y), 0.0);
								}
							} else {
								setPixelAndValue(waits, new Pixel(x, y), 0.0);
							}
						} else {
							setPixelAndValue(waits, new Pixel(x, y), 0.0);
						}
					} else {
						setPixelAndValue(waits, new Pixel(x, y), 0.0);
					}
				}
				updateProgression(total);
			}
		}

		System.out.println("sélection des pixels sources");
		
		System.out.println("diffusion");
		int treat = 0;
		while (waits.size() > 0) {

			//if (treat % 10000 == 0) {
			//	System.out.println(treat + " " + waits.size());
			//}

			treat = diffusionPaquet(treat, matrix(), dist, waits);
		}
		System.out.println("fin diffusion");

		// nettoyage
		for (int y = 0; y < height; y++) {
			// System.out.println("1 : "+y);
			for (int x = 0; x < width; x++) {
				v = new Double(matrix().get(x, y)).intValue();
				if (v == Raster.getNoDataValue()) {
					dist.put(x, y, Raster.getNoDataValue());
				}
				updateProgression(total);
			}
		}

		setResult(dist);
	}

	private void setPixelAndValue(Map<Double, Set<Pixel>> waits, Pixel pixel, Double value) {
		if (!waits.containsKey(value)) {
			waits.put(value, new HashSet<Pixel>());
		}
		waits.get(value).add(pixel);
	}

	private int diffusionPaquet(int treat, Matrix m, Matrix dist, Map<Double, Set<Pixel>> waits){
		
		//System.out.println(treat + " " + waits.size());
		
		Iterator<Entry<Double, Set<Pixel>>> iteEntry = waits.entrySet().iterator();
		Entry<Double, Set<Pixel>> entry = iteEntry.next(); // récupération des pixels à diffuser
		iteEntry.remove();
		
		if(entry.getValue().size() != 0){
			double dd = entry.getKey(); // récupération de la valeur de diffusion 
			
			System.out.println(treat + " " + waits.size()+" "+dd+" "+entry.getValue().size());
			
			Iterator<Pixel> itePixel = entry.getValue().iterator();
			Pixel p;
			while(itePixel.hasNext()){
				p = itePixel.next();
				itePixel.remove();
				
				treat = diffusion(treat, m, dist, waits, p, dd);
			}
		}
		
		
		
		/*
		iteEntry = waits.entrySet().iterator();
		while(iteEntry.hasNext()){
			entry = iteEntry.next();
			if(entry.getValue().size() == 0){
				iteEntry.remove();
			}
		}*/
		
		return treat;
	}
	
	private int diffusion(int treat, Matrix m, Matrix dist, Map<Double, Set<Pixel>> waits, Pixel p, double dd) {

		// System.out.println("traitement de pixel "+p);

		if (threshold > dd) {
			double vd = m.get(p); // valeur au point de diffusion
			double fd = friction(vd, p.x(), p.y()); // friction au point de diffusion
			
			Pixel np;
			double v, d;
			Iterator<Pixel> ite = p.getCardinalMargins(); // pour chaque pixel cardinal (4)
			while (ite.hasNext()) {
				np = ite.next();
				v = m.get(np); // valeur au point cardinal
				if (v != Raster.getNoDataValue()) {
					double fc = friction(v, np.x(), np.y());
					d = dd + (m.cellsize() / 2) * fd + (m.cellsize() / 2) * fc; // distance au point cardinal
					double vdn = dist.get(np);
					
					if (d < vdn) { // MAJ ?
						dist.put(np, d);

						if(vdn != threshold){
							waits.get(vdn).remove(np);
						}
						setPixelAndValue(waits, np, d);
					}
				}
			}
			ite = p.getDiagonalMargins(); // pour chaque pixel diagonal (4)
			while (ite.hasNext()) {
				np = ite.next();
				v = m.get(np); // valeur au point diagonal
				if (v != Raster.getNoDataValue()) {
					double fc = friction(v, np.x(), np.y());
					d = dd + (m.cellsize() * Math.sqrt(2) / 2) * fd + (m.cellsize() * Math.sqrt(2) / 2) * fc; // distance au point diagonal
					double vdn = dist.get(np);
					if (d < vdn) { // MAJ ?
						dist.put(np, d);

						if(vdn != threshold){
							waits.get(vdn).remove(np);
						}
						setPixelAndValue(waits, np, d);
					}
				}
			}
		}

		return treat + 1;
	}

	private double friction(double v, int x, int y) {
		if (map) {
			return frictionMap.get(v);
		}
		/*
		 * long lcount = System.currentTimeMillis(); double fm =
		 * frictionMat.get(x, y); long lcount2 = System.currentTimeMillis();
		 * count += lcount2 - lcount; return fm;
		 */
		return frictionMat.get(x, y);
	}

	@Override
	public void doClose() {
		// sources = null;
		// System.out.println("temps "+count);
	}

}
