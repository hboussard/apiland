package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class NewClusteringRookAnalysis extends NewClusteringAnalysis {

	public NewClusteringRookAnalysis(Matrix m, Collection<Integer> interest, List<Double> minimumAreas, double minimumTotal, String outputFolder, String name) {
		super(m, interest, minimumAreas, minimumTotal, outputFolder, name);
	}

	@Override
	public Iterator<Pixel> getMargins(Pixel p) {
		return p.getCardinalMargins();
	}

}
