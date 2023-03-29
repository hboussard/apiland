package fr.inrae.act.bagap.raster;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import fr.inra.sad.bagap.apiland.core.space.ComplexGeometry;
import fr.inra.sad.bagap.apiland.core.space.Curve;
import fr.inra.sad.bagap.apiland.core.space.Geometry;
import fr.inra.sad.bagap.apiland.core.space.Surface;

public class RasterLineString extends Geometry {
	
	private static final long serialVersionUID = 1L;

	private int deltaI, deltaJ;
	
	private float[] datas;
	
	private int width, height;
	
	public RasterLineString(int deltaI, int deltaJ, int width, int height, float[] datas){
		this.deltaI = deltaI;
		this.deltaJ = deltaJ;
		this.width = width;
		this.height = height;
		this.datas = datas;
	}
	
	public static RasterLineString getRasterLineString(LineString line, double minx, double maxy, double cellsize){
		Envelope internal = line.getEnvelopeInternal();
		
		double iminx = internal.getMinX();
		double imaxx = internal.getMaxX();
		double iminy = internal.getMinY();
		double imaxy = internal.getMaxY();
		
		int deltaI = new Double((iminx - minx)/cellsize).intValue();
		int deltaJ = new Double((maxy-imaxy)/cellsize).intValue();
		
		double eminx = minx + cellsize*deltaI;
		double emaxy = maxy - cellsize*deltaJ;
		
		int width = new Double((imaxx - eminx)/cellsize).intValue() + 1;
		int height = new Double((emaxy - iminy)/cellsize).intValue() + 1;
		
		//System.out.println(deltaI+" "+deltaJ+" "+width+" "+height);
		
		float[] datas = new float[width * height];
		
		//PreparedLineString pp = new PreparedLineString(line);
		GeometryFactory gf = new GeometryFactory();
		Point p;
		double x, y;
		for(int j=0; j<height; j++){
			y = emaxy - (cellsize / 2.0) - j * cellsize;
			for(int i=0; i<width; i++){
				x = eminx + (cellsize / 2.0) + i * cellsize;
				p = gf.createPoint(new Coordinate(x, y));
					
				if(line.distance(p)<=((cellsize/2)*Math.sqrt(2))){
					datas[j*width+i] = 1;
				}
			}	
		}
		
		return new RasterLineString(deltaI, deltaJ, width, height, datas);
	}

	public int getDeltaI() {
		return deltaI;
	}

	public int getDeltaJ() {
		return deltaJ;
	}

	public float[] getDatas() {
		return datas;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	@Override
	public Geometry smooth() {
		return this;
	}

	@Override
	public Geometry addGeometry(Geometry g) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Geometry addPoint(fr.inra.sad.bagap.apiland.core.space.Point g) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Geometry addCurve(Curve g) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Geometry addSurface(Surface g) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <G extends Geometry> Geometry addComplexGeometry(ComplexGeometry<G> g) {
		throw new UnsupportedOperationException();
	}
	
}
