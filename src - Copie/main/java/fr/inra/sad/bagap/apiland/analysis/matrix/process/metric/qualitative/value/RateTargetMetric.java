package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class RateTargetMetric extends MatrixMetric implements ValueMetric {

	
	public RateTargetMetric() {
		super(VariableManager.get("pTarget"));
	}

	@Override
	public void doCalculate(Counting co) {
		int pos = ((SimpleWindowMatrixProcess) co.process()).values().length / 2;
		double vc = ((SimpleWindowMatrixProcess) co.process()).values()[pos][pos];
		if(vc != Raster.getNoDataValue() && co.validValues() > 0){
			value = new Double(co.countValue((int) vc))/co.validValues();
		}else{
			value = Raster.getNoDataValue();
		}
	}

}