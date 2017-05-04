package fr.inra.sad_paysage.apiland.analysis;


public class StupidAnalysisObserver implements AnalysisObserver{

	@Override
	public void notifyFromAnalysis(Analysis ma, AnalysisState state) {
		System.out.println(state);
	}

	@Override
	public void updateProgression(int total) {
		// do nothing
	}

}
