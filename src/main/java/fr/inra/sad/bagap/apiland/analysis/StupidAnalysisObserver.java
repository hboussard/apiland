package fr.inra.sad.bagap.apiland.analysis;


public class StupidAnalysisObserver implements AnalysisObserver{

	@Override
	public void notify(Analysis ma, AnalysisState state) {
		System.out.println(state);
	}

	@Override
	public void updateProgression(Analysis a, int total) {
		// do nothing
	}

}
