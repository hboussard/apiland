package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.quantitative;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.QuantitativeMetric;

public class CountPositivesMetric extends MatrixMetric implements QuantitativeMetric {
	
	public CountPositivesMetric() {
		super(VariableManager.get("count_positives"));
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.countPositives();
	}

}
