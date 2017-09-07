package fr.inra.sad.bagap.apiland.analysis.vector.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.metric.AbstractMetricOutput;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.analysis.vector.process.VectorProcess;
import fr.inra.sad.bagap.apiland.analysis.vector.window.SlidingWindowVectorAnalysis;
import fr.inra.sad.bagap.apiland.analysis.vector.window.WindowVectorAnalysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class AsciiGridOutputV extends AbstractMetricOutput {

	private String ascii;
	
	private BufferedWriter out;
	
	private String metric;
	
	private double yGlobal;
	
	public AsciiGridOutputV(String metric, double buffer, String file){
		super();
		this.metric = buffer+"_"+metric;
		this.ascii = file;
		this.yGlobal = -1.0;
	}
	
	@Override
	public String toString(){
		return "ascii_"+metric;
	}
	
	@Override
	public void notify(Analysis ma, AnalysisState s) {
		switch (s){
		case INIT : notifyAnalysisInit((WindowVectorAnalysis)ma);break;
		case FINISH : notifyAnalysisFinish((WindowVectorAnalysis)ma);break;
		}
	}
	
	private void notifyAnalysisInit(WindowVectorAnalysis wa) {
		try {
			out = new BufferedWriter(new FileWriter(ascii));
			
			int ncols = new Double((Math.floor((wa.layer().maxX() - wa.layer().minX()) / ((SlidingWindowVectorAnalysis) wa).displacement())) + 1).intValue();
			out.write("ncols "+ncols);
			out.newLine();
			
			int nrows = new Double((Math.floor((wa.layer().maxY() - wa.layer().minY()) / ((SlidingWindowVectorAnalysis) wa).displacement())) + 1).intValue();
			out.write("nrows "+nrows);
			out.newLine();
			
			out.write("xllcorner "+wa.layer().minX());
			out.newLine();
			out.write("yllcorner "+wa.layer().minY());
			out.newLine();
			out.write("cellsize "+((SlidingWindowVectorAnalysis) wa).displacement());
			out.newLine();
			out.write("NODATA_value "+Raster.getNoDataValue());
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void notifyAnalysisFinish(WindowVectorAnalysis wa) {
		try {
			out.newLine();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
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
					out.write(format(v)+"");
					yGlobal = ((VectorProcess) p).y();
				}else if(((VectorProcess) p).y() != yGlobal){
					out.newLine();
					out.write(format(v));
					yGlobal = ((VectorProcess) p).y();
				}else{
					out.write(" "+format(v));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			//m.removeObserver(this);
		}
	}
	
}
