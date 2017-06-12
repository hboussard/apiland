package fr.inra.sad_paysage.apiland.treatment.window;

import java.util.Set;

import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.analysis.AnalysisObserver;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;
import fr.inra.sad_paysage.apiland.analysis.matrix.output.SelectedAsciiGridOutput;
import fr.inra.sad_paysage.apiland.analysis.matrix.output.SelectedCsvOutput;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad_paysage.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad_paysage.apiland.analysis.matrix.window.WindowMatrixAnalysisBuilder;
import fr.inra.sad_paysage.apiland.analysis.metric.MatrixMetricManager;
import fr.inra.sad_paysage.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.treatment.GlobalTreatmentManager;
import fr.inra.sad_paysage.apiland.treatment.Treatment;
import fr.inra.sad_paysage.apiland.window.CenteredWindow;
import fr.inra.sad_paysage.apiland.window.Window;
import fr.inra.sad_paysage.apiland.window.shape.WindowShapeType;

public class SelectedWindowMatrixTreatment extends Treatment implements AnalysisObserver {
	
	private Matrix matrix;
	
	private boolean qualitative;
	
	private WindowShapeType shape;
	
	private int windowSize;
	
	private Set<Pixel> pixels;
	
	private double minRate;
	
	private String csv;
	
	private String ascii;
	
	private Friction frictionMap;
	
	private Matrix frictionMatrix;
	
	private Set<String> metrics;
	
	private String path;
	
	public SelectedWindowMatrixTreatment() {
		super("selected", GlobalTreatmentManager.get());
		defineInput("matrix", Matrix.class);
		defineInput("qualitative", Boolean.class);
		defineInput("shape", WindowShapeType.class);
		defineInput("friction_map", Friction.class);
		defineInput("friction_matrix", Matrix.class);
		defineInput("min_rate", Double.class);
		defineInput("window_size", Integer.class);
		defineInput("pixels", Set.class);
		defineInput("metrics", Set.class);
		defineInput("csv", String.class);
		defineInput("ascii", String.class);
		defineInput("path", String.class);
	}

	@Override
	protected void doInit() {
		matrix = (Matrix) getInput("matrix");
		qualitative = (Boolean) getInput("qualitative");
		shape = (WindowShapeType) getInput("shape");
		frictionMap = (Friction) getInput("friction_map");
		frictionMatrix = (Matrix) getInput("friction_matrix");
		windowSize = (Integer) getInput("window_size");
		minRate = (Double) getInput("min_rate");
		metrics = (Set<String>) getInput("metrics");
		csv = (String) getInput("csv");
		ascii = (String) getInput("ascii");
		pixels = (Set<Pixel>) getInput("pixels");
		path = (String) getInput("path");
	}

	@Override
	protected void doRun() {
		
		WindowMatrixProcessType pt = new WindowMatrixProcessType(matrix);
		
		for(String metric : metrics){
			pt.addMetric(MatrixMetricManager.get(metric));
		}
			
		Window w;
		if(frictionMap != null){
			w = new CenteredWindow(shape.create(matrix, windowSize*matrix.cellsize()/2, frictionMap, pt));
		}else if(frictionMatrix != null){
			w = new CenteredWindow(shape.create(matrix, windowSize*matrix.cellsize()/2, frictionMatrix, pt));
			System.out.println(w.width()+" "+w.height());
		}else{
			w = new CenteredWindow(shape.create(windowSize));
		}
		
		WindowMatrixAnalysisBuilder builder = new WindowMatrixAnalysisBuilder(WindowAnalysisType.SELECTED);
		builder.addMatrix(matrix);
		builder.setWindow(w);
		builder.setProcessType(pt);
		builder.setMinRate(minRate);
		builder.setPixels(pixels);
		builder.setPath(path);
		if(csv != null){
			builder.addObserver(new SelectedCsvOutput(matrix, csv, pixels));
		}
		if(ascii != null){
			for(String metric : metrics){
				builder.addObserver(new SelectedAsciiGridOutput(metric, ascii+metric+".asc", matrix.width(), matrix.height()/*, pixels*/));
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
	public void notifyFromAnalysis(Analysis ma, AnalysisState state) {
		// do nothing
	}
	

}
