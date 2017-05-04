package fr.inra.sad_paysage.apiland.analysis.matrix.output;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;
import fr.inra.sad_paysage.apiland.analysis.process.Process;
import fr.inra.sad_paysage.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad_paysage.apiland.analysis.matrix.window.VolatileWindowAnalysis;
import fr.inra.sad_paysage.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.analysis.metric.AbstractMetricOutput;
import fr.inra.sad_paysage.apiland.analysis.metric.Metric;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessState;

public abstract class SchedulerOutput extends AbstractMetricOutput{

	private Map<WindowMatrixProcess, Map<String, Double>> processes;
	
	private Map<WindowMatrixProcess, Map<String, Double>> currents;
	
	private Pixel next;
	
	private VolatileWindowAnalysis analysis;
	
	@Override
	public void notifyFromAnalysis(Analysis ma, AnalysisState s) {
		switch (s){
		case INIT : notifyAnalysisInit((WindowMatrixAnalysis)ma);break;
		case FINISH : notifyAnalysisFinish((WindowMatrixAnalysis)ma);break;
		}
	}
	
	private void notifyAnalysisInit(WindowMatrixAnalysis wa) {
		processes = new TreeMap<WindowMatrixProcess, Map<String, Double>>();
		currents = new TreeMap<WindowMatrixProcess, Map<String, Double>>();
		next = new Pixel(0, 0);
		analysis = (VolatileWindowAnalysis)wa;
	}

	private void notifyAnalysisFinish(WindowMatrixAnalysis wa) {
		processes = null;
		currents = null;
	}
	
	@Override
	public void notifyFromProcess(Process p, ProcessState s) {
		if(p instanceof WindowMatrixProcess){
			switch(s){
			case DONE : updateDone((WindowMatrixProcess) p); break;
			}
		}
	}

	private void updateDone(WindowMatrixProcess wp) {
		Map<String, Double> c = currents.remove(wp);
		
		if(c != null){
			processes.put(wp, c);
			Iterator<Entry<WindowMatrixProcess, Map<String, Double>>> ite = processes.entrySet().iterator();
			Entry<WindowMatrixProcess, Map<String, Double>> e;
			while(ite.hasNext()){
				e = ite.next();
				if(e.getKey().x() == next.x() && e.getKey().y() == next.y()){
					
					next = analysis.next(next);
					for(Entry<String, Double> e2 : e.getValue().entrySet()){
						notifyChildMetric(e2.getKey(), e2.getValue(), e.getKey());
					}
					
					notifyProcessDone(e.getKey());
					
					ite.remove();
					
					if(next == null){
						return;
					}
					
				}else{
					
					return;
				}
			}
		}
	}

	protected abstract void notifyChildMetric(String key, double value, WindowMatrixProcess wp);

	@Override
	public void notifyFromMetric(Metric m, String metric, double v, Process p) {
		if(acceptMetric(metric)){
			if(!currents.containsKey((WindowMatrixProcess) p)){
				currents.put((WindowMatrixProcess) p, new HashMap<String, Double>());
			}
			currents.get((WindowMatrixProcess) p).put(metric, v);
		}else{
			//m.removeObserver(this);
		}
	}

	protected abstract void notifyProcessDone(WindowMatrixProcess key);	
	
}
