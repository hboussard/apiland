package fr.inra.sad.bagap.apiland.analysis.matrix.output;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.metric.AbstractMetricOutput;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;

public class DeltaAsciiGridOutput extends AbstractMetricOutput {
	
	private String ascii;
	
	private int delta;
	
	private int xOrigin;
	
	private int yOrigin;
	
	private BufferedWriter writer;
	
	private String metric;
	
	private int yGlobal;
	
	public DeltaAsciiGridOutput(String metric, String output, int delta, int xOrigin, int yOrigin){
		super();
		this.metric = metric;
		this.ascii = output;
		this.delta = delta;
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
		this.yGlobal = yOrigin - 1;
	}
	
	@Override
	public String toString(){
		return "ascii_"+metric;
	}
	
	@Override
	public void notify(Analysis ma, AnalysisState s) {
		switch (s){
		case INIT : notifyAnalysisInit((WindowMatrixAnalysis)ma);break;
		case FINISH : notifyAnalysisFinish((WindowMatrixAnalysis)ma);break;
		}
	}
	
	private void notifyAnalysisInit(WindowMatrixAnalysis wa) {
		try {
			
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
			double mod = (wa.matrix().height() * Raster.getCellSize() + ydecalage) % (delta * Raster.getCellSize());
			
			writer = new BufferedWriter(new FileWriter(ascii));
			
			writer.write("ncols "+nc);
			writer.newLine();
			
			writer.write("nrows "+nr);
			writer.newLine();
			
			writer.write("xllcorner "+(wa.matrix().minX() - xdecalage));
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void notifyAnalysisFinish(WindowMatrixAnalysis wa) {
			try {
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				//String prj_input = DynamicLayerFactory.class.getResource("lambert93.prj").toString().replace("file:/", "");
				//Tool.copy(prj_input, ascii.replace(".asc", "")+".prj");
				Tool.copy(DynamicLayerFactory.class.getResourceAsStream(MatrixManager.epsg()), ascii.replace(".asc", "")+".prj");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean acceptMetric(String metric) {
		return this.metric.equalsIgnoreCase(metric);
	}
	
	@Override
	public void notify(Metric m, String n, double v, Process p) {
		if(acceptMetric(n)){
			try {
				if(yGlobal == -1){
					writer.write(format(v)+"");
					yGlobal = 0;
				}else if(((WindowMatrixProcess) p).y() != yGlobal){
					writer.newLine();
					writer.write(format(v)+"");
					yGlobal = ((WindowMatrixProcess) p).y();
				}else{
					writer.write(" "+format(v));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			//m.removeObserver(this);
		}
	}
	
}
