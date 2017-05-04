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
package fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix;

import java.util.Iterator;
import java.util.Set;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.util.Couple;

public abstract class MatrixDecorator implements Matrix{

	private Matrix decorate;
	
	public MatrixDecorator(Matrix decorate){
		this.decorate = decorate;
	}
	
	@Override
	public double cellsize() {
		return decorate.cellsize();
	}

	@Override
	public void display() {
		decorate.display();
	}

	@Override
	public double get(int x, int y) {
		return decorate.get(x, y);
	}

	@Override
	public double get(Pixel p) {
		return decorate.get(p);
	}

	@Override
	public String getFile() {
		return decorate.getFile();
	}

	@Override
	public int height() {
		return decorate.height();
	}

	@Override
	public void init(double v) {
		decorate.init(v);
	}

	@Override
	public double maxX() {
		return decorate.maxX();
	}

	@Override
	public double maxY() {
		return decorate.maxY();
	}

	@Override
	public double minX() {
		return decorate.minX();
	}

	@Override
	public double minY() {
		return decorate.minY();
	}

	@Override
	public int noDataValue() {
		return decorate.noDataValue();
	}

	@Override
	public void put(int x, int y, double value) {
		decorate.put(x, y, value);
	}

	@Override
	public void put(Pixel p, double value) {
		decorate.put(p, value);
	}

	@Override
	public void put(Raster r, double value) {
		decorate.put(r, value);
	}

	@Override
	public void setFile(String file) {
		decorate.setFile(file);
	}

	@Override
	public Set<Integer> values() {
		return decorate.values();
	}

	@Override
	public void visualize() {
		decorate.visualize();
	}

	@Override
	public int width() {
		return decorate.width();
	}

	@Override
	public Iterator<Pixel> iterator() {
		return decorate.iterator();
	}

	@Override
	public MatrixType getType() {
		return decorate.getType();
	}

	@Override
	public double getActiveArea() {
		return decorate.getActiveArea();
	}

	@Override
	public void getCouples(Matrix horizontals, Matrix verticals) {
		decorate.getCouples(horizontals, verticals);
	}
		
	
	
}
