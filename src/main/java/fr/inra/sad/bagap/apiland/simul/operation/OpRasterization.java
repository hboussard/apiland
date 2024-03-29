package fr.inra.sad.bagap.apiland.simul.operation;

import java.util.Iterator;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Puntal;
import org.locationtech.jts.geom.prep.PreparedPoint;
import org.locationtech.jts.index.strtree.STRtree;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.cluster.ClusteringQueenAnalysis;
import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.element.DynamicFeature;
import fr.inra.sad.bagap.apiland.core.element.DynamicLayer;
import fr.inra.sad.bagap.apiland.core.space.Geometry;
import fr.inra.sad.bagap.apiland.core.space.GeometryFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RasterComposite;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.ArrayMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.structure.Representation;
import fr.inra.sad.bagap.apiland.core.time.Instant;

public class OpRasterization extends OpTranslation {

	private static final long serialVersionUID = 1L;

	private double cellsize;
	
	private double minX = -1, maxX = -1, minY = -1, maxY = -1;
	
	private String rasterRepresentation = "raster";
	
	public OpRasterization(OpRasterizationType type) {
		super(type);
		cellsize = type.getCellSize();
		if(type.minX() != -1 && type.maxX() != -1 && type.minY() != -1 && type.maxY() != -1){
			minX = type.minX();
			maxX = type.maxX();
			minY = type.minY();
			maxY = type.maxY();
		}
		rasterRepresentation = type.raster();
	}
	
	public double getCellSize() {
		return cellsize;
	}

	@Override
	public boolean make(Instant t, DynamicElement... e) {
		//System.out.println("je rasterise a l'instant "+t);
		
		if(minX != -1 && maxX != -1 && minY != -1 && maxY != -1){
			getRasterRepresentation(e[0], e[0].getRepresentation(getRepresentation()), t, getCellSize(), minX, maxX, minY, maxY);
		}else{
			getRasterRepresentation(e[0], e[0].getRepresentation(getRepresentation()), t, getCellSize());
		}
		
		return true;
	}
	
	private void getRasterRepresentation(DynamicElement element, Representation vectorRepresentation, Instant t, double cellsize){
		Iterator<DynamicFeature> ite = ((DynamicLayer<DynamicElement>)element).deepIterator();
		DynamicFeature f;
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		while(ite.hasNext()){
			f = ite.next();
			minX = Math.min(f.minX(), minX);
			minY = Math.min(f.minY(), minY);
			maxX = Math.max(f.maxX(), maxX);
			maxY = Math.max(f.maxY(), maxY);
		}
		
		getRasterRepresentation(element, vectorRepresentation, t, cellsize, minX, maxX, minY, maxY);
	}
	
	private void getRasterRepresentation(DynamicElement element, Representation<?> vectorRepresentation, Instant t, double cellsize,
			double minX, double maxX, double minY, double maxY){
		Raster.setCellSize(cellsize);
		 
		//Raster representation = new RasterComposite();
		
		int ncols = new Double((Math.floor((maxX - minX) / Raster.getCellSize())) /*+ 1*/).intValue();
		int nrows = new Double((Math.floor((maxY - minY) / Raster.getCellSize())) /*+ 1*/).intValue();
		
		Matrix mt = ArrayMatrixFactory.get().create(ncols, nrows, cellsize, minX, maxX, minY, maxY, Raster.getNoDataValue());
		
		boolean ok;
		Envelope env;
		PreparedPoint p = null;
		double x;
		double y;
		DynamicFeature f;
		List<DynamicFeature> l = null;
		DynamicFeature dftemp = null;
		int vtemp = Raster.getNoDataValue();
		int modulo = 1;
		WKTReader wktr = new WKTReader();
		STRtree sIndex = new STRtree();
		Iterator<DynamicFeature> ite = ((DynamicLayer<DynamicElement>)element).deepIterator();
		while(ite.hasNext()){
			f = ite.next();
			sIndex.insert(f.getGeometry(t).get().getJTS().getEnvelopeInternal(), f);
		}

		sIndex.build();
		
		for(int j=0; j<nrows; j++){
			x = minX;
			y = maxY /*+ (cellsize - (maxY - minY)%cellsize)*/ - (1.0/2 + j)*cellsize;
			if(j%modulo == 0){
				env = new Envelope(new Coordinate(x - modulo*cellsize, y), new Coordinate(maxX, y - modulo*cellsize));
				//env = new Envelope(new Coordinate(0, y), new Coordinate(maxX, y - modulo*cellsize));
				//env = new Envelope(new Coordinate(-100000, -1000000), new Coordinate(100000000, 1000000000));
				l = sIndex.query(env);
			}
					
			for(int i=0; i<ncols; i++){
				x = minX + (1.0/2 + i)*cellsize;
				ok = false;
				try {
					p = new PreparedPoint((Puntal) wktr.read("POINT ("+x+" "+y+")"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(dftemp != null){
					if(p.intersects(dftemp.getGeometry(t).get().getJTS())){
						mt.put(i, j, vtemp);
						ok = true;
					}
				}
				if(!ok){
					for(DynamicFeature df : l){
						if(p.intersects(df.getGeometry(t).get().getJTS())){
							vtemp = new Integer(df.getId().toString());
							dftemp = df;
							mt.put(i, j, vtemp);
							ok = true;
							break;
						}
					}
				}
					
				if(!ok){
					mt.put(i, j, Raster.getNoDataValue());
				}
			}
		}
		
		ClusteringAnalysis ca = new ClusteringQueenAnalysis(mt, null);
		ca.allRun();
		RasterComposite raster = (RasterComposite) ca.getResult();
		
		String id;
		Geometry g;
		for(Raster r : raster.getRasters()){
			id = new Integer((int) mt.get(r.iterator().next())).toString();
			ite = ((DynamicLayer<DynamicElement>) element).deepIterator();
			while(ite.hasNext()){
				f = ite.next();
				if(f.getId().equalsIgnoreCase(id)){
					g = GeometryFactory.create(r);
					f.getRepresentation(rasterRepresentation).setGeometry(t, g);
					//representation.addGeometryImpl(g.get());
					break;
				}
			}
		} 
		
		//element.getStructure().getRepresentation("raster").addRepresentation(representation);
	}

}
