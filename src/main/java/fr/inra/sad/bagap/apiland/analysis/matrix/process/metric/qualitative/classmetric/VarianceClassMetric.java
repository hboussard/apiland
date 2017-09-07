package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.classmetric;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ClassMetric;

public class VarianceClassMetric extends MatrixMetric implements ClassMetric{

	public VarianceClassMetric() {
		super(VariableManager.get("variance_class"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = co.varianceClass();
	}

}
