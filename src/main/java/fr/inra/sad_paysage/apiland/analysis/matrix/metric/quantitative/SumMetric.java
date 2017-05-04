package fr.inra.sad_paysage.apiland.analysis.matrix.metric.quantitative;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.QuantitativeMetric;

public class SumMetric extends MatrixMetric implements QuantitativeMetric {

	public SumMetric() {
		super(VariableManager.get("sum"));
	}

	@Override
	public void doCalculate(Counting co) {
		value = co.getSum();
	}

}
