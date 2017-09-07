package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.quantitative;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.QuantitativeMetric;

public class SumMetric extends MatrixMetric implements QuantitativeMetric {

	public SumMetric() {
		super(VariableManager.get("sum"));
	}

	@Override
	public void doCalculate(Counting co) {
		value = co.getSum();
	}

}
