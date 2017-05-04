package fr.inra.sad_paysage.apiland.analysis.process;

import fr.inra.sad_paysage.apiland.analysis.metric.Metric;
 
public abstract class Process<M extends Metric> implements Comparable<Process<M>> {

	/** the process state */
	private ProcessState state;
	
	/** the process type */
	private ProcessType<M> processType;;
	
	/** constructor */
	public Process(){
		state = ProcessState.IDLE;
	}
	
	/** constructor */
	public Process(ProcessType<M> pt){
		state = ProcessState.IDLE;
		this.processType = pt;
	}
	
	/**
	 * change the state of the process and notify the observers
	 * @param s the new state 
	 */
	public void setState(ProcessState s){
		state = s;
		for(ProcessObserver po : processType.observers()){
			po.notifyFromProcess(this, state);
		}
	}
	
	/**
	 * to get the process type
	 * @return the process type
	 */
	public ProcessType<M> processType(){
		return processType;
	}
	
	/**
	 * to get the state of the process
	 * @return the state of the process
	 */
	public ProcessState state(){
		return state;
	}
	
}
