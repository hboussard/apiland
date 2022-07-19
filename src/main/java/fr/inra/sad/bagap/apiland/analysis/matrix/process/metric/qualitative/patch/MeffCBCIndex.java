package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.patch;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.PatchMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.patch.Patch;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public class MeffCBCIndex extends MatrixMetric implements PatchMetric {
	
	public MeffCBCIndex() {
		super(VariableManager.get("MeffCBC"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		
		double surf_ter = co.validValues() * Raster.getCellSize() * Raster.getCellSize();
		
		value = 0;
		double surf_nat;
		for(Patch pa : ((PatchComposite) co.patches()).patches()){
			surf_nat = pa.getArea();
			value += surf_nat * pa.getValue();
		}
		
		value /= surf_ter;
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
	}
	
}
