package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.classmetric;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.ClassMetric;

public class VarianceClassMetric extends MatrixMetric implements ClassMetric{

	public VarianceClassMetric() {
		super(VariableManager.get("variance_class"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = co.varianceClass();
	}

}
