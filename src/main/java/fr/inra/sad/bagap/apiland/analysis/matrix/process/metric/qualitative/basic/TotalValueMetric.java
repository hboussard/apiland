package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.basic;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.BasicMetric;

public class TotalValueMetric extends MatrixMetric implements BasicMetric {
	
	public TotalValueMetric() {
		super(VariableManager.get("total_value"));
	}

	@Override
	public void doCalculate(Counting co) {
		value = co.totalValues();
	}

}
