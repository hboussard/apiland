package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class HeterogeneityConfigurationStrictIndex extends MatrixMetric implements CoupleMetric {
	
	public HeterogeneityConfigurationStrictIndex() {
		super(VariableManager.get("HET-frag-strict"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		if(co.countCouples() > 0){
			value = 0;
			double p;
			for(double c : co.couples()){
				if(!Couple.isHomogeneous(c)){
					//p = co.countCouple(c) / (double)co.validCouples();
					p = co.countCouple(c) / (double)co.heterogeneousCouples();
					if(p != 0){
						value += p*Math.log(p);
					}
				}
			}
			if(value != 0){
				value *= -1;
			}
		}
	}

}
