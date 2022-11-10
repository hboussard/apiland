package fr.inra.sad.bagap.apiland.treatment;

import java.util.Iterator;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.analysis.AnalysisState;

public class CompositeTreatment extends Treatment implements TreatmentManager {

	private LocalTreatmentManager local;
	
	public CompositeTreatment(String name){
		this(name, GlobalTreatmentManager.get());
	}
	
	public CompositeTreatment(String name, TreatmentManager manager){
		super(name, manager);
		local = new LocalTreatmentManager();
	}
	
	@Override
	public void addTreatment(Treatment t){
		local.addTreatment(this, t);
	}
	
	@Override
	public void addLink(Treatment t1, String port1, Treatment t2, String port2){
		local.addLink(t1, port1, t2, port2);
	}
	
	@Override
	public void flow(Treatment t, String port, Object o){
		local.flow(t, port, o);
	}
	
	@Override
	public Iterator<Treatment> iterator() {
		return local.iterator();
	}

	@Override
	public void clear() {
		local.clear();
	}
	
	@Override
	public void doInit() throws TreatmentException{
		// do nothing
	}
	
	@Override
	public void doRun() throws TreatmentException{
		for(Entry<String, Object> e : inputs().entrySet()){
			flow(this, e.getKey(), e.getValue());
		}
		
		for(Treatment t : this){
			t.allRun();
		}
	}

	@Override
	public void doClose() throws TreatmentException{
		clear();
	}


}
