package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.patch;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.PatchMetric;

public class VariationCoefficientPatchSizeClassMetric extends MatrixMetric implements PatchMetric {

	private int classMetric;
	
	public VariationCoefficientPatchSizeClassMetric(Integer cm) {
		super(VariableManager.get("VCPS-class_"+cm));
		classMetric = cm;
	}
	
	@Override
	public void doCalculate(Counting co) {
		//value = co.getVariationCoefficientPatchSize(classMetric);
		/*
		value = 0;
		Stats s = new Stats();
		for(Patch p : ((PatchComposite) co.patches()).patches()){
			if(p.getValue() == classMetric){
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

