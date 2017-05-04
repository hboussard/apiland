package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.patch;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.PatchMetric;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.patch.PatchComposite;
import fr.inra.sad_paysage.apiland.patch.Patch;

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
