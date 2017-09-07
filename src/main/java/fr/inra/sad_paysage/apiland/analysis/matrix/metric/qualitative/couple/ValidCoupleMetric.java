package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.couple;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.CoupleMetric;

public class ValidCoupleMetric extends MatrixMetric implements CoupleMetric {
	
	public ValidCoupleMetric() {
		super(VariableManager.get("valid_couple"));
	}

	@Override
	public void doCalculate(Counting co) {
		value = co.validCouples();
	}

}
