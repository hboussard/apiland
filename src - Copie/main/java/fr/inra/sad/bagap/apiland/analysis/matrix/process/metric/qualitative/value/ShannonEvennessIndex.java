package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class ShannonEvennessIndex extends MatrixMetric implements ValueMetric {

	public ShannonEvennessIndex() {
		super(VariableManager.get("SHEI"));
	}
	
	@Override
	public void doCalculate(Counting co) {
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
				value *= -1 / Math.log(co.values().size());
			}
		}
	}
	
}
