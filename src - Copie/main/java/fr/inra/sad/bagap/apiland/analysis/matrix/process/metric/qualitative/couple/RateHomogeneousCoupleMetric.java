package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;

public class RateHomogeneousCoupleMetric extends MatrixMetric implements CoupleMetric {

	public RateHomogeneousCoupleMetric() {
		super(VariableManager.get("pNC-homo"));
	}

	@Override
	public void doCalculate(Counting co) {
		
		if(co.countCouples() >= 0){
			value = co.homogeneousCouples() / co.validCouples();
		}
		
		/*
		if(co.countCouples() > 0){
			value = 0;
			for(double c : co.couples()){
				if(Couple.isHomogeneous(c)){
					value += co.countCouple(c);
				}
			}
			value /= co.validCouples();
		}
		*/
	}

}
