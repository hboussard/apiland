package fr.inra.sad.bagap.apiland.analysis.matrix;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public abstract class MatrixCalculation extends MatrixAnalysis {
	
	public MatrixCalculation(Matrix... m){
		super(m);
	}
	
	@Override
	public Matrix getResult(){
		return (Matrix) super.getResult();
	}
	
	@Override
	public Matrix allRun(){
		return (Matrix) super.allRun();
	}
	
}
