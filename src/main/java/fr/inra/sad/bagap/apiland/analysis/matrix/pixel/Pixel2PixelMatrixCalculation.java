package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;

import fr.inra.sad.bagap.apiland.analysis.matrix.MatrixCalculation;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixFactory;

public abstract class Pixel2PixelMatrixCalculation extends MatrixCalculation {
	
	public Pixel2PixelMatrixCalculation(Matrix... m){
		super(m);
	}
	
	@Override
	public void doRun(){
		Matrix treat = MatrixFactory.get(matrix().getType()).create(matrix().width(), matrix().height(), matrix().cellsize(), matrix().minX(), matrix().maxX(), matrix().minY(), matrix().maxY(), matrix().noDataValue());
		//Matrix treat = MatrixFactory.get(matrix().getType()).create(matrix());
		int total = matrix().width() * matrix().height();
		for(Pixel p : matrix()){
			treat.put(p, treatPixel(p));
			updateProgression(total);
		}
		setResult(treat);
	}
	
	protected abstract double treatPixel(Pixel p);
	
	@Override
	protected void doInit() {}

	@Override
	protected void doClose() {}
	
}
