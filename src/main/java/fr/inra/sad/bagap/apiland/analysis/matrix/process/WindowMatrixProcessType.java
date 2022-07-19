package fr.inra.sad.bagap.apiland.analysis.matrix.process;

import java.io.File;
import java.io.IOException;

import org.jumpmind.symmetric.csv.CsvReader;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.process.metric.DistanceValueMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.util.Couple;
import fr.inra.sad.bagap.apiland.core.util.DistanceValueMatrix;

/**
 * modeling class of a window process type
 * @author H.Boussard
 */
public class WindowMatrixProcessType extends MatrixProcessType {
	
	/** vertical values */
	private double[] verticals;
		
	/** horizontal values */
	private double[] horizontals;
	
	/** horizontal couple */
	private double hc;
	
	/** vertical couple */
	private double vc;
	
	/** constructor */
	public WindowMatrixProcessType(boolean distanceType, Matrix m){
		super(distanceType, m);
		
		horizontals = new double[m.width()];
		for(int x=0; x<m.width(); x++){
			horizontals[x] = Raster.getNoDataValue();
		}

		verticals = new double[m.height()];
		for(int y=0; y<m.height(); y++){
			verticals[y] = Raster.getNoDataValue();
		}
	}
	
	/** constructor */
	public WindowMatrixProcessType(Matrix m){
		this(false, m);
	}
	
	/**
	 * to create a window process with a specific window 
	 * at a specific position pixel
	 * @param w the specific window
	 * @param p the position pixel
	 * @return a window process
	 */
	public WindowMatrixProcess create(Window w, Pixel p){
		return new SimpleWindowMatrixProcess(w, p, this);
	}
	
	public WindowMatrixProcess create(Window w, Pixel p, boolean selected){
		return this.create(w, p);
	}

	/**
	 * to set a value at a specific location
	 * @param x x position
	 * @param y y position
	 * @param v the value
	 * @param m 
	 */
	public void setValue(int x, int y, double v, Matrix m){
		vc = Couple.get(v, horizontals[x]);
		horizontals[x] = v;
		hc = Couple.get(v, verticals[y]);
		verticals[y] = v;
	}
	
	/** @return the horizontal couple */
	public double getHorizontal(Matrix m){
		return hc;
	}
	
	/** @return the vertical couple */
	public double getVertical(Matrix m){
		return vc;
	}
	
	@Override
	public void addMetric(MatrixMetric wm){
		super.addMetric(wm);
		if(wm instanceof DistanceValueMetric){
			File f = new File(matrix().getFile());
			String distanceMatrixFile = f.getParent()+"/"+f.getName().replace(".asc", "")+".distance";
			try {
				CsvReader cr = new CsvReader(distanceMatrixFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				DistanceValueMatrix dvm = new DistanceValueMatrix();
				
				while(cr.readRecord()){
					for(int v : matrix().values()){
						if(v != 0 && v != Raster.getNoDataValue()){
							dvm.setDistance(Integer.parseInt(cr.get("distance")), v, Double.parseDouble(cr.get(v+"")));
						}
					}
					//dvm.setDistance(cr.get("distance"), v2, distance);
				}
				cr.close();
				
				((DistanceValueMetric) wm).setDistanceMatrix(dvm);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
	}
}
