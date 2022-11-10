package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;

public class CountCoupleTypeMetric extends MatrixMetric implements CoupleMetric {

	public CountCoupleTypeMetric() {
		super(VariableManager.get("NC-type"));
	}

	@Override
	public void doCalculate(Counting co) {
		value = co.countCouples();
	}

}
