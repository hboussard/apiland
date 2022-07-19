package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class NormalizedShannonDiversityIndex extends MatrixMetric implements ValueMetric {
	
	private int countValues = -1;
	
	public NormalizedShannonDiversityIndex() {
		super(VariableManager.get("NSHDI"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		if(countValues < 0){
			countValues = ((SimpleWindowMatrixProcess) co.process()).processType().matrix().values().size();
			if(((SimpleWindowMatrixProcess) co.process()).processType().matrix().values().contains(0)){
				countValues--;
			}
		}
		if(co.countValues() > 0){
			value = 0;
			double p; 
			for(int v : co.values()){
				p = co.countValue(v) / (double)co.validValues();
				if(p != 0){
					value += p*Math.log(p);
				}
			}
			if(value != 0){
				value *= -1;
			}
			
			
			double max = -1 * Math.log(1.0/countValues);
			
			value = value / max;
		}
	}
	
}
