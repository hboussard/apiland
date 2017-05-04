package fr.inra.sad_paysage.apiland.treatment;

public interface TreatmentManager extends Iterable<Treatment> {

	void addTreatment(Treatment t);
	
	void addLink(Treatment t1, String port1, Treatment t2, String port2);
	
	void flow(Treatment t, String port, Object o);
	
	void clear();
	
}
