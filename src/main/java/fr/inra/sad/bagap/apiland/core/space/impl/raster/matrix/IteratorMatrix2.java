package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

import java.util.Iterator;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelManager;

public class IteratorMatrix2 implements Iterator<Pixel>{

	private int width;
	
	private int height;
	
	private int x;
	
	private int y;
	
	public IteratorMatrix2(Matrix m){
		width = m.width();
		height = m.height();
		x = 0;
		y = 0;
	}
	
	@Override
	public boolean hasNext() {
		if(y == height-1
				&& x == width-1){
			return false;
		}
		return true;
	}

	@Override
	public Pixel next() {
		if(x == (width-1)){
			x = 0;
			y++;
		}else{
			x++;
		}
		return PixelManager.get(x,y);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
