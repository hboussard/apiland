package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.basic;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.BasicMetric;

public class RateCountValueTypeMetric  extends MatrixMetric implements BasicMetric {
	
	public RateCountValueTypeMetric() {
		super(VariableManager.get("pN-type"));
	}

	@Override
	public void doCalculate(Counting co) {
		value = (double) (co.countValues())/co.validValues();
	}

}
