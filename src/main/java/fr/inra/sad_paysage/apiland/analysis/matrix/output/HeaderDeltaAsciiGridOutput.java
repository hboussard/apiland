package fr.inra.sad_paysage.apiland.analysis.matrix.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;
import fr.inra.sad_paysage.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad_paysage.apiland.analysis.metric.AbstractMetricOutput;
import fr.inra.sad_paysage.apiland.analysis.metric.Metric;
import fr.inra.sad_paysage.apiland.analysis.process.Process;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;

public class HeaderDeltaAsciiGridOutput extends AbstractMetricOutput {
	
	private String ascii;
	
	private int delta;
	
	public HeaderDeltaAsciiGridOutput(String f, int d){
		super();
		this.ascii = f;
		this.delta = d;
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
			int nc = new Double(wa.matrix().width()/delta).intValue();
			if(wa.matrix().width()%delta != 0){
				nc++;
			}
			int nr = new Double(wa.matrix().height()/delta).intValue();
			if(wa.matrix().height()%delta != 0){
				nr++;
			}
			
			double decalage = ((delta-1)*Raster.getCellSize())/2;
			double mod = (wa.matrix().height() * Raster.getCellSize() + decalage) % (delta * Raster.getCellSize());
			
			writer.write("ncols "+nc);
			writer.newLine();
			
			writer.write("nrows "+nr);
			writer.newLine();
			
			writer.write("xllcorner "+(wa.matrix().minX() - decalage));
			writer.newLine();
			/*
			System.out.println("nb colonnes : "+nc);
			System.out.println("nb lignes : "+nr);
			System.out.println("height : "+wa.matrix().height());
			System.out.println("delta  : "+delta);
			System.out.println("modulo : "+mod);
			System.out.println("decalage : "+decalage);
			*/
			if(mod == 0){
				//System.out.println("rien");
				writer.write("yllcorner "+wa.matrix().minY());
			}else if(mod >= (delta*Raster.getCellSize())/2.0){
				//System.out.println("moins "+(delta*Raster.getCellSize()-mod));
				writer.write("yllcorner "+(wa.matrix().minY() - (delta*Raster.getCellSize()-mod)));
			}else{
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
