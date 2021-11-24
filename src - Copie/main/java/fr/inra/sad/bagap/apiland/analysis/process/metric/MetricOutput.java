package fr.inra.sad.bagap.apiland.analysis.process.metric;

import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisObserver;

public interface MetricOutput extends WindowAnalysisObserver {
	
	boolean acceptMetric(String metric);
	
}
