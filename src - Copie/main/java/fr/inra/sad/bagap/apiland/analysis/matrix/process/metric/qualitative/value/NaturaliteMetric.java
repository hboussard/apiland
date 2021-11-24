package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

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
				area = co.countValue(v) * Math.pow(Raster.getCellSize(), 2);
				//value += area * coeffs.get(v);
				value += area * v;
			}
		}
	}
	
}
