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

import java.util.HashSet;
import java.util.Set;
import fr.inra.sad_paysage.apiland.core.util.Landator;

public class OpTypeLandment extends OpTypeComposition {

	private static final long serialVersionUID = 1L;
	
	private Set<Landment<Landator<?>>> landments;
	
	public OpTypeLandment(){
		super();
		reset();
	}
	
	@Override
	public void reset(){
		super.reset();
		landments = new HashSet<Landment<Landator<?>>>();
	}
	
	private double addition(){
		double total = 0.0;
		for(Landment<?> a : landments){
			total += a.getRate();
		}
		return total;
	}
	
	@Override
	public boolean isValid(){
		double total = addition();
		if(total <= 101.0 && total >= 99.0){
			return super.isValid();
		}
		System.err.println("invalid landment total --> "+total+", must be (around) 100 %");
		return false;
	}

	private void setLandment(Landment<Landator<?>> affectation){
		landments.add(affectation);
	}
	
	public Set<Landment<Landator<?>>> getLandments(){
		return landments;
	}
	
	@Override
	public boolean setParameter(String name, Object value){
		if(name.equalsIgnoreCase("assignment")){
			try{
				setLandment((Landment<Landator<?>>)value);
				return true;
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}
		}
		return super.setParameter(name, value);
	}

	@Override
	public OpLandment getOperation() {
		return new OpLandment(this);
	}
	
	
}
