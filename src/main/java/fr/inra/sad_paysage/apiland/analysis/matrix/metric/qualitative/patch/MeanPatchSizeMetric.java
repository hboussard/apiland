package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.patch;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.PatchMetric;
import fr.inra.sad_paysage.apiland.patch.Patch;
import fr.inra.sad_paysage.apiland.patch.PatchComposite;

public class MeanPatchSizeMetric extends MatrixMetric implements PatchMetric {

	public MeanPatchSizeMetric() {
		super(VariableManager.get("MPS"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = 0;
		int count = 0;
		for(Patch p : ((PatchComposite) co.patches()).patches()){
			if(p.getValue() != 0){
				value += p.getArea();
				count++;
			}
		}
		if(count == 0){
			value = 0;
		}else{
			value /= (double) count;
		}
		
	}

}
