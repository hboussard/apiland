/*Copyright 2010 by INRA SAD-Paysage (Rennes)

Author: Hugues BOUSSARD 
email : hugues.boussard@rennes.inra.fr

This library is a JAVA toolbox made to create and manage dynamic landscapes.

This software is governed by the CeCILL-C license under French law and
abiding by the rules of distribution of free software.  You can  use,
modify and/ or redistribute the software under the terms of the CeCILL-C
license as circulated by CEA, CNRS and INRIA at the following URL
"http://www.cecill.info".

As a counterpart to the access to the source code and rights to copy,
modify and redistribute granted by the license, users are provided only
with a limited warranty  and the software's author,  the holder of the
economic rights,  and the successive licensors  have only  limited
liability.

In this respect, the user's attention is drawn to the risks associated
with loading,  using,  modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean  that it is complicated to manipulate, and that also
therefore means  that it is reserved for developers and experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards their
requirements in conditions enabling the security of their systems and/or
data to be ensured and,  more generally, to use and operate it in the
same conditions as regards security.

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-C license and that you accept its terms.
 */
package fr.inra.sad_paysage.apiland.core.space.impl;

import java.util.HashSet;
import java.util.Set;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.prep.PreparedPolygon;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.PixelComposite;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.PixelManager;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.RasterNull;

public class Vector2Raster {

	private static double cell;
/*
	public static <E extends DynamicElement> void convert(DynamicLayer<E> layer, String vector,
			String raster, Instant t, double cellsize) {
		
		� faire
		
		Raster.setCellSize(cellsize);

		int ncols = new Double((layer.maxX() - layer.minX()) / cellsize)
				.intValue();
		int nrows = new Double((layer.maxY() - layer.minY()) / cellsize)
				.intValue();

		boolean ok;
		Envelope env;
		PreparedPoint p = null;
		double x;
		double y;
		List<DynamicFeature> l = null;
		DynamicFeature ftemp = null;
		double vtemp = -1;
		// int modulo = nrows/100;
		int modulo = 1;
		WKTReader r = new WKTReader();
		STRtree sIndex = new STRtree();
		Iterator<E> ite = (Iterator<E>) layer.activeDeepIterator(t);
		for (E f : layer) {
			sIndex.insert(f.getGeometry(t).getVectorial().getJTS()
					.getEnvelopeInternal(), f);
		}
		sIndex.build();

		for (int j = 0; j < nrows; j++) {
			x = layer.minX();
			y = layer.maxY() - j * cellsize;
			if (j % modulo == 0) {
				env = new Envelope(new Coordinate(x, y), new Coordinate(layer
						.maxX(), y - modulo * cellsize));
				l = sIndex.query(env);
			}

			for (int i = 0; i < ncols; i++) {
				x = layer.minX() + i * cellsize;
				ok = false;
				try {
					p = new PreparedPoint((Puntal) r.read("POINT (" + x + " "
							+ y + ")"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				// System.out.println(p);
				if (ftemp != null) {
					if (p.intersects(ftemp.getGeometry(t).getVectorial().getJTS())) {
						mt.put(i, j, vtemp);
						ok = true;
					}
				}
				if (!ok) {
					for (DynamicFeature f : l) {
						if (p.intersects(f.getGeometry(t).getVectorial().getJTS())) {
							vtemp = new Double(f.getId());
							ftemp = f;
							mt.put(i, j, vtemp);
							ok = true;
							break;
						}
					}
				}

				if (!ok) {
					mt.put(i, j, 0);
				}
			}
		}
	}
*/
	public static Raster convert(Vectorial v, double cellsize) {
		if (v != null) {
			cell = cellsize;
			if (v.getJTS() instanceof Point) {
				return convertJTSPoint((Point) v.getJTS());
			} else if (v.getJTS() instanceof LineString) {
				return convertJTSLineString((LineString) v.getJTS());
			} else if (v.getJTS() instanceof Polygon) {
				return convertJTSPolygon((Polygon) v.getJTS());
			} else {
				throw new IllegalArgumentException("JTS geometry type "
						+ v.getJTS().getGeometryType()
						+ " is not managed yet !!!");
			}
		}
		return null;
	}

	private static Raster convertJTSPoint(Point g) {
		return getPixel(g.getCoordinate());
	}

	private static Raster convertJTSLineString(LineString g) {
		Coordinate[] coord = g.getCoordinates();
		Set<Coordinate> coord2;
		Coordinate c1 = coord[0];
		Coordinate c2;
		LineSegment ls;
		Set<Pixel> pixels = new HashSet<Pixel>();

		for (int i = 1; i < coord.length; i++) {
			c2 = coord[i];
			ls = new LineSegment(c1, c2);
			coord2 = getEnvelope(c1, c2);
			for (Coordinate c : coord2) {
				if (ls.distance(c) < (cell * Math.sqrt(2) / 2)) {
					pixels.add(getPixel(c));
				}
			}
			c1 = c2;
		}
		if (pixels.size() == 0) {
			return new RasterNull();
		}
		if (pixels.size() == 1) {
			return pixels.iterator().next();
		}
		return new PixelComposite(pixels);
	}

	private static Raster convertJTSPolygon(Polygon g) {
		Set<Coordinate> env = getEnvelope(g.getEnvelope().getCoordinates());
		PreparedPolygon pp = new PreparedPolygon(g);
		Point p;
		Set<Pixel> pixels = new HashSet<Pixel>();
		for (Coordinate c : env) {
			p = g.getFactory().createPoint(c);
			if (pp.intersects(p)) {
				pixels.add(getPixel(p.getCoordinate()));
			}
		}
		if (pixels.size() == 0) {
			return new RasterNull();
		}
		if (pixels.size() == 1) {
			return pixels.iterator().next();
		}
		return new PixelComposite(pixels);
	}

	private static Set<Coordinate> getEnvelope(Coordinate[] coord) {
		Set<Coordinate> l = new HashSet<Coordinate>();
		boolean init = true;
		int minX = 0, maxX = 0, minY = 0, maxY = 0;
		Pixel p;
		for (Coordinate c : coord) {
			p = getPixel(c);
			if (init) {
				minX = p.x();
				maxX = p.x();
				minY = p.y();
				maxY = p.y();
				init = false;
			} else {
				minX = Math.min(minX, p.x());
				maxX = Math.max(maxX, p.x());
				minY = Math.min(minY, p.y());
				maxY = Math.max(maxY, p.y());
			}
		}
		// System.out.println(minX+" "+maxX+" "+minY+" "+maxY);

		// for(int j=minY; j<maxY; j+=Raster.cellsize){
		// for(int i=minX; i<maxX; i+=Raster.cellsize){
		// l.add(PixelManager.get(i,j).getCentroid());
		// }
		// }

		for (int j = minY; j < maxY; j++) {
			for (int i = minX; i < maxX; i++) {
				l.add(PixelManager.get(i, j).getCentroid());
			}
		}

		// System.out.println("liste de centroids "+l);
		return l;
	}

	private static Pixel getPixel(Coordinate c) {
		return PixelManager.get(new Double(Math.floor(new Double(c.x / cell)))
				.intValue(), new Double(Math.floor(new Double(c.y / cell)))
				.intValue());
	}

	private static Set<Coordinate> getEnvelope(Coordinate c1, Coordinate c2) {
		// System.out.println("test de r�cup�ration de l'enveloppe de "+c1+"
		// "+c2);

		Pixel p1 = getPixel(c1);
		Pixel p2 = getPixel(c2);

		// System.out.println(p1);
		// System.out.println(p2);

		Set<Coordinate> l = new HashSet<Coordinate>();
		// if(p1.getX()<p2.getX()){
		// if(p1.getY()<p2.getY()){
		// for(int j=p1.getY(); j<=p2.getY(); j+=Raster.cellsize){
		// for(int i=p1.getX(); i<=p2.getX(); i+=Raster.cellsize){
		// l.add(PixelManager.get(i,j).getCentroid());
		// }
		// }
		// }else if(p1.getY()>p2.getY()){
		// for(int j=p2.getY(); j<=p1.getY(); j+=Raster.cellsize){
		// for(int i=p1.getX(); i<=p2.getX(); i+=Raster.cellsize){
		// l.add(PixelManager.get(i,j).getCentroid());
		// }
		// }
		// }else{
		// for(int i=p1.getX(); i<=p2.getX(); i+=Raster.cellsize){
		// l.add(PixelManager.get(i,p1.getY()).getCentroid());
		// }
		// }
		// }else if(p1.getX()>p2.getX()){
		// if(p1.getY()<p2.getY()){
		// for(int j=p1.getY(); j<=p2.getY(); j+=Raster.cellsize){
		// for(int i=p2.getX(); i<=p1.getX(); i+=Raster.cellsize){
		// l.add(PixelManager.get(i,j).getCentroid());
		// }
		// }
		// }else if(p1.getY()>p2.getY()){
		// for(int j=p2.getY(); j<=p1.getY(); j+=Raster.cellsize){
		// for(int i=p2.getX(); i<=p1.getX(); i+=Raster.cellsize){
		// l.add(PixelManager.get(i,j).getCentroid());
		// }
		// }
		// }else{
		// for(int i=p2.getX(); i<=p1.getX(); i+=Raster.cellsize){
		// l.add(PixelManager.get(i,p1.getY()).getCentroid());
		// }
		// }
		// }else{
		// if(p1.getY()<p2.getY()){
		// for(int j=p1.getY(); j<=p2.getY(); j+=Raster.cellsize){
		// l.add(PixelManager.get(p1.getX(),j).getCentroid());
		// }
		// }else if(p1.getY()>p2.getY()){
		// for(int j=p2.getY(); j<=p1.getY(); j+=Raster.cellsize){
		// l.add(PixelManager.get(p1.getX(),j).getCentroid());
		// }
		// }else{
		// l.add(p1.getCentroid());
		// }
		// }

		if (p1.x() < p2.x()) {
			if (p1.y() < p2.y()) {
				for (int j = p1.y(); j <= p2.y(); j++) {
					for (int i = p1.x(); i <= p2.x(); i++) {
						l.add(PixelManager.get(i, j).getCentroid());
					}
				}
			} else if (p1.y() > p2.y()) {
				for (int j = p2.y(); j <= p1.y(); j++) {
					for (int i = p1.x(); i <= p2.x(); i++) {
						l.add(PixelManager.get(i, j).getCentroid());
					}
				}
			} else {
				for (int i = p1.x(); i <= p2.x(); i++) {
					l.add(PixelManager.get(i, p1.y()).getCentroid());
				}
			}
		} else if (p1.x() > p2.x()) {
			if (p1.y() < p2.y()) {
				for (int j = p1.y(); j <= p2.y(); j++) {
					for (int i = p2.x(); i <= p1.x(); i++) {
						l.add(PixelManager.get(i, j).getCentroid());
					}
				}
			} else if (p1.y() > p2.y()) {
				for (int j = p2.y(); j <= p1.y(); j++) {
					for (int i = p2.x(); i <= p1.x(); i++) {
						l.add(PixelManager.get(i, j).getCentroid());
					}
				}
			} else {
				for (int i = p2.x(); i <= p1.x(); i++) {
					l.add(PixelManager.get(i, p1.y()).getCentroid());
				}
			}
		} else {
			if (p1.y() < p2.y()) {
				for (int j = p1.y(); j <= p2.y(); j++) {
					l.add(PixelManager.get(p1.x(), j).getCentroid());
				}
			} else if (p1.y() > p2.y()) {
				for (int j = p2.y(); j <= p1.y(); j++) {
					l.add(PixelManager.get(p1.x(), j).getCentroid());
				}
			} else {
				l.add(p1.getCentroid());
			}
		}

		System.out.println("liste de centroids " + l);
		return l;
	}

}
