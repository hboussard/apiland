package fr.inra.sad.bagap.apiland.analysis.matrix.window;

import java.util.HashSet;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisObserver;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.cluster.Cluster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class WindowMatrixAnalysisBuilder {
	
	private WindowAnalysisType type;
	
	private Set<Matrix> matrix;
	
	private Window window;
	
	private WindowMatrixProcessType processType;
	
	private int displacement;
	
	private int xOrigin;
	
	private int yOrigin;
	
	private Set<WindowAnalysisObserver> observers;
	
	private Matrix matrixFilter;
	
	private Matrix matrixUnFilter;
	
	private Set<Integer> filters;
	
	private Set<Integer> unfilters;
	
	private Set<Pixel> pixels;
	
	private double minRate;
	
	private Set<Cluster> clusters;
	
	private String path;
	
	public WindowMatrixAnalysisBuilder(WindowAnalysisType t) {
		this.type = t;
		observers = new HashSet<WindowAnalysisObserver>();
		filters = new HashSet<Integer>();
		unfilters = new HashSet<Integer>();
		matrix = new HashSet<Matrix>();
		reset();
	}
	
	private void reset(){
		window = null;
		processType = null;
		displacement = 1;
		xOrigin = 0;
		yOrigin = 0;
		minRate = 0;
		observers.clear();
		filters.clear();
		unfilters.clear();
		matrix.clear();
		matrixFilter = null;
		matrixUnFilter = null;
		clusters = null;
		path = null;
	}
	
	public void addMatrix(Matrix m){
		this.matrix.add(m);
	}
	
	public void setMatrix(Set<Matrix> m){
		this.matrix.addAll(m);
	}
	
	public void setWindow(Window w) {
		window = w;
	}
	
	public void setProcessType(WindowMatrixProcessType pt) {
		this.processType = pt;
		//this.processType.deleteObservers();
	}
	
	public void setDisplacement(int d) {
		this.displacement = d;
	}
	
	public void setXOrigin(int xOrigin) {
		this.xOrigin = xOrigin;
	}
	
	public void setYOrigin(int yOrigin) {
		this.yOrigin = yOrigin;
	}
	
	public void setMinRate(double mr){
		this.minRate = mr;
	}
	
	public void setPixels(Set<Pixel> p) {
		this.pixels = p;
	}
	
	public void addObserver(WindowAnalysisObserver o){
		observers.add(o);
	}
	
	public void addFilter(Matrix m){
		matrixFilter = m;
	}
	
	public void addFilter(int f){
		filters.add(f);
	}
	
	public void setFilters(Set<Integer> f){
		filters.addAll(f);
	}
	
	public void addUnFilter(Matrix m){
		matrixUnFilter = m;
	}
	
	public void addUnfilter(int f){
		unfilters.add(f);
	}
	
	public void setUnfilters(Set<Integer> f){
		unfilters.addAll(f);
	}
	
	public void setClusters(Set<Cluster> clusters){
		this.clusters = clusters;
	}

	public void setPath(String path){
		if(path.endsWith("/")){
			this.path = path;
		}else{
			this.path = path+"/";
		}
	}

	public <W extends WindowMatrixAnalysis> W build(){
		W wa = null;
		
		Matrix[] mat = new Matrix[matrix.size()];
		int index = 0;
		for(Matrix m : matrix){
			mat[index++] = m;
		}
		
		switch (type) {
		case SLIDING : 
			wa = (W) new SlidingWindowMatrixAnalysis(mat[0], window, processType, displacement, xOrigin, yOrigin, minRate, filters, unfilters, matrixFilter, matrixUnFilter);
			break;
		case SELECTED : 
			wa = (W) new SelectedWindowMatrixAnalysis(mat[0], window, processType, minRate, pixels, path);
			break;
		case MAP : 
			wa = (W) new MapWindowMatrixAnalysis(mat[0], window, processType, matrixFilter, matrixUnFilter);
			break;
		case GRID : 
			wa = (W) new GridWindowMatrixAnalysis(mat[0], window, processType, displacement, minRate);
			break;
		case CLUSTERED : 
			wa = (W) new ClusteredWindowMatrixAnalysis(mat[0], window, processType, clusters);
			break;
		}
		
		for(WindowAnalysisObserver o : observers){
			wa.addObserver(o); // the analysis is observed 
			processType.addObserver(o); // the process type is observed
			// simple metrics are observed
			for(Metric wm : processType.metrics()){
				wm.addObserver(o);
			}
		}
		
		reset();
		
		return wa;
	}

	
}
