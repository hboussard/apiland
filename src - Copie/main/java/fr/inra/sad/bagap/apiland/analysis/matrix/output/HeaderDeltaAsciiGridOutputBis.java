package fr.inra.sad.bagap.apiland.analysis.matrix.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.metric.AbstractMetricOutput;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class HeaderDeltaAsciiGridOutputBis extends AbstractMetricOutput {
	
	private String ascii;
	
	private int delta;
	
	private int xOrigin;
	
	private int yOrigin;
	
	public HeaderDeltaAsciiGridOutputBis(String output, int delta, int xOrigin, int yOrigin){
		super();
		this.ascii = output;
		this.delta = delta;
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
	}
	
	@Override
	public String toString(){
		return "ascii_header";
	}
	
	@Override
	public void notify(Analysis a, AnalysisState s) {
		switch (s){
		case INIT : notifyAnalysisInit((WindowMatrixAnalysis) a); break;
		}
	}
	
	private void notifyAnalysisInit(WindowMatrixAnalysis wa) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(ascii));
			int width = wa.matrix().width() - xOrigin;
			int height = wa.matrix().height() - yOrigin;
			int nc = width / delta;
			
			if(width%delta != 0){
				nc++;
			}
			int nr = new Double(height/delta).intValue();
		
			if(height%delta != 0){
				nr++;
			}
			
			double xdecalage = ((delta-1)*Raster.getCellSize())/2 - xOrigin*Raster.getCellSize();
			double ydecalage = ((delta-1)*Raster.getCellSize())/2 - yOrigin*Raster.getCellSize();
			double mod = (wa.matrix().height() * Raster.getCellSize() + ydecalage) % ((delta) * Raster.getCellSize());
			
			writer.write("ncols "+nc);
			writer.newLine();
			
			writer.write("nrows "+nr);
			writer.newLine();
			
			writer.write("xllcorner "+(wa.matrix().minX() - xdecalage));
			writer.newLine();
			
			if(mod == 0){
				//System.out.println("rien");
				writer.write("yllcorner "+wa.matrix().minY());
			}else if(mod >= (delta*Raster.getCellSize())/2.0){
				//System.out.println("moins ");
				//System.out.println("moins "+(delta*Raster.getCellSize()-mod));
				writer.write("yllcorner "+(wa.matrix().minY() - (delta*Raster.getCellSize()-mod)));
			}else{
				//System.out.println("plus ");
				//System.out.println("plus "+(mod /** Raster.getCellSize()*/));
				writer.write("yllcorner "+(wa.matrix().minY() + (mod)));
			}
			
			writer.newLine();
			writer.write("cellsize "+delta*wa.matrix().cellsize());
			writer.newLine();
			writer.write("NODATA_value "+Raster.getNoDataValue());
			writer.newLine();
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void notify(Metric m, String metric, double v, Process wp) {
		m.removeObserver(this);
	}
	
}
