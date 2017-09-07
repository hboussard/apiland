package fr.inra.sad_paysage.apiland.analysis.vector.output;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;
import fr.inra.sad_paysage.apiland.analysis.metric.AbstractMetricOutput;
import fr.inra.sad_paysage.apiland.analysis.metric.Metric;
import fr.inra.sad_paysage.apiland.analysis.process.Process;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessState;
import fr.inra.sad_paysage.apiland.analysis.vector.process.VectorProcess;

public class SharedCsvOutputV extends AbstractMetricOutput {
	
	private CsvWriter out;
	
	private Map<String, Double> values;
	
	private int nbSimulation;
	
	private int year;
	
	public SharedCsvOutputV(CsvWriter cw, int nbSimulation, int year){
		super();
		this.out = cw;
		this.nbSimulation = nbSimulation;
		this.year = year;
		values = new TreeMap<String, Double>();
		
		flush();
	}
	
	@Override
	public String toString(){
		return "csv";
	}
	
	private void flush() {
		values.clear();
	}
	
	@Override
	public void notify(Analysis a, AnalysisState state) {
		switch (state){ 
			// do nothing
		}
	}
	
	@Override
	public void notify(Metric m, String metric, double value, Process process){
		values.put(metric, value);
	}
	
	@Override
	public void notify(Process p, ProcessState state) {
		switch (state){
		case DONE : notifyProcessDone((VectorProcess) p); break;
		}
	}
	
	private void notifyProcessDone(VectorProcess p) {
		try {
			
			out.write(nbSimulation+"");
			out.write(year+"");
				
			for(String v : values.keySet()){
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
	
}
