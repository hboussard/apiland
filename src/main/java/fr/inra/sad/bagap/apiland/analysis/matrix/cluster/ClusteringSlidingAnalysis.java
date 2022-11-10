package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.util.HashSet;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisObserver;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.matrix.MatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.output.MatrixOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.Pixel2PixelMatrixCalculation;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value.CountValueMetric;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysisBuilder;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.CircleWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.WindowShapeType;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.CenteredWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RasterManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;

public class ClusteringSlidingAnalysis extends MatrixAnalysis implements AnalysisObserver{

	private double distance;
	
	private int size;
	
	//private double radius;
	
	private Set<Integer> interest;
	
	private String path;
	
	private String name;
	
	private int gTotal;
	
	private Friction frictionMap;
	
	private Matrix frictionMat;
	
	public ClusteringSlidingAnalysis(Matrix m, double distance, Set<Integer> interest, String path, String name, Friction frictionMap){
		this(m, distance, interest, path, name);
		this.frictionMap = frictionMap;		
	}
	
	public ClusteringSlidingAnalysis(Matrix m, double distance, Set<Integer> interest, String path, String name, Matrix frictionMat){
		this(m, distance, interest, path, name);
		this.frictionMat = frictionMat;
	}
	
	public ClusteringSlidingAnalysis(Matrix m, double distance, Set<Integer> interest, String path, String name){
		super(m);
		this.distance = distance;
		getSize();
		this.interest = interest;
		this.path = path+"/";
		this.name = name;
		gTotal = 4 * matrix().size();
	}
	
	private void getSize() {
		size = 3;
		while(size * Raster.getCellSize() < distance){
			size += 2;
		}
		//System.out.println(size+" "+distance);
	}

	@Override
	protected void doRun() {
		try {
			
			Matrix[] mNV = sliding(matrix());
			
			Matrix mClassif = classification(mNV);
			
			Matrix mCluster = cluster(mClassif);
			
			overlay(matrix(), mCluster);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} 
	}
	
	private Matrix[] sliding(Matrix matrix) {
		//System.out.println("sliding");
		try {
			WindowMatrixProcessType pt = new WindowMatrixProcessType(matrix); 
			for(int v : interest){
				pt.addMetric(new CountValueMetric(v));
			}
			
			Window w = new CenteredWindow(new CircleWindow(size));
			
			if(frictionMap != null){
				w = new CenteredWindow(WindowShapeType.FUNCTIONAL.create(matrix, distance/2, frictionMap, pt, null));
			}else if(frictionMat != null){
				w = new CenteredWindow(WindowShapeType.FUNCTIONAL.create(matrix, distance/2, frictionMat, pt, null));
			}else{
				w = new CenteredWindow(WindowShapeType.CIRCLE.create(size, null));
			}
			
			WindowMatrixAnalysisBuilder builder = new WindowMatrixAnalysisBuilder(WindowAnalysisType.SLIDING);
			builder.addMatrix(matrix);
			builder.setWindow(w);
			/*if(friction != null && !friction.equalsIgnoreCase("")){
				builder.
			}*/
			builder.setProcessType(pt);
			Set<MatrixOutput> mos = new HashSet<MatrixOutput>();
			for(int v : interest){
				//builder.addObserver(new AsciiGridOutput("NV_"+v, path+"cluster/sliding_"+v+"_"+code+"_"+size+".asc"));
				MatrixOutput mo = new MatrixOutput("NV_"+v, matrix);
				builder.addObserver(mo);
				mos.add(mo);
			}
			WindowMatrixAnalysis wa = builder.build();
			
			wa.addObserver(this);
			
			wa.allRun();
			
			
			Matrix[] m = new Matrix[mos.size()];
			int i = 0;
			for(MatrixOutput mo : mos){
				m[i++] = mo.getMatrix();
			}
			return m;			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Matrix classification(Matrix[] mNV) {
		//System.out.println("classification");
		try{
			//Pixel2PixelMatrixCalculation classif = new Pixel2PixelMatrixCalculation(m.toArray(new Matrix[m.size()]))
			Pixel2PixelMatrixCalculation classif = new Pixel2PixelMatrixCalculation(mNV){
				@Override
				protected double treatPixel(Pixel p) {
					double v;
					for(Matrix mat : wholeMatrix()){
						v = mat.get(p);
						if(v > 0){
							return 1;
						}else if(v == Raster.getNoDataValue()){
							return Raster.getNoDataValue();
						}
					}
					return 0;
				}
			};
			classif.addObserver(this);
			Matrix m = classif.allRun();
			//MatrixManager.exportAsciiGridAndVisualize(m, path+"cluster/classification_"+code+"_"+size+".asc");
			return m;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Matrix cluster(Matrix mClassif) {
		//System.out.println("queen");
		try {
			Set<Integer> vcluster = new HashSet<Integer>();
			vcluster.add(1);
			ClusteringQueenAnalysis cqa = new ClusteringQueenAnalysis(mClassif, vcluster);
			cqa.addObserver(this);
			Matrix mCluster = RasterManager.exportMatrix((Raster) cqa.allRun(), mClassif);
			MatrixManager.exportAsciiGrid(mCluster, path+"/cluster_queen.asc");
			return mCluster;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void overlay(Matrix mOccsol, Matrix mCluster){
		//System.out.println("cluster");
		try {
			//Matrix mqueen = JaiMatrixFactory.get().createWithAsciiGridOld(path+"cluster_extension_"+code+"_"+size+".asc", true);
			//String csv = path+"cluster_"+code+"_"+size+".csv";
			String csv = path+"cluster_"+distance+".csv";
			ClusterOverlay co = new ClusterOverlay(interest, csv, mCluster, mOccsol);
			co.addObserver(this);
			//MatrixManager.exportAsciiGridAndVisualize(cy.allRun(), path+"cluster/cluster_"+code+"_"+size+".asc");
			MatrixManager.exportAsciiGrid(co.allRun(), path+"cluster_"+distance+".asc");
		} catch (NumberFormatException /*| IOException*/ e) {
			e.printStackTrace();
		}
	}

	@Override
	public void notify(Analysis ma, AnalysisState state) {
		// do nothing
	}

	@Override
	public void updateProgression(Analysis a, int total) {
		updateProgression(gTotal);
	}


}
