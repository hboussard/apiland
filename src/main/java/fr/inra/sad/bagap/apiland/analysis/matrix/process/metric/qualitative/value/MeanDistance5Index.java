package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class MeanDistance5Index extends MatrixMetric implements ValueMetric {
	
	public MeanDistance5Index() {
		super(VariableManager.get("MD5"));
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			double pc2 = new Double(co.countValue(2))/co.validValues();
			double pc3 = new Double(co.countValue(3))/co.validValues();
			double pc4 = new Double(co.countValue(4))/co.validValues();
			double pc5 = new Double(co.countValue(5))/co.validValues();
			value = pc2/4 + pc3/2 + 3*pc4/4 + pc5;
		}
	}

}
