package fr.inra.sad_paysage.apiland.analysis.matrix.output;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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

public class MapCsvOutput extends AbstractMetricOutput implements MetricOutput {
	
	private CsvWriter out;
	
	private Map<String, Double> values;
	
	public MapCsvOutput(String metric, CsvWriter cw){
		super();
		this.out = cw;
		values = new TreeMap<String, Double>();
		values.put(metric, -1.0);
	}
	
	public MapCsvOutput(Set<String> metrics, CsvWriter cw){
		super();
		this.out = cw;
		values = new TreeMap<String, Double>();
		for(String me : metrics){
			values.put(me, -1.0);
		}
	}
	
	@Override
	public String toString(){
		return "csv";
	}
	
	private void flush() {
		values.clear();
	}
	
	@Override
	public void notifyFromAnalysis(Analysis ma, AnalysisState state) {
		switch(state){
		case INIT : notifyAnalysisInit((WindowMatrixAnalysis) ma);break;
		}
	}
	
	private void notifyAnalysisInit(WindowMatrixAnalysis ma) {
		try {
			String f = new File(ma.matrix().getFile()).getName();
			out.write(f);
		} catch (FinalizedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void notifyFromProcess(Process wp, ProcessState state) {
		switch(state){
		case DONE : notifyProcessDone((WindowMatrixProcess) wp);break;
		}
	}
	
	private void notifyProcessDone(WindowMatrixProcess wp) {
		try {
			for(String v : values.keySet()){
				//System.out.println(format(values.get(v)));
				out.write(format(values.get(v)));
			}
			out.endRecord();
		} catch (FinalizedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		flush();
	}

	@Override
	public boolean acceptMetric(String metric) {
		return values.containsKey(metric);
	}

	@Override
	public void updateProgression(int total) {
		// do nothing	
	}

	@Override
	public void notifyFromMetric(Metric m, String metric, double v, Process wp) {
		if(acceptMetric(metric)){
			values.put(metric, v);
		}else{
			m.removeObserver(this);
		}
	}

}
