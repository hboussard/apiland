package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.value;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.ValueMetric;

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
