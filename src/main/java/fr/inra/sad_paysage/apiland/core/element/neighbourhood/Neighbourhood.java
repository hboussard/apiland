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
package fr.inra.sad_paysage.apiland.core.element.neighbourhood;

import fr.inra.sad_paysage.apiland.core.element.DefaultDynamicFeature;
import fr.inra.sad_paysage.apiland.core.element.DynamicElement;
import fr.inra.sad_paysage.apiland.core.time.Instant;

public class Neighbourhood extends DefaultDynamicFeature {

	private static final long serialVersionUID = 1L;

	private DynamicElement e1, e2;
	
	public Neighbourhood(NeighbourhoodType type, NeighbourElement e1, NeighbourElement e2) {
		super(type);
		e1.addNeighbourhood(this);
		e2.addNeighbourhood(this);
	}
	
	@Override
	public Neighbourhood clone(){
		Neighbourhood clone = (Neighbourhood)super.clone();
		clone.e1 = this.e1;
		clone.e2 = this.e2;
		return clone;
	}
	
	public double length(Instant t){
		return getDefaultRepresentation().getLength(t);
	}

	@Override
	public boolean equals(Object other){
		if(this == other){
			return true;
		}
		if(other instanceof Neighbourhood){
			Neighbourhood v = (Neighbourhood) other;
			if((e1.equals(v.e1) && e2.equals(v.e2))
				|| (e2.equals(v.e1) && e1.equals(v.e2))){
					return true;
				}
		}
		return false;
	}
	
	public void display(Instant t){
		System.out.println(e1+" - "+e2+" : "+length(t));
	}

	public DynamicElement getNeighbour(DynamicElement e){
		if(e1.equals(e)){
			return e2;
		}
		if(e2.equals(e)){
			return e1;
		}
		throw new IllegalArgumentException();
	}
	
	public DynamicElement getNeighbour1(){
		return e1;
	}
	
	public DynamicElement getNeighbour2(){
		return e2;
	}
	
	public void setNeighbour(DynamicElement n){
		if(e1 == null){
			e1 = n;
		}else if(e2 == null){
			e2 = n;
		}else{
			throw new IllegalArgumentException();
		}
	}
	
}
