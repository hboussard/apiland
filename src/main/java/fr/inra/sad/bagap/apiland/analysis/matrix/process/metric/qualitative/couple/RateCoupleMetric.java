package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class RateCoupleMetric extends MatrixMetric implements CoupleMetric {

	private double couple;
	
	public RateCoupleMetric(Integer i1, Integer i2) {
		super(VariableManager.get("pNC_"+i1+"-"+i2));
		this.couple = Couple.get(i1, i2);
	}

	@Override
	public void doCalculate(Counting co) {
		/*
		if(co.countCouples() >= 0){
			value = new Double(co.countCouple(couple))/co.validCouples();
			if(Double.isNaN(value)){
				value = 0;
			}
		}*/
		
		if(co.validCouples() > 0){
			value = new Double(co.countCouple(couple))/co.validCouples();
		}
	}

}
