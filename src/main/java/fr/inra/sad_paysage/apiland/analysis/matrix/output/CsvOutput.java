package fr.inra.sad_paysage.apiland.analysis.matrix.output;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import fr.inra.sad_paysage.apiland.window.MultipleWindow;
import fr.inra.sad_paysage.apiland.window.SimpleWindow;
import fr.inra.sad_paysage.apiland.window.Window;

import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;
import fr.inra.sad_paysage.apiland.analysis.process.Process;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad_paysage.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.CoordinateManager;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.analysis.metric.AbstractMetricOutput;
import fr.inra.sad_paysage.apiland.analysis.metric.Metric;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessState;

public class CsvOutput extends AbstractMetricOutput {
	
	private Matrix matrix;
	
	private CsvWriter out;
	
	private String file;
	
	private Map<String, Double> values;
	
	private boolean okValues;
	
	public CsvOutput(Matrix m, String f){
		super();
		this.matrix = m;
		this.file = f;
		values = new TreeMap<String, Double>();
		
		flush();
	}
	
	@Override
	public String toString(){
		return "csv";
	}
	
	private void flush() {
		values.clear();
		okValues = false;
	}
	
	@Override
	public void notify(Metric m, String metric, double value, Process process) {
		values.put(metric, value);
		if(value != Raster.getNoDataValue()){
			okValues = true;
		}
	}

	@Override
	public void notify(Analysis ma, AnalysisState s) {
		switch (s){
		case INIT : notifyAnalysisInit((WindowMatrixAnalysis)ma);break;
		case RUNNING : notifyAnalysisRun((WindowMatrixAnalysis)ma);break;
		case FINISH : notifyAnalysisFinish((WindowMatrixAnalysis)ma);break;
		}
	}
	
	private void notifyAnalysisInit(WindowMatrixAnalysis a) {
		out = new CsvWriter(file);
		out.setDelimiter(';');
		if(a.window() instanceof MultipleWindow){
			for(Window w : ((MultipleWindow) a.window()).windows()){
				for(Metric wm : a.metrics()){
					values.put("w"+((SimpleWindow) w).diameter()+"_"+wm.getName(), -9999.0);
				}
			}
		}else{
			for(Metric wm : a.metrics()){
				values.put(wm.getName(), -9999.0);
			}
		}
		
	}
	
	private void notifyAnalysisRun(WindowMatrixAnalysis a) {
		try {
			out.write("X");
			out.write("Y");
			for(String v : values.keySet()){
				out.write(v);
			}
			out.endRecord();
		} catch (FinalizedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void notifyAnalysisFinish(WindowMatrixAnalysis a) {
		out.close();
		out = null;
		values.clear();
		values = null;
		matrix = null;
	}
	
	@Override
	public void notify(Process p, ProcessState s) {
		switch (s){
		case DONE : notifyProcessDone((WindowMatrixProcess) p);break;
		}
	}
	
	private void notifyProcessDone(WindowMatrixProcess wp) {
		//System.out.println(wp);
		try {
			if(okValues){
				out.write(CoordinateManager.getProjectedX(matrix, wp.x())+"");
				out.write(CoordinateManager.getProjectedY(matrix, wp.y())+"");
				//out.write(wp.x()+"");
				//out.write(wp.y()+"");
						
				for(String v : values.keySet()){
					out.write(format(values.get(v)));
				}
				out.endRecord();
			}
		} catch (FinalizedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		flush();
	}

}
