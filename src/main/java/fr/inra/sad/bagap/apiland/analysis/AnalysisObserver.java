package fr.inra.sad.bagap.apiland.analysis;

public interface AnalysisObserver {
	
	void notify(Analysis ma, AnalysisState state);
	
	void updateProgression(Analysis a, int total);

}
