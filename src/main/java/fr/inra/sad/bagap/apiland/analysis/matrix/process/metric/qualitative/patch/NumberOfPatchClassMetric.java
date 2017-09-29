package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.patch;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.PatchMetric;
import fr.inra.sad.bagap.apiland.patch.Patch;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public class NumberOfPatchClassMetric extends MatrixMetric implements PatchMetric {

	private int classMetric;
	
	public NumberOfPatchClassMetric(Integer cm) {
		super(VariableManager.get("NP-class_"+cm));
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
