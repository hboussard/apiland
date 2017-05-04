package fr.inra.sad_paysage.apiland.analysis.vector.window;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.vividsolutions.jts.geom.Point;

import fr.inra.sad_paysage.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad_paysage.apiland.analysis.vector.process.VectorProcess;
import fr.inra.sad_paysage.apiland.analysis.vector.process.VectorProcessType;
import fr.inra.sad_paysage.apiland.core.element.DynamicFeature;
import fr.inra.sad_paysage.apiland.core.element.DynamicLayer;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public class SelectedWindowVectorAnalysis extends WindowVectorAnalysis {

	private VectorProcess process;
	
	private Set<Point> points;
	
	public SelectedWindowVectorAnalysis(DynamicLayer<?> layer,
			Set<Double> buffers, Set<VectorMetric> metrics, double minRate,
			Map<String, Set<Object>> filters, Map<String, Set<Object>> unfilters, Set<Point> points,
			Instant t, VectorProcessType processType) {
		super(layer, buffers, metrics, minRate, filters, unfilters, t, processType);
		
		this.points = points;
		this.process = processType.create();
	}

	@Override
	protected void doInit() {
		// do nothing
	}

	@Override
	protected void doRun() {
		
		List<DynamicFeature> features = new ArrayList<DynamicFeature>();
		Iterator<DynamicFeature> ite = layer.activeDeepIterator(t);
		while(ite.hasNext()){
			features.add(ite.next());
		}
		
		for(Point p : points){
			if(filters != null){
				mainLoop:
				for(DynamicFeature f : features){
					if(f.getGeometry(t).get().getJTS().intersects(p)){
						for(Entry<String, Set<Object>> e : filters.entrySet()){
							for(Object o : e.getValue()){
								//System.out.println("run analysis "+f.getAttribute(e.getKey()).getValue(t)+" "+o);
								if(f.getAttribute(e.getKey()).getValue(t).equals(o)){
									process.setPoint(p);
									process.calculate(buffers, metrics, features, t);
									
									break mainLoop;
								}
							}
						}
					}
				}
			}else{
				process.setPoint(p);
				process.calculate(buffers, metrics, features, t);
			}
		}
	}

	@Override
	protected void doClose() {
		// do nothing
	}

}
