package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;

public class LandscapeGrain4Index extends MatrixMetric implements ValueMetric {
	
	public LandscapeGrain4Index() {
		super(VariableManager.get("LG4"));
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			double pc1 = new Double(co.countValue(1))/co.validValues();
			double pc2 = new Double(co.countValue(2))/co.validValues();
			double pc3 = new Double(co.countValue(3))/co.validValues();
			double pc4 = new Double(co.countValue(4))/co.validValues();
			value = (pc2+2*pc3+pc4)/(1+2*(pc1+pc2+pc3));
		}
	}

}