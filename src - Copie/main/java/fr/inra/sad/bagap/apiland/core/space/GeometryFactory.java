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
package fr.inra.sad.bagap.apiland.core.space;

import fr.inra.sad.bagap.apiland.core.space.impl.GeometryImpl;
import fr.inra.sad.bagap.apiland.core.space.impl.Vectorial;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RasterComposite;

public class GeometryFactory {
	
	public static final org.locationtech.jts.geom.GeometryFactory factory = new org.locationtech.jts.geom.GeometryFactory();
	
	public static Geometry create(org.locationtech.jts.geom.Geometry g){
		if(g instanceof org.locationtech.jts.geom.Point){
			return create((org.locationtech.jts.geom.Point)g);
		}else if(g instanceof org.locationtech.jts.geom.LineString){
			return create((org.locationtech.jts.geom.LineString)g);
		}else if(g instanceof org.locationtech.jts.geom.Polygon){
			return create((org.locationtech.jts.geom.Polygon)g);
		}else if(g instanceof org.locationtech.jts.geom.MultiPoint){
			return create((org.locationtech.jts.geom.MultiPoint)g);
		}else if(g instanceof org.locationtech.jts.geom.MultiLineString){
			return create((org.locationtech.jts.geom.MultiLineString)g);
		}else if(g instanceof org.locationtech.jts.geom.MultiPolygon){
			return create((org.locationtech.jts.geom.MultiPolygon)g);
		}else if(g instanceof org.locationtech.jts.geom.GeometryCollection){
			return create((org.locationtech.jts.geom.GeometryCollection)g);
		}
		throw new IllegalArgumentException();
	}
	
	public static Surface create(org.locationtech.jts.geom.Polygon g){
		return new Surface(new Vectorial(g));
	}
	
	public static MultiCurve create(org.locationtech.jts.geom.LineString g){
		return new MultiCurve(new Vectorial(g));
	}
	
	public static Point create(org.locationtech.jts.geom.Point g){
		return new Point(new Vectorial(g));
	}
	
	public static MultiSurface create(org.locationtech.jts.geom.MultiPolygon g){
		return new MultiSurface(new Vectorial(g));
	}
	
	public static MultiCurve create(org.locationtech.jts.geom.MultiLineString g){
		return new MultiCurve(new Vectorial(g));
	}
	
	public static MultiPoint create(org.locationtech.jts.geom.MultiPoint g){
		return new MultiPoint(new Vectorial(g));
	}
	
	public static ComplexGeometry<Geometry> create(org.locationtech.jts.geom.GeometryCollection g){
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
