package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.patch;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.PatchMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.patch.Envelope;
import fr.inra.sad.bagap.apiland.patch.Patch;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public class HanskiConnectivityMetric extends MatrixMetric implements PatchMetric {
	
	public HanskiConnectivityMetric() {
		super(VariableManager.get("HC"));
	}

	@Override
	protected void doCalculate(Counting co) {
		value = 0;
		int v1;
		for(Patch p1 : ((PatchComposite) co.patches()).patches()){
			v1 = p1.getValue();
			for(Patch p2 : ((PatchComposite) co.patches()).patches()){
				if(v1 == p2.getValue() && p1 != p2){
					//double d = PatchManager.distance(p1, p2) / 1000.0;
					double d = Envelope.distance(p1.getEnvelope(), p2.getEnvelope())*Raster.getCellSize() / 1000.0;
					double a = p2.getArea() / 10000;
				
					value += Math.exp(-1*d)*a;
				}
			}
		}
	}
	
	
	
	/*
	 public double distance(Envelope env)
	  {
	    if (intersects(env)) return 0;
	    
	    double dx = 0.0;
	    if (maxx < env.minx) 
	      dx = env.minx - maxx;
	    else if (minx > env.maxx) 
	      dx = minx - env.maxx;
	    
	    double dy = 0.0;
	    if (maxy < env.miny) 
	      dy = env.miny - maxy;
	    else if (miny > env.maxy) dy = miny - env.maxy;

	    // if either is zero, the envelopes overlap either vertically or horizontally
	    if (dx == 0.0) return dy;
	    if (dy == 0.0) return dx;
	    return Math.sqrt(dx * dx + dy * dy);
	  }
	*/
}