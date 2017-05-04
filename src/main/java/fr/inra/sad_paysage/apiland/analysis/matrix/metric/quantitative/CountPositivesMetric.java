package fr.inra.sad_paysage.apiland.analysis.matrix.metric.quantitative;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.QuantitativeMetric;

public class CountPositivesMetric extends MatrixMetric implements QuantitativeMetric {
	
	public CountPositivesMetric() {
		super(VariableManager.get("count_positives"));
	}

	@Override
	protected void doCalculate(Counting co) {
		value = co.countPositives();
	}

}
