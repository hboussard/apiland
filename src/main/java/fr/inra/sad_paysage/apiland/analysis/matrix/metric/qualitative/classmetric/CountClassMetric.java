package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.classmetric;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.ClassMetric;

public class CountClassMetric extends MatrixMetric implements ClassMetric{

	public CountClassMetric() {
		super(VariableManager.get("count_class"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = co.countClass();
	}

}
