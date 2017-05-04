package fr.inra.sad_paysage.apiland.analysis.matrix.calculation;

import java.util.Iterator;
import java.util.Set;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class ClusteringQueenAnalysis extends ClusteringAnalysis {

	public ClusteringQueenAnalysis(Matrix m, Set<Integer> interest) {
		super(m, interest);
	}

	@Override
	public Iterator<Pixel> getMargins(Pixel p) {
		return p.getMargins();
	}

}
