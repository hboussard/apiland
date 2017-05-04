package fr.inra.sad_paysage.apiland.window;

import java.util.List;
import fr.inra.sad_paysage.apiland.window.shape.WindowShape;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class CenteredWindow extends SimpleWindow{

	private final int centerX, centerY;
	
	public CenteredWindow(WindowShape shape) {
		super(shape);
		centerX = width()/2;
		centerY = height()/2;
	}
	
	@Override
	public Pixel getPixel(int wx, int wy) {
		return new Pixel(pixel().x() - width()/2 + wx, pixel().y() - height()/2 + wy);
	}

	@Override
	public Pixel getPixel(Pixel wp) {
		return getPixel(wp.x(), wp.y());
	}
	
	@Override
	public double get(Matrix m, int wx, int wy) {
		return m.get(pixel().x() - width()/2 + wx, pixel().y() - height()/2 + wy);
	}

	@Override
	public double get(Matrix m, Pixel wp) {
		return get(m, wp.x(), wp.y());
	}

	@Override
	public boolean accept(int x, int y) {
		return Math.abs(pixel().x()-x) <= width()/2 && Math.abs(pixel().y()-y) <= height()/2;
	}
	
	@Override
	public boolean accept(Pixel p) {
		return accept(p.x(), p.y());
	}

	@Override
	public Pixel toWindow(int x, int y) {
		return new Pixel(x + centerX - pixel().x(), y + centerY - pixel().y());
		//return PixelManager.get(x + centerX - pixel().x(), y + centerY - pixel().y());
	}
	
	@Override
	public int toXWindow(int x) {
		return x + centerX - pixel().x();
	}
	
	@Override
	public int toYWindow(int y) {
		return y + centerY - pixel().y();
	}
	
	@Override
	public int outXWindow(int inX){
		return inX + pixel().x() - centerX;
	}
	
	@Override
	public int outYWindow(int inY){
		return inY + pixel().y() - centerY;
	}
	
	@Override
	public Pixel toWindow(Pixel p) {
		return toWindow(p.x(), p.y());
	}
	
	@Override
	public int size(){
		return shape().size();
	}
	
	@Override
	public int size(int width, int height) {
		int du = 0 - (pixel().y() - height()/2);
		int dd = (pixel().y() + height()/2) - (/*matrix().height()*/ height -1);
		int dl = 0 - (pixel().x() - width()/2);
		int dr = (pixel().x() + width()/2) - (/*matrix().width()*/ width -1);
		if(dl < 0){ // la fenetre "rentre" a gauche
			if(dr < 0){ // la fenetre "rentre" a droite
				if(du < 0){ // la fenetre "rentre" en haut
					if(dd < 0){ // la fenetre "rentre" en bas
						// cas 1 : la fenetre est completement rentree
						return shape().size();
					}else{ // la fenetre "sort" en bas
						// cas 2 : la fenetre "depasse" en bas
						return shape().sizeTrunkDown(dd);
					}
				}else{  // la fenetre "sort" en haut
					if(dd < 0){  // la fenetre "rentre" en bas
						// cas 3 : la fenetre "depasse" en haut
						return shape().sizeTrunkUp(du);
					}else{ // la fenetre "sort" en bas
						// cas 4 : la fenetre "depasse" en haut et en bas
						return shape().sizeTrunkUpDown(du,dd);
					}
				}
			}else{ // la fenetre "sort" a droite
				if(du < 0){ // la fenetre "rentre" en haut
					if(dd < 0){ // la fenetre "rentre" en bas
						// cas 5 : la fenetre "depasse" a droite
						return shape().sizeTrunkRight(dr);
					}else{ // la fenetre "sort" en bas
						// cas 6 : la fenetre "depasse" en bas a droite
						return shape().sizeTrunkDownRight(dd, dr);
					}
				}else{ // la fenetre "sort" en haut
					if(dd < 0){  // la fenetre "rentre" en bas
						// cas 7 : la fenetre "depasse" en haut a droite
						return shape().sizeTrunkUpRight(du, dr);
					}else{ // la fenetre "sort" en bas
						// cas 8 : la fenetre "depasse" de haut en bas a droite
						return shape().sizeTrunkUpDownRight(du, dd, dr);
					}
				}
			}
		}else{ // la fenetre "sort" a gauche
			if(dr < 0){ // la fenetre "rentre" a droite
				if(du < 0){ // la fenetre "rentre" en haut
					if(dd < 0){ // la fenetre "rentre" en bas
						// cas 9 : la fenetre "depasse" a gauche
						return shape().sizeTrunkLeft(dl);
					}else{ // la fenetre "sort" en bas
						// cas 10 : la fenetre "depasse" en bas a gauche
						return shape().sizeTrunkDownLeft(dd, dl);
					}
				}else{ // la fenetre "sort" en haut
					if(dd < 0){ // la fenetre "rentre" en bas
						// cas 11 : la fenetre "depasse" en haut a gauche
						return shape().sizeTrunkUpLeft(du, dl);
					}else{ // la fenetre "sort" en bas
						// cas 12 : la fenetre "depasse" de haut en bas a gauche
						return shape().sizeTrunkUpDownLeft(du, dd, dl);
					}
				}
			}else{ // la fenetre "sort" a droite
				if(du < 0){ // la fenetre "rentre" en haut
					if(dd < 0){ // la fenetre "rentre" en bas
						// cas 13 : la fenetre "depasse" de gauche a doite
						return shape().sizeTrunkLeftRight(dl, dr);
					}else{ // la fenetre "sort" en bas
						// cas 14 : la fenetre "depasse" de gauche a doite en bas
						return shape().sizeTrunkDownLeftRight(dd, dl, dr);
					}
				}else{ // la fenetre "sort" en haut
					if(dd < 0){ // la fenetre "rentre" en bas
						// cas 15 : la fenetre "depasse" de gauche a doite en haut
						return shape().sizeTrunkUpLeftRight(du, dl, dr);
					}else{ // la fenetre "sort" en bas
						// cas 16 : la fenetre "depasse" de gauche a doite et de haut en bas
						return shape().sizeTrunkUpDownLeftRight(du, dd, dl, dr);
					}
				}
			}
		}
	}

	@Override
	public List<Pixel> removeDownList() {
		return shape().removeDownList();
	}
	
	@Override
	public List<Pixel> addDownList() {
		return shape().addDownList();
	}

	@Override
	public List<Pixel> removeHorizontalDownList() {
		return shape().removeHorizontalDownList();
	}

	@Override
	public List<Pixel> addHorizontalDownList() {
		return shape().addHorizontalDownList();
	}

	@Override
	public List<Pixel> removeVerticalDownList() {
		return shape().removeVerticalDownList();
	}

	@Override
	public List<Pixel> addVerticalDownList() {
		return shape().addVerticalDownList();
	}

	
}
