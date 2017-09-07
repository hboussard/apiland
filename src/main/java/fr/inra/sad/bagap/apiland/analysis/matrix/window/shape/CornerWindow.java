package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape;

import java.util.List;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class CornerWindow extends SimpleWindow {

	public CornerWindow(WindowShape shape) {
		super(shape);
	}

	@Override
	public Pixel getPixel(int wx, int wy) {
		return PixelManager.get(pixel().x() + wx, pixel().y() + wy);
	}
	
	@Override
	public Pixel getPixel(Pixel wp) {
		return getPixel(wp.x(), wp.y());
	}
	
	@Override
	public int outXWindow(int wx) {
		return pixel().x() + wx;
	}

	@Override
	public int outYWindow(int wy) {
		return pixel().y() + wy;
	}
	
	@Override
	public double get(Matrix m, int wx, int wy) {
		return m.get(pixel().x() + wx, pixel().y() + wy);
	}
	
	@Override
	public double get(Matrix m, Pixel wp) {
		return get(m, wp.x(), wp.y());
	}
	
	@Override
	public boolean accept(int x, int y) {
		return x >= pixel().x() && x < pixel().x()+width() 
				&& y >= pixel().y() && y < pixel().y()+height();
	}
	
	@Override
	public boolean accept(Pixel p) {
		return accept(p.x(), p.y());
	}
	
	@Override
	public Pixel toWindow(int x, int y) {
		return PixelManager.get(x - pixel().x(), y - pixel().y());
	}

	@Override
	public Pixel toWindow(Pixel p) {
		return toWindow(p.x(), p.y());
	}
	
	@Override
	public int toXWindow(int x) {
		return x - pixel().x();
	}

	@Override
	public int toYWindow(int y) {
		return y - pixel().y();
	}

	@Override
	public int size(){
		return shape().size();
	}
	
	@Override
	public int size(int width, int height) {
		int du = 0 - pixel().y();
		int dd = (pixel().y() + height()) - (/*matrix().height()*/ height);
		int dl = 0 - pixel().x();
		int dr = (pixel().x() + width()) - (/*matrix().width()*/ width);
		if(dl < 0){ // la fenêtre "rentre" à gauche
			if(dr < 0){ // la fenêtre "rentre" à droite
				if(du < 0){ // la fenêtre "rentre" en haut
					if(dd < 0){ // la fenêtre "rentre" en bas
						// cas 1 : la fenêtre est complétement rentrée
						return shape().size();
					}else{ // la fenêtre "sort" en bas
						// cas 2 : la fenêtre "dépasse" en bas
						return shape().sizeTrunkDown(dd);
					}
				}else{  // la fenêtre "sort" en haut
					if(dd < 0){  // la fenêtre "rentre" en bas
						// cas 3 : la fenêtre "dépasse" en haut
						return shape().sizeTrunkUp(du);
					}else{ // la fenêtre "sort" en bas
						// cas 4 : la fenêtre "dépasse" en haut et en bas
						return shape().sizeTrunkUpDown(du,dd);
					}
				}
			}else{ // la fenêtre "sort" à droite
				if(du < 0){ // la fenêtre "rentre" en haut
					if(dd < 0){ // la fenêtre "rentre" en bas
						// cas 5 : la fenêtre "dépasse" à droite
						return shape().sizeTrunkRight(dr);
					}else{ // la fenêtre "sort" en bas
						// cas 6 : la fenêtre "dépasse" en bas à droite
						return shape().sizeTrunkDownRight(dd, dr);
					}
				}else{ // la fenêtre "sort" en haut
					if(dd < 0){  // la fenêtre "rentre" en bas
						// cas 7 : la fenêtre "dépasse" en haut à droite
						return shape().sizeTrunkUpRight(du, dr);
					}else{ // la fenêtre "sort" en bas
						// cas 8 : la fenêtre "dépasse" de haut en bas à droite
						return shape().sizeTrunkUpDownRight(du, dd, dr);
					}
				}
			}
		}else{ // la fenêtre "sort" à gauche
			if(dr < 0){ // la fenetre "rentre" à droite
				if(du < 0){ // la fenetre "rentre" en haut
					if(dd < 0){ // la fenetre "rentre" en bas
						// cas 9 : la fenêtre "dépasse" à gauche
						return shape().sizeTrunkLeft(dl);
					}else{ // la fenêtre "sort" en bas
						// cas 10 : la fenêtre "dépasse" en bas à gauche
						return shape().sizeTrunkDownLeft(dd, dl);
					}
				}else{ // la fenêtre "sort" en haut
					if(dd < 0){ // la fenetre "rentre" en bas
						// cas 11 : la fenêtre "dépasse" en haut à gauche
						return shape().sizeTrunkUpLeft(du, dl);
					}else{ // la fenêtre "sort" en bas
						// cas 12 : la fenêtre "dépasse" de haut en bas à gauche
						return shape().sizeTrunkUpDownLeft(du, dd, dl);
					}
				}
			}else{ // la fenêtre "sort" à droite
				if(du < 0){ // la fenetre "rentre" en haut
					if(dd < 0){ // la fenetre "rentre" en bas
						// cas 13 : la fenêtre "dépasse" de gauche à doite
						return shape().sizeTrunkLeftRight(dl, dr);
					}else{ // la fenêtre "sort" en bas
						// cas 14 : la fenêtre "dépasse" de gauche à doite en bas
						return shape().sizeTrunkDownLeftRight(dd, dl, dr);
					}
				}else{ // la fenêtre "sort" en haut
					if(dd < 0){ // la fenetre "rentre" en bas
						// cas 15 : la fenêtre "dépasse" de gauche à doite en haut
						return shape().sizeTrunkUpLeftRight(du, dl, dr);
					}else{ // la fenêtre "sort" en bas
						// cas 16 : la fenêtre "dépasse" de gauche à doite et de haut en bas
						return shape().sizeTrunkUpDownLeftRight(du, dd, dl, dr);
					}
				}
			}
		}
	}

	@Override
	public List<Pixel> removeDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> addDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> removeHorizontalDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> addHorizontalDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> removeVerticalDownList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pixel> addVerticalDownList() {
		throw new UnsupportedOperationException();
	}

}
