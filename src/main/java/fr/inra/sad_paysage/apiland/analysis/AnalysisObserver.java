package fr.inra.sad_paysage.apiland.analysis;

public interface AnalysisObserver {
	
	void notifyFromAnalysis(Analysis ma, AnalysisState state);
	
	void updateProgression(int total);

}
