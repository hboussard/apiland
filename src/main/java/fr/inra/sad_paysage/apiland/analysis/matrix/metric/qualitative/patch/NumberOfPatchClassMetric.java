package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.patch;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.PatchMetric;
import fr.inra.sad_paysage.apiland.patch.PatchComposite;
import fr.inra.sad_paysage.apiland.patch.Patch;

public class NumberOfPatchClassMetric extends MatrixMetric implements PatchMetric {

	private int classMetric;
	
	public NumberOfPatchClassMetric(Integer cm) {
		super(VariableManager.get("NPC_"+cm));
		classMetric = cm;
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = 0;
		for(Patch p : ((PatchComposite) co.patches()).patches()){
			if(p.getValue() == classMetric){
				value++;
			}
		}
	}

}
