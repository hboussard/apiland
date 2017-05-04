package fr.inra.sad_paysage.apiland.capfarm.model;

import java.util.Collection;

public interface Covering {

	void addCover(Cover c);
	
	void addCovers(Collection<Cover> covers);
	
}
