package fr.inra.sad.bagap.apiland.treatment;

public interface TreatmentObserver {

	void notify(Treatment t, TreatmentState s);
	
	void updateProgression(Treatment t, int total);
	
}
