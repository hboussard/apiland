package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.value;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.ValueMetric;

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