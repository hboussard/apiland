package fr.inra.sad_paysage.apiland.treatment.window;

import java.util.Set;

import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.analysis.AnalysisObserver;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;
import fr.inra.sad_paysage.apiland.analysis.matrix.output.AsciiGridOutput;
import fr.inra.sad_paysage.apiland.analysis.matrix.output.CsvOutput;
import fr.inra.sad_paysage.apiland.analysis.matrix.output.GridAsciiGridOutput;
import fr.inra.sad_paysage.apiland.analysis.matrix.output.GridCsvOutput;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad_paysage.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad_paysage.apiland.analysis.matrix.window.WindowMatrixAnalysisBuilder;
import fr.inra.sad_paysage.apiland.analysis.metric.MatrixMetricManager;
import fr.inra.sad_paysage.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.treatment.GlobalTreatmentManager;
import fr.inra.sad_paysage.apiland.treatment.Treatment;
import fr.inra.sad_paysage.apiland.window.CornerWindow;
import fr.inra.sad_paysage.apiland.window.Window;
import fr.inra.sad_paysage.apiland.window.shape.SquareWindow;

public class GridWindowMatrixTreatment extends Treatment implements AnalysisObserver {
	
	private Matrix matrix;
	
	private boolean qualitative;
	
	private int gridSize;

	private String csv;
	
	private String ascii;
	
	private double minRate;
	
	private Set<String> metrics;
	
	public GridWindowMatrixTreatment() {
		super("grid", GlobalTreatmentManager.get());
		defineInput("matrix", Matrix.class);
		defineInput("qualitative", Boolean.class);
		defineInput("grid_size", Integer.class);
		defineInput("min_rate", Double.class);
		defineInput("metrics", Set.class);
		defineInput("csv", String.class);
		defineInput("ascii", String.class);
	}

	@Override
	protected void doInit() {
		matrix = (Matrix) getInput("matrix");
		qualitative = (Boolean) getInput("qualitative");
		gridSize = (Integer) getInput("grid_size");
		minRate = (Double) getInput("min_rate");
		metrics = (Set<String>) getInput("metrics");
		csv = (String) getInput("csv");
		ascii = (String) getInput("ascii");
	}

	@Override
	protected void doRun() {
		
		WindowMatrixProcessType pt = new WindowMatrixProcessType(matrix);
		
		for(String metric : metrics){
			pt.addMetric(MatrixMetricManager.get(metric));
		}
		
		Window w = new CornerWindow(new SquareWindow(gridSize));
		
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
			for(String metric : metrics){
				builder.addObserver(new GridAsciiGridOutput(gridSize, metric, ascii+metric+".asc", gridSize));
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