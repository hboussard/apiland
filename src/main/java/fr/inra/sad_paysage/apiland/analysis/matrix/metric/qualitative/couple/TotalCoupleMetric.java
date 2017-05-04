package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.couple;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.CoupleMetric;

public class TotalCoupleMetric extends MatrixMetric implements CoupleMetric {
	
	public TotalCoupleMetric() {
		super(VariableManager.get("total_couple"));
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = co.totalCouples();
		}
	}

}

