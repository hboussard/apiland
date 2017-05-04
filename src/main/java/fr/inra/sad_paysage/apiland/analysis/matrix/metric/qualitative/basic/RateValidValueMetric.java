package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.basic;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.BasicMetric;

public class RateValidValueMetric extends MatrixMetric implements BasicMetric {
	
	public RateValidValueMetric() {
		super(VariableManager.get("rate_valid_value"));
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = (double) (co.validValues())/co.theoricalSize();
		}
	}

}
