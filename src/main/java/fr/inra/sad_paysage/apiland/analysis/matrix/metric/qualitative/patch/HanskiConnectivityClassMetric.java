package fr.inra.sad_paysage.apiland.analysis.matrix.metric.qualitative.patch;

import java.util.HashSet;
import java.util.Set;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.PatchMetric;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.patch.PatchComposite;
import fr.inra.sad_paysage.apiland.patch.PatchManager;
import fr.inra.sad_paysage.apiland.patch.Envelope;
import fr.inra.sad_paysage.apiland.patch.Patch;

public class HanskiConnectivityClassMetric extends MatrixMetric implements PatchMetric {

	private int classMetric;
	
	private int index = 0;
	
	public HanskiConnectivityClassMetric(Integer cm) {
		super(VariableManager.get("HCC_"+cm));
		classMetric = cm;
	}

	@Override
	protected void doCalculate(Counting co) {
		value = 0;
		int nb = 0;
		//Set<Patch> ever = new HashSet<Patch>();
		for(Patch p1 : ((PatchComposite) co.patches()).patches()){
			if(classMetric == p1.getValue()){
				//ever.add(p1);
				nb++;
				for(Patch p2 : ((PatchComposite) co.patches()).patches()){
					if(classMetric == p2.getValue() /*&& !ever.contains(p2)*/ && p1 != p2){
						//double d = (PatchManager.distance(p1, p2)* Raster.getCellSize()) / 1000.0;
						double d = (Envelope.distance(p1.getEnvelope(), p2.getEnvelope())*Raster.getCellSize()) / 1000.0;
						//System.out.println("raster : distance (en km) = "+d);
						double a = p2.getArea() / 10000.0;
						//System.out.println("raster : aire (en hectare) = "+a);
					
						value += Math.exp(-1*d)*a;
					}
				}
			}
			
		}
		value /= nb;
		//System.out.println("raster "+index++);
		System.out.println("raster "+(index++)+": value = "+value+" ("+nb+")");
	}

}