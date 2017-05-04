package fr.inra.sad_paysage.apiland.simul;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OutputManager implements Iterable<OutputAnalysis>, Serializable {

	private static final long serialVersionUID = 1L;

	private List<OutputAnalysis> analyses;

	public OutputManager(){
		analyses = new LinkedList<OutputAnalysis>();
	}

	public void add(OutputAnalysis analysis){
		analyses.add(analysis);
	}
	
	@Override
	public Iterator<OutputAnalysis> iterator() {
		return analyses.iterator();
	}
	
	public void init(Simulator s) {
		for(OutputAnalysis output : this){
			output.init(s);
		}
	}
	
	public void init(Scenario s) {
		for(OutputAnalysis output : this){
			output.init(s);
		}
	}
	
	public void init(Simulation s) {
		for(OutputAnalysis output : this){
			output.init(s);
		}
	}
	
	public void calculate(Simulator s){
		for(OutputAnalysis output : this){
			output.calculate(s);
		}
	}
	
	public void calculate(Scenario s){
		for(OutputAnalysis output : this){
			output.calculate(s);
		}
	}
	
	public void calculate(Simulation s){
		for(OutputAnalysis output : this){
			output.calculate(s);
		}
	}
	
	public void close(Simulator s){
		for(OutputAnalysis output : this){
			output.close(s);
		}
	}
	
	public void close(Scenario s){
		for(OutputAnalysis output : this){
			output.close(s);
		}
	}
	
	public void close(Simulation s){
		for(OutputAnalysis output : this){
			output.close(s);
		}
	}

	
}
