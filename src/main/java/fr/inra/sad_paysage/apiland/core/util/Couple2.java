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
package fr.inra.sad_paysage.apiland.core.util;

/**
 * modeling class of a couple of values
 * @author Hugues BOUSSARD
 */
public class Couple2 implements Comparable<Couple2>{

	/**
	 * values
	 */
	public int v1, v2;
	
	/**
	 * constructor
	 * @param v1 : value 1
	 * @param v2 : value 2
	 */
	protected Couple2(int v1, int v2){
		this.v1 = Math.min(v1, v2);
		this.v2 = Math.max(v1, v2);
	}
	
	@Override
	public String toString(){
		return v1+"/"+v2;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Couple2)){
			return false;
		}
		Couple2 c = (Couple2) o;
		if(c.v1 == this.v1 && c.v2 == this.v2){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean equals(int v1, int v2){
		return (this.v1 == Math.min(v1, v2)) && (this.v2 == Math.max(v1,v2));
	}
	
	@Override
	public int compareTo(Couple2 c){
		if(c.v1 > this.v1){
			return -1;
		}
		else if(c.v1 < this.v1){
			return 1;
		}
		else{
			if(c.v2 > this.v2){
				return -1;
			}
			else if(c.v2 < this.v2){
				return 1;
			}
			else{
				return 0;
			}
		}
	}
	
	/**
	 * retourne si un couple est valide
	 * @param noData : valeur de noData
	 * @return vrai si le couple est valide
	 */
	public boolean valide(int noData){
		if(v1 == 0 || v1 == noData){
			return false;
		}
		if(v2 == 0 || v2 == noData){
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * to get the other value
	 * @param v : the given value
	 * @return the other value
	 */
	public int getOther(int v){
		if(v == v1){
			return v2;
		}
		else return v1;
	}
}
