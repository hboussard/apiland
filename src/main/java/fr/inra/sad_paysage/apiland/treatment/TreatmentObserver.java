package fr.inra.sad_paysage.apiland.treatment;

public interface TreatmentObserver {

	void notify(Treatment t, TreatmentState s);
	
	void updateProgression(Treatment t, int total);
	
}
