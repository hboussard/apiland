package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.patch;

import fr.inra.sad_paysage.apiland.analysis.Stats;
import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.PatchMetric;
import fr.inra.sad_paysage.apiland.patch.PatchComposite;
import fr.inra.sad_paysage.apiland.patch.Patch;

public class StandardDeviationPatchSizeClassMetric extends MatrixMetric implements PatchMetric {

	private int classMetric;
	
	public StandardDeviationPatchSizeClassMetric(Integer cm) {
		super(VariableManager.get("SDPSC_"+cm));
		classMetric = cm;
	}
	
	@Override
	public void doCalculate(Counting co) {
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
			value = s.getStandardDeviation();
		}
		
	}

}

