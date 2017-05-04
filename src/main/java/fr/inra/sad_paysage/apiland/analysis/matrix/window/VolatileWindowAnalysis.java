package fr.inra.sad_paysage.apiland.analysis.matrix.window;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;

public interface VolatileWindowAnalysis {

	Pixel next(Pixel pixel);
	
}
