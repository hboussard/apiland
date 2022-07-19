package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.patch;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.PatchMetric;

public class VariationCoefficientPatchSizeMetric extends MatrixMetric implements PatchMetric {

	public VariationCoefficientPatchSizeMetric() {
		super(VariableManager.get("VCPS"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		//value = co.getVariationCoefficientPatchSize();
		/*
		value = 0;
		Stats s = new Stats();
		for(Patch p : ((PatchComposite) co.patches()).patches()){
			if(p.getValue() != 0){
				s.add(p.getArea());
			}
		}
		s.calculate();
		if(s.size() == 0){
			value = 0;
		}else{
			value = s.getVariationCoefficient();
		}
		*/
	}

}

