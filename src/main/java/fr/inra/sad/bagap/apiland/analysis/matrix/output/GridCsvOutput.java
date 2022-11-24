package fr.inra.sad.bagap.apiland.analysis.matrix.output;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.MultipleWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.SimpleWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessState;
import fr.inra.sad.bagap.apiland.analysis.process.metric.AbstractMetricOutput;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class GridCsvOutput extends AbstractMetricOutput {
	
	private int size;
	
	private double decalage;
	
	private Matrix matrix;
	
	private CsvWriter out;
	
	private String file;
	
	private Map<String, Double> values;
	
	private boolean okValues;
	
	public GridCsvOutput(int size, Matrix m, String f){
		super();
		this.size = size;
		this.decalage = (m.height() % size) * Raster.getCellSize();
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
				out.write((CoordinateManager.getProjectedX(matrix, wp.x()) - Raster.getCellSize()/2 + (((double) size)/2)*Raster.getCellSize())+"");
				out.write((CoordinateManager.getProjectedY(matrix, wp.y()) + Raster.getCellSize()/2 - (((double) size)/2)*Raster.getCellSize())+"");
				//out.write(wp.x()+"");
				//out.write(wp.y()+"");
						
				for(String v : values.keySet()){
					out.write(format(values.get(v)));
				}
				out.endRecord();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		flush();
	}

}
