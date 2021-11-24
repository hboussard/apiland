package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class CountClassValueMetric extends MatrixMetric implements ValueMetric {
	
	public CountClassValueMetric() {
		super(VariableManager.get("Nclass"));
	}

	@Override
	public void doCalculate(Counting co) {
		value = 0;
		if(co.countValues() > 0){
			value = co.countClass();
			//value = co.values().size();
		}
	}

}

