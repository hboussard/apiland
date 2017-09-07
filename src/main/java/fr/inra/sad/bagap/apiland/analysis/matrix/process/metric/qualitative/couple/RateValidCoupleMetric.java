package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;

public class RateValidCoupleMetric extends MatrixMetric implements CoupleMetric {
	
	public RateValidCoupleMetric() {
		super(VariableManager.get("rate_valid_couple"));
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countCouples() != 0){
			value = (double) (co.validCouples())/co.totalCouples();
		}
	}

}
