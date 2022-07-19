package fr.inra.sad.bagap.apiland.analysis.matrix.output;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.MultipleWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.SimpleWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessState;
import fr.inra.sad.bagap.apiland.analysis.process.metric.AbstractMetricOutput;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class InterpolateLinearSplineCsvOutput extends AbstractMetricOutput {
	
	private Matrix matrix;
	
	private CsvWriter out;
	
	private int delta;
	
	private String file;
	
	private int width, height, maxWidth, maxHeight;
	
	private Map<String, Double[]> values_d;
	
	private Map<String, Map<Integer, Double[]>> values;
	
	public InterpolateLinearSplineCsvOutput(Matrix m, String f, int d){
		super();
		this.matrix = m;
		this.file = f;
		this.delta = d;
	}
	
	@Override
	public String toString(){
		return "csv";
	}

	@Override
	public void notify(Analysis ma, AnalysisState s) {
		switch (s){
		case INIT : notifyAnalysisInit((WindowMatrixAnalysis)ma);break;
		case RUNNING : notifyAnalysisRun((WindowMatrixAnalysis)ma);break;
		case FINISH : notifyAnalysisFinish((WindowMatrixAnalysis)ma);break;
		}
	}
	
	private void notifyAnalysisInit(WindowMatrixAnalysis wa) {
		out = new CsvWriter(file);
		out.setDelimiter(';');
		
		width = wa.matrix().width();
		maxWidth = width - ((width-1)%delta) - 1;
		
		height = wa.matrix().height();
		maxHeight = height - ((height-1)%delta) - 1;
		
		values = new TreeMap<String, Map<Integer, Double[]>>();
		values_d = new TreeMap<String, Double[]>();
		
		if(wa.window() instanceof MultipleWindow){
			for(Window w : ((MultipleWindow) wa.window()).windows()){
				for(Metric wm : wa.metrics()){
					values.put("w"+((SimpleWindow) w).diameter()+"_"+wm.getName(), new TreeMap<Integer, Double[]>());
					values.get("w"+((SimpleWindow) w).diameter()+"_"+wm.getName()).put(0, new Double[width]);
					for(int i=0; i<width; i++){
						values.get("w"+((SimpleWindow) w).diameter()+"_"+wm.getName()).get(0)[i] = (double) Raster.getNoDataValue();
					}
					values_d.put("w"+((SimpleWindow) w).diameter()+"_"+wm.getName(), new Double[width]);
				}
			}
		}else{
			for(Metric wm : wa.metrics()){
				values.put(wm.getName(), new TreeMap<Integer, Double[]>());
				values.get(wm.getName()).put(0, new Double[width]);
				for(int i=0; i<width; i++){
					values.get(wm.getName()).get(0)[i] = (double) Raster.getNoDataValue();
				}
				values_d.put(wm.getName(), new Double[width]);
			}
		}
	}
	
	private void notifyAnalysisRun(WindowMatrixAnalysis a) {
		try {
			out.write("X");
			out.write("Y");
			for(String v : values.keySet()){
				out.write(v);
			}
			out.endRecord();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void notifyAnalysisFinish(WindowMatrixAnalysis a) {
		out.close();
	}
	
	@Override
	public void notify(Metric m, String metric, double v, Process p) {
		
		int x = ((WindowMatrixProcess) p).x();
		int y = ((WindowMatrixProcess) p).y();
		//System.out.println("notify "+metric+" "+x+" "+y+" with "+v);
		
		if(!values.get(metric).containsKey(y)){ 
			// affectation des anciennes valeurs
			for(int i=0; i<width; i++){
				values_d.get(metric)[i] = values.get(metric).get(y-delta)[i];
			}
			// initialisation des nouvelles lignes
			for(int yv=1; yv<=delta; yv++){
				values.get(metric).put(y-delta+yv, new Double[width]);
				for(int i=0; i<width; i++){
					values.get(metric).get(y-delta+yv)[i] = (double) Raster.getNoDataValue();
				}
			}
		}
		
		// j'affecte la nouvelle valeur
		values.get(metric).get(y)[x] = v;
	}
	
	private double droite(double v_delta, double v, double yv){
		if(v == Raster.getNoDataValue() || v_delta == Raster.getNoDataValue()){
			return Raster.getNoDataValue();
		}
		return yv*(v-v_delta)/delta + v_delta;
	}
	
	@Override
	public void notify(Process p, ProcessState s) {
		switch (s){
		case DONE : notifyProcessDone((WindowMatrixProcess) p);break;
		}
	}
	
	protected void notifyProcessDone(WindowMatrixProcess p) {
		
		int x = ((WindowMatrixProcess) p).x();
		int y = ((WindowMatrixProcess) p).y();
		//System.out.println("process done "+x+" "+y);
		
		double v;
		int yy, im;
		if(y != 0 && x == 0){ // ecriture des valeurs precedentes
			double[] vv = new double[values.keySet().size()];
			if(y > delta){
				for(int yv=1; yv<=delta; yv++){
					yy = y-2*delta+yv;
					for(int i=0; i<width; i++){
						im = 0;
						for(String m : values.keySet()){
							vv[im++] = values.get(m).get(yy)[i]; 
						}
						write(vv, i, yy);
					}
					for(String m : values.keySet()){
						values.get(m).remove(yy);
					}
				}		
			}else{
				for(int i=0; i<width; i++){
					im = 0;
					for(String m : values.keySet()){
						vv[im++] = values.get(m).get(0)[i];
					}
					write(vv, i, 0);
				}
			}		
		}
		
		for(String m : values.keySet()){ // pour chaque metrique
			//System.out.println("y "+y);
			if(y == 0){ // on est sur la premiere ligne
				if(x == 0){ // on est sur la première valeur de la ligne
					// les autres valeurs sont vides
					for(int i=1; i<width; i++){
						values.get(m).get(0)[i] = (double) Raster.getNoDataValue();
					}
				}else{ // on n'est pas sur la premiere valeur de la ligne
					v = values.get(m).get(0)[x];
					// j'affecte les valeurs horizontales
					for(int xv=1; xv<delta; xv++){
						values.get(m).get(0)[x-delta+xv] = droite(values.get(m).get(0)[x-delta], v, xv);
					}
				}
			}else{ // on n'est pas sur la premiere ligne
				if(x == 0){ // on est sur la première valeur de la ligne
					v = values.get(m).get(y)[0];
					// j'affecte les valeurs verticales 
					for(int yv=1; yv<delta; yv++){
						values.get(m).get(y-delta+yv)[0] = droite(values_d.get(m)[0], v, yv);
					}
				}else{ // on n'est pas sur la premiere valeur de la ligne
					v = values.get(m).get(y)[x];
					// j'affecte les valeurs horizontales
					for(int xv=1; xv<delta; xv++){
						values.get(m).get(y)[x-delta+xv] = droite(values.get(m).get(y)[x-delta], v, xv);
					}
					double vh;
					// j'affecte les valeurs verticales 
					for(int yv=1; yv<delta; yv++){
						vh = droite(values_d.get(m)[x], v, yv);
						values.get(m).get(y-delta+yv)[x] = vh;
						// j'affecte les valeurs intermediaires
						for(int xv=1; xv<delta; xv++){
							values.get(m).get(y-delta+yv)[x-delta+xv] = droite(values.get(m).get(y-delta+yv)[x-delta], vh, xv);
						}
					}
				}
			}
		}
		
		if(y == maxHeight && x == maxWidth){
			double[] vv = new double[values.keySet().size()];
			// ecriture des valeurs precedentes
			for(int yv=1; yv<=delta; yv++){
				yy = y-delta+yv;
				for(int i=0; i<width; i++){
					im = 0;
					for(String m : values.keySet()){
						vv[im++] = values.get(m).get(yy)[i]; 
					}
					write(vv, i, yy);
				}
			}
			// ecriture des valeurs suivantes
			for(int j=maxHeight+1; j<height; j++){
				for(int i=0; i<width; i++){
					im = 0;
					for(String m : values.keySet()){
						vv[im++] = Raster.getNoDataValue();
					}
					write(vv, i, j);
				}
			}
		}
		
	}
	
	private void write(double[] vv, int x, int y){
		try {
			out.write(CoordinateManager.getProjectedX(matrix, x)+"");
			out.write(CoordinateManager.getProjectedY(matrix, y)+"");
			
			for(double v : vv){
				out.write(format(v));
			}
			out.endRecord();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
