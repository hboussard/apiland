package fr.inra.sad_paysage.apiland.analysis.matrix.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;
import fr.inra.sad_paysage.apiland.analysis.process.Process;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad_paysage.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad_paysage.apiland.analysis.metric.AbstractMetricOutput;
import fr.inra.sad_paysage.apiland.analysis.metric.Metric;
import fr.inra.sad_paysage.apiland.analysis.metric.MetricOutput;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessState;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;

public class SelectedAsciiGridOutput extends AbstractMetricOutput implements MetricOutput {

	private String ascii;
	
	private BufferedWriter writer;
	
	private String metric;

	private Set<Pixel> pixels;
	
	private Map<Pixel, Double> values;
	
	private int y, nc, nr;
	
	private Pixel pp;
	
	public SelectedAsciiGridOutput(String metric, String f, int w, int h/*, Set<Pixel> p*/){
		super();
		this.metric = metric;
		this.ascii = f;
		this.nc = w;
		this.nr = h;
		this.pixels = new TreeSet<Pixel>();
		this.y = -1;
		values = new TreeMap<Pixel, Double>();
	}
	
	@Override
	public String toString(){
		return "ascii_"+metric;
	}
	
	@Override
	public void notifyFromAnalysis(Analysis ma, AnalysisState s) {
		switch (s){
		case INIT : notifyAnalysisInit((WindowMatrixAnalysis)ma); break;
		case DONE : notifyAnalysisDone((WindowMatrixAnalysis)ma); break;
		case FINISH : notifyAnalysisFinish((WindowMatrixAnalysis)ma); break;
		}
	}
	
	private void notifyAnalysisInit(WindowMatrixAnalysis wa) {
		try {
			writer = new BufferedWriter(new FileWriter(ascii));
			writer.write("ncols "+nc);
			writer.newLine();
			writer.write("nrows "+nr);
			writer.newLine();
			writer.write("xllcorner "+wa.matrix().minX());
			writer.newLine();
			writer.write("yllcorner "+wa.matrix().minY());
			writer.newLine();
			writer.write("cellsize "+wa.matrix().cellsize());
			writer.newLine();
			writer.write("NODATA_value "+Raster.getNoDataValue());
			writer.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void notifyAnalysisDone(WindowMatrixAnalysis wa) {
		if(pixels.size() > 0){
			pp = new Pixel(0, 0);
			Pixel end = new Pixel(nc-1, nr-1);
			Iterator<Pixel> ite = pixels.iterator();
			Pixel p = ite.next();
			while(pp.compareTo(end) <= 0){
				if(p != null && p.equals(pp)){
					write(values.get(p), p.y());
					if(ite.hasNext()){
						p = ite.next();
					}else{
						p = null;
					}
					
				}else{
					write(Raster.getNoDataValue(), pp.y());
				}
				next();
			}
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

	
	@Override
	public void notifyFromProcess(Process p, ProcessState s) {
		if(p instanceof WindowMatrixProcess){
			switch(s){
			case DONE : notifyProcessDone((WindowMatrixProcess) p); break;
			}
		}
	}
	
	protected void notifyProcessDone(WindowMatrixProcess wp) {
		pixels.add(wp.pixel());
	}
	
	
	@Override
	public void notifyFromMetric(Metric m, String metric, double v, Process wp) {
		//if(acceptMetric(metric) && pixels.contains(((WindowProcess) wp).pixel())){
		if(acceptMetric(metric)){
			values.put(((WindowMatrixProcess) wp).pixel(), v);
		}else{
			m.removeObserver(this);
		}
	}

	private void next() {
		if(pp.x() + 1 >= nc){
			pp = new Pixel(0, pp.y() + 1);
		}else{
			pp = new Pixel(pp.x() + 1, pp.y());
		}
	}

	private void write(double v, int py){
		try {
			if(y == -1){
				writer.write(format(v)+"");
				y = 0;
			}else if(py != y){
				writer.newLine();
				writer.write(format(v));
				y = py;
			}else{
				writer.write(" "+format(v));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
