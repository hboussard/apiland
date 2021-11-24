package fr.inra.sad.bagap.apiland.analysis.matrix;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ArrayRCMDistanceAnalysis extends Analysis {

	private float[] outDatas, frictionDatas;

	private double threshold;
	
	private int width, height;
	
	private float cellSize;
	
	private Map<Float, Set<Pixel>> waits;

	public ArrayRCMDistanceAnalysis(float[] outDatas, float[] frictionDatas, int width, int height, float cellSize, Map<Float, Set<Pixel>> waits) {
		this(outDatas, frictionDatas, width, height, cellSize, waits, Raster.getNoDataValue());
	}

	public ArrayRCMDistanceAnalysis(float[] outDatas, float[] frictionDatas, int width, int height, float cellSize, Map<Float, Set<Pixel>> waits, double threshold) {
		this.outDatas = outDatas;
		this.frictionDatas = frictionDatas;
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.waits = waits;
		if(threshold == Raster.getNoDataValue()){
			this.threshold = Integer.MAX_VALUE;
		}else{
			this.threshold = threshold;
		}
	}
	
	@Override
	protected void doInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doRun() {

		// diffusion
		
		while (waits.size() > 0) {
			diffusionPaquet();
			
		}
		
		setResult(outDatas);
	}

	private void setPixelAndValue(Map<Float, Set<Pixel>> waits, Pixel pixel, float value) {
		if (!waits.containsKey(value)) {
			waits.put(value, new HashSet<Pixel>());
		}
		waits.get(value).add(pixel);
	}

	private void diffusionPaquet(){
		
		Iterator<Entry<Float, Set<Pixel>>> iteEntry = waits.entrySet().iterator();
		Entry<Float, Set<Pixel>> entry = iteEntry.next(); // récupération des pixels à diffuser
		iteEntry.remove();
		
		if(entry.getValue().size() != 0){
			double dd = entry.getKey(); // récupération de la valeur de diffusion 
			
			Iterator<Pixel> itePixel = entry.getValue().iterator();
			Pixel p;
			while(itePixel.hasNext()){
				p = itePixel.next();
				itePixel.remove();
				
				diffusion(p, dd);
			}
		}
	}
	
	private void diffusion(Pixel p, double dd) {
		if (threshold > dd) { 
			double vd = outDatas[p.x()+p.y()*width];  // valeur au point de diffusion
			if (vd != Raster.getNoDataValue()) {
				double fd = frictionDatas[p.x()+p.y()*width]; // friction au point de diffusion
				
				Pixel np;
				float v, d;
				Iterator<Pixel> ite = p.getCardinalMargins(); // pour chaque pixel cardinal (4)
				while (ite.hasNext()) {
					np = ite.next();
						
					if(np.x() >= 0 && np.x() < width && np.y() >= 0 && np.y() < height){
						v = outDatas[np.x()+np.y()*width]; // valeur au point cardinal
						if (v != Raster.getNoDataValue()) {
							float fc = frictionDatas[np.x()+np.y()*width];
							d = (float) (dd + (cellSize / 2) * fd + (cellSize / 2) * fc); // distance au point cardinal
							if (v == -2 || d < v) { // MAJ ?
								outDatas[np.x()+np.y()*width] = (float) d;
								
								/*if(v != -2){
									waits.get(v).remove(np);
								}*/
								setPixelAndValue(waits, np, (float) d);
							}
						}
					}
				}
				ite = p.getDiagonalMargins(); // pour chaque pixel diagonal (4)
				while (ite.hasNext()) {
					np = ite.next();
						
					if(np.x() >= 0 && np.x() < width && np.y() >= 0 && np.y() < height){
						v = outDatas[np.x()+np.y()*width]; // valeur au point cardinal
						if (v != Raster.getNoDataValue()) {
							float fc = frictionDatas[np.x()+np.y()*width];
							d = (float) (dd + (cellSize * Math.sqrt(2) / 2) * fd + (cellSize * Math.sqrt(2) / 2) * fc); // distance au point diagonal
							if (v == -2 || d < v) { // MAJ ?
								outDatas[np.x()+np.y()*width] = (float) d;
								
								/*if(v != -2){
									waits.get(v).remove(np);
								}*/
								setPixelAndValue(waits, np, (float) d);
							}
						}
					}
				}	
			}
		}
	}


	@Override
	protected void doClose() {
		// TODO Auto-generated method stub
		
	}

}
