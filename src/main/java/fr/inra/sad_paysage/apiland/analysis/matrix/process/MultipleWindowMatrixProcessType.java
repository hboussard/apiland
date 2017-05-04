package fr.inra.sad_paysage.apiland.analysis.matrix.process;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.window.Window;

public class MultipleWindowMatrixProcessType extends WindowMatrixProcessType {

	public MultipleWindowMatrixProcessType(Matrix m) {
		super(m);
	}
	
	@Override
	public MultipleWindowMatrixProcess create(Window w, Pixel p){
		return new MultipleWindowMatrixProcess(w, p, this);
	}

}
