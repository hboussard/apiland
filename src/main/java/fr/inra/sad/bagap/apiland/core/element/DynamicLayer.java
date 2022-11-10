/*Copyright 2010 by INRA SAD-Paysage (Rennes)

Author: Hugues BOUSSARD 
email : hugues.boussard@rennes.inra.fr

This library is a JAVA toolbox made to create and manage dynamic landscapes.

This software is governed by the CeCILL-C license under French law and
abiding by the rules of distribution of free software.  You can  use,
modify and/ or redistribute the software under the terms of the CeCILL-C
license as circulated by CEA, CNRS and INRIA at the following URL
"http://www.cecill.info".

As a counterpart to the access to the source code and rights to copy,
modify and redistribute granted by the license, users are provided only
with a limited warranty  and the software's author,  the holder of the
economic rights,  and the successive licensors  have only  limited
liability.

In this respect, the user's attention is drawn to the risks associated
with loading,  using,  modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean  that it is complicated to manipulate, and that also
therefore means  that it is reserved for developers and experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards their
requirements in conditions enabling the security of their systems and/or
data to be ensured and,  more generally, to use and operate it in the
same conditions as regards security.

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-C license and that you accept its terms.
*/
package fr.inra.sad.bagap.apiland.core.element;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.element.type.DynamicLayerType;
import fr.inra.sad.bagap.apiland.core.time.Instant;

/**
 * modeling interface of a dynamic layer
 * @author H. BOUSSARD
 *
 * @param <T> the temporal object
 * @param <G> the spatial object
 */
public interface DynamicLayer<E extends DynamicElement> extends DynamicElement, Collection<E>{
	
	@Override
	DynamicLayer<E> clone();
	
	Iterator<E> completeIterator();
	
	Iterator<E> activeIterator(Instant t);
	
	<D extends DynamicFeature> Iterator<D> deepIterator();
	
	<D extends DynamicFeature> Iterator<D> activeDeepIterator(Instant t);
	
	E get(String id);
	
	DynamicFeature getDeep(String id);
	
	boolean contains(String id);
	
	@Override
	DynamicLayerType getType();
	
	void setName(String name);
	
	String getName();
	
	<F extends DynamicElement> Set<F> set(Class<F> theClass); 
	
	
}
