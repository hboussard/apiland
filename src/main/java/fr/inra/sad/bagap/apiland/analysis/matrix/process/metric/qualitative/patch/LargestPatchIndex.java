package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.patch;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.PatchMetric;
import fr.inra.sad.bagap.apiland.patch.Patch;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public class LargestPatchIndex extends MatrixMetric implements PatchMetric {

	public LargestPatchIndex() {
		super(VariableManager.get("LPI"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = 0;
		for(Patch p : ((PatchComposite) co.patches()).patches()){
			if(p.getValue() != 0){
				//System.out.println(p.size()+" "+p.getArea()+" "+p.getClass());
				value = Math.max(value, p.getArea());
			}
		}
		value /= 10000.0; // en hectares
	}

}
