package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric;

import fr.inra.sad.bagap.apiland.analysis.Variable;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.MatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public abstract class MatrixMetric extends Metric {

	public MatrixMetric(Variable v) {
		super(v);
	}
	
	/**
	 * to calculate the metric on a specific process
	 * @param wp the specific process
	 */
	public final void calculate(MatrixProcess p, String pref){
		value = Raster.getNoDataValue();
		if((double) (p.counting().validValues())/p.counting().theoreticalSize() >= minRate){
			doCalculate(p.counting());
		}
		notifyObservers(p, pref);
	}
	
	public final void unCalculate(MatrixProcess p, String pref){
		value = Raster.getNoDataValue();
		notifyObservers(p, pref);
	}

	protected abstract void doCalculate(Counting co);

}
