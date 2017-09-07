package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class RateValueMetric extends MatrixMetric implements ValueMetric {

	private int v;
	
	public RateValueMetric(Integer v) {
		super(VariableManager.get("rate_value_"+v));
		this.v = v;
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = new Double(co.countValue(v))/co.validValues();
		}
	}

}
