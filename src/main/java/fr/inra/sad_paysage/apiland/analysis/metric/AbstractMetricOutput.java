package fr.inra.sad_paysage.apiland.analysis.metric;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;
import fr.inra.sad_paysage.apiland.analysis.process.Process;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessObserver;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessState;

public abstract class AbstractMetricOutput implements MetricOutput {

	private DecimalFormat format;
	
	public AbstractMetricOutput(){
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format = new DecimalFormat("0.00000", symbols);
	}
	
	@Override
	public int compareTo(ProcessObserver obs) {
		if(obs instanceof MetricOutput){
			return 1;
		}
		return -1;
	}
	
	protected String format(double v){
		int f = new Double(Math.floor(v)).intValue();
		if(v == f){
			return f+"";
		}
		return format.format(v);
	}
	
	protected double formatDouble(double v){
		if(Double.isNaN(v)){
			return -1;
		}
		int f = new Double(Math.floor(v)).intValue();
		if(v == f){
			return f;
		}
		return Double.parseDouble(format.format(v));
	}
	
	@Override
	public boolean acceptMetric(String metric) {
		return true;
	}

	@Override
	public void notify(Metric m, String metric, double value, Process process){
		// do nothing
	}
	
	@Override
	public void notify(Process p, ProcessState s) {
		// do nothing
	}
	
	@Override
	public void notify(Analysis ma, AnalysisState state) {
		// do nothing
	}
	
	@Override
	public void updateProgression(Analysis a, int total) {
		// do nothing
	}

}
