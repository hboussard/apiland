package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.classmetric;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ClassMetric;

public class StandardDeviationClassMetric extends MatrixMetric implements ClassMetric{

	public StandardDeviationClassMetric() {
		super(VariableManager.get("standard_deviation_class"));
	}
	
	@Override
	protected void doCalculate(Counting co) {
		value = co.standardDeviationClass();
	}

}
