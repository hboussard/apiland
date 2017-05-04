package fr.inra.sad_paysage.apiland.analysis.metric;

import fr.inra.sad_paysage.apiland.analysis.window.WindowAnalysisObserver;

public interface MetricOutput extends WindowAnalysisObserver {
	
	boolean acceptMetric(String metric);
	
}
