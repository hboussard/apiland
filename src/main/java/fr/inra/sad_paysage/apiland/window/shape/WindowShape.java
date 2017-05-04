package fr.inra.sad_paysage.apiland.window.shape;

import java.util.List;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.window.Window;

public abstract class WindowShape {
	
	protected Window window;
	
	public abstract int width();

	public abstract int height();
	
	public void setWindow(Window w){
		this.window = w;
	}
	
	public int diameter(){
		return width();
	}
	
	public void locate(int x, int y) {
		// do nothing
	}
	
	/**
	 * filter return if the pixel is contained in the shape
	 * --> 0 if the pixel is not filtered
	 * --> 1 if the pixel is totally contained in the shape
	 * --> 2 if the pixel is in the north boundary of the shape
	 * --> 3 if the pixel is in the west boundary of the shape
	 * --> 4 if the pixel is in the north-west boundary of the shape
	 * @param wx : x coordinate
	 * @param wy : y coordinate
	 * @return possible values are 0, 1, 2, 3 or 4
	 */
	public int filter(int wx, int wy){
		if(wx > 0 && wx <= width()){
			if(wy > 0 && wy <= height()){
				return 1;
			}
			return 2;
		}
		if(wy > 0 && wy <= height()){
			return 3;
		}
		return 4;
	}	

	/**
	 * the number of filtered pixels
	 * @return the number of filtered pixels
	 */
	public int size() {
		return width() * height();
	}
	
	public int theoricalSize(){
		return width() * height();
	}

	public int sizeTrunkUp(int u) {
		return width() * (height() - u);
	}
	
	public int sizeTrunkDown(int d) {
		return width() * (height() - d);
	}
	
	public int sizeTrunkLeft(int l) {
		return (width() - l) * height();
	}
	
	public int sizeTrunkRight(int r) {
		return (width() - r) * height();
	}
	
	public int sizeTrunkUpDown(int u, int d) {
		return width() * (height() - u - d);
	}
	
	public int sizeTrunkLeftRight(int l, int r) {
		return (width() - l - r) * height();
	}

	public int sizeTrunkUpLeft(int u, int l) {
		return (width() - l) * (height() - u);
	}
	
	public int sizeTrunkUpRight(int u, int r) {
		return (width() - r) * (height() - u);
	}
	
	public int sizeTrunkDownLeft(int d, int l) {
		return (width() - l) * (height() - d);
	}

	public int sizeTrunkDownRight(int d, int r) {
		return (width() - r) * (height() - d);
	}

	public int sizeTrunkUpDownLeft(int u, int d, int l) {
		return (width() - l) * (height() - u - d);
	}
	
	public int sizeTrunkUpDownRight(int u, int d, int r) {
		return (width() - r) * (height() - u - d);
	}

	public int sizeTrunkUpLeftRight(int u, int l, int r) {
		return (width() - l - r) * (height() - u);
	}

	public int sizeTrunkDownLeftRight(int d, int l, int r) {
		return (width() - l - r) * (height() - d);
	}

	public int sizeTrunkUpDownLeftRight(int u, int d, int l, int r) {
		return (width() - l - r) * (height() - u - d);
	}

	public abstract List<Pixel> removeDownList();
	
	public abstract List<Pixel> addDownList();
	
	public abstract List<Pixel> removeHorizontalDownList();
	
	public abstract List<Pixel> addHorizontalDownList();
	
	public abstract List<Pixel> removeVerticalDownList();
	
	public abstract List<Pixel> addVerticalDownList();

	public void reinit() {
		// do nothing
	}
	
	public void close() {
		// do nothing
	}
	
	public abstract void display();

	public abstract void export(int x, int y, Matrix m, String path);

	public abstract void infos();
}
