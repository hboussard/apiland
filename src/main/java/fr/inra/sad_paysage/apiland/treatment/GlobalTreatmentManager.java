package fr.inra.sad_paysage.apiland.treatment;

import java.util.Iterator;

public class GlobalTreatmentManager implements TreatmentManager {
	
	private LocalTreatmentManager local;
	
	public static GlobalTreatmentManager manager = new GlobalTreatmentManager();
	
	private GlobalTreatmentManager(){
		local = new LocalTreatmentManager();
	}
	
	public static GlobalTreatmentManager get(){
		return manager;
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
	
}
