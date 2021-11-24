package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.basic;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.BasicMetric;

public class ValidValueMetric extends MatrixMetric implements BasicMetric {
	
	public ValidValueMetric() {
		super(VariableManager.get("N-valid"));
	}

	@Override
	public void doCalculate(Counting co) {
		value = co.validValues();
	}

}
