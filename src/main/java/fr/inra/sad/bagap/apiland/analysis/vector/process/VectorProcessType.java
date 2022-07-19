package fr.inra.sad.bagap.apiland.analysis.vector.process;

import org.locationtech.jts.geom.Point;

import fr.inra.sad.bagap.apiland.analysis.process.ProcessType;
import fr.inra.sad.bagap.apiland.analysis.vector.metric.VectorMetric;

public class VectorProcessType extends ProcessType<VectorMetric> {

	public VectorProcess create(Point p){
		return new VectorProcess(p, this);
	}
	
	public VectorProcess create(){
		return new VectorProcess(this);
	}
	
}
