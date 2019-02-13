package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

import fr.inra.sad.bagap.apiland.analysis.Count;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.Pixel2PixelMatrixCalculation;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class GroupOverlay extends Pixel2PixelMatrixCalculation {

	private List<Integer> values;
	
	private List<Double> minimumAreas;
	
	private double minimumTotal;
	
	private String csv;
	
	private Map<Integer, Map<Integer, Count>> patchs;
	
	public GroupOverlay(List<Integer> values, List<Double> minimumAreas, double minimumTotal, String csv, Matrix... matrix){
		super(matrix);
		this.values = values;
		this.minimumAreas = minimumAreas;
		this.minimumTotal = minimumTotal;
		this.csv = csv;
	}
	
	@Override
	protected void doInit() {
		patchs = new TreeMap<Integer, Map<Integer, Count>>();
		for(int vC : matrix(0).values()){
			if(vC != 0 && vC != Raster.getNoDataValue()){
				patchs.put(vC, new TreeMap<Integer, Count>());
				for(int vM : values){
					if(vM != 0 && vM != Raster.getNoDataValue()){
						patchs.get(vC).put(vM, new Count());
					}
				}
			}
		}
		
		for(Pixel p : matrix()){
			int vM = new Double(matrix(1).get(p)).intValue();
			if(vM != Raster.getNoDataValue()){
				int vC = new Double(matrix(0).get(p)).intValue();
				if(vC != 0 && vC != Raster.getNoDataValue()){
					if(values.contains(vM)){
						patchs.get(vC).get(vM).add();
					}
				}
			}
		}
		
		Set<Integer> toDelete = new HashSet<Integer>();
		for(Entry<Integer, Map<Integer, Count>> e : patchs.entrySet()){
			int patchNumber = e.getKey();
			//System.out.println("numéro de patch : "+patchNumber); 
			double totalSurface = 0.0;
			for(Entry<Integer, Count> e2 : patchs.get(e.getKey()).entrySet()){
				int ocsol = e2.getKey();
				double surface = e2.getValue().get() * Math.pow(Raster.getCellSize(), 2);
				totalSurface += surface;
				//System.out.println("occsol : "+ocsol+" surface "+surface);
				int code = 0;
				for(; code<values.size(); code++){
					if(values.get(code) == ocsol){
						break;
					}
				}
				if(surface < minimumAreas.get(code)*10000.0){
					toDelete.add(patchNumber);
				}
			}
			if(totalSurface < minimumTotal*10000.0){
				toDelete.add(patchNumber);
			}
		}
		
		
		for(int pn : toDelete){
			//System.out.println(pn);
			patchs.remove(pn);
			//System.out.println("oui");
		}
		
		
		/*
		Iterator<Entry<Integer, Map<Integer, Count>>> ite = patchs.entrySet().iterator();
		while(ite.hasNext()){
			System.out.println("la");
			Entry<Integer, Map<Integer, Count>> e = ite.next();
		
			int vi = 0;
			boolean ok = true;
			
			for(Entry<Integer, Count> e2 : e.getValue().entrySet()){
				e2.getValue().get();
				Math.pow(Raster.getCellSize(), 2);
				System.out.println(vi);
				//System.out.println(minimumAreas.get(vi));
			
				if(e2.getValue().get() * Math.pow(Raster.getCellSize(), 2) < minimumAreas.get(vi)){
					ok = false;
				}
		
				vi++;
				
			}
		
			if(!ok){
				//ite.remove();
			}
			
		}
		*/
		
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		int vM = new Double(matrix(1).get(p)).intValue();
		if(vM != Raster.getNoDataValue()){
			int vC = new Double(matrix(0).get(p)).intValue();
			if(vC != 0 && vC != Raster.getNoDataValue() && patchs.containsKey(vC)){
				if(values.contains(vM)){
					//System.out.println(vC);
					return vC;
				}
			}
			return 0;
		}else{
			return Raster.getNoDataValue();
		}
	}
	
	@Override
	protected void doClose() {
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
