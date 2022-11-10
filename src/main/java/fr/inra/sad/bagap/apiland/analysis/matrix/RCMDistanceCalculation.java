package fr.inra.sad.bagap.apiland.analysis.matrix;

import java.util.Collection;
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

public class RCMDistanceCalculation extends MatrixCalculation {

	private Collection<Integer> values;

	private boolean map;

	private Friction frictionMap;
	
	private double[] mat, frictionMat;

	private double threshold;
	
	private int width, height, total;

	public RCMDistanceCalculation(Matrix m, Friction f, Collection<Integer> values) {
		this(m, f, values, Raster.getNoDataValue());
	}

	public RCMDistanceCalculation(Matrix m, Friction f, Collection<Integer> values, double threshold) {
		super(m);
		
		this.width = m.width();
		this.height = m.height();
		
		this.mat = new double[width*height];
		for(int j=0; j<height; j++){
			for(int i=0; i<width; i++){
				this.mat[i+j*width] = m.get(i, j);
			}
		}
		
		this.frictionMap = f;
		this.map = true;
		this.values = values;
		if(threshold == Raster.getNoDataValue()){
			this.threshold = Integer.MAX_VALUE;
		}else{
			this.threshold = threshold;
		}
	}

	public RCMDistanceCalculation(Matrix m, Matrix f, Collection<Integer> values) {
		this(m, f, values, Raster.getNoDataValue());
	}

	public RCMDistanceCalculation(Matrix m, Matrix f, Collection<Integer> values, double threshold) {
		super(m);
		
		this.width = m.width();
		this.height = m.height();
		
		this.mat = new double[width*height];
		this.frictionMat = new double[width*height];
		
		for(int j=0; j<height; j++){
			for(int i=0; i<width; i++){
				this.mat[i+j*width] = m.get(i, j);
				this.frictionMat[i+j*width] = f.get(i, j);
			}
		}
		
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

		//System.out.println("création de la matrice");
		//Matrix dist = MatrixFactory.get(matrix().getType()).create(matrix().width(), matrix().height(),
		//		matrix().cellsize(), matrix().minX(), matrix().maxX(), matrix().minY(), matrix().maxY(),
		//		matrix().noDataValue());
		double[] dist = new double[width*height];
		for(int j=0; j<height; j++){
			for(int i=0; i<width; i++){
				dist[i+j*width] = threshold;	
			}
		}

		int v;

		total = (width * height) * 4;
		//System.out.println(total);
		// pour la gestion des pixels a traiter en ordre croissant de distance
		Map<Double, Set<Pixel>> waits = new TreeMap<Double, Set<Pixel>>();

		//System.out.println("récupération des pixels sources");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				//System.out.println(x+" "+y);
				//v = new Double(matrix().get(x, y)).intValue();
				v = new Double(mat[x+y*width]).intValue();
				if (values.contains(v)) {
					dist[x+y*width] = 0;
					int v0 = new Double(matrix().get(x - 1, y)).intValue();
					//int v0 = new Double(mat[(x - 1)+y*width]).intValue();
					if (v0 == Raster.getNoDataValue() || values.contains(v0)) {
						int v1 = new Double(matrix().get(x - 1, y - 1)).intValue();
						//int v1 = new Double(mat[(x - 1) + (y - 1)*width]).intValue();
						if (v1 == Raster.getNoDataValue() || values.contains(v1)) {
							int v2 = new Double(matrix().get(x - 1, y + 1)).intValue();
							//int v2 = new Double(mat[(x - 1) + (y + 1)*width]).intValue();
							if (v2 == Raster.getNoDataValue() || values.contains(v2)) {
								int v3 = new Double(matrix().get(x, y + 1)).intValue();
								//int v3 = new Double(mat[x+(y + 1)*width]).intValue();
								if (v3 == Raster.getNoDataValue() || values.contains(v3)) {
									int v4 = new Double(matrix().get(x, y - 1)).intValue();
									//int v4 = new Double(mat[x+(y - 1)*width]).intValue();
									if (v4 == Raster.getNoDataValue() || values.contains(v4)) {
										int v5 = new Double(matrix().get(x + 1, y - 1)).intValue();
										//int v5 = new Double(mat[(x + 1)+(y - 1)*width]).intValue();
										if (v5 == Raster.getNoDataValue() || values.contains(v5)) {
											int v6 = new Double(matrix().get(x + 1, y)).intValue();
											//int v6 = new Double(mat[(x + 1)+y*width]).intValue();
											if (v6 == Raster.getNoDataValue() || values.contains(v6)) {
												int v7 = new Double(matrix().get(x + 1, y + 1)).intValue();
												//int v7 = new Double(mat[(x + 1) + (y + 1)*width]).intValue();
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

		//System.out.println("sélection des pixels sources");
		
		//System.out.println("diffusion");
		int treat = 0;
		while (waits.size() > 0) {

			//if (treat % 10000 == 0) {
			//	System.out.println(treat + " " + waits.size());
			//}

			treat = diffusionPaquet(treat, dist, waits);
		}
		//System.out.println("fin diffusion");

		// nettoyage
		if(frictionMat != null){
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					v = new Double(frictionMat[x+y*width]).intValue();
					if (v == Raster.getNoDataValue()) {
						dist[x+y*width] = Raster.getNoDataValue();
					}
					updateProgression(total);
				}
			}
		}else{
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					v = new Double(mat[x+y*width]).intValue();
					if (v == Raster.getNoDataValue()) {
						dist[x+y*width] = Raster.getNoDataValue();
					}
					updateProgression(total);
				}
			}
		}
		/*
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("F:/Requete_SIG_LabPSE/sud_mayenne/raster/continuite/continuite_boise_3.asc"));
			
			writer.write("ncols "+matrix().width());
			writer.newLine();
			writer.write("nrows "+matrix().height());
			writer.newLine();
			writer.write("xllcorner "+matrix().minX());
			writer.newLine();
			writer.write("yllcorner "+matrix().minY());
			writer.newLine();
			writer.write("cellsize "+matrix().cellsize());
			writer.newLine();
			writer.write("NODATA_value "+Raster.getNoDataValue());
			writer.newLine();
			
			for(int j=0; j<height; j++){
				for(int i=0; i<width; i++){
					writer.write(dist[i+j*width]+" ");
					updateProgression(total);
				}
				writer.newLine();
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setResult(null);
		*/
		
		
		Matrix mdist = MatrixFactory.get(matrix().getType()).create(matrix().width(), matrix().height(),
				matrix().cellsize(), matrix().minX(), matrix().maxX(), matrix().minY(), matrix().maxY(),
				matrix().noDataValue());

		for(int j=0; j<height; j++){
			for(int i=0; i<width; i++){
				mdist.put(i, j, dist[i+j*width]);
				updateProgression(total);
			}
		}
		
		setResult(mdist);
		
	}

	private void setPixelAndValue(Map<Double, Set<Pixel>> waits, Pixel pixel, Double value) {
		if (!waits.containsKey(value)) {
			waits.put(value, new HashSet<Pixel>());
		}
		waits.get(value).add(pixel);
	}

	private int diffusionPaquet(int treat, double[] dist, Map<Double, Set<Pixel>> waits){
		
		//System.out.println(treat + " " + waits.size());
		
		Iterator<Entry<Double, Set<Pixel>>> iteEntry = waits.entrySet().iterator();
		Entry<Double, Set<Pixel>> entry = iteEntry.next(); // récupération des pixels à diffuser
		iteEntry.remove();
		
		if(entry.getValue().size() != 0){
			double dd = entry.getKey(); // récupération de la valeur de diffusion 
			
			//System.out.println(treat + " " + waits.size()+" "+dd+" "+entry.getValue().size());
			
			Iterator<Pixel> itePixel = entry.getValue().iterator();
			Pixel p;
			while(itePixel.hasNext()){
				p = itePixel.next();
				itePixel.remove();
				
				treat = diffusion(treat, dist, waits, p, dd);
			}
		}
		
		return treat;
	}
	
	private int diffusion(int treat, double[] dist, Map<Double, Set<Pixel>> waits, Pixel p, double dd) {

		// System.out.println("traitement de pixel "+p);

		if (threshold > dd) { 
			double vd = mat[p.x()+p.y()*width];  // valeur au point de diffusion
			if (vd != Raster.getNoDataValue()) {
				double fd = friction(vd, p.x(), p.y()); // friction au point de diffusion
				if(fd != Raster.getNoDataValue()){
					Pixel np;
					double v, d;
					Iterator<Pixel> ite = p.getCardinalMargins(); // pour chaque pixel cardinal (4)
					while (ite.hasNext()) {
						np = ite.next();
						
						if(np.x() >= 0 && np.x() < width && np.y() >= 0 && np.y() < height){
							v = mat[np.x()+np.y()*width]; // valeur au point cardinal
							if (v != Raster.getNoDataValue()) {
								double fc = friction(v, np.x(), np.y());
								if(fc != Raster.getNoDataValue()){
									d = dd + (matrix().cellsize() / 2) * fd + (matrix().cellsize() / 2) * fc; // distance au point cardinal
									double vdn = dist[np.x()+np.y()*width];
									if (d < vdn) { // MAJ ?
										dist[np.x()+np.y()*width] = d;
										if(vdn != threshold){
											waits.get(vdn).remove(np);
										}
										setPixelAndValue(waits, np, d);
									}
								}
							}
						}
					}
					ite = p.getDiagonalMargins(); // pour chaque pixel diagonal (4)
					while (ite.hasNext()) {
						np = ite.next();
						
						if(np.x() >= 0 && np.x() < width && np.y() >= 0 && np.y() < height){
							v = mat[np.x()+np.y()*width]; // valeur au point diagonal
							if (v != Raster.getNoDataValue()) {
								double fc = friction(v, np.x(), np.y());
								if(fc != Raster.getNoDataValue()){
									d = dd + (matrix().cellsize() * Math.sqrt(2) / 2) * fd + (matrix().cellsize() * Math.sqrt(2) / 2) * fc; // distance au point diagonal
									double vdn = dist[np.x()+np.y()*width];
									if (d < vdn) { // MAJ ?
										dist[np.x()+np.y()*width] = d;
										if(vdn != threshold){
											waits.get(vdn).remove(np);
										}
										setPixelAndValue(waits, np, d);
									}
								}
							}
						}
					}
				}
			}
		}

		updateProgression(total);
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
		return frictionMat[x+y*width];
	}

	@Override
	public void doClose() {
		// sources = null;
		// System.out.println("temps "+count);
	}

}
