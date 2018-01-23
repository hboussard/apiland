package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class ShannonDiversityIndex extends MatrixMetric implements ValueMetric {
	
	public ShannonDiversityIndex() {
		super(VariableManager.get("SHDI"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = 0;
		if(co.countValues() > 0){
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
		}
	}
	
}
