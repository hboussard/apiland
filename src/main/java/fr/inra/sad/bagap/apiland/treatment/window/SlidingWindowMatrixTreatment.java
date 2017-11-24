package fr.inra.sad.bagap.apiland.treatment.window;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.CsvOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.DeltaAsciiGridOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.HeaderAsciiGridOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.HeaderDeltaAsciiGridOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.InterpolateLinearSplineAsciiGridOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.InterpolateLinearSplineCsvOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.MultipleWindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetricManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysisBuilder;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.CenteredWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.MultipleWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.Window;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.WindowShapeType;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.treatment.GlobalTreatmentManager;
import fr.inra.sad.bagap.apiland.treatment.Treatment;

public class SlidingWindowMatrixTreatment extends Treatment /*implements AnalysisObserver*/ {
	
	private Matrix matrix;
	
	private boolean interpolation;
	
	private WindowShapeType shape;
	
	private List<Integer> windowSizes;
	
	private int delta;
	
	private double minRate;
	
	private String csv;
	
	private String ascii;
	
	private Friction frictionMap;
	
	private Matrix frictionMatrix;
	
	private Set<String> metrics;
	
	private Set<Integer> filters, unfilters;
	
	public SlidingWindowMatrixTreatment() {
		super("sliding", GlobalTreatmentManager.get());
		defineInput("matrix", Matrix.class);
		defineInput("shape", WindowShapeType.class);
		defineInput("friction_map", Friction.class);
		defineInput("friction_matrix", Matrix.class);
		defineInput("window_sizes", List.class);
		defineInput("delta", Integer.class);
		defineInput("interpolation", Boolean.class);
		defineInput("min_rate", Double.class);
		defineInput("metrics", Set.class);
		defineInput("csv", String.class);
		defineInput("ascii", String.class);
		defineInput("filters", Set.class);
		defineInput("unfilters", Set.class);
	}
	
	@Override
	protected void doInit() {
		
		matrix = (Matrix) getInput("matrix");
		shape = (WindowShapeType) getInput("shape");
		frictionMap = (Friction) getInput("friction_map");
		frictionMatrix = (Matrix) getInput("friction_matrix");
		windowSizes = (List<Integer>) getInput("window_sizes");
		metrics = (Set<String>) getInput("metrics");
		csv = (String) getInput("csv");
		ascii = (String) getInput("ascii");
		delta = (Integer) getInput("delta");
		interpolation = (Boolean) getInput("interpolation");
		minRate = (Double) getInput("min_rate");
		filters = (Set<Integer>) getInput("filters");
		unfilters = (Set<Integer>) getInput("unfilters");
		
	}

	@Override
	protected void doRun() {
		
		WindowMatrixProcessType pt;
		if(windowSizes.size() == 1){
			pt = new WindowMatrixProcessType(matrix);
		}else{
			pt = new MultipleWindowMatrixProcessType(matrix);
		}
		
		for(String metric : metrics){
			pt.addMetric(MatrixMetricManager.get(metric));
		}
		
		Window w;
		if(windowSizes.size() == 1){
			if(frictionMap != null){
				w = new CenteredWindow(shape.create(matrix, (windowSizes.get(0))*matrix.cellsize()/2, frictionMap, pt));
			}else if(frictionMatrix != null){
				w = new CenteredWindow(shape.create(matrix, (windowSizes.get(0))*matrix.cellsize()/2, frictionMatrix, pt));
			}else{
				w = new CenteredWindow(shape.create(windowSizes.get(0)));
			}
		}else{
			Collections.sort(windowSizes);
			Collections.reverse(windowSizes);
			Window[] ws = new Window[windowSizes.size()];
			for(int i=0; i<windowSizes.size(); i++){
				if(frictionMap != null){
					ws[i] = new CenteredWindow(shape.create(matrix, (windowSizes.get(i))*matrix.cellsize()/2, frictionMap, pt));
				}else if(frictionMatrix != null){
					ws[i] = new CenteredWindow(shape.create(matrix, (windowSizes.get(i))*matrix.cellsize()/2, frictionMatrix, pt));
				}else{
					ws[i] = new CenteredWindow(shape.create(windowSizes.get(i)));
				}
			}
			w = new MultipleWindow(ws);
		}
			
		WindowMatrixAnalysisBuilder builder = new WindowMatrixAnalysisBuilder(WindowAnalysisType.SLIDING);
		builder.addMatrix(matrix);
		builder.setWindow(w);
		builder.setProcessType(pt);
		builder.setDisplacement(delta);
		builder.setMinRate(minRate);
		builder.setFilters(filters);
		builder.setUnfilters(unfilters);
		
		if(csv != null){
			if(delta != 1 && interpolation){
				builder.addObserver(new InterpolateLinearSplineCsvOutput(matrix, csv, delta));
				builder.addObserver(new HeaderAsciiGridOutput(csv.replace(".csv", "")+"_header.txt", 1));
			}else{
				builder.addObserver(new CsvOutput(matrix, csv));
				builder.addObserver(new HeaderDeltaAsciiGridOutput(csv.replace(".csv", "")+"_header.txt", delta));
			}
		}
		if(ascii != null){
			if(ascii.endsWith(".asc") && metrics.size() == 1 && windowSizes.size() == 1){
				String metric = metrics.iterator().next();
				if(delta != 1 && interpolation){
					builder.addObserver(new InterpolateLinearSplineAsciiGridOutput(metric, ascii, delta));
				}else{
					builder.addObserver(new DeltaAsciiGridOutput(metric, ascii, delta));
				}
			}else{
				for(String metric : metrics){
					if(delta != 1 && interpolation){
						if(windowSizes.size() == 1){
							builder.addObserver(new InterpolateLinearSplineAsciiGridOutput(metric, ascii+"w"+windowSizes.get(0)+"_"+metric+".asc", delta));
						}else{
							for(int size : windowSizes){
								builder.addObserver(new InterpolateLinearSplineAsciiGridOutput("w"+size+"_"+metric, ascii+"w"+size+"_"+metric+".asc", delta));
							}
						}
					}else{
						if(windowSizes.size() == 1){
							builder.addObserver(new DeltaAsciiGridOutput(metric, ascii+"w"+windowSizes.get(0)+"_"+metric+"_d_"+delta+".asc", delta));
						}else{
							for(int size : windowSizes){
								builder.addObserver(new DeltaAsciiGridOutput("w"+size+"_"+metric, ascii+"w"+size+"_"+metric+".asc", delta));
							}
						}
					}
				}
			}
		}
		
		WindowMatrixAnalysis wa = builder.build();
		
		wa.addObserver(this); // le treatment observe l'analyse pour gerer la barre de progression
		
		wa.allRun();
	}

	@Override
	protected void doClose() {
		// do nothing
	}
	

}