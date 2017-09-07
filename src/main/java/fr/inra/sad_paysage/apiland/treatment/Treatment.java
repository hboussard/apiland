package fr.inra.sad_paysage.apiland.treatment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.inra.sad_paysage.apiland.analysis.Analysis;
import fr.inra.sad_paysage.apiland.analysis.AnalysisObserver;
import fr.inra.sad_paysage.apiland.analysis.AnalysisState;

import java.util.Map.Entry;

/**
 * modeling class of a treatment
 * @author H.Boussard
 */
public abstract class Treatment implements AnalysisObserver{

	/** the name */
	private String name;
	
	/** the treatment manager */
	private TreatmentManager manager;
	
	/** the inputs */
	private Map<String, Object> inputs;
	
	/** the inputs binding */
	private Map<String, Class<?>> inputsBinding;
	
	/** the outputs */
	private Map<String, Object> outputs;
	
	/** the outputs binding */
	private Map<String, Class<?>> outputsBinding;
	
	/** the views */
	private Set<TreatmentObserver> observers;
	
	/** the state of the treatment */
	private TreatmentState state;
	
	public Treatment(String name){
		this(name, GlobalTreatmentManager.get());
	}
	
	public Treatment(String name, TreatmentManager manager){
		this.name = name;
		inputs = new HashMap<String, Object>();
		inputsBinding = new HashMap<String, Class<?>>();
		outputs = new HashMap<String, Object>();
		outputsBinding = new HashMap<String, Class<?>>();
		state = TreatmentState.IDLE;
		observers = new HashSet<TreatmentObserver>();
		manager.addTreatment(this);
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public String getName(){
		return name;
	}
	
	public TreatmentManager manager(){
		return manager;
	}
	
	public void setTreatmentManager(TreatmentManager tm){
		this.manager = tm;
	}
	
	public void addObserver(TreatmentObserver v){
		observers.add(v);
	}
	
	public Set<TreatmentObserver> observers(){
		return observers;
	}
	
	private void notifyViews(){
		TreatmentState s = state;
		for(TreatmentObserver v : observers){
			v.notify(this, s);
		}
	}
	
	@Override
	public void updateProgression(Analysis a, int total){
		for(TreatmentObserver v : observers){
			v.updateProgression(this, total);
		}
	}
	

	@Override
	public void notify(Analysis ma, AnalysisState state) {
		// do nothing
	}
	
	public final void init() throws TreatmentException {
		state = TreatmentState.SETTING;
		notifyViews();
		
		if(!checkInputs()){
			//abort();
			//throw new TreatmentException("inputs exception at treatment '"+this+"'");
		}
		
		// specific init work
		doInit();
		
		state = TreatmentState.INIT;
		notifyViews();
	}
	
	protected abstract void doInit() throws TreatmentException;
	
	public final void run() throws TreatmentException {
		state = TreatmentState.RUNNING;
		notifyViews();
		
		// specific run work
		doRun();
		
		if(!checkOutputs()){
			//abort();
			//throw new TreatmentException("outputs exception at treatment '"+this+"'");
		}
		
		for(Entry<String, Object> e : outputs.entrySet()){
			manager.flow(this, e.getKey(), e.getValue());
		}
		
		state = TreatmentState.DONE;
		notifyViews();
	}
	
	protected abstract void doRun() throws TreatmentException;
	
	public final void close() throws TreatmentException {
		state = TreatmentState.CLOSING;
		notifyViews();
		
		// specific close work
		doClose();
		
		//clear();
		
		state = TreatmentState.FINISH;
		// notifyViews(); --> views have been destroyed
	}
	
	public void clearObservers(){
		observers.clear();
	}
	
	public void clearOutputs(){
		outputs.clear();
		outputsBinding.clear();
	}
		
//		inputs.clear();
//		inputsBinding.clear();
//		outputs.clear();
//		outputsBinding.clear();
//		views.clear();
	
	protected abstract void doClose() throws TreatmentException;
	
	public final void abort(){
		state = TreatmentState.FAILED;
		notifyViews();
		
		// specific abort work
		doAbort();
		
		//clear();
	}

	protected void doAbort() {
		// do nothing
	}
	
	public void allRun() throws TreatmentException {
		init();
		run();
		close();
	}
	
	public boolean hasPort(String port){
		return inputs.containsKey(port) || outputs.containsKey(port);
	}
	
	public Class<?> getPortBinding(String port){
		if(inputsBinding.containsKey(port)){
			return inputsBinding.get(port);
		}else{
			return outputsBinding.get(port);
		}
	}
	
	private boolean checkInputs(){
		for(Entry<String, Object> e : inputs.entrySet()){
			if(e.getValue() == null){
				System.err.println("warning : the input named '"+e.getKey()+"' is not initialized in the treatment '"+this+"'");
				return false;
			}
		}
		return true;
	}
	
	private boolean checkOutputs(){
		for(Entry<String, Object> e : outputs.entrySet()){
			if(e.getValue() == null){
				System.err.println("warning : the output named '"+e.getKey()+"' is not initialized in the treatment '"+this+"'");
				return false;
			}
		}
		return true;
	}
	
	protected Map<String, Object> inputs(){
		return inputs;
	}
	
	protected Map<String, Object> outputs(){
		return outputs;
	}
	
	public void setInput(String s, Object o){
		if(!inputs.containsKey(s)){
			throw new IllegalArgumentException("name error : the input '"+s+"' does not exist in the treatment '"+name+"'");
		}
		if(!inputsBinding.get(s).isAssignableFrom(o.getClass())){
			throw new IllegalArgumentException("cast error : the input '"+s+"' has the following cast '"+inputsBinding.get(s)+"' and not '"+o.getClass()+"'");
		}
		inputs.put(s, o);
	}
	
	public void setOutput(String s, Object o){
		if(!outputs.containsKey(s)){
			throw new IllegalArgumentException("name error : the output '"+s+"' does not exist in the treatment '"+name+"'");
		}
		if(!outputsBinding.get(s).isAssignableFrom(o.getClass())){
			throw new IllegalArgumentException("cast error : the output '"+s+"' has the following cast '"+outputsBinding.get(s)+"' and not '"+o.getClass()+"'");
		}
		outputs.put(s, o);
	}
	
	public Object getInput(String s){
		return inputs.get(s);
	}
	
	public Object getOutput(String s){
		return outputs.get(s);
	}
	
	public void defineInput(String input, Class<?> binding){
		inputs.put(input, null);
		inputsBinding.put(input, binding);
	}
	
	public void defineOutput(String output, Class<?> binding){
		outputs.put(output, null);
		outputsBinding.put(output, binding);
	}
	
	public static void defineLink(Treatment t1, String port1, Treatment t2, String port2){
		GlobalTreatmentManager.get().addLink(t1, port1, t2, port2);
	}
	
	public void displayPorts(){
		displayInputs();
		displayOutputs();
	}
	
	public void displayInputs(){
		System.out.println("inputs of treatment '"+name+"':");
		for(Entry<String, Object> e : inputs.entrySet()){
			System.out.println(e.getKey()+" --> "+e.getValue());
		}
	}
	
	public void displayOutputs(){
		System.out.println("outputs of treatment '"+name+"':");
		for(Entry<String, Object> e : outputs.entrySet()){
			System.out.println(e.getKey()+" --> "+e.getValue());
		}
	}
	
}
