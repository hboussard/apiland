package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class BaudryBurelHeterogeneityStructureIndex extends MatrixMetric implements CoupleMetric {
	
	public BaudryBurelHeterogeneityStructureIndex() {
		super(VariableManager.get("BBHSI"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = 0;
		if(co.countCouples() > 0){
			double p;
			for(double c : co.couples()){
				if(!Couple.isHomogeneous(c)){
					p = co.countCouple(c) / (double)co.validCouples();
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
