package fr.inra.sad_paysage.apiland.analysis.matrix.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;
import fr.inra.sad_paysage.apiland.analysis.process.Process;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad_paysage.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.analysis.metric.AbstractMetricOutput;
import fr.inra.sad_paysage.apiland.analysis.metric.Metric;

public class AsciiGridOutput extends AbstractMetricOutput {
	
	private String ascii;
	
	private int delta;
	
	private BufferedWriter writer;
	
	private String metric;
	
	private int yGlobal;
	
	public AsciiGridOutput(String metric, String f, int d){
		super();
		this.metric = metric;
		this.ascii = f;
		this.delta = d;
		this.yGlobal = -1;
	}
	
	@Override
	public String toString(){
		return "ascii_"+metric;
	}
	
	@Override
	public void notifyFromAnalysis(Analysis ma, AnalysisState s) {
		switch (s){
		case INIT : notifyAnalysisInit((WindowMatrixAnalysis)ma);break;
		case FINISH : notifyAnalysisFinish((WindowMatrixAnalysis)ma);break;
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
			writer.write("yllcorner "+wa.matrix().minY());
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
		}
	}

	@Override
	public boolean acceptMetric(String metric) {
		return this.metric.equalsIgnoreCase(metric);
	}
	
	@Override
	public void notifyFromMetric(Metric m, String n, double v, Process p) {
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
