package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.Pixel2PixelMatrixCalculation;
import fr.inra.sad.bagap.apiland.analysis.Count;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class ClusterOverlay extends Pixel2PixelMatrixCalculation {

	private Set<Integer> values;
	
	private String csv;
	
	//private Map<Integer, Count> etendues;
	
	private Map<Integer, Map<Integer, Count>> patchs;
	
	public ClusterOverlay(Set<Integer> values, String csv, Matrix... matrix){
		super(matrix);
		this.values = values;
		this.csv = csv;
	}
	
	@Override
	protected void doInit() {
		//etendues = new TreeMap<Integer, Count>();
		patchs = new TreeMap<Integer, Map<Integer, Count>>();
		for(int vC : matrix(0).values()){
			if(vC != 0 && vC != Raster.getNoDataValue()){
				//etendues.put(vC, new Count());
				patchs.put(vC, new TreeMap<Integer, Count>());
				for(int vM : matrix(1).values()){
					if(vM != 0 && vM != Raster.getNoDataValue()){
						patchs.get(vC).put(vM, new Count());
					}
				}
			}
		}
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		int vM = new Double(matrix(1).get(p)).intValue();
		if(vM != Raster.getNoDataValue()){
			int vC = new Double(matrix(0).get(p)).intValue();
			if(vC != 0 && vC != Raster.getNoDataValue()){
				//etendues.get(vC).add();
				if(values.contains(vM)){
					patchs.get(vC).get(vM).add();
					return matrix(0).get(p);
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
			//cw.write("nb_pixels_extension");
			//cw.write("surface_extension");
			cw.write("nb_pixels_patch");
			cw.write("surface_patch");
			//cw.write("rate_patch_in_extension");
			
			//for(int vM : matrix(1).values()){
			for(int vM : values){
				if(vM != 0 && vM != Raster.getNoDataValue()){
					cw.write("nb_pixels_patch_"+vM);
					cw.write("surface_patch_"+vM);
					cw.write("rate_patch_"+vM);
				}
			}
			
			cw.endRecord();
			
			for(int vC : matrix(0).values()){
				if(vC != 0 && vC != Raster.getNoDataValue()){
					cw.write(vC+"");
					//int nbpe = etendues.get(vC).get();
					//cw.write(nbpe+"");
					//cw.write((nbpe*Math.pow(Raster.getCellSize(), 2))+"");
					int nbpp = 0;
					//for(int vM : matrix(1).values()){
					for(int vM : values){
						if(vM != 0 && vM != Raster.getNoDataValue()){
							nbpp += patchs.get(vC).get(vM).get();
						}
					}
					cw.write(nbpp+"");
					cw.write((nbpp*Math.pow(Raster.getCellSize(), 2))+"");
					
					//cw.write((nbpp * 100.0 / nbpe)+"");
					
					int nbpsp = 0;					
					//for(int vM : matrix(1).values()){
					for(int vM : values){
						if(vM != 0 && vM != Raster.getNoDataValue()){
							nbpsp = patchs.get(vC).get(vM).get();
							cw.write(nbpsp+"");
							cw.write((nbpsp*Math.pow(Raster.getCellSize(), 2))+"");
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
