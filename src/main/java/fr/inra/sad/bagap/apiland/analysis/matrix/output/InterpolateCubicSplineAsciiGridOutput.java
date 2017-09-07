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
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class InterpolateCubicSplineAsciiGridOutput extends SchedulerOutput {
	
	private String ascii;
	
	private int delta;
	
	private BufferedWriter writer;
	
	private String metric;
	
	private int yGlobal;
	
	private int width, maxWidth, height, maxHeight;
	
	private Map<Integer, Double[]> values;
	
	private Double[] line;
	
	/** private counting class */
	private static class Count { int n = 0; }
	
	private Map<Integer, Count> vhIndex;
	
	public InterpolateCubicSplineAsciiGridOutput(String metric, String f, int d){
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
		super.notify(ma, s);
		switch (s){
		case INIT : notifyAnalysisInit((WindowMatrixAnalysis)ma);break;
		case FINISH : notifyAnalysisFinish((WindowMatrixAnalysis)ma);break;
		}
	}
	
	private void notifyAnalysisInit(WindowMatrixAnalysis wa) {
		try {
			writer = new BufferedWriter(new FileWriter(ascii));
			writer.write("ncols "+wa.matrix().width()+"\n");
			writer.write("nrows "+wa.matrix().height()+"\n");
			writer.write("xllcorner "+wa.matrix().minX()+"\n");
			writer.write("yllcorner "+wa.matrix().minY()+"\n");
			writer.write("cellsize "+wa.matrix().cellsize()+"\n");
			writer.write("NODATA_value "+Raster.getNoDataValue()+"\n");
			
			width = wa.matrix().width();
			maxWidth = width - ((width-1)%delta) - 1;
			
			height = wa.matrix().height();
			maxHeight = height - ((height-1)%delta) - 1;
			
			//System.out.println(width+" ("+maxWidth+"), "+height+" ("+maxHeight+")");
			
			line = new Double[width];
			for(int i=0; i<width; i++){
				line[i] = (double) Raster.getNoDataValue();
			}
			vhIndex = new TreeMap<Integer, Count>();
			values = new TreeMap<Integer, Double[]>();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void notifyAnalysisFinish(WindowMatrixAnalysis wa) {
		try {
			writer.write('\n');
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean acceptMetric(String metric) {
		return this.metric.equalsIgnoreCase(metric);
	}
	
	@Override
	protected void notifyChildMetric(String m, double v, WindowMatrixProcess p) {
		
		int x = ((WindowMatrixProcess) p).x();
		int y = ((WindowMatrixProcess) p).y();
		
		//System.out.println(x+" "+y+" "+v);
		
		if(x == 0){
			if(y >= delta){
				for(int j=y-delta+1; j<y; j++){
					//System.out.println("init vhIndex avec y = "+j);
					vhIndex.put(j, new Count());
				}
			}
			//System.out.println("init vhIndex avec y = "+y);
			vhIndex.put(y, new Count());
		}
		
		if(!values.containsKey(y)){
			if(y >= delta){
				for(int j=y-delta+1; j<y; j++){
					values.put(j, line.clone());
				}
			}
			values.put(y, line.clone());
		}
		// affectation de la nouvelle valeur
		values.get(y)[x] = v;
		if(v != Raster.getNoDataValue()){
			vhIndex.get(y).n++;
		}
		
		if(x == maxWidth && y >= 3*delta){ // en fin de ligne
			
			// interpollation de lignes verticales
			interpolateVerticalLines(y);
			
			// interpolation des lignes horizontales
			interpolateHorizontalLines(y);
			
			for(int j=y-3*delta; j<y-2*delta; j++){
				values.remove(j);
				vhIndex.remove(j);
			}
		}
	}
	
	private void interpolateHorizontalLines(int y) {
		if(y == 3*delta){
			for(int j=0; j<=delta; j++){
				interpolateHorizontalLine(j);
			}
		}
		
		for(int j=y-2*delta+1; j<=y-delta; j++){
			interpolateHorizontalLine(j);
		}
		
		if(y == maxHeight){
			for(int j=y-delta+1; j<=y; j++){
				interpolateHorizontalLine(j);
			}
			for(int j=y+1; j<height; j++){
				vhIndex.put(j, new Count());
				interpolateHorizontalLine(j);
			}
		}
	}

	private void interpolateVerticalLines(int y) {
		int[] yy;
		double[] ff;
		double v;
		SplineCubic spline;
		for(int i=0; i<width; i+=delta){
			yy = new int[4];
			ff = new double[4];
			
			yy[0] = y-3*delta;
			v = values.get(y-3*delta)[i];
			if(v == Raster.getNoDataValue()){
				continue;
			}
			ff[0] = v;
			
			yy[1] = y-2*delta;
			v = values.get(y-2*delta)[i];
			if(v == Raster.getNoDataValue()){
				continue;
			}
			ff[1] = v;
			
			yy[2] = y-delta;
			v = values.get(y-delta)[i];
			if(v == Raster.getNoDataValue()){
				continue;
			}
			ff[2] = v;
			
			yy[3] = y;
			v = values.get(y)[i];
			if(v == Raster.getNoDataValue()){
				continue;
			}
			ff[3] = v;
			
			spline = new SplineCubic(yy, ff);
			
			if(y == 3*delta){ // remplir les premières lignes verticales
				for(int j=1; j<delta; j++){
					values.get(j)[i] = spline.spline_positive_value(j);
					vhIndex.get(j).n++;
				}
			}
				
			// remplir les lignes verticales
			for(int j=y-2*delta+1; j<y-delta; j++){
				values.get(j)[i] = spline.spline_positive_value(j);
				vhIndex.get(j).n++;
			}
				
			//System.out.println(y+" "+maxHeight);
			if(y == maxHeight){
				//System.out.println("rempli les dernière lignes");
				// remplir les dernières lignes verticales
				for(int j=y-delta+1; j<y; j++){
					values.get(j)[i] = spline.spline_positive_value(j);
					vhIndex.get(j).n++;
				}
			}
		}
	}

	
	private void interpolateHorizontalLine(int y) {
		
		//System.out.println("ligne y "+y);
		
		int vi = vhIndex.get(y).n;
		
		//System.out.println("ligne y "+y+", nombre d'éléments = "+vi);
		
		if(vi > 3){
			int[] xx = new int[vi];
			double[] ff = new double[vi];
			double v;
			int index = 0;
			for(int i=0; i*delta<width; i++){
				v = values.get(y)[i*delta];
				if(v != Raster.getNoDataValue()){	
					//System.out.println("ligne y "+y+", nombre d'éléments = "+vi+" --> "+index);
					xx[index] = i*delta;
					ff[index] = v;
					index++;
				}
			}
			SplineCubic spline = new SplineCubic(xx, ff);
			//System.out.println(y+" : ");
			for(int i=0; i<width; i++){
				//System.out.print(spline.spline_value(i)+" ");
				write(spline.spline_positive_value(i), y);
			}
		}else{
			for(int i=0; i<width; i++){
				//System.out.print(spline.spline_value(i)+" ");
				write(Raster.getNoDataValue(), y);
			}
		}
	}

	private void write(double v, int y){
		try {
			if(yGlobal == -1){
				writer.write(format(v)+"");
				yGlobal = 0;
			}else if(y != yGlobal){
				writer.write("\n"+format(v));
				yGlobal = y;
			}else{
				writer.write(" "+format(v));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void notifyProcessDone(WindowMatrixProcess key) {
		// do nothing
	}

	
}
