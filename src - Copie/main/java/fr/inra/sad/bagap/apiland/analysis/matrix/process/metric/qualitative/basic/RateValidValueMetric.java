package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.basic;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.BasicMetric;

public class RateValidValueMetric extends MatrixMetric implements BasicMetric {
	
	public RateValidValueMetric() {
		super(VariableManager.get("pN-valid"));
	}

	@Override
	public void doCalculate(Counting co) {
		value = (double) (co.validValues())/co.theoreticalSize();
	}

}
