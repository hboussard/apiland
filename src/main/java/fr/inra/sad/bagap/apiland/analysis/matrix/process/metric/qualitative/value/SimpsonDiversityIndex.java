package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class SimpsonDiversityIndex extends MatrixMetric implements ValueMetric {

	public SimpsonDiversityIndex() {
		super(VariableManager.get("SIDI"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = 0;
		if(co.countValues() > 0){
			double p;
			for(int v : co.values()){
				p = co.countValue(v) / (double)co.validValues();
				value += p*p;
			}
			value = 1 - value;
		}
	}
	
}
