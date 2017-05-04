package fr.inra.sad_paysage.apiland.analysis.matrix.metric.quantitative;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.QuantitativeMetric;

public class VarianceMetric extends MatrixMetric implements QuantitativeMetric {

	public VarianceMetric() {
		super(VariableManager.get("variance"));
	}

	@Override
	public void doCalculate(Counting co) {
		value = co.getVariance();
	}

}
