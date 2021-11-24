package fr.inra.sad.bagap.apiland.capfarm.model;

import java.util.Collection;

public interface Covering {

	void addCover(Cover c);
	
	void addCovers(Collection<Cover> covers);
	
}
