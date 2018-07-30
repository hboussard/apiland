package fr.inra.sad.bagap.apiland.analysis.matrix.process.counting;

import java.util.Collection;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting.Count;

public interface ValueCountingInterface {

	Set<Integer> values();
	
	//Collection<Count> counts();
	
	int countValue(int v);
	
}
