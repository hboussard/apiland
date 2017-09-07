package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

import java.util.Iterator;

import javax.media.jai.PlanarImage;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelManager;

public class TiledMatrixIterator implements Iterator<Pixel>{
	
	private int width;
	
	private int height;
	
	private int nbYTile;
	
	private int nbXTile;
	
	private int tileWidth;
	
	private int tileHeight;
	
	private int xt;
	
	private int yt;
	
	private int x;
	
	private int y;
	
	public TiledMatrixIterator(PlanarImage pi) {
		nbYTile = pi.getNumYTiles();
		nbXTile = pi.getNumXTiles();
		tileWidth = pi.getTileWidth();
		tileHeight = pi.getTileHeight();
		width = pi.getWidth();
		height = pi.getHeight();
		xt = 0;
		yt = 0;
		x = -1;
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
		if(x == (xt+1)*tileWidth-1 || x == width-1){
			x = xt*tileWidth;
			if(y == (yt+1)*tileHeight-1 || y == height-1){
				if(xt == nbXTile-1){
					xt = 0;
					x = 0;
					yt++;
					y = yt*tileHeight;
					//System.out.println(x+" "+y+" "+xt+" "+yt);
				}else{
					xt++;
					x = xt*tileWidth;
					y = yt*tileHeight;
					//System.out.println(x+" "+y+" "+xt+" "+yt);
				}
			}else{
				y++;
			}
		}else{
			x++;
		}
		return PixelManager.get(x,y);
	}
	
	/*
	for(int yt=0; yt<pi.getNumYTiles(); yt++){
		for(int xt=0; xt<pi.getNumXTiles(); xt++){
			for(int y=yt*pi.getTileHeight(); y<(yt+1)*pi.getTileHeight() && y<height(); y++) {
				for(int x=xt*pi.getTileWidth(); x<(xt+1)*pi.getTileWidth() && x<width(); x++){
					int v = new Double(get(x, y)).intValue();
					if(v != Raster.getNoDataValue()){
						System.out.println(x+" "+y);
						values.add(v);
					}
				}
			}
		}
	}
	*/

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}

