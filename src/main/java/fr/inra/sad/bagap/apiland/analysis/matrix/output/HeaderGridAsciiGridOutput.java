package fr.inra.sad.bagap.apiland.analysis.matrix.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.process.metric.AbstractMetricOutput;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class HeaderGridAsciiGridOutput extends AbstractMetricOutput {
	
	private int size;
	
	private String ascii;
	
	private int delta;
	
	private BufferedWriter writer;
	
	private String metric;
	
	public HeaderGridAsciiGridOutput(int size, String f){
		super();
		this.size = size;
		this.ascii = f;
		this.delta = size;
	}
	
	@Override
	public String toString(){
		return "ascii_"+metric;
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void notify(Analysis ma, AnalysisState s) {
		switch (s){
		case INIT : notifyAnalysisInit((WindowMatrixAnalysis)ma);break;
		}
	}
	
	private void notifyAnalysisInit(WindowMatrixAnalysis wa) {
		try {
			writer = new BufferedWriter(new FileWriter(ascii));
			int nc = new Double(wa.matrix().width()/delta).intValue();
			if(wa.matrix().width()%delta != 0){
				nc++;
			}
			writer.write("ncols "+nc);
			writer.newLine();
			int nr = new Double(wa.matrix().height()/delta).intValue();
			if(wa.matrix().height()%delta != 0){
				nr++;
			}
			writer.write("nrows "+nr);
			writer.newLine();
			writer.write("xllcorner "+wa.matrix().minX());
			writer.newLine();
			if(wa.matrix().height() % size != 0){
				double decalage = (size - (wa.matrix().height() % size)) * Raster.getCellSize();
				writer.write("yllcorner "+(wa.matrix().minY() - decalage));
			}else{
				writer.write("yllcorner "+(wa.matrix().minY()));
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

}
