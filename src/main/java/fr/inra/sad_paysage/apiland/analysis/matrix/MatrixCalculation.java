package fr.inra.sad_paysage.apiland.analysis.matrix;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public abstract class MatrixCalculation extends MatrixAnalysis {
	
	public MatrixCalculation(Matrix... m){
		super(m);
	}
	
	@Override
	public Matrix getResult(){
		return (Matrix)super.getResult();
	}
	
	@Override
	public Matrix allRun(){
		return (Matrix)super.allRun();
	}
	
}
