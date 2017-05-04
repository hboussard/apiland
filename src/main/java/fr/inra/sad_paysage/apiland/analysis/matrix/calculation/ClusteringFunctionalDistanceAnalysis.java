package fr.inra.sad_paysage.apiland.analysis.matrix.calculation;

import java.util.Iterator;
import java.util.Set;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class ClusteringFunctionalDistanceAnalysis extends ClusteringAnalysis {

	private double distance;
	
	private boolean map;
	
	private Friction frictionMap;
	
	private Matrix frictionMat;
	
	public ClusteringFunctionalDistanceAnalysis(Matrix m, double distance, Set<Integer> interest, Friction friction) {
		super(m, interest);
		this.distance = distance;
		this.frictionMap = friction;
		this.map = true;
	}
	
	public ClusteringFunctionalDistanceAnalysis(Matrix m, double distance, Set<Integer> interest, Matrix friction) {
		super(m, interest);
		this.distance = distance;
		this.frictionMat = friction;
		this.map = false;
	}

	@Override
	public Iterator<Pixel> getMargins(Pixel p) {
		if(map){
			return p.getFunctionalDistanceMargins(distance, matrix(), frictionMap);
		}
		return p.getFunctionalDistanceMargins(distance, matrix(), frictionMat);
	}

}
