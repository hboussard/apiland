package fr.inra.sad.bagap.apiland.analysis.vector.window;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.index.strtree.STRtree;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad.bagap.apiland.analysis.vector.process.VectorProcess;
import fr.inra.sad.bagap.apiland.analysis.vector.process.VectorProcessType;
import fr.inra.sad.bagap.apiland.core.element.DynamicFeature;
import fr.inra.sad.bagap.apiland.core.element.DynamicLayer;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class SlidingWindowVectorAnalysis extends WindowVectorAnalysis {
	
	private double displacement;
	
	private STRtree sIndex;
	
	private double minX, maxX, minY, maxY;
	
	private Geometry geomFilter;
	
	public SlidingWindowVectorAnalysis(DynamicLayer<?> layer, Set<Double> buffers,
			Set<VectorMetric> metrics, double minRate,
			Map<String, Set<Object>> filters, Map<String, Set<Object>> unfilters, Geometry geomFilter,
			double displacement, Instant t, VectorProcessType processType,
			double minX, double maxX, double minY, double maxY) {
		super(layer, buffers, metrics, minRate, filters, unfilters, t, processType);
		this.displacement = displacement;
		
		this.geomFilter = geomFilter;
		
		if(minX == -1 && maxX == -1 && minY == -1 && maxY == -1){
			this.minX = layer.minX();
			this.maxX = layer.maxX();
			this.minY = layer.minY();
			this.maxY = layer.maxY();
		}else{
			this.minX = minX;
			this.maxX = maxX;
			this.minY = minY;
			this.maxY = maxY;
		}
	}
	
	public double displacement(){
		return displacement;
	}

	@Override
	protected void doInit() {
		sIndex = new STRtree();
		Iterator<DynamicFeature> ite = layer.activeDeepIterator(t);
		DynamicFeature f;
		while(ite.hasNext()){
			f = ite.next();
			sIndex.insert(f.getGeometry(t).get().getJTS().getEnvelopeInternal(), f);
		}
		sIndex.build();
	}
	
	@Override
	protected void doRun() {
		try {
			int ncols = new Double((Math.floor((maxX - minX) / displacement)) + 1).intValue();
			int nrows = new Double((Math.floor((maxY - minY) / displacement)) + 1).intValue();
			
			double x;
			double y;
			WKTReader r = new WKTReader();
			double yorigin;
			if (maxY - minY % displacement == 0) {
				yorigin = minY + Math.floor((maxY - minY) / displacement) * displacement;
			} else {
				yorigin = minY + (Math.floor((maxY - minY) / displacement) + 1) * displacement;
			}
			
			double tempY = -1;
			List<DynamicFeature> features = null;
			Point point;
			int modulo = getMaxBuffer();
			VectorProcess process;
			for (int j=0; j<nrows; j++) {
				x = minX + (displacement / 2);
				y = yorigin - (displacement / 2) - j * displacement;
				for (int i=0; i<ncols; i++) {
					x = minX + (displacement / 2) + i * displacement;
					point = (Point) r.read("POINT (" + x	+ " " + y + ")");
					process = processType.create(point);
					//System.out.println(x+" "+y);
					if(geomFilter == null || point.intersects(geomFilter)){
						if(tempY == -1 || Math.abs(tempY-y) > modulo){
							tempY = y;
							features = sIndex.query(new Envelope(new Coordinate(minX, y + modulo), new Coordinate(maxX, y - 2*modulo)));
						}
						if(filters != null){
							mainLoop:
							for(DynamicFeature f : features){
								if(f.getGeometry(t).get().getJTS().intersects(point)){
									for(Entry<String, Set<Object>> e : filters.entrySet()){
										for(Object o : e.getValue()){
											if(f.getAttribute(e.getKey()).getValue(t).equals(o)){
												process = processType.create(point);
												process.calculate(buffers, metrics, features, t);
												break mainLoop;
											}
										}
									}
								}
							}
						}else{
							process.calculate(buffers, metrics, features, t);
						}
					}else{
						process.unCalculate(buffers, metrics);
					}
				}
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/*
	@Override
	protected void doRun() {
		try {
			int ncols = new Double((Math.floor((layer.maxX() - layer.minX()) / displacement)) + 1).intValue();
			int nrows = new Double((Math.floor((layer.maxY() - layer.minY()) / displacement)) + 1).intValue();
			
			double x;
			double y;
			WKTReader r = new WKTReader();
			double yorigin;
			if (layer.maxY() - layer.minY() % displacement == 0) {
				yorigin = layer.minY() + Math.floor((layer.maxY() - layer.minY()) / displacement) * displacement;
			} else {
				yorigin = layer.minY() + (Math.floor((layer.maxY() - layer.minY()) / displacement) + 1) * displacement;
			}

			double tempY = -1;
			List<DynamicFeature> features = null;
			Point point;
			int modulo = getMaxBuffer();
			VectorProcess process;
			for (int j=0; j<nrows; j++) {
				x = layer.minX() + (displacement / 2);
				y = yorigin - (displacement / 2) - j * displacement;
				for (int i=0; i<ncols; i++) {
					x = layer.minX() + (displacement / 2) + i * displacement;
					point = (Point) r.read("POINT (" + x	+ " " + y + ")");
					
					if(Math.abs(tempY-y) > modulo){
						tempY = y;
						features = sIndex.query(new Envelope(new Coordinate(layer.minX(), y + modulo), new Coordinate(layer.maxX(), y - 2*modulo)));
					}
					
					process = processType.create(point);
					
					process.calculate(buffers, metrics, features, t);
				}
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}*/

	@Override
	protected void doClose() {
		//layer = null;
		t = null;
		sIndex = null;
	}

}
