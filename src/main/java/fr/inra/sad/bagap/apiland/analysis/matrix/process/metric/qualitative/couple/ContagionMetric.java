package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.couple;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.CoupleMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class ContagionMetric extends MatrixMetric implements ValueMetric, CoupleMetric {

	private int countValues = -1;
	
	public ContagionMetric() {
		super(VariableManager.get("CONTAG"));
	}

	@Override
	protected void doCalculate(Counting co) {
		if(countValues < 0){
			countValues = ((SimpleWindowMatrixProcess) co.process()).processType().matrix().values().size();
			if(((SimpleWindowMatrixProcess) co.process()).processType().matrix().values().contains(0)){
				countValues--;
			}
		}
		if(co.countCouples() > 0){
			value = 0;
			double pv, pc;
			for(int v : co.values()){
				pv = co.countValue(v) / (double)co.validValues();
				if(pv != 0){
					for(double c : co.couples()){
						pc = co.countCouple(c) / (double)co.validCouples(); 
						if(pc != 0){
							value += (pv*pc)*Math.log(pv*pc);
						}
					}
				}
			}
			
			value = (1 + (value / (2 * Math.log(countValues)))) * 100;
			
		}
	}

}
