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
package fr.inra.sad.bagap.apiland.core.space.impl.raster;

import java.util.Iterator;

public class MarginSinglePixelIterator implements Iterator<Pixel>{
	
	private Pixel pixel;
	
	private int pos = 0;
	
	public MarginSinglePixelIterator(Pixel pixel){
		this.pixel = pixel;
	}

	public boolean hasNext() {
		if(pos < 8){
			return true;
		}
		return false;
	}

	public Pixel next() {
		if(hasNext()){
			switch(pos){
			case 0 : pos = 1;
			return PixelManager.get(pixel.x(),pixel.y()-1);
			case 1 : pos = 2;
			return PixelManager.get(pixel.x()+1,pixel.y()-1);
			case 2 : pos = 3;
			return PixelManager.get(pixel.x()+1,pixel.y());
			case 3 : pos = 4;
			return PixelManager.get(pixel.x()+1,pixel.y()+1);
			case 4 : pos = 5;
			return PixelManager.get(pixel.x(),pixel.y()+1);
			case 5 : pos = 6;
			return PixelManager.get(pixel.x()-1,pixel.y()+1);
			case 6 : pos = 7;
			return PixelManager.get(pixel.x()-1,pixel.y());
			case 7 : pos = 8;
			return PixelManager.get(pixel.x()-1,pixel.y()-1);
			}
		}
		return null;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
