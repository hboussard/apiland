package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class CleanNoDataMetric extends MatrixMetric implements ValueMetric {
	
	public CleanNoDataMetric() {
		super(VariableManager.get("clean-nodata"));
	}

	@Override
	public void doCalculate(Counting co) {
		int pos = ((SimpleWindowMatrixProcess) co.process()).values().length / 2;
		double vc = ((SimpleWindowMatrixProcess) co.process()).values()[pos][pos];
		value = Raster.getNoDataValue();
		if(vc == Raster.getNoDataValue() && ((co.totalValues() - co.validValues()) == 1  || (co.totalValues() - co.validValues()) == 2)){
			int imax = 0;
			int vmax = -1;
			for(int vv : co.values()){
				int cv = (int) co.countValue(vv);
				if(cv > vmax){
					vmax = cv;
					imax = vv;
				}
			}
			value = imax;
			//System.out.println(value);
		}
	}
	
}
