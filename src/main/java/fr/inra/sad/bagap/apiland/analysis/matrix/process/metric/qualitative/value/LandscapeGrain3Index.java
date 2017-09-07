package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class LandscapeGrain3Index extends MatrixMetric implements ValueMetric {
	
	public LandscapeGrain3Index() {
		super(VariableManager.get("LG3"));
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			double pc2 = new Double(co.countValue(2))/co.validValues();
			double pc3 = new Double(co.countValue(3))/co.validValues();
			value = (pc2+pc3)/(2-pc3);
		}
	}

}