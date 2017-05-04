package fr.inra.sad_paysage.apiland.analysis;

import java.util.HashSet;
import java.util.Set;

import fr.inra.sad_paysage.apiland.analysis.AnalysisObserver;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;

public abstract class Analysis {

	private Object result;
	
	private Set<AnalysisObserver> observers;
	
	private AnalysisState state;
	
	public Analysis(){
		this.state = AnalysisState.IDLE;
		this.observers = new HashSet<AnalysisObserver>();
	}
	
	protected void setResult(Object r){
		this.result = r;
	}
	
	public Object getResult(){
		return result;
	}

	public Set<AnalysisObserver> observers(){
		return observers;
	}
	
	public void addObserver(AnalysisObserver o){
		observers.add(o);
	}
	
	private void notifyObservers(){
		AnalysisState s = state;
		for(AnalysisObserver o : observers){
			o.notifyFromAnalysis(this, s);
		}
	}
	
	public final void init(){
		state = AnalysisState.SETTING;
		notifyObservers();
		
		// specific init work
		doInit();
		
		state = AnalysisState.INIT;
		notifyObservers();
	}
	
	protected abstract void doInit();
	
	public final void run(){
		state = AnalysisState.RUNNING;
		notifyObservers();
		
		// specific run work
		doRun();
		
		state = AnalysisState.DONE;
		notifyObservers();
	}
	
	protected abstract void doRun();
	
	public final void close(){
		state = AnalysisState.CLOSING;
		notifyObservers();
		
		// specific close work
		doClose();
		
		state = AnalysisState.FINISH;
		notifyObservers();
	}
	
	protected abstract void doClose();
	
	public final void abort(){
		state = AnalysisState.FAILED;
		notifyObservers();
		
		// specific abort work
		doAbort();
	}

	protected void doAbort() {
		// do nothing
	}
	
	public Object allRun(){
		init();
		run();
		close();
		return result;
	}
	
	/** 
	 * to update the progression of the analysis
	 * @param total the total to aim
	 */
	public void updateProgression(int total){
		for(AnalysisObserver o : observers()){
			o.updateProgression(total);
		}
	}
}
