package fr.inra.sad_paysage.apiland.analysis.process;

public interface ProcessObserver extends Comparable<ProcessObserver> {

	 void notify(Process p, ProcessState s);
	
}
