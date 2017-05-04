package fr.inra.sad_paysage.agriconnect.model.metric;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import fr.inra.sad_paysage.apiland.analysis.VariableManager;
import fr.inra.sad_paysage.apiland.analysis.vector.metric.VectorMetric;
import fr.inra.sad_paysage.apiland.core.composition.Composition;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.capfarm.model.Cover;
import fr.inra.sad_paysage.apiland.capfarm.model.CoverGroup;

public class EdgeLengthMetric extends VectorMetric {

	private Object code1, code2;
	
	//private Map<Point, Set<Polygon>> bufferCollection;
	
	//private Map<Point, Map<Polygon, Map<Polygon, Geometry>>> intersectionCollection;
	
	public EdgeLengthMetric(Object code1, Object code2) {
		super(VariableManager.get("E_"+code1+"-"+code2));
		this.code1 = code1;
		this.code2 = code2;
		//bufferCollection = new HashMap<Point, Set<Polygon>>();
		//intersectionCollection = new HashMap<Point, Map<Polygon, Map<Polygon, Geometry>>>();
	}

	private Set<Polygon> getBuffers(Point point, Set<Polygon> polygons){
		/*
		if(!bufferCollection.containsKey(point)){
			//System.out.println("calcul du buffer...");
			bufferCollection.put(point, calculateBuffers(polygons));
			intersectionCollection.put(point, new HashMap<Polygon, Map<Polygon, Geometry>>());
			for(Polygon b : bufferCollection.get(point)){
				intersectionCollection.get(point).put(b, new HashMap<Polygon, Geometry>());
			}
		}
		return bufferCollection.get(point);
		*/
		return calculateBuffers(polygons);
	}
	
	private Set<Polygon> calculateBuffers(Set<Polygon> polygons){
		Set<Polygon> buffers = new HashSet<Polygon>();
		Polygon buffer;
		for(Polygon p : polygons){
			buffer = (Polygon) p.buffer(0.5);
			buffer.setUserData(p.getUserData());
			buffers.add(buffer); 
		}
		return buffers;
	}
	
	private Geometry getIntersection(Point point, Polygon buffer1, Polygon buffer2){
	/*
		if(!intersectionCollection.get(point).get(buffer1).containsKey(buffer2)){
			Geometry intersection = buffer1.intersection(buffer2);
			intersectionCollection.get(point).get(buffer1).put(buffer2, intersection);
			intersectionCollection.get(point).get(buffer2).put(buffer1, intersection);
			return intersection;
		}
		return intersectionCollection.get(point).get(buffer1).get(buffer2);
		*/
		return buffer1.intersection(buffer2);
	}
	
	@Override
	public void doCalculate(Point point, Set<Polygon> polygons, Instant t) {
		
		Set<Polygon> buffers = getBuffers(point, polygons);
				
		Geometry i;
		value = 0.0;
		
		if(code1 instanceof CoverGroup && code2 instanceof CoverGroup){
			if(code1 == code2){	
				Set<Polygon> ever = new HashSet<Polygon>();
				for(Polygon p1 : buffers){
					if(((Composition) p1.getUserData()).getAttribute("cover").isActive(t)
							&& ((CoverGroup) code1).contains((Cover) ((Composition) p1.getUserData()).getAttribute("cover").getValue(t))){
						ever.add(p1);
						//System.out.println(p1.toString());
						for(Polygon p2 : buffers){
							if(!ever.contains(p2)
									&& ((Composition) p2.getUserData()).getAttribute("cover").isActive(t)
									&& ((CoverGroup) code2).contains((Cover) ((Composition) p2.getUserData()).getAttribute("cover").getValue(t))){
								if(p1.intersects(p2)){
									i = getIntersection(point, p1, p2);
									//i = p1.intersection(p2);
									if(i instanceof Polygon || i instanceof MultiPolygon){
										value += i.getLength()/2;
									}else{
										System.out.println(i.getClass());
									}
								}
							}
						}
					}
				}
			}else{
				for(Polygon p1 : buffers){
					if(((Composition) p1.getUserData()).getAttribute("cover").isActive(t)
							&& ((CoverGroup) code1).contains((Cover) ((Composition) p1.getUserData()).getAttribute("cover").getValue(t))){
						for(Polygon p2 : buffers){
							if(((Composition) p2.getUserData()).getAttribute("cover").isActive(t)
									&& ((CoverGroup) code2).contains((Cover) ((Composition) p2.getUserData()).getAttribute("cover").getValue(t))){
								if(p1.intersects(p2)){
									i = getIntersection(point, p1, p2);
									//i = p1.intersection(p2);
									if(i instanceof Polygon || i instanceof MultiPolygon){
										value += i.getLength()/2;
									}else{
										System.out.println(i.getClass());
									}
								}
							}
						}
					}
				}
			}
		}else if(code1 instanceof CoverGroup){
			for(Polygon p1 : buffers){
				if(((Composition) p1.getUserData()).getAttribute("cover").isActive(t)
						&& ((CoverGroup) code1).contains((Cover) ((Composition) p1.getUserData()).getAttribute("cover").getValue(t))){
					for(Polygon p2 : buffers){
						if(((Composition) p2.getUserData()).getAttribute("cover").isActive(t)
								&& ((Composition) p2.getUserData()).getAttribute("cover").getValue(t).equals(code2)){
							if(p1.intersects(p2)){
								i = getIntersection(point, p1, p2);
								//i = p1.intersection(p2);
								if(i instanceof Polygon || i instanceof MultiPolygon){
									value += i.getLength()/2;
								}else{
									System.out.println(i.getClass());
								}
							}
						}
					}
				}
			}
		}else{
			if(code1 == code2){	
				
				Set<Polygon> ever = new HashSet<Polygon>();
				for(Polygon p1 : buffers){
					if(((Composition) p1.getUserData()).getAttribute("cover").isActive(t) 
							&& ((Composition) p1.getUserData()).getAttribute("cover").getValue(t).equals(code1)){
						ever.add(p1);
						for(Polygon p2 : buffers){
							if(!ever.contains(p2)
									&& ((Composition) p2.getUserData()).getAttribute("cover").isActive(t)
									&& ((Composition) p2.getUserData()).getAttribute("cover").getValue(t).equals(code1)){
								if(p1.intersects(p2)){
									i = getIntersection(point, p1, p2);
									//i = p1.intersection(p2);
									if(i instanceof Polygon || i instanceof MultiPolygon){
										value += i.getLength()/2;
									}else{
										System.out.println(i.getClass());
									}
								}
							}
						}
					}
				}
				
			}else{
				
				for(Polygon p1 : buffers){
					if(((Composition) p1.getUserData()).getAttribute("cover").isActive(t)
							&& ((Composition) p1.getUserData()).getAttribute("cover").getValue(t).equals(code1)){
						for(Polygon p2 : buffers){
							if(((Composition) p2.getUserData()).getAttribute("cover").isActive(t)
									&& ((Composition) p2.getUserData()).getAttribute("cover").getValue(t).equals(code2)){
								if(p1.intersects(p2)){
									i = getIntersection(point, p1, p2);
									//i = p1.intersection(p2);
									if(i instanceof Polygon || i instanceof MultiPolygon){
										value += i.getLength()/2;
									}else{
										System.out.println(i.getClass());
									}
								}
							}
						}
					}
				}
			}
		}
		
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
		
		//System.out.println(t.year()+" edgelength = "+value);
	} 
	
}
