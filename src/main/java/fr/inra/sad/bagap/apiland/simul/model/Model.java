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
package fr.inra.sad.bagap.apiland.simul.model;

import java.io.Serializable;

import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.simul.Simulation;
import fr.inra.sad.bagap.apiland.simul.SimulationManager;
import fr.inra.sad.bagap.apiland.simul.Simulator;

public abstract class Model implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private Instant current;
	
	private DynamicElement element;
	
	private Model parent;
	
	public Model(String name, Simulator simulator){
		this.name = name;
		setCurrent(simulator.manager().start());
	}
	
	public Model(String name, Instant start, Simulator simulator){
		this.name = name;
		setCurrent(start);
	}
	
	public Model(String name, Simulator simulator, DynamicElement element){
		this.name = name;
		setCurrent(simulator.manager().start());
		this.element = element;
	}
	
	public Model(String name, Instant start, Simulator simulator, DynamicElement element){
		this.name = name;
		setCurrent(start);
		this.element = element;
	}
	
	/*@Override
	public String toString(){
		return name;
	}*/
	
	public void setParent(Model parent) {
		this.parent = parent;
	}
	
	public Simulator simulator(){
		return simulation().simulator();
	}
	
	public Simulation simulation(){
		return parent.simulation();
	}
	
	public SimulationManager manager(){
		return simulator().manager();
	}
	
	protected Instant current(){
		return current;
	}
	
	public void setCurrent(Instant t){
		this.current = t;
	}
	
	public void initCurrent(Instant t){
		setCurrent(t);
	}
	
	public String getName(){
		return name;
	}
	
	public DynamicElement getElement(){
		return element;
	}
	
	public void setDynamicElement(DynamicElement element){
		this.element = element;
	}
	
	public boolean deepContains(String name){
		return this.name.equalsIgnoreCase(name);
	}
	
	public Model deepGet(String name){
		if(deepContains(name))return this;
		throw new IllegalArgumentException();
	}

	public abstract boolean run(Instant t);
	
	public void delete(){
		name = null;
		current = null;
	}

}
