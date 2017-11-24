package fr.inra.sad.bagap.apiland.treatment.window;

import java.util.Set;

import com.csvreader.CsvWriter;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisObserver;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.MapCsvOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetricManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysisBuilder;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.CornerWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.RectangularWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.Window;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.treatment.GlobalTreatmentManager;
import fr.inra.sad.bagap.apiland.treatment.Treatment;

public class MapWindowMatrixTreatment extends Treatment implements AnalysisObserver {
	
	private Matrix matrix;
	
	private CsvWriter csv;
	
	private Set<String> metrics;
	
	public MapWindowMatrixTreatment() {
		super("whole map", GlobalTreatmentManager.get());
		defineInput("matrix", Matrix.class); 
		defineInput("metrics", Set.class);
		defineInput("csv", CsvWriter.class);
	}

	@Override
	protected void doInit() {
		matrix = (Matrix) getInput("matrix");
		metrics = (Set<String>) getInput("metrics");
		csv = (CsvWriter) getInput("csv");		
	}

	@Override
	protected void doRun() {
		
		WindowMatrixProcessType pt = new WindowMatrixProcessType(matrix);	
		
		for(String metric : metrics){	
			pt.addMetric(MatrixMetricManager.get(metric));
		}
		
		Window w = new CornerWindow(new RectangularWindow(matrix.width(), matrix.height()));		
		
		WindowMatrixAnalysisBuilder builder = new WindowMatrixAnalysisBuilder(WindowAnalysisType.MAP);
		builder.addMatrix(matrix);
		builder.setWindow(w);
		builder.setProcessType(pt);
		builder.addObserver(new MapCsvOutput(metrics, csv));
		WindowMatrixAnalysis wa = builder.build();
	
		wa.addObserver(this); // le treatment observe l'analyse pour gerer la barre de progression
		
		wa.allRun();
	
	}

	@Override
	protected void doClose() {
		//csv.close(); // ???
	}
	
	@Override
	public void notify(Analysis ma, AnalysisState state) {
		// do nothing
	}

}
