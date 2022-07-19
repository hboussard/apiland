package fr.inra.sad.bagap.apiland.analysis.process.metric;

import fr.inra.sad.bagap.apiland.analysis.process.Process;

public interface MetricObserver {

	void notify(Metric m, String metric, double value, Process process);
	
}
