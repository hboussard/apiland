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
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.FilteredRectangularWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.CornerWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.MultipleWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.cluster.Cluster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.treatment.GlobalTreatmentManager;
import fr.inra.sad.bagap.apiland.treatment.Treatment;

public class ClusteredWindowMatrixTreatment  extends Treatment implements AnalysisObserver {

	private Matrix matrix;
	
	private CsvWriter csv;
	
	private Set<String> metrics;
	
	private Set<Cluster> clusters;
	
	public ClusteredWindowMatrixTreatment() {
		super("clustered", GlobalTreatmentManager.get());
		defineInput("matrix", Matrix.class); 
		defineInput("metrics", Set.class);
		defineInput("csv", CsvWriter.class);
		defineInput("clusters", Set.class);
	}

	@Override
	protected void doInit() {
		matrix = (Matrix) getInput("matrix");
		metrics = (Set<String>) getInput("metrics");
		csv = (CsvWriter) getInput("csv");
		clusters = (Set<Cluster>) getInput("clusters");
	}

	@Override
	protected void doRun() {
		
		WindowMatrixProcessType pt = new WindowMatrixProcessType(false, matrix);	
		
		for(String metric : metrics){	
			pt.addMetric(MatrixMetricManager.get(metric));
		}
		
		Window w;
		Window[] ws = new Window[clusters.size()];
		int index = 0;
		
		for(Cluster c : clusters){
			w = new CornerWindow(new FilteredRectangularWindow(c));
			ws[index++] = w;
		}
		w = new MultipleWindow(ws);	 		
		
		WindowMatrixAnalysisBuilder builder = new WindowMatrixAnalysisBuilder(WindowAnalysisType.AREA);
		builder.addMatrix(matrix);
		builder.setWindow(w);
		builder.setClusters(clusters);
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
