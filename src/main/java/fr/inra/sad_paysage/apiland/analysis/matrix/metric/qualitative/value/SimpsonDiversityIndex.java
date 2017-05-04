package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.value;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.ValueMetric;

public class SimpsonDiversityIndex extends MatrixMetric implements ValueMetric {

	public SimpsonDiversityIndex() {
		super(VariableManager.get("SIDI"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = 0;
			double p;
			for(int v : co.values()){
				p = co.countValue(v) / (double)co.validValues();
				value += p*p;
			}
			value = 1 - value;
		}
	}
	
}
