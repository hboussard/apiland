package fr.inra.sad.bagap.apiland.treatment.window;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.combination.CombinationExpressionFactory;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.CsvOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.DeltaAsciiGridOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.HeaderAsciiGridOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.HeaderDeltaAsciiGridOutputBis;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.InterpolateLinearSplineAsciiGridOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.InterpolateLinearSplineCsvOutput;
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
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
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
	
	private int xOrigin;
	
	private int yOrigin;
	
	private double minRate;
	
	private String csv;
	
	private String ascii;
	
	private Friction frictionMap;
	
	private Matrix frictionMatrix;
	
	private Set<String> metrics;
	
	private Set<Integer> filters, unfilters;
	
	private boolean distanceType;
	
	private String distanceFunction;
	
	public SlidingWindowMatrixTreatment() {
		super("sliding", GlobalTreatmentManager.get());
		defineInput("matrix", Matrix.class);
		defineInput("shape", WindowShapeType.class);
		defineInput("friction_map", Friction.class);
		defineInput("friction_matrix", Matrix.class);
		defineInput("window_sizes", List.class);
		defineInput("delta", Integer.class);
		defineInput("x_origin", Integer.class);
		defineInput("y_origin", Integer.class);
		defineInput("delta", Integer.class);
		defineInput("interpolation", Boolean.class);
		defineInput("min_rate", Double.class);
		defineInput("metrics", Set.class);
		defineInput("csv", String.class);
		defineInput("ascii", String.class);
		defineInput("filters", Set.class);
		defineInput("unfilters", Set.class);
		defineInput("distance_type", Boolean.class);
		defineInput("distance_function", String.class);
	}
	
	@SuppressWarnings("unchecked")
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
		xOrigin = (Integer) getInput("x_origin");
		yOrigin = (Integer) getInput("y_origin");
		interpolation = (Boolean) getInput("interpolation");
		minRate = (Double) getInput("min_rate");
		filters = (Set<Integer>) getInput("filters");
		unfilters = (Set<Integer>) getInput("unfilters");
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
				/*
				w = null;
				double rate = 0.0;
				
				for(double d=50.0; d<=1000.0; d+=Raster.getCellSize()){
					w = new CenteredWindow(shape.create(d, null));
					//rate = ((CenteredWindow) w).getRate();
					double t1 =  ((CenteredWindow) w).theoreticalSize() * Math.pow(Raster.getCellSize(), 2);
					double a1 = Math.PI * Math.pow((Raster.getCellSize() * ((CenteredWindow) w).diameter()/2), 2);
					
					System.out.println(d+";"+t1+";"+a1+";"+Math.abs(100.0-(t1*100.0/a1)));
				}*/
				
				
				/*
				for(double d=50.0; d<=1000.0; d+=Raster.getCellSize()){
					w = new CenteredWindow(shape.create(d, null));
					//rate = ((CenteredWindow) w).getRate();
					int t1 =  ((CenteredWindow) w).theoreticalSize();
					int d1 = ((CenteredWindow) w).diameter() * ((CenteredWindow) w).diameter(); 
					System.out.println(d+";"+t1+";"+d1+";"+Math.abs(100.0-(t1*100.0/d1)));
				}*/
				
				/*
				for(double d=50.0; d<=500.0; d+=Raster.getCellSize()){
					
					w = new CenteredWindow(shape.create(d, null));
					//rate = ((CenteredWindow) w).getRate();
					int t1 =  ((CenteredWindow) w).theoreticalSize();
					int d1 = ((CenteredWindow) w).diameter() * ((CenteredWindow) w).diameter(); 
					//System.out.println((t1 +" "+ d1 +" "+ (t1*100.0/d1)));
					
					double dd = d;
					for(; dd<=(d+Raster.getCellSize()/2); dd++){
						w = new CenteredWindow(shape.create(dd, null));
						//rate = ((CenteredWindow) w).getRate();
						int t2 =  ((CenteredWindow) w).theoreticalSize();
						int d2 = ((CenteredWindow) w).diameter() * ((CenteredWindow) w).diameter(); 
						//System.out.println((t2 +" "+ d2 +" "+ (t2*100.0/d2)));
						
						System.out.println(dd+";"+t1+";"+t2+";"+Math.abs(100.0-(t2*100.0/t1)));
						
					}
					
					w = new CenteredWindow(shape.create(d+Raster.getCellSize(), null));
					//rate = ((CenteredWindow) w).getRate();
					t1 =  ((CenteredWindow) w).theoreticalSize();
					d1 = ((CenteredWindow) w).diameter() * ((CenteredWindow) w).diameter(); 
					//System.out.println((t1 +" "+ d1 +" "+ (t1*100.0/d1)));
					
					for(; dd<(d+Raster.getCellSize()); dd++){
						w = new CenteredWindow(shape.create(dd, null));
						//rate = ((CenteredWindow) w).getRate();
						int t2 =  ((CenteredWindow) w).theoreticalSize();
						int d2 = ((CenteredWindow) w).diameter() * ((CenteredWindow) w).diameter(); 
						//System.out.println((t2 +" "+ d2 +" "+ (t2*100.0/d2)));
						
						System.out.println(dd+";"+t1+";"+t2+";"+Math.abs(100.0-(t2*100.0/t1)));
					}
				}*/
				
				/*
				w = new CenteredWindow(shape.create(d+(Raster.getCellSize()-1), null));
				//rate = (-1)*(rate-((CenteredWindow) w).getRate());
				int t2 =  ((CenteredWindow) w).theoreticalSize();
				int d2 = ((CenteredWindow) w).diameter() * ((CenteredWindow) w).diameter(); 
				System.out.println((t2 +" "+ d2 +" "+ (t2*100.0/d2)));
				
				System.out.println(d+";"+t1+" "+t2);
				*/
				//System.out.println(d+";"+rate);
				
				//w = new CenteredWindow(shape.create(60.0, null));
				//w = new CenteredWindow(shape.create(69.0, null));
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
			
		WindowMatrixAnalysisBuilder builder = new WindowMatrixAnalysisBuilder(WindowAnalysisType.SLIDING);
		builder.addMatrix(matrix);
		builder.setWindow(w);
		builder.setProcessType(pt);
		builder.setDisplacement(delta);
		builder.setXOrigin(xOrigin);
		builder.setYOrigin(yOrigin);
		builder.setMinRate(minRate);
		builder.setFilters(filters);
		builder.setUnfilters(unfilters);
		
		
		if(csv != null){
			if(delta != 1 && interpolation){
				builder.addObserver(new InterpolateLinearSplineCsvOutput(matrix, csv, delta));
				builder.addObserver(new HeaderAsciiGridOutput(csv.replace(".csv", "")+"_header.txt", 1));
			}else{
				builder.addObserver(new CsvOutput(matrix, csv));
				//builder.addObserver(new HeaderDeltaAsciiGridOutput(csv.replace(".csv", "")+"_header.txt", delta));
				builder.addObserver(new HeaderDeltaAsciiGridOutputBis(csv.replace(".csv", "")+"_header.txt", delta, xOrigin, yOrigin));
			}
		}
		if(ascii != null){
			if(ascii.endsWith(".asc") && metrics.size() == 1 && windowSizes.size() == 1){
				String metric = metrics.iterator().next();
				if(delta != 1 && interpolation){
					builder.addObserver(new InterpolateLinearSplineAsciiGridOutput(metric, ascii, delta));
				}else{
					builder.addObserver(new DeltaAsciiGridOutput(metric, ascii, delta, xOrigin, yOrigin));
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
							builder.addObserver(new DeltaAsciiGridOutput(metric, ascii+"w"+windowSizes.get(0)+"_"+metric+"_d_"+delta+".asc", delta, xOrigin, yOrigin));
						}else{
							for(int size : windowSizes){
								builder.addObserver(new DeltaAsciiGridOutput("w"+size+"_"+metric, ascii+"w"+size+"_"+metric+"_d_"+delta+".asc", delta, xOrigin, yOrigin));
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