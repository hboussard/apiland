package fr.inra.sad.bagap.apiland.capfarm.simul.ecological;

import java.util.ArrayList;
import java.util.List;

import fr.inra.sad.bagap.apiland.capfarm.model.Cover;

public class EcologicalModelBuilder {

	private String name;
	
	private double intercept;
	
	private List<EcologicalModelMetric> metrics;
	
	private List<Double> coeffs;
	
	public EcologicalModelBuilder(){
		metrics = new ArrayList<EcologicalModelMetric>();
		coeffs = new ArrayList<Double>();
		reset();
	}
	
	private void reset(){
		name = null;
		intercept = Double.NaN;
		metrics.clear();
		coeffs.clear();
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setIntercept(double intercept) {
		this.intercept = intercept;
	}

	public void setMetric(String metric, int buffer, double coeff) {
		metrics.add(new EcologicalModelMetric(metric, buffer));
		coeffs.add(coeff);
	}
	
	public void setFilter(Cover cover) {
		
	}

	public EcologicalModelAnalysis build() {
		EcologicalModelAnalysis model = new EcologicalModelAnalysis(name, intercept, null, null, 
				coeffs.toArray(new Double[coeffs.size()]), 
				metrics.toArray(new EcologicalModelMetric[metrics.size()]));
		
		EcologicalModelManager.add(model);
		
		reset();
		return model;
	}

}
