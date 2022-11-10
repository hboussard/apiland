package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.util.Iterator;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class ClusteringRookAnalysis extends ClusteringAnalysis {

	public ClusteringRookAnalysis(Matrix m, Set<Integer> interest) {
		super(m, interest);
	}

	@Override
	public Iterator<Pixel> getMargins(Pixel p) {
		return p.getCardinalMargins();
	}

}
