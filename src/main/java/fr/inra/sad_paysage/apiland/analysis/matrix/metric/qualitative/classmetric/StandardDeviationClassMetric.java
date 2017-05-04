package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.classmetric;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.ClassMetric;

public class StandardDeviationClassMetric extends MatrixMetric implements ClassMetric{

	public StandardDeviationClassMetric() {
		super(VariableManager.get("standard_deviation_class"));
	}
	
	@Override
	protected void doCalculate(Counting co) {
		value = co.standardDeviationClass();
	}

}
