package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class SHDIComplexityIndex extends MatrixMetric implements ValueMetric {
	
	private int countValues = -1;
	
	public SHDIComplexityIndex() {
		super(VariableManager.get("CSHDI"));
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
//			if(value > 0){
//				value = 0;
//			}
			if(value != 0){
				value *= -1;
			}
			
			//double max = -1 * Math.log(1.0/3);
			//double max = -1 * Math.log(1.0/co.values().size());
			double max = -1 * Math.log(1.0/countValues);		
			//System.out.println(max);
			//System.out.println(co.values().size());
			
			double e = value / max;
			
			double s = 1 - e;
			
			double c = (e*s) * 4;
			
			value = c;
			
		}
	}
	
}
