package fr.inra.sad.bagap.apiland.analysis.matrix.output;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.FilteredRectangularWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.SimpleWindow;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessState;
import fr.inra.sad.bagap.apiland.analysis.process.metric.AbstractMetricOutput;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.MetricOutput;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public class ClusteredCsvOutput extends AbstractMetricOutput implements MetricOutput {
	
	private CsvWriter out;
	
	private Set<String> metrics;
	
	private String file;
	
	private Map<Pixel, Map<String, Double>> values;
	
	private Map<Pixel, String> names;
	
	public ClusteredCsvOutput(Set<String> metrics, String f){
		super();
		this.metrics = metrics;
		this.file = f;
		names = new TreeMap<Pixel, String>();
		values = new TreeMap<Pixel, Map<String, Double>>();
	}
	
	@Override
	public String toString(){
		return "csv";
	}
	
	@Override
	public void notify(Analysis ma, AnalysisState state) {
		switch(state){
		case INIT : notifyAnalysisInit((WindowMatrixAnalysis) ma);break;
		case FINISH : notifyAnalysisFinish((WindowMatrixAnalysis)ma);break;
		}
	}
	
	private void notifyAnalysisInit(WindowMatrixAnalysis ma) {
		try {
			out = new CsvWriter(file);
			out.setDelimiter(';');
			out.write("cluster");
			for(String m : metrics){
				out.write(m);
			}
			out.endRecord();
		} catch (FinalizedException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void notifyAnalysisFinish(WindowMatrixAnalysis a) {
		
		out.close();
	}

	@Override
	public void notify(Process wp, ProcessState state) {
		switch(state){
		case READY: notifyProcessReady((WindowMatrixProcess) wp);break; 
		case DONE : notifyProcessDone((WindowMatrixProcess) wp);break;
		}
	}
	
	private void notifyProcessReady(WindowMatrixProcess wp) {
		names.put(wp.pixel(), ((FilteredRectangularWindow) ((SimpleWindow) wp.window()).shape()).getValue()+"");
		values.put(wp.pixel(), new TreeMap<String, Double>());
	}

	private void notifyProcessDone(WindowMatrixProcess wp) {
		try {
			out.write(names.get(wp.pixel()));
			for(double v : values.get(wp.pixel()).values()){
				out.write(format(v));
			}
			out.endRecord();
		} catch (FinalizedException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean acceptMetric(String metric) {
		return metrics.contains(metric);
	}

	@Override
	public void notify(Metric m, String metric, double v, Process wp) {
		if(acceptMetric(metric)){
			//System.out.println(m+" "+metric+" "+v);
			values.get(((WindowMatrixProcess) wp).pixel()).put(metric, v);
		}
	}

}
