package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.patch;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.PatchMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.patch.Envelope;
import fr.inra.sad.bagap.apiland.patch.Patch;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;
import fr.inra.sad.bagap.apiland.patch.PatchManager;

public class HanskiConnectivityClassMetric extends MatrixMetric implements PatchMetric {

	private int classMetric;
	
	public HanskiConnectivityClassMetric(Integer cm) {
		super(VariableManager.get("HC-class_"+cm));
		classMetric = cm;
	}

	@Override
	protected void doCalculate(Counting co) {
		value = 0;
		int nb = 0;
		for(Patch p1 : ((PatchComposite) co.patches()).patches()){
			if(classMetric == p1.getValue()){
				nb++;
				for(Patch p2 : ((PatchComposite) co.patches()).patches()){
					if(classMetric == p2.getValue() && p1 != p2){
						//double d = PatchManager.distance(p1, p2)*Raster.getCellSize() / 1000.0;
						double d = (Envelope.distance(p1.getEnvelope(), p2.getEnvelope())*Raster.getCellSize()) / 1000.0;
						double a = p2.getArea() / 10000.0;
					
						value += Math.exp(-1*d)*a;
					}
				}
			}
			
		}
		value /= nb;
	}

}