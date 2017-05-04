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
package fr.inra.sad_paysage.apiland.core.space.impl.raster;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class PixelManager{

	private static Map<Integer,Set<Pixel>> pixels = new TreeMap<Integer,Set<Pixel>>();
	
	private static final int modulo = 500;
	
	private static final int max = 100;
	
	private static int size;
	
	private static void clear(){
		pixels.clear();
		size = 0;
	}
	
	public static Pixel get(int x, int y){
		int n = (x*y)%modulo;
		
		if(size++ >= max){
			clear();
		}else if(pixels.containsKey(n)){
			for(Pixel p : pixels.get(n)){
				if(p.equals(x, y)){
					return p;
				}
			}
			Pixel p = new Pixel(x,y);
			pixels.get(n).add(p);
			return p;
		}
		
		pixels.put(n, new TreeSet<Pixel>());
		Pixel p = new Pixel(x,y);
		pixels.get(n).add(p);
		return p;
	}
	
	public static Pixel get(int x, int y, String id, double X, double Y){
		return new PixelWithID(x, y, id, X, Y);
	}
	
}
