package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class ComplexityHeterogeneityStrictIndex extends MatrixMetric implements CoupleMetric {
	
	private int countCouplesHomo = -1;
	
	private int countCouplesHete = -1;
	
	public ComplexityHeterogeneityStrictIndex() {
		super(VariableManager.get("CHET-strict"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		if(countCouplesHomo < 0){
			int countValues = ((SimpleWindowMatrixProcess) co.process()).processType().matrix().values().size();
			if(((SimpleWindowMatrixProcess) co.process()).processType().matrix().values().contains(0)){
				countValues--;
			}
			countCouplesHomo = countValues;
			for(int i=0; i<countValues; i++){
				for(int j=i+1; j<countValues; j++){
					countCouplesHete++;
				}
			}
		}
		if(co.countCouples() > 0){
			double valueHomo = 0.0;
			double valueHete = 0.0;
			double p;
			for(double c : co.couples()){
				if(!Couple.isHomogeneous(c)){
					p = co.countCouple(c) / (double)co.heterogeneousCouples();
					if(p != 0){
						valueHete += p*Math.log(p);
					}
				}else{
					p = co.countCouple(c) / (double)co.homogeneousCouples();
					if(p != 0){
						valueHomo += p*Math.log(p);
					}
				}
			}
			if(valueHomo != 0){
				valueHomo *= -1;
			}
			if(valueHete != 0){
				valueHete *= -1;
			}
			
			double maxHomo = -1 * Math.log(1.0/countCouplesHomo);
			double maxHete = -1 * Math.log(1.0/countCouplesHete);
			
			valueHomo = valueHomo / maxHomo;
			valueHete = valueHete / maxHete;
			
			value = (valueHomo + valueHete) / 2.0;
			
			double ivalue = 1 - value;
			
			value = 4 * (value * ivalue);
		}
	}

}
