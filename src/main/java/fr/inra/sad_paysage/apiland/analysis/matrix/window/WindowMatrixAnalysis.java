package fr.inra.sad_paysage.apiland.analysis.matrix.window;

import java.util.Set;

import fr.inra.sad_paysage.apiland.window.Window;
import fr.inra.sad_paysage.apiland.analysis.matrix.metric.MatrixMetric;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessObserver;
import fr.inra.sad_paysage.apiland.analysis.matrix.MatrixAnalysis;
import fr.inra.sad_paysage.apiland.analysis.metric.Metric;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

/**
 * modeling class of window analysis
 * @author H.Boussard
 */
public abstract class WindowMatrixAnalysis extends MatrixAnalysis implements ProcessObserver {
	
	/** the refereed window */
	private Window window;
	
	/** the process type */
	private WindowMatrixProcessType processType;
	
	/**
	 * constructor
	 * @param m the refereed matrix
	 * @param w the refereed window
	 * @param pt the process type
	 */
	public WindowMatrixAnalysis(Matrix m, Window w, WindowMatrixProcessType pt){
		super(m);
		window = w;
		Metric.setMinRate(0);
		processType = pt;
		processType.addObserver(this);
	}
	
	public WindowMatrixAnalysis(Matrix[] m, Window w, WindowMatrixProcessType pt){
		super(m);
		window = w;
		Metric.setMinRate(0);
		processType = pt;
		processType.addObserver(this);
	}
	
	/** @return the delta of displacement if exists */
	public int delta(){
		return 1;
	}
	
	/** @return the metrics */
	public Set<MatrixMetric> metrics(){
		return processType.metrics();
	}
	
	/** @return the window */
	public Window window(){
		return window;
	}
	
	/** @return the process type */
	public WindowMatrixProcessType processType(){
		return processType;
	}
	
	@Override
	public int compareTo(ProcessObserver obs) {
		if(obs instanceof WindowMatrixAnalysis){
			return -1;
		}
		return 1;
	}
	
	
}
