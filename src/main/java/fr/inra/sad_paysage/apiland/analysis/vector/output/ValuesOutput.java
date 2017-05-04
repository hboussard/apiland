package fr.inra.sad_paysage.apiland.analysis.vector.output;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;
import fr.inra.sad_paysage.apiland.analysis.metric.AbstractMetricOutput;
import fr.inra.sad_paysage.apiland.analysis.metric.Metric;
import fr.inra.sad_paysage.apiland.analysis.process.Process;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;

public class ValuesOutput extends AbstractMetricOutput {

	private Map<String, Double> values;
	
	public ValuesOutput(){
		super();
		values = new TreeMap<String, Double>();
	}
	
	@Override
	public void notifyFromMetric(Metric m, String metric, double value, Process process){
		//System.out.println("enter : "+metric.replace(".0", ""));
		values.put(metric.replace(".0", ""), value);
	}
	
	public void display(){
		for(Entry<String, Double> v : values.entrySet()){
			System.out.println(v.getKey()+" --> "+v.getValue());
		}
	}
	
	@Override
	public void notifyFromAnalysis(Analysis ma, AnalysisState state) {
		switch (state){
		case INIT : values.clear(); break;
		}
	}
	
	public boolean hasValues(){
		return values.size() > 0;
	}
	
	public double get(String metric){
		//System.out.println("try : "+metric);
		if(values.containsKey(metric)){
			return values.get(metric);
		}
		return Raster.getNoDataValue();
	}
	
}