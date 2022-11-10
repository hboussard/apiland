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

public class CentralPixelHanskiConnectivityClassMetric extends MatrixMetric implements PatchMetric {

	private int classMetric;
	
	public CentralPixelHanskiConnectivityClassMetric(Integer cm) {
		super(VariableManager.get("CPXHCC_"+cm));
		classMetric = cm;
	}

	@Override
	protected void doCalculate(Counting co) {
		Pixel pixel = ((SimpleWindowMatrixProcess) co.process()).window().toWindow(((SimpleWindowMatrixProcess) co.process()).window().pixel());
		
		value = 0;
		for(Patch p : ((PatchComposite) co.patches()).patches()){
			if(p.getValue() == classMetric){
				double d = PatchManager.distance(p, pixel) / 1000.0;
				double a = p.getArea() / 10000;
				
				//System.out.println(d+" "+Math.exp(-1*d));
				
				value += Math.exp(-1*d)*a;
			}
		}
	}

}