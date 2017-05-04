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
package fr.inra.sad_paysage.apiland.simul.operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import fr.inra.sad_paysage.apiland.core.element.DynamicElement;
import fr.inra.sad_paysage.apiland.core.element.DynamicFeature;
import fr.inra.sad_paysage.apiland.core.element.DynamicLayer;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.util.Landator;

public class OpLandment extends OpComposition{

	private static final long serialVersionUID = 1L;

	private Set<Landment<Landator<?>>> landments;

	public OpLandment(OpTypeLandment type) {
		super(type);
		this.landments = type.getLandments();
	}

	public Set<Landment<Landator<?>>> getLandments(){
		return landments;
	}
	
	@Override
	public boolean make(Instant t, DynamicElement... e) {
		//double total = getElement().getArea(t);
		double total = e[0].getArea(t);
		double surface;
		//Iterator<DynamicElement> ite = (Iterator<DynamicElement>)((DynamicLayer<?>)getElement()).activeDeepIterator(t);
		Iterator<DynamicFeature> ite = (Iterator<DynamicFeature>)((DynamicLayer<?>)e[0]).activeDeepIterator(t);
		List<DynamicElement> features = new ArrayList<DynamicElement>();
		while(ite.hasNext()){
			features.add(ite.next());
		}
		int rd;
		DynamicElement f;
		double real;
		Landment<Landator<?>> last = null;
		for(Landment<Landator<?>> a : getLandments()){
			last = a;
			surface = total * (a.getRate()/100);
			real = 0.0;
			while(surface > 0 && features.size() != 0){
				rd = new Double(features.size() * Math.random()).intValue();
				f = features.remove(rd);
				f.getAttribute(getAttribute()).setValue(a.getObject(f, t));
				surface -= f.getArea(t);
				real += f.getArea(t);
			}
		}
		while(features.size() != 0){
			f = features.remove(0);
			f.getAttribute(getAttribute()).setValue(last.getObject(f, t));
		}
		
		return true;
	}
	
	@Override
	public void delete(){
		super.delete();
		landments.clear();
		landments = null;
	}


}
