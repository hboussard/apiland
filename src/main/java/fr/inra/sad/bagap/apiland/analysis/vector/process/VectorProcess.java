package fr.inra.sad.bagap.apiland.analysis.vector.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.prep.PreparedPolygon;

import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessState;
import fr.inra.sad.bagap.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad.bagap.apiland.core.element.DynamicFeature;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class VectorProcess extends Process<VectorMetric> {

	private Point point;
	
	private Set<Polygon> polygons;
	
	private Map<Point, Set<Polygon>> references;
	
	public VectorProcess(VectorProcessType pt){
		super(pt);
		references = new HashMap<Point, Set<Polygon>>();
	}
	
	public VectorProcess(Point p, VectorProcessType pt){
		super(pt);
		this.point = p;
	}
	
	public void setPoint(Point p){
		this.point = p;
		if(references.containsKey(p)){
			polygons = references.get(p);
		}else{
			polygons = null;
		}
	}
	
	public double x(){
		return point.getCoordinate().x;
	}
	
	public double y(){
		return point.getCoordinate().y;
	}
	
	public Point point(){
		return point;
	}
	
	@Override
	public int compareTo(Process<VectorMetric> other) {
		if(other instanceof VectorProcess){
			if(((VectorProcess) other).point.getCoordinate().y > this.point.getCoordinate().y){
				return -1;
			}else if(((VectorProcess) other).point.getCoordinate().y < this.point.getCoordinate().y){
				return 1;
			}else{
				if(((VectorProcess) other).point.getCoordinate().x > this.point.getCoordinate().x){
					return -1;
				}else if(((VectorProcess) other).point.getCoordinate().x < this.point.getCoordinate().x){
					return 1;
				}
			}
		}
		return 1;
	}

	@Override
	public VectorProcessType processType(){
		return (VectorProcessType) super.processType();
	}
	
	public void unCalculate(Set<Double> buffers, Set<VectorMetric> metrics){
		for(double buffer : buffers){
			for(VectorMetric vm : metrics){
				vm.unCalculate(this, buffer+"_");
			}
		}
		setState(ProcessState.DONE);
	}
	
	public void calculate(Set<Double> buffers, Set<VectorMetric> metrics, List<DynamicFeature> features, Instant t){
		for(double buffer : buffers){
			//Set<Polygon> polygons = getBuffer(features, t, point, buffer);
			getBuffer(features, t, point, buffer);
			//createBuffer(features, t, point, buffer);
			for(VectorMetric vm : metrics){
				vm.calculate(this, buffer+"_", point, polygons, t);
			}
			
			polygons = null;
		}
		setState(ProcessState.DONE);
	}
	
	public void calculate(Set<VectorMetric> metrics, List<DynamicFeature> features, Instant t){
		
		polygons = new HashSet<Polygon>();
		Polygon fp;
		for(DynamicFeature f : features){
			fp = (Polygon) f.getDefaultRepresentation().getGeometry(t).get().getJTS();
			fp.setUserData(f.getComposition());
			polygons.add(fp);
		}
		for(VectorMetric vm : metrics){
			vm.calculate(this, "", point, polygons, t);
		}
			
		polygons = null;
		
		setState(ProcessState.DONE);
	}
	
	private void getBuffer(List<DynamicFeature> features, Instant t, Point p, double b){
		if(polygons == null){
			createBuffer(features, t, p, b);
		}
	}
	
	private void createBuffer(List<DynamicFeature> features, Instant t, Point p, double b){
		//System.out.println("création");
		polygons = new HashSet<Polygon>();
		Polygon buffer = (Polygon) p.buffer(b);
		PreparedPolygon pp = new PreparedPolygon(buffer);
		Polygon gt;
		Geometry fp, g;
		for(DynamicFeature f : features){
			fp = f.getDefaultRepresentation().getGeometry(t).get().getJTS();
			if(pp.intersects(fp)){
				//System.out.print("intersects ");
				if(pp.overlaps(fp)){
					//System.out.println("overlaps");
					g = buffer.intersection(fp);
					if(g instanceof Polygon){
						g.setUserData(f.getComposition());
						polygons.add((Polygon) g);
					}else if(g instanceof MultiPolygon){
						for(int i=0; i<((MultiPolygon) g).getNumGeometries(); i++){
							gt = (Polygon) ((MultiPolygon) g).getGeometryN(i);
							gt.setUserData(f.getComposition());
							polygons.add(gt);
						}
					}/*else if(g instanceof LineString){
						// do nothing
					}else if(g instanceof Point){
						// do nothing
					}else{
						throw new IllegalArgumentException(g.getClass()+"");
					}*/
				}else if(pp.contains(fp)){
					//System.out.println("contains");
					fp.setUserData(f.getComposition());
					polygons.add((Polygon) fp);
				}else if(pp.within(fp)){
					//System.out.println("within");
					buffer.setUserData(f.getComposition());
					polygons.add((Polygon) buffer);
					break;
				}/*else{
				//System.out.println("erreur !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				//System.out.println(pp.covers(fp));
				//System.out.println(pp.crosses(fp));
				//System.out.println(pp.containsProperly(fp));
				//System.out.println(pp.coveredBy(fp));
				//System.out.println(pp.touches(fp));
				}*/
			}
		}
		//references.put(p, polygons);
	}

}
