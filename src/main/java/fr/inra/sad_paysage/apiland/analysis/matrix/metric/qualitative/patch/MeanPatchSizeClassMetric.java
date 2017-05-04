package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.patch;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.PatchMetric;
import fr.inra.sad_paysage.apiland.patch.PatchComposite;
import fr.inra.sad_paysage.apiland.patch.Patch;

public class MeanPatchSizeClassMetric extends MatrixMetric implements PatchMetric {

	private int classMetric;
	
	public MeanPatchSizeClassMetric(Integer cm) {
		super(VariableManager.get("MPSC_"+cm));
		classMetric = cm;
	}
	
	@Override
	public void doCalculate(Counting co) {
		value = 0;
		int count = 0;
		for(Patch p : ((PatchComposite) co.patches()).patches()){
			if(p.getValue() == classMetric){
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
