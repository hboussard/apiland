package fr.inra.sad_paysage.apiland.analysis.process;

public interface ProcessObserver extends Comparable<ProcessObserver> {

	 void notifyFromProcess(Process p, ProcessState s);
	
}
