package fr.inra.sad.bagap.apiland.treatment.window;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisObserver;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.combination.CombinationExpressionFactory;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.DeltaAsciiGridOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.SelectedAsciiGridOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.SelectedCsvOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.MultipleWindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetricManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysisBuilder;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.WindowShapeType;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.CenteredWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.MultipleWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.treatment.GlobalTreatmentManager;
import fr.inra.sad.bagap.apiland.treatment.Treatment;

public class SelectedWindowMatrixTreatment extends Treatment implements AnalysisObserver {
	
	private Matrix matrix;
	
	private WindowShapeType shape;
	
	private List<Integer> windowSizes;
	
	private Set<Pixel> pixels;
	
	private double minRate;
	
	private String csv;
	
	private String ascii;
	
	private Friction frictionMap;
	
	private Matrix frictionMatrix;
	
	private Set<String> metrics;
	
	private String path;
	
	private boolean distanceType;
	
	private String distanceFunction;
	
	public SelectedWindowMatrixTreatment() {
		super("selected", GlobalTreatmentManager.get());
		defineInput("matrix", Matrix.class);
		defineInput("shape", WindowShapeType.class);
		defineInput("friction_map", Friction.class);
		defineInput("friction_matrix", Matrix.class);
		defineInput("min_rate", Double.class);
		defineInput("window_sizes", List.class);
		defineInput("pixels", Set.class);
		defineInput("metrics", Set.class);
		defineInput("csv", String.class);
		defineInput("ascii", String.class);
		defineInput("path", String.class);
		defineInput("distance_type", Boolean.class);
		defineInput("distance_function", String.class);
	}

	@Override
	protected void doInit() {
		matrix = (Matrix) getInput("matrix");
		shape = (WindowShapeType) getInput("shape");
		frictionMap = (Friction) getInput("friction_map");
		frictionMatrix = (Matrix) getInput("friction_matrix");
		windowSizes = (List<Integer>) getInput("window_sizes");
		minRate = (Double) getInput("min_rate");
		metrics = (Set<String>) getInput("metrics");
		csv = (String) getInput("csv");
		ascii = (String) getInput("ascii");
		pixels = (Set<Pixel>) getInput("pixels");
		path = (String) getInput("path");
		distanceType = (Boolean) getInput("distance_type");
		distanceFunction = (String) getInput("distance_function");
	}

	@Override
	protected void doRun() {
		WindowMatrixProcessType pt;
		if(windowSizes.size() == 1){
			pt = new WindowMatrixProcessType(distanceType, matrix);
		}else{
			pt = new MultipleWindowMatrixProcessType(distanceType, matrix);
		}
		//WindowMatrixProcessType pt = new WindowMatrixProcessType(matrix);
		
		for(String metric : metrics){
			pt.addMetric(MatrixMetricManager.get(metric));
		}
			
		Window w;
		if(windowSizes.size() == 1){
			
			DistanceFunction function = null;
			if(distanceType){
				double dMax = ((windowSizes.get(0)-1)/2)*Raster.getCellSize();
				//function = new DistanceFunction(distanceFunction, dMax);	
				function = CombinationExpressionFactory.createDistanceFunction(distanceFunction, dMax);
			}
			
			if(frictionMap != null){
				w = new CenteredWindow(shape.create(matrix, (windowSizes.get(0)-1)*matrix.cellsize()/2, frictionMap, pt, function));
			}else if(frictionMatrix != null){
				w = new CenteredWindow(shape.create(matrix, (windowSizes.get(0)-1)*matrix.cellsize()/2, frictionMatrix, pt, function));
			}else{
				w = new CenteredWindow(shape.create(windowSizes.get(0), function));
			}
		}else{
			Collections.sort(windowSizes);
			Collections.reverse(windowSizes);
			Window[] ws = new Window[windowSizes.size()];
			for(int i=0; i<windowSizes.size(); i++){
				
				DistanceFunction function = null;
				if(distanceType){
					double dMax = ((windowSizes.get(i)-1)/2)*Raster.getCellSize();
					 //function = new DistanceFunction(distanceFunction, dMax);
					 function = CombinationExpressionFactory.createDistanceFunction(distanceFunction, dMax);
				}
				
				if(frictionMap != null){
					ws[i] = new CenteredWindow(shape.create(matrix, (windowSizes.get(i))*matrix.cellsize()/2, frictionMap, pt, function));
				}else if(frictionMatrix != null){
					ws[i] = new CenteredWindow(shape.create(matrix, (windowSizes.get(i))*matrix.cellsize()/2, frictionMatrix, pt, function));
				}else{
					ws[i] = new CenteredWindow(shape.create(windowSizes.get(i), function));
				}
			}
			w = new MultipleWindow(ws);
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
			builder.setExportFilters(true);
			if(ascii.endsWith(".asc") && metrics.size() == 1 && windowSizes.size() == 1){
				String metric = metrics.iterator().next();
				builder.addObserver(new SelectedAsciiGridOutput(metric, ascii, matrix.width(), matrix.height()));
			}else{
				for(String metric : metrics){
					if(windowSizes.size() == 1){
						//builder.addObserver(new DeltaAsciiGridOutput(metric, ascii+"w"+windowSizes.get(0)+"_"+metric+"_d_"+delta+".asc", delta));
						builder.addObserver(new SelectedAsciiGridOutput(metric, ascii+"w"+windowSizes.get(0)+"_"+metric+".asc", matrix.width(), matrix.height()));
					}else{
						for(int size : windowSizes){
							//builder.addObserver(new DeltaAsciiGridOutput("w"+size+"_"+metric, ascii+"w"+size+"_"+metric+".asc", delta));
							builder.addObserver(new SelectedAsciiGridOutput("w"+size+"_"+metric, ascii+"w"+size+"_"+metric+".asc", matrix.width(), matrix.height()));
						}
					}
				}
			}
		}else{
			builder.setExportFilters(false);
		}
		
		WindowMatrixAnalysis wa = builder.build();
		
		wa.addObserver(this); // le treatment observe l'analyse pour gerer la barre de progression
		
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
