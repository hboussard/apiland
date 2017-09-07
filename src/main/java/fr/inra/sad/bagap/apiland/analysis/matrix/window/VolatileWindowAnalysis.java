package fr.inra.sad.bagap.apiland.analysis.matrix.window;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public interface VolatileWindowAnalysis {

	Pixel next(Pixel pixel);
	
}
