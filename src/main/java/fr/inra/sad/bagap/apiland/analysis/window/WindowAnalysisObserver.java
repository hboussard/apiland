package fr.inra.sad.bagap.apiland.analysis.window;

import fr.inra.sad.bagap.apiland.analysis.AnalysisObserver;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessObserver;
import fr.inra.sad.bagap.apiland.analysis.process.metric.MetricObserver;

public interface WindowAnalysisObserver extends ProcessObserver, AnalysisObserver, MetricObserver {

}
