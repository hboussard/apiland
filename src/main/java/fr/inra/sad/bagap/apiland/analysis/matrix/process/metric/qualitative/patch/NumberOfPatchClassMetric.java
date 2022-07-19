package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.patch;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.PatchMetric;

public class NumberOfPatchClassMetric extends MatrixMetric implements PatchMetric {

	private int classMetric;
	
	public NumberOfPatchClassMetric(Integer cm) {
		super(VariableManager.get("NP-class_"+cm));
		classMetric = cm;
	}
	
	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = co.getPatchNumber(classMetric);
		}
		/*
		value = 0;
		for(Patch p : ((PatchComposite) co.patches()).patches()){
			if(p.getValue() == classMetric){
				value++;
			}
		}
		*/
	}

}
