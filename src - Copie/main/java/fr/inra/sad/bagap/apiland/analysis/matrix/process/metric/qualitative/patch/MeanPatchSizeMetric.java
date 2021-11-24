package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.patch;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.PatchMetric;
import fr.inra.sad.bagap.apiland.patch.Patch;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public class MeanPatchSizeMetric extends MatrixMetric implements PatchMetric {

	public MeanPatchSizeMetric() {
		super(VariableManager.get("MPS"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = co.getMeanPatchSize();
		}
		/*
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
		*/
	}

}
