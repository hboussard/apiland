package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;
import fr.inra.sad.bagap.apiland.analysis.Count;
import fr.inra.sad.bagap.apiland.analysis.matrix.MatrixAnalysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class ClusteringOutput extends MatrixAnalysis {

	private List<Integer> values;
	
	private List<Double> minimumAreas;
	
	private double minimumTotal;
	
	private String csv;
	
	private Map<Integer, Map<Integer, Count>> patchs;
	
	private double totalSurface = 0;
	
	private int nbPatch = 0;
	
	public ClusteringOutput(List<Integer> values, List<Double> minimumAreas, double minimumTotal, String csv, Matrix... matrix){
		super(matrix);
		this.values = values;
		this.minimumAreas = minimumAreas;
		this.minimumTotal = minimumTotal;
		this.csv = csv;
	}
	
	public double getTotalSurface(){
		return totalSurface;
	}
	
	public int getNbPatch(){
		return nbPatch;
	}
	
	@Override
	protected void doInit() {
		//System.out.println("ici init");
		patchs = new TreeMap<Integer, Map<Integer, Count>>();
		for(int vC : matrix(0).values()){
			if(vC != 0 && vC != Raster.getNoDataValue()){
				patchs.put(vC, new TreeMap<Integer, Count>());
				for(int vM : values){
					patchs.get(vC).put(vM, new Count());
				}
			}
		}
	}
	
	@Override
	protected void doRun() {
		//System.out.println("ici run");
		int total = matrix().width() * matrix().height() * 3;
		
		for(int j=0; j<matrix().height(); j++){
			for(int i=0; i<matrix().width(); i++){
				double vC = matrix(0).get(i, j);
				//System.out.println(vC);
				if(vC > 0){
					//System.out.println(matrix(1).get(i, j));
					patchs.get((int) vC).get((int) matrix(1).get(i, j)).add();
				}
				
				updateProgression(total);
			}
		}
		//System.out.println("ici1");
		Set<Integer> toDelete = new HashSet<Integer>();
		for(Entry<Integer, Map<Integer, Count>> e : patchs.entrySet()){
			int patchNumber = e.getKey();
			double totalSurface = 0.0;
			for(Entry<Integer, Count> e2 : patchs.get(e.getKey()).entrySet()){
				double surface = e2.getValue().get() * Math.pow(Raster.getCellSize(), 2);
				totalSurface += surface;
			}
			if(totalSurface ==0 || totalSurface < minimumTotal*10000.0){
				toDelete.add(patchNumber);
			}
		}
		//
		
		if(toDelete.size() > 0){
			//System.out.println("ici : "+toDelete);
			for(int pn : toDelete){
				patchs.remove(pn);
			}
			for(int j=0; j<matrix().height(); j++){
				for(int i=0; i<matrix().width(); i++){
					double vC = matrix(0).get(i, j);
					if(toDelete.contains((int) vC)){
						//System.out.println("ici : "+vC);
						matrix(0).put(i, j, 0);
					}
				}
			}
		}
		
		setResult(matrix(0));
		//System.out.println("ici3");
	}
	
	@Override
	protected void doClose() {
		//System.out.println("ici close");
		try {
			CsvWriter cw = new CsvWriter(csv);
			cw.setDelimiter(';');
			
			cw.write("id");
			cw.write("nb_pixels_patch");
			cw.write("surface_patch");
			
			for(int vM : values){
				if(vM != 0 && vM != Raster.getNoDataValue()){
					cw.write("nb_pixels_patch_"+vM);
					cw.write("surface_patch_"+vM);
					cw.write("rate_patch_"+vM);
				}
			}
			
			cw.endRecord();
			
			for(int vC : patchs.keySet()){
				if(vC != 0 && vC != Raster.getNoDataValue()){
					cw.write(vC+"");
					
					int nbpp = 0;
					for(int vM : values){
						if(vM != 0 && vM != Raster.getNoDataValue()){
							nbpp += patchs.get(vC).get(vM).get();
						}
					}
					cw.write(nbpp+"");
					cw.write((nbpp*Math.pow(Raster.getCellSize(), 2))/10000.0+"");
					
					nbPatch++;
					totalSurface += (nbpp*Math.pow(Raster.getCellSize(), 2))/10000.0;
					
					int nbpsp = 0;		
					
					for(int vM : values){
						if(vM != 0 && vM != Raster.getNoDataValue()){
							nbpsp = patchs.get(vC).get(vM).get();
							cw.write(nbpsp+"");
							cw.write((nbpsp*Math.pow(Raster.getCellSize(), 2))/10000.0+"");
							cw.write((nbpsp*100.0/nbpp)+"");
						}
					}
					
					cw.endRecord();
				}
			}
			
			cw.close();
		} catch (FinalizedException | IOException e) {
			e.printStackTrace();
		}
	}

}
