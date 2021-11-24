package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class NormalizedHeterogeneityCompositionStrictIndex extends MatrixMetric implements CoupleMetric {
	
	private int countCouples = -1;
	
	public NormalizedHeterogeneityCompositionStrictIndex() {
		super(VariableManager.get("NHET-agg-strict"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		if(countCouples < 0){
			int countValues = ((SimpleWindowMatrixProcess) co.process()).processType().matrix().values().size();
			if(((SimpleWindowMatrixProcess) co.process()).processType().matrix().values().contains(0)){
				countValues--;
			}
			countCouples = countValues;
		}
		if(co.countCouples() > 0){
			value = 0;
			double p;
			for(double c : co.couples()){
				if(Couple.isHomogeneous(c)){
					//p = co.countCouple(c) / (double) co.validCouples();
					p = co.countCouple(c) / (double) co.homogeneousCouples();
					if(p != 0){
						value += p*Math.log(p);
					}
				}
			}
			if(value != 0){
				value *= -1;
			}
			
			double max = -1 * Math.log(1.0/countCouples);
			
			value = value / max;
		}
	}

}