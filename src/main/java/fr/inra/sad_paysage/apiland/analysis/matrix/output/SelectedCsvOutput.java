package fr.inra.sad_paysage.apiland.analysis.matrix.output;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;
import fr.inra.sad_paysage.apiland.analysis.process.Process;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad_paysage.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad_paysage.apiland.analysis.metric.AbstractMetricOutput;
import fr.inra.sad_paysage.apiland.analysis.metric.Metric;
import fr.inra.sad_paysage.apiland.analysis.metric.MetricOutput;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessState;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.PixelWithID;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.CoordinateManager;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.window.SimpleWindow;
import fr.inra.sad_paysage.apiland.window.MultipleWindow;
import fr.inra.sad_paysage.apiland.window.Window;

public class SelectedCsvOutput extends AbstractMetricOutput implements MetricOutput {
	
	private CsvWriter out;
	
	private Matrix matrix;
	
	private String file;
	
	private Set<Pixel> pixels;
	
	private Map<Pixel, Map<String, Double>> values;
	
	private int metricSize;
	
	public SelectedCsvOutput(Matrix m, String f, Set<Pixel> p){
		this.matrix = m;
		this.file = f;
		this.pixels = p;
		values = new TreeMap<Pixel, Map<String, Double>>();
	}
	
	@Override
	public String toString(){
		return "csv";
	}

	@Override
	public void notifyFromAnalysis(Analysis ma, AnalysisState s) {
		switch (s){
		case INIT : notifyAnalysisInit((WindowMatrixAnalysis)ma);break;
		case RUNNING : notifyAnalysisRun((WindowMatrixAnalysis)ma);break;
		case FINISH : notifyAnalysisFinish((WindowMatrixAnalysis)ma);break;
		}
	}
	
	private void notifyAnalysisInit(WindowMatrixAnalysis a) {
		out = new CsvWriter(file);
		out.setDelimiter(';');
	}
	
	private void notifyAnalysisRun(WindowMatrixAnalysis a) {
		Set<String> columns = new TreeSet<String>();
		if(a.window() instanceof MultipleWindow){
			for(Window w : ((MultipleWindow) a.window()).windows()){
				for(Metric wm : a.metrics()){
					columns.add("w"+((SimpleWindow) w).diameter()+"_"+wm.getName());
				}
			}
		}else{
			for(Metric wm : a.metrics()){
				columns.add(wm.getName());
			}
		}
		try {
			
			if(pixels.iterator().next() instanceof PixelWithID){
				out.write("id");
			}
			out.write("X");
			out.write("Y");
			for(String column : columns){
				out.write(column);
			}
			out.endRecord();
		} catch (FinalizedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void notifyAnalysisFinish(WindowMatrixAnalysis a) {
		metricSize = values.values().iterator().next().keySet().size();
		for(Pixel p : pixels){
			write(p);
		}
		
		out.close();
	}
	
	private void write(Pixel p){
		try {
			if(p instanceof PixelWithID){
				out.write(((PixelWithID) p).getId());
				out.write(((PixelWithID) p).getX()+"");
				out.write(((PixelWithID) p).getY()+"");
			}else{
				out.write(CoordinateManager.getProjectedX(matrix, p.x())+"");
				out.write(CoordinateManager.getProjectedY(matrix, p.y())+"");
			}
			
			if(values.containsKey(p)){
				Map<String, Double> val = values.get(p);
				for(String v : val.keySet()){
					out.write(format(val.get(v)));
					//System.out.println(p+" "+v+" "+val.get(v));
				}
			}else{
				//System.out.println("pas initialisee pour "+p);
				for(int i=0; i<metricSize; i++){
					out.write(Raster.getNoDataValue()+"");
				}
			}
			
			out.endRecord();
		} catch (FinalizedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void notifyFromMetric(Metric m, String metric, double v, Process wp) {
		if(pixels.contains(((WindowMatrixProcess) wp).pixel())){
			values.get(((WindowMatrixProcess) wp).pixel()).put(metric, v);
		}else{
			m.removeObserver(this);
		}
	}
	
	@Override
	public void notifyFromProcess(Process p, ProcessState s) {
		if(p instanceof WindowMatrixProcess){
			switch(s){
			case INIT : notifyProcessInit((WindowMatrixProcess) p); break;
			}
		}
	}
	
	private void notifyProcessInit(WindowMatrixProcess wp) {
		if(pixels.contains(((WindowMatrixProcess) wp).pixel())){
			values.put(((WindowMatrixProcess) wp).pixel(), new TreeMap<String, Double>());
		}
	}

}
