package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.couple;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.CoupleMetric;

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
