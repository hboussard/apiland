package fr.inra.sad.bagap.apiland.analysis.matrix.cluster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisObserver;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.matrix.ChamferDistance;
import fr.inra.sad.bagap.apiland.analysis.matrix.MatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.MatrixCalculation;
import fr.inra.sad.bagap.apiland.analysis.matrix.RCMDistanceCalculation;
import fr.inra.sad.bagap.apiland.analysis.matrix.pixel.Pixel2PixelMatrixCalculation;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RasterManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;

public class GroupDistanceAnalysis extends MatrixAnalysis implements AnalysisObserver{
	
	private double[] distances;
	
	private List<Integer> interest;
	
	private List<Double> minimumAreas;
	
	private double minimumTotal;
	
	private String path;
	
	private String name;
	
	private int gTotal;
	
	private Friction frictionMap;
	
	private Matrix frictionMat;
	
	public GroupDistanceAnalysis(Matrix m, double[] distances, List<Integer> interest, List<Double> minimumAreas, double minimumTotal, String path, String name, Friction frictionMap){
		this(m, distances, interest, minimumAreas, minimumTotal, path, name);
		this.frictionMap = frictionMap;		
	}
	
	public GroupDistanceAnalysis(Matrix m, double[] distances, List<Integer> interest, List<Double> minimumAreas, double minimumTotal, String path, String name, Matrix frictionMat){
		this(m, distances, interest, minimumAreas, minimumTotal, path, name);
		this.frictionMat = frictionMat;
	}
	
	public GroupDistanceAnalysis(Matrix m, double[] distances, List<Integer> interest, List<Double> minimumAreas, double minimumTotal, String path, String name){
		super(m);
		
		this.distances = distances;
		this.interest = interest;
		this.minimumAreas = minimumAreas;
		this.minimumTotal = minimumTotal;
		
		if(path == null || path.equalsIgnoreCase("")){
			if(!name.endsWith(".asc")){
				this.name = name+".asc";
			}else{
				this.name = name;
			}
		}else{
			if(!path.endsWith("/")){
				this.path = path+"/";
			}else{
				this.path = path;
			}
			this.name = name;
		}
		
		
		if(frictionMap != null || frictionMat != null){
			gTotal = 3 * matrix().size();
		}else{
			gTotal = 8 * matrix().size();
		}
		
	}
	
	private double getMaxDistance(){
		double d = 0.0;
		for(double distance : distances){
			d = Math.max(d, distance);
		}
		return d;
	}

	@Override
	protected void doRun() {
		try {
			Matrix mD = distance(matrix());
			
			for(double distance : distances){
				Matrix mClassif = classification(mD, distance);
				
				Matrix mCluster = cluster(mClassif);
				
				overlay(matrix(), mCluster, distance);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} 
	}
	
	private Matrix distance(Matrix matrix) {
		//System.out.println("distance "+name);
		
		MatrixCalculation cdistance = null;
		
		if(frictionMap != null){
			cdistance = new RCMDistanceCalculation(matrix, frictionMap, interest, getMaxDistance()/2);
		}else{
			cdistance = new ChamferDistance(matrix, interest);
		}
		
		cdistance.addObserver(this);
		Matrix d = cdistance.allRun();
		//MatrixManager.exportAsciiGrid(d, path+"/distance.asc");
		return d;
	}
	
	private Matrix classification(Matrix mD, double distance) {
		//System.out.println("classification "+name);
		try{
			Pixel2PixelMatrixCalculation classif = new Pixel2PixelMatrixCalculation(mD){
				@Override
				protected double treatPixel(Pixel p) {
					double v = matrix().get(p);
					if(v == Raster.getNoDataValue()){
						return Raster.getNoDataValue();
					}else if(v <= ((distance-Raster.getCellSize())/2)){
						return 1;
					}else{ 
						return 0;
					}
				}
			};
			classif.addObserver(this);
			Matrix m = classif.allRun();
			//MatrixManager.exportAsciiGrid(m, path+"/classification.asc");
			return m;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Matrix cluster(Matrix mClassif) {
		//System.out.println("cluster "+name);
		try {
			Set<Integer> vcluster = new HashSet<Integer>();
			vcluster.add(1);
			ClusteringQueenAnalysis cqa = new ClusteringQueenAnalysis(mClassif, vcluster);
			cqa.addObserver(this);
			Matrix mCluster = RasterManager.exportMatrix((Raster) cqa.allRun(), mClassif);
			//MatrixManager.exportAsciiGrid(mCluster, path+"/cluster_queen.asc");
			return mCluster;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void overlay(Matrix mOccsol, Matrix mCluster, double distance){
		//System.out.println("overlay "+name);
		try {
			String csv = null;
			String ascii = null;
			if(path == null || path.equalsIgnoreCase("")){
				csv = name.replace(".asc", ".csv");
				ascii = name;
			}else{
				String[] splitdistances = new Double(distance).toString().split("\\.");
				String d = splitdistances[0]/*+"-"+splitdistances[1]*/;
				csv = path+"cluster_"+name+"_"+d+".csv";
				ascii = path+"cluster_"+name+"_"+d+".asc";
			}

			GroupOverlay co = new GroupOverlay(interest, minimumAreas, minimumTotal, csv, mCluster, mOccsol);
			co.addObserver(this);
			//MatrixManager.exportAsciiGridAndVisualize(cy.allRun(), path+"cluster/cluster_"+code+"_"+size+".asc");
			MatrixManager.exportAsciiGrid(co.allRun(), ascii);
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
