package fr.inra.sad.bagap.apiland.analysis.vector.window;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad.bagap.apiland.analysis.vector.process.VectorProcess;
import fr.inra.sad.bagap.apiland.analysis.vector.process.VectorProcessType;
import fr.inra.sad.bagap.apiland.core.element.DynamicFeature;
import fr.inra.sad.bagap.apiland.core.element.DynamicLayer;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class MapWindowVectorAnalysis extends WindowVectorAnalysis {
	
	public MapWindowVectorAnalysis(DynamicLayer<?> layer,
			Set<VectorMetric> metrics, Instant t, VectorProcessType processType) {
		super(layer, metrics, t, processType);
	}

	@Override
	protected void doInit() {
		// do nothing
	}
	
	@Override
	protected void doRun() {
		List<DynamicFeature> features = new ArrayList<DynamicFeature>();
		Iterator<DynamicFeature> ite = layer.activeDeepIterator(t);
		DynamicFeature f;
		while(ite.hasNext()){
			f = ite.next();
			features.add(f);
		}
		VectorProcess process = processType.create();
		process.calculate(metrics, features, t);
	}

	@Override
	protected void doClose() {
		//layer = null;
		t = null;
	}

}
