package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class CleanSurounded extends MatrixMetric implements ValueMetric {
	
	public CleanSurounded() {
		super(VariableManager.get("clean"));
	}

	@Override
	public void doCalculate(Counting co) {
		
		int pos = ((SimpleWindowMatrixProcess) co.process()).values().length / 2;
		double vc = ((SimpleWindowMatrixProcess) co.process()).values()[pos][pos];
		value = Raster.getNoDataValue();
		if(vc != Raster.getNoDataValue() && vc != 0){
			/*if(co.validValues() == 1 || co.countValues() == 1){
				value = vc;
			}*/
			if(co.countValues() < co.totalValues()/2){
				value = vc;
			}
		}
	}
	
}
