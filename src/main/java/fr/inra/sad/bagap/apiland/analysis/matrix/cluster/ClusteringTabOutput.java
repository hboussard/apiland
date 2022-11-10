package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ClusteringTabOutput extends Analysis {
	
	private double totalSurface;
	
	private int[] values;
	
	private int nbPatch;
	
	private int[] tabCluster;
	
	private double cellSize;
	
	public ClusteringTabOutput(int[] tabCluster, double cellSize){
		this.tabCluster = tabCluster;
		this.cellSize = cellSize;
	}
	
	public double getTotalSurface(){
		return totalSurface;
	}
	
	public int getNbPatch(){
		return nbPatch;
	}
	
	@Override
	protected void doInit() {
		nbPatch = 0;
		totalSurface = 0;
		int vMax = 0;
		for(int vC : tabCluster){
			if(vC != 0){
				vMax = Math.max(vMax, vC);
			}
		}
		
		values = new int[vMax+1];
	}
	
	@Override
	protected void doRun() {
		
		int nbPixel = 0;
		for(int vC : tabCluster){
			if(vC != 0){
				values[vC] = 1;
				nbPixel++;
			}
		}
		for(int v : values){
			if(v == 1){
				nbPatch++;
			}
		}
		totalSurface = (nbPixel*Math.pow(cellSize, 2))/10000.0;
		
		setResult(true);
	}
	
	@Override
	protected void doClose() {
		// do nothing
	}

}
