package fr.inra.sad.bagap.apiland.analysis.matrix.output;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.metric.AbstractMetricOutput;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixFactory;

public class MatrixOutput extends AbstractMetricOutput {
	
	private String metric;
	
	private Matrix matrix;
	
	private int delta;
	
	public MatrixOutput(String metric, Matrix m){
		super();
		this.metric = metric;
		//this.matrix = MatrixFactory.get(m.getType()).create(m);
		this.matrix = MatrixFactory.get(m.getType()).create(m.width(), m.height(), m.cellsize(), m.minX(), m.maxX(), 
				m.minY(), m.maxY(), Raster.getNoDataValue());
	}
	
	@Override
	public String toString(){
		return metric;
	}
	
	@Override
	public void notify(Analysis ma, AnalysisState s) {
		switch (s){
		case INIT : 
			matrix.init(0); 
			this.delta = ((WindowMatrixAnalysis) ma).delta(); 
			break;
		}
	}

	@Override
	public boolean acceptMetric(String metric) {
		return this.metric.equalsIgnoreCase(metric);
	}
	
	public Matrix getMatrix(){
		return matrix;
	}

	@Override
	public void notify(Metric m, String n, double v, Process p) {
		if(acceptMetric(n)){
			matrix.put(((WindowMatrixProcess) p).x()/delta, ((WindowMatrixProcess) p).y()/delta, formatDouble(v));
		}
	}

	
}

