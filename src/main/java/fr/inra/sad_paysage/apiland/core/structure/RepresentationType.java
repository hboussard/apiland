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
package fr.inra.sad_paysage.apiland.core.structure;

import java.io.Serializable;
import fr.inra.sad_paysage.apiland.core.space.GeometryType;
import fr.inra.sad_paysage.apiland.core.space.impl.GeometryImplType;

public class RepresentationType implements Serializable{

	private static final long serialVersionUID = 1L;

	protected final String name;
	
	protected final Class<? extends GeometryType> spatial;
	
	private final GeometryImplType geometryType;
	
	public RepresentationType(String name, Class<? extends GeometryType> spatial, GeometryImplType geometryType){
		this.name = name;
		this.spatial = spatial;
		this.geometryType = geometryType;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("representation type : ");
		sb.append(name);
		sb.append(" ( ");
		sb.append("spatial type : ");
		sb.append(spatial.getName());
		sb.append(" )\n");
		return sb.toString();
	}
	
	public String getName() {
		return name;
	}
	
	public Class<? extends GeometryType> getSpatialBinding() {
		return spatial;
	}
	
	public GeometryImplType getGeometryType(){
		return this.geometryType;
	}
	
	@Override
	public boolean equals(Object other){
		if(other==this){
			return true;
		}
		if(other instanceof RepresentationType){	
			return ((RepresentationType)other).name.equalsIgnoreCase(name);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	
}
