package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ContinuityMetric extends MatrixMetric implements ValueMetric {
	
	public ContinuityMetric() {
		super(VariableManager.get("CONT"));
	}

	@Override
	public void doCalculate(Counting co) {
		
		int pos = ((SimpleWindowMatrixProcess) co.process()).values().length / 2;
		double vc = ((SimpleWindowMatrixProcess) co.process()).values()[pos][pos];
		value = Raster.getNoDataValue();
		if(vc != Raster.getNoDataValue() && vc != 0){
			value = co.countValue((int) vc);
		}
	}
	
}