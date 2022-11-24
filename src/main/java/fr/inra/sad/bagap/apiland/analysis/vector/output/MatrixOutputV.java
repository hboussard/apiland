package fr.inra.sad.bagap.apiland.analysis.vector.output;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.metric.AbstractMetricOutput;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.analysis.vector.process.VectorProcess;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixFactory;

public class MatrixOutputV extends AbstractMetricOutput {
	
	private String metric;
	
	private Matrix matrix;
	
	public MatrixOutputV(String metric, double buffer, Matrix m){
		super();
		this.metric = buffer+"_"+metric;
		this.matrix = MatrixFactory.get(m.getType()).create(m);
	}
	
	@Override
	public String toString(){
		return metric;
	}
	
	@Override
	public void notify(Analysis ma, AnalysisState s) {
		switch (s){
		case INIT : matrix.init(0); break;
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
			int x = CoordinateManager.getLocalX(matrix, ((VectorProcess) p).x());
			int y = CoordinateManager.getLocalY(matrix, ((VectorProcess) p).y());
			if(matrix.contains(x, y)){
				matrix.put(x, y, formatDouble(v));
			}
		}
	}
	
}
