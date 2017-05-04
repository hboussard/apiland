package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.couple;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.CoupleMetric;

public class BaudryBurelHeterogeneityIndex extends MatrixMetric implements CoupleMetric {
	
	public BaudryBurelHeterogeneityIndex() {
		super(VariableManager.get("BBHI"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		if(co.countCouples() > 0){
			double p;
			value = 0;
			for(double c : co.couples()){
				p = co.countCouple(c) / (double)co.validCouples(); 
				if(p != 0){
					value += p*Math.log(p);
				}
			}
			if(value != 0){
				value *= -1;
			}
		}
	}

}
