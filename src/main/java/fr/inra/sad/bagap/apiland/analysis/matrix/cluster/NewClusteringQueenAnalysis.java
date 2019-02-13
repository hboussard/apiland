package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.util.Iterator;
import java.util.Collection;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class NewClusteringQueenAnalysis extends NewClusteringAnalysis {

	public NewClusteringQueenAnalysis(Matrix m, Collection<Integer> interest, String outputFolder, String name) {
		super(m, interest, outputFolder, name);
	}

	@Override
	public Iterator<Pixel> getMargins(Pixel p) {
		return p.getMargins();
	}

}
