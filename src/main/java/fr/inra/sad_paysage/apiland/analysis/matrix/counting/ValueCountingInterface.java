package fr.inra.sad_paysage.apiland.analysis.matrix.counting;

import java.util.Collection;
import java.util.Set;

import fr.inra.sad_paysage.apiland.analysis.matrix.counting.Counting.Count;

public interface ValueCountingInterface {

	Set<Integer> values();
	
	Collection<Count> counts();
	
	int countValue(int v);
	
}
