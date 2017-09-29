package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class SimpsonEvennessIndex extends MatrixMetric implements ValueMetric {

	public SimpsonEvennessIndex() {
		super(VariableManager.get("SIEI"));
	}

	@Override
	protected void doCalculate(Counting co) {
		value = 0;
		if(co.countValues() > 0){
			double p;
			for(int v : co.values()){
				p = co.countValue(v) / (double)co.validValues();
				value += p*p;
			}
			value = (1 - value) / co.values().size();
		}
	}
	
}
