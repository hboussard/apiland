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
package fr.inra.sad_paysage.apiland.core.space;

import fr.inra.sad_paysage.apiland.core.space.impl.GeometryImpl;
import fr.inra.sad_paysage.apiland.core.space.impl.Vectorial;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.RasterComposite;

public class GeometryFactory {
	
	public static final com.vividsolutions.jts.geom.GeometryFactory factory = new com.vividsolutions.jts.geom.GeometryFactory();
	
	public static Geometry create(com.vividsolutions.jts.geom.Geometry g){
		if(g instanceof com.vividsolutions.jts.geom.Point){
			return create((com.vividsolutions.jts.geom.Point)g);
		}else if(g instanceof com.vividsolutions.jts.geom.LineString){
			return create((com.vividsolutions.jts.geom.LineString)g);
		}else if(g instanceof com.vividsolutions.jts.geom.Polygon){
			return create((com.vividsolutions.jts.geom.Polygon)g);
		}else if(g instanceof com.vividsolutions.jts.geom.MultiPoint){
			return create((com.vividsolutions.jts.geom.MultiPoint)g);
		}else if(g instanceof com.vividsolutions.jts.geom.MultiLineString){
			return create((com.vividsolutions.jts.geom.MultiLineString)g);
		}else if(g instanceof com.vividsolutions.jts.geom.MultiPolygon){
			return create((com.vividsolutions.jts.geom.MultiPolygon)g);
		}else if(g instanceof com.vividsolutions.jts.geom.GeometryCollection){
			return create((com.vividsolutions.jts.geom.GeometryCollection)g);
		}
		throw new IllegalArgumentException();
	}
	
	public static Surface create(com.vividsolutions.jts.geom.Polygon g){
		return new Surface(new Vectorial(g));
	}
	
	public static MultiCurve create(com.vividsolutions.jts.geom.LineString g){
		return new MultiCurve(new Vectorial(g));
	}
	
	public static Point create(com.vividsolutions.jts.geom.Point g){
		return new Point(new Vectorial(g));
	}
	
	public static MultiSurface create(com.vividsolutions.jts.geom.MultiPolygon g){
		return new MultiSurface(new Vectorial(g));
	}
	
	public static MultiCurve create(com.vividsolutions.jts.geom.MultiLineString g){
		return new MultiCurve(new Vectorial(g));
	}
	
	public static MultiPoint create(com.vividsolutions.jts.geom.MultiPoint g){
		return new MultiPoint(new Vectorial(g));
	}
	
	public static ComplexGeometry<Geometry> create(com.vividsolutions.jts.geom.GeometryCollection g){
		return new ComplexGeometry<Geometry>(new Vectorial(g));
	}
	
	public static Geometry create(Raster r){
		return new Surface(r);
	}
	
	public static Geometry create(RasterComposite r){
		return new MultiSurface(r);
	}
	
	public static Geometry create(GeometryImpl impl){
		if(impl instanceof RasterComposite){
			return create((RasterComposite)impl);
		}else if(impl instanceof Raster){
			return create((Raster)impl);
		}
		throw new IllegalArgumentException();
	}
	
}