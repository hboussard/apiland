package fr.inra.sad.bagap.apiland.treatment.window;

import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisObserver;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.GridAsciiGridOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.GridCsvOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetricManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysisBuilder;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.SquareWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.CornerWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.treatment.GlobalTreatmentManager;
import fr.inra.sad.bagap.apiland.treatment.Treatment;

public class GridWindowMatrixTreatment extends Treatment implements AnalysisObserver {
	
	private Matrix matrix;
	
	private int gridSize;

	private String csv;
	
	private String ascii;
	
	private double minRate;
	
	private Set<String> metrics;
	
	public GridWindowMatrixTreatment() {
		super("grid", GlobalTreatmentManager.get());
		defineInput("matrix", Matrix.class);
		defineInput("grid_size", Integer.class);
		defineInput("min_rate", Double.class);
		defineInput("metrics", Set.class);
		defineInput("csv", String.class);
		defineInput("ascii", String.class);
	}

	@Override
	protected void doInit() {
		matrix = (Matrix) getInput("matrix");
		gridSize = (Integer) getInput("grid_size");
		minRate = (Double) getInput("min_rate");
		metrics = (Set<String>) getInput("metrics");
		csv = (String) getInput("csv");
		ascii = (String) getInput("ascii");
	}

	@Override
	protected void doRun() {
		
		WindowMatrixProcessType pt = new WindowMatrixProcessType(false, matrix);
		
		for(String metric : metrics){
			pt.addMetric(MatrixMetricManager.get(metric));
		}
		
		Window w = new CornerWindow(new SquareWindow(gridSize, null));
		
		WindowMatrixAnalysisBuilder builder = new WindowMatrixAnalysisBuilder(WindowAnalysisType.GRID);
		builder.addMatrix(matrix);
		builder.setWindow(w);
		builder.setProcessType(pt);
		builder.setDisplacement(gridSize);
		builder.setMinRate(minRate);
		if(csv != null){
			builder.addObserver(new GridCsvOutput(gridSize, matrix, csv));
		}
		if(ascii != null){
			if(ascii.endsWith(".asc") && metrics.size() == 1){
				for(String metric : metrics){
					builder.addObserver(new GridAsciiGridOutput(gridSize, metric, ascii, gridSize));
				}
			}else{
				for(String metric : metrics){
					builder.addObserver(new GridAsciiGridOutput(gridSize, metric, ascii+metric+".asc", gridSize));
				}
			}
		}
		WindowMatrixAnalysis wa = builder.build();
		
		wa.addObserver(this); // le treatment observe l'analyse pour gérer la barre de progression
		
		wa.allRun();
	}

	@Override
	protected void doClose() {
		// do nothing
	}
	
	@Override
	public void notify(Analysis ma, AnalysisState state) {
		// do nothing
	}
	

}