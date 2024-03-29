package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.util.Iterator;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class ClusteringEuclidianDistanceAnalysis extends ClusteringAnalysis {

	private double distance;
	
	public ClusteringEuclidianDistanceAnalysis(Matrix m, double distance, Set<Integer> interest) {
		super(m, interest);
		this.distance = distance;
	}

	@Override
	public Iterator<Pixel> getMargins(Pixel p) {
		return p.getEuclidianDistanceMargins(distance);
	}

}
