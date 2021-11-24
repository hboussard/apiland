package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

import java.io.File;
import java.io.IOException;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.referencing.operation.transform.AffineTransform2D;

public class CoverageMatrixFactory extends MatrixFactory{

	public static CoverageMatrix readAsciiFile(String file){
		try{
			ArcGridReader agr = new ArcGridReader(new File(file));
			GridCoverage2D g = 	(GridCoverage2D)agr.read(null);
			
			double cellsize = ((AffineTransform2D)g.getGridGeometry().getGridToCRS()).getScaleX();
			
			return new CoverageMatrix(g,file);
		}catch(DataSourceException ex){
			ex.printStackTrace();
			return null;
		}catch(IOException ex){
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public Matrix create(Matrix mRef) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix create(int width, int height, double cellsize, double minX,
			double maxX, double minY, double maxY, int noData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix create(Matrix mRef, int divisor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix create(int width, int height, double cellsize, double minX, double maxX, double minY, double maxY,
			int noData, PlanarImage ref) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
