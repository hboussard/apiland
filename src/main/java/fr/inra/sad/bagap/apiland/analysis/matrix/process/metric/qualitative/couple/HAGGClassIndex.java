package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class HAGGClassIndex extends MatrixMetric implements CoupleMetric {
	
	private double couple;
	
	public HAGGClassIndex(Integer i1, Integer i2) {
		super(VariableManager.get("HETC_"+i1+"-"+i2));
		this.couple = Couple.get(i1, i2);
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = 0;
		if(co.countCouples() > 0){
			double p;
			for(double c : co.couples()){
				if(c == couple){
					p = co.countCouple(c) / (double) co.validCouples();
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