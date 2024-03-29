package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;

public class CountHeterogeneousCoupleMetric extends MatrixMetric implements CoupleMetric {

	public CountHeterogeneousCoupleMetric() {
		super(VariableManager.get("NC-hete"));
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countCouples() >= 0){
			value = co.heterogeneousCouples();
		}
		/*
		if(co.countCouples() > 0){
			value = 0;
			for(double c : co.couples()){
				if(!Couple.isHomogeneous(c)){
					value += co.countCouple(c);
				}
			}
		}
		*/
	}

}