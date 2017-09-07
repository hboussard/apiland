package fr.inra.sad.bagap.apiland.core.util;

import java.io.Serializable;
import java.util.Collection;

public interface LandManager<L extends Landator<?>> extends Serializable {
	
	Collection<L> gets();
	
	int size();
	
	void clear();
	
}
