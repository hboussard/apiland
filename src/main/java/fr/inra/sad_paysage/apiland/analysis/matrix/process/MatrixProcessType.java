package fr.inra.sad_paysage.apiland.analysis.matrix.process;

import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.metric.Metric;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessType;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

/**
 * modeling class of a process type
 * @author H.Boussard
 */
public abstract class MatrixProcessType extends ProcessType<MatrixMetric> {
	
	private Matrix[] matrix;
	
	/** constructor */
	public MatrixProcessType(Matrix... m){
		super();
		this.matrix = m;
	}
	
	/** @return a specific metric */
	public Metric getMetric(String variable){
		for(MatrixMetric m : metrics()){
			if(m.getName().equalsIgnoreCase(variable)){
				return m;
			}
		}
		throw new IllegalArgumentException();
	}
	
	public Matrix[] wholeMatrix(){
		return matrix;
	}
	
	public Matrix matrix(){
		return matrix[0];
	}
	
	public Matrix matrix(int index){
		return matrix[index];
	}
	
}