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
package fr.inra.sad.bagap.apiland.simul.operation;

import java.io.Serializable;


public class OperationBuilder implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private OperationType type;
	
	public OperationBuilder(OperationType type){
		this.type = type;
		reset();
	}
	
	public void setType(OperationType type){
		this.type = type;
	}
	
	private void reset(){
		type.reset();
	}
	
	public void setParameter(String name, Object value){
		if(!type.setParameter(name,value)){
			throw new IllegalArgumentException("parameter "+name+" does not exist");
		}
	}
	
	public <O extends Operation> O build(){
		if(type.isValid()){
			O o = (O)type.getOperation();
			reset();
			return o;
		}
		throw new IllegalArgumentException("parameters are not valid !!!");
	}
	
}