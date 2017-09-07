package fr.inra.sad.bagap.apiland.analysis.process;

public interface ProcessObserver extends Comparable<ProcessObserver> {

	 void notify(Process p, ProcessState s);
	
}
