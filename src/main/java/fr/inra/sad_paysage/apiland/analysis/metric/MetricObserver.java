package fr.inra.sad_paysage.apiland.analysis.metric;

import fr.inra.sad_paysage.apiland.analysis.process.Process;

public interface MetricObserver {

	void notify(Metric m, String metric, double value, Process process);
	
}
