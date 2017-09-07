package fr.inra.sad.bagap.apiland.analysis.matrix.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.metric.AbstractMetricOutput;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class InterpolateLinearSplineAsciiGridOutput extends AbstractMetricOutput {
	
	private String ascii;
	
	private int delta;
	
	private BufferedWriter writer;
	
	private String metric;
	
	private int yGlobal;
	
	private int width, height, maxWidth, maxHeight;
	
	private Double[] values_d;
	
	private Map<Integer, Double[]> values;
	
	public InterpolateLinearSplineAsciiGridOutput(String metric, String f, int d){
		super();
		this.metric = metric;
		this.ascii = f;
		this.delta = d;
		this.yGlobal = -1;
	}
	
	@Override
	public String toString(){
		return "ascii_"+metric;
	}
	
	@Override
	public void notify(Analysis ma, AnalysisState s) {
		switch (s){
		case INIT : notifyAnalysisInit((WindowMatrixAnalysis)ma);break;
		case FINISH : notifyAnalysisFinish((WindowMatrixAnalysis)ma);break;
		}
	}
	
	private void notifyAnalysisInit(WindowMatrixAnalysis wa) {
		try {
			writer = new BufferedWriter(new FileWriter(ascii));
			writer.write("ncols "+wa.matrix().width());
			writer.newLine();
			writer.write("nrows "+wa.matrix().height());
			writer.newLine();
			writer.write("xllcorner "+wa.matrix().minX());
			writer.newLine();
			writer.write("yllcorner "+wa.matrix().minY());
			writer.newLine();
			writer.write("cellsize "+wa.matrix().cellsize());
			writer.newLine();
			writer.write("NODATA_value "+Raster.getNoDataValue());
			writer.newLine();
			
			width = wa.matrix().width();
			maxWidth = width - ((width-1)%delta) - 1;
			
			height = wa.matrix().height();
			maxHeight = height - ((height-1)%delta) - 1;
			
			//System.out.println(width+" ("+maxWidth+"), "+height+" ("+maxHeight+")");
			
			values = new TreeMap<Integer, Double[]>();
			values_d = new Double[width];
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void notifyAnalysisFinish(WindowMatrixAnalysis wa) {
		try {
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean acceptMetric(String metric) {
		return this.metric.equalsIgnoreCase(metric);
	}
	
	private double droite(double v_delta, double v, double yv){
		if(v == Raster.getNoDataValue() || v_delta == Raster.getNoDataValue()){
			return Raster.getNoDataValue();
		}
		return yv*(v-v_delta)/delta + v_delta;
	}
	
	@Override
	public void notify(Metric m, String name, double v, Process p){
		if(acceptMetric(name)){
			int x = ((WindowMatrixProcess) p).x();
			int y = ((WindowMatrixProcess) p).y();
			
			//System.out.println("y "+y);
			if(y == 0){ // on est sur la première ligne
				if(x == 0){ // on est sur la première valeur de la ligne
					values.put(0, new Double[width]);
					// j'affecte la nouvelle valeur
					values.get(0)[0] = v;
					// les autres valeurs sont vides
					for(int i=1; i<width; i++){
						values.get(0)[i] = (double) Raster.getNoDataValue();
					}
				}else{ // on n'est pas sur la première valeur de la ligne
					// j'affecte la nouvelle valeur
					values.get(0)[x] = v;
					// j'affecte les valeurs horizontales
					for(int xv=1; xv<delta; xv++){
						values.get(0)[x-delta+xv] = droite(values.get(0)[x-delta], v, xv);
					}
				}
			}else{ // on n'est pas sur la première ligne
				if(x == 0){ // on est sur la première valeur de la ligne
					
					if(y > delta){
						// écriture des valeurs précédentes
						for(int yv=1; yv<=delta; yv++){
							//System.out.println(index++);
							for(double d : values.get(y-2*delta+yv)){
								write(d, y-2*delta+yv);
							}
						}
					}else{
						// écriture des valeurs précédentes
						//System.out.println(index++);
						for(double d : values.get(0)){
							write(d, 0);
						}
					}
					
					// affectation des anciennes valeurs
					for(int i=0; i<width; i++){
						values_d[i] = values.get(y-delta)[i];
					}
					// remise à zéro
					values.clear();
					for(int yv=1; yv<=delta; yv++){
						values.put(y-delta+yv, new Double[width]);
						for(int i=0; i<width; i++){
							values.get(y-delta+yv)[i] = (double) Raster.getNoDataValue();
						}
					}
					// j'affecte la nouvelle valeur
					values.get(y)[0] = v;
					// j'affecte les valeurs verticales 
					for(int yv=1; yv<delta; yv++){
						values.get(y-delta+yv)[0] = droite(values_d[0], v, yv);
					}
				}else{ // on n'est pas sur la première valeur de la ligne
					// j'affecte la nouvelle valeur
					values.get(y)[x] = v;
					// j'affecte les valeurs horizontales
					for(int xv=1; xv<delta; xv++){
						values.get(y)[x-delta+xv] = droite(values.get(y)[x-delta], v, xv);
					}
					double vh;
					// j'affecte les valeurs verticales 
					for(int yv=1; yv<delta; yv++){
						vh = droite(values_d[x], v, yv);
						values.get(y-delta+yv)[x] = vh;
						// j'affecte les valeurs intermédiaires
						for(int xv=1; xv<delta; xv++){
							values.get(y-delta+yv)[x-delta+xv] = droite(values.get(y-delta+yv)[x-delta], vh, xv);
						}
					}
					if(y == maxHeight && x == maxWidth){
						// écriture des valeurs précédentes
						for(int yv=1; yv<=delta; yv++){
							//System.out.println(index++);
							for(double d : values.get(y-delta+yv)){
								write(d, y-delta+yv);
							}
						}
						// écriture des valeurs suivantes
						for(int j=maxHeight+1; j<height; j++){
							for(int i=0; i<width; i++){
								write(Raster.getNoDataValue(), j);
							}
						}
					}
				}
			}
		}
	}
	
	private void write(double v, int y){
		try {
			if(yGlobal == -1){
				writer.write(format(v)+"");
				yGlobal = 0;
			}else if(y != yGlobal){
				writer.newLine();
				writer.write(format(v));
				yGlobal = y;
			}else{
				writer.write(" "+format(v));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
