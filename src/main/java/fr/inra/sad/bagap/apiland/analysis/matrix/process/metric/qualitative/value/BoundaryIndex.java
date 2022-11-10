package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.ValueMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class BoundaryIndex extends MatrixMetric implements ValueMetric {
	
	public BoundaryIndex() {
		super(VariableManager.get("Bindex"));
	}

	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = 0;
			if(co.countClass() > 1){
				value = 1;
			}
		}
	}


	
	
}
