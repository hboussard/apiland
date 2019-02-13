package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import java.util.Set;

public interface ValueCountingInterface {

	Set<Integer> values();
	
	double countValue(int v);
	
	double countClass();
	
}
