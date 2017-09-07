package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.patch;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.SimpleWindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.PatchMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.patch.Patch;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;
import fr.inra.sad.bagap.apiland.patch.PatchManager;

public class CentralPatchHanskiConnectivityClassMetric extends MatrixMetric implements PatchMetric {

	private int classMetric;
	
	public CentralPatchHanskiConnectivityClassMetric(Integer cm) {
		super(VariableManager.get("CPHCC_"+cm));
		classMetric = cm;
	}

	@Override
	protected void doCalculate(Counting co) {
		Pixel pixel = ((SimpleWindowMatrixProcess) co.process()).window().toWindow(((SimpleWindowMatrixProcess) co.process()).window().pixel());
		
		Patch patch = null;
		for(Patch p : ((PatchComposite) co.patches()).patches()){
			if(p.getValue() == classMetric && p.contains(pixel)){
				patch = p;
			}
		}
		
		if(patch != null){
			value = 0;
			for(Patch p : ((PatchComposite) co.patches()).patches()){
				if(patch != p && p.getValue() == classMetric){
					double d = PatchManager.distance(patch, p) / 1000.0;
					double a = p.getArea() / 10000;
					
					value += Math.exp(-1*d)*a;
				}
			}
		}
	}

}
