package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.classmetric;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ClassMetric;

public class CountClassMetric extends MatrixMetric implements ClassMetric{

	public CountClassMetric() {
		super(VariableManager.get("count_class"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = co.countClass();
	}

}
