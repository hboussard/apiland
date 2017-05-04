package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.couple;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.CoupleMetric;
import fr.inra.sad_paysage.apiland.core.util.Couple;

public class CountHomogeneousCoupleMetric extends MatrixMetric implements CoupleMetric {

	public CountHomogeneousCoupleMetric() {
		super(VariableManager.get("counthomo"));
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countCouples() > 0){
			value = 0;
			for(double c : co.couples()){
				if(Couple.isHomogeneous(c)){
					value += co.countCouple(c);
				}
			}
		}
	}

}
