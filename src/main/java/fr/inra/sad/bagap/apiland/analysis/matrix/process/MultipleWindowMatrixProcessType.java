package fr.inra.sad.bagap.apiland.analysis.matrix.process;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class MultipleWindowMatrixProcessType extends WindowMatrixProcessType {

	public MultipleWindowMatrixProcessType(boolean distanceType, Matrix m) {
		super(distanceType, m);
	}
	
	@Override
	public MultipleWindowMatrixProcess create(Window w, Pixel p){
		return new MultipleWindowMatrixProcess(w, p, this);
	}

}
