package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.couple;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.util.Couple;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.CoupleMetric;

public class RateCoupleMetric extends MatrixMetric implements CoupleMetric {

	private double couple;
	
	public RateCoupleMetric(Integer i1, Integer i2) {
		super(VariableManager.get("rate_couple_"+i1+"-"+i2));
		this.couple = Couple.get(i1, i2);
	}

	@Override
	public void doCalculate(Counting co) {
		value = new Double(co.countCouple(couple))/co.validCouples();
		if(Double.isNaN(value)){
			value = 0;
		}
	}

}
