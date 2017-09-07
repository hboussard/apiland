package fr.inra.sad.bagap.apiland.analysis.vector.output;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import com.csvreader.CsvWriter;
import com.csvreader.CsvWriter.FinalizedException;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessState;
import fr.inra.sad.bagap.apiland.analysis.process.metric.AbstractMetricOutput;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad.bagap.apiland.analysis.vector.process.VectorProcess;
import fr.inra.sad.bagap.apiland.analysis.vector.window.WindowVectorAnalysis;

public class CsvOutputV extends AbstractMetricOutput {

	private String file;
	
	private CsvWriter out;
	
	private Map<String, Double> values;
	
	public CsvOutputV(String f){
		super();
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
	}
	
	@Override
	public void notify(Analysis a, AnalysisState state) {
		switch (state){
		case INIT : notifyAnalysisInit((WindowVectorAnalysis) a); break;
		case RUNNING : notifyAnalysisRun((WindowVectorAnalysis) a); break;
		case FINISH : notifyAnalysisFinish((WindowVectorAnalysis) a); break;
		}
	}

	private void notifyAnalysisInit(WindowVectorAnalysis a) {
		out = new CsvWriter(file);
		out.setDelimiter(';');
	}

	private void notifyAnalysisRun(WindowVectorAnalysis a) {
		try {
			out.write("X");
			out.write("Y");
			String entete;
			for(double buffer : a.buffers()){
				for(VectorMetric vm : a.metrics()){
					entete = vm.getName()+"_"+buffer;
					out.write(entete.replace(".0", ""));
				}
			}
			out.endRecord();
		} catch (FinalizedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void notifyAnalysisFinish(WindowVectorAnalysis a) {
		out.close();
		out = null;
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
			out.write(p.x()+"");
			out.write(p.y()+"");
				
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
