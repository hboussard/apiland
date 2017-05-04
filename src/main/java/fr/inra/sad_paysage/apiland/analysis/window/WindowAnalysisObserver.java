package fr.inra.sad_paysage.apiland.analysis.window;

import fr.inra.sad_paysage.apiland.analysis.AnalysisObserver;
import fr.inra.sad_paysage.apiland.analysis.metric.MetricObserver;
import fr.inra.sad_paysage.apiland.analysis.process.ProcessObserver;

public interface WindowAnalysisObserver extends ProcessObserver, AnalysisObserver, MetricObserver {

}
