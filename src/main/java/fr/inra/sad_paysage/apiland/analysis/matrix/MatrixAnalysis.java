package fr.inra.sad_paysage.apiland.analysis.matrix;


import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

/**
 * modeling class of a matrix analysis
 * @author H.Boussard
 */
public abstract class MatrixAnalysis extends Analysis {

	/** the whole refereed matrix */
	private Matrix[] matrix;
	
	/**
	 * constructor
	 * @param m the whole refereed matrix
	 */
	public MatrixAnalysis(Matrix... m){
		super();
		this.matrix = m;
	}
	
	/** @return the first matrix */
	public Matrix matrix(){
		return matrix[0];
	}
	
	/** 
	 * to get the matrix at the given index
	 * @param index which matrix 
	 * @return the matrix at the index position
	 */
	public Matrix matrix(int index){
		return matrix[index];
	}
	
	/** @return to get the whole matrix */
	public Matrix[] wholeMatrix(){
		return matrix;
	}

}
