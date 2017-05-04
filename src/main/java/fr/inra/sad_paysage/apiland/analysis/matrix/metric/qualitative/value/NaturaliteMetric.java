package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.value;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.ValueMetric;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;

public class NaturaliteMetric extends MatrixMetric implements ValueMetric {

	//private Map<Integer, Integer> coeffs;
	
	public NaturaliteMetric(/*Map<Integer, Integer> coeffs*/) {
		super(VariableManager.get("NAT"));
		//this.coeffs = coeffs;
	}
	
	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = 0;
			double area;
			for(int v : co.values()){
				area = co.countValue(v) * Raster.getCellSize();
				//value += area * coeffs.get(v);
				value += area * v;
			}
		}
	}
	
}