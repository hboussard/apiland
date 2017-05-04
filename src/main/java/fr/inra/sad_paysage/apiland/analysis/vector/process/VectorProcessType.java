package fr.inra.sad_paysage.apiland.analysis.vector.process;

import com.vividsolutions.jts.geom.Point;

import fr.inra.sad_paysage.apiland.analysis.process.ProcessType;
import fr.inra.sad_paysage.apiland.analysis.vector.metric.VectorMetric;

public class VectorProcessType extends ProcessType<VectorMetric> {

	public VectorProcess create(Point p){
		return new VectorProcess(p, this);
	}
	
	public VectorProcess create(){
		return new VectorProcess(this);
	}
	
}
