package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.quantitative;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.FullQuantitativeMetric;

public class MinimumMetric extends MatrixMetric implements FullQuantitativeMetric {

	public MinimumMetric() {
		super(VariableManager.get("minimum"));
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.getMinimum();
	}

}
