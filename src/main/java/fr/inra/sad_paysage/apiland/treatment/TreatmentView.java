package fr.inra.sad_paysage.apiland.treatment;

public interface TreatmentView {

	void notify(Treatment t, TreatmentState s);
	
	void updateProgression(int total);
	
}
