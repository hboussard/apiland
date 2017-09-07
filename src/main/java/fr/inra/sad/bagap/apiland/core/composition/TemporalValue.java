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
package fr.inra.sad.bagap.apiland.core.composition;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Temporal;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.TimeException;

public class TemporalValue<O extends Serializable> implements Temporal{

	private static final long serialVersionUID = 1L;
	
	private Time temporal;
	
	private O value;

	public TemporalValue(Time t, O o){
		temporal = t;
		value = o;
	}
	
	@Override
	public TemporalValue<O> clone(){
		try{
			TemporalValue<O> clone = (TemporalValue<O>)super.clone();
			clone.temporal = this.temporal.clone();
			try{
				java.lang.reflect.Method m = value.getClass().getMethod("clone", null);
				clone.value = (O)m.invoke(value, null);
			}catch(NoSuchMethodException ex){
				clone.value = value;
			}catch(IllegalAccessException ex){
				ex.printStackTrace();
			}catch(InvocationTargetException ex){
				ex.printStackTrace();
			}	
			return clone;
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String toString(){
		return "["+temporal.toString()+", "+value.toString()+"]";
	}
	
	public O getValue(){
		return value;
	}
	
	protected void setValue(O v){
		this.value = v;
	}
	
	@Override
	public Time getTime() {
		return temporal;
	}

	@Override
	public boolean isActive(Instant t) {
		return temporal.isActive(t);
	}

	@Override
	public void kill(Instant t) throws TimeException {
		temporal = (Time)Time.kill(temporal,t);
	}

	@Override
	public void setTime(Time t) {
		temporal = t;
	}
	
	public void delete(){
		temporal = null;
		value = null;
	}

}
