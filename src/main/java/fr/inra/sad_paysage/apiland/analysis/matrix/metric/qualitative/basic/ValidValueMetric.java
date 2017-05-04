package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.basic;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.BasicMetric;

public class ValidValueMetric extends MatrixMetric implements BasicMetric {
	
	public ValidValueMetric() {
		super(VariableManager.get("valid_value"));
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = co.validValues();
		}
	}

}
