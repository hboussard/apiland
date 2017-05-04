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
package fr.inra.sad_paysage.apiland.core.time;

import java.util.List;

/**
 * modeling class of a instant collection
 * @author H. BOUSSARD
 */
public class MultiInstant extends ComplexTime<Instant> {
	
	/**
	 * version number
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * constructor
	 */
	public MultiInstant(){
		super();
	}
	
	/**
	 * constructor
	 * @param l list of instants
	 */
	public MultiInstant(List<Instant> l){
		super(l);
	}
	
	@Override
	public MultiInstant clone(){
		return (MultiInstant)super.clone();
	}
	
	@Override
	protected boolean withinInstant(Instant t){
		return times.size() == 1 && times.get(0).isActive(t);
	}
	
	@Override
	protected boolean crossesInstant(Instant t) {
		return this.isActive(t) && this.times.size() != 1;
	}
	
	@Override
	public Time addTime(Time t){
		return t.addMultiInstant(this);
	}
	
	@Override
	protected Time addInstant(Instant t) {
		if(this.contains(t)){
			return this.clone();
		}
		MultiInstant mi = this.clone();
		mi.add(t);
		return mi.smooth();
	}
	
	@Override
	protected Time addInterval(Interval t){
		ComplexTime<Time> time = new ComplexTime<Time>();
		for(Instant tt : this){
			time.add(tt);
		}
		time.add(t);
		return time.smooth();
	}
	
	@Override
	protected <TT extends Time> Time addComplexTime(ComplexTime<TT> t){
		ComplexTime<TT> ct = t.clone();
		for(Instant time  : this){
			ct.add((TT)time);
		}
		return ct.smooth();
	}
	
	@Override
	protected Time addMultiInstant(MultiInstant t) {
		MultiInstant mi = this.clone();
		for(Instant i : t){
			mi.add(i);
		}
		return mi.smooth();
	}
	
	@Override
	protected Time addMultiInterval(MultiInterval t) {
		return t.addMultiInstant(this);
	}
	
	
	
}
