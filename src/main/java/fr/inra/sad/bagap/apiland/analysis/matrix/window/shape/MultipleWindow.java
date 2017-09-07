package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape;

import java.util.List;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class MultipleWindow extends Window {

	private Window[] windows;
	
	public MultipleWindow(Window... w) {
		super();
		this.windows = w;
	}
	
	public Window window(){
		return windows[0];
	}
	
	public Window[] windows(){
		return windows;
	}

	@Override
	public void locate(Pixel p) {
		for(Window w : windows){
			w.locate(p);
		}
	}

	@Override
	public void locate(int x, int y) {
		locate(new Pixel(x, y));
	}
	
	@Override
	public int filter(Pixel wp) {
		return windows[0].filter(wp);
	}
	
	@Override
	public int filter(int inpX, int inpY) {
		return windows[0].filter(inpX, inpY);
	}

	@Override
	public int width() {
		return windows[0].width();
	}

	@Override
	public int height() {
		return windows[0].height();
	}
	
	@Override
	public boolean accept(int x, int y) {
		return windows[0].accept(x , y);
	}

	@Override
	public boolean accept(Pixel p) {
		return windows[0].accept(p);
	}

	@Override
	public int size(){
		return windows[0].size();
	}
	
	@Override
	public int size(int width, int height) {
		return windows[0].size(width, height);
	}
	
	@Override
	public Pixel getPixel(int wx, int wy) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Pixel getPixel(Pixel wp) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int outXWindow(int wx) {
		return windows[0].outXWindow(wx);
	}

	@Override
	public int outYWindow(int wy) {
		return windows[0].outYWindow(wy);
	}
	
	@Override
	public Pixel toWindow(int x, int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Pixel toWindow(Pixel p) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int toXWindow(int x) {
		return windows[0].toXWindow(x);
	}

	@Override
	public int toYWindow(int y) {
		return windows[0].toYWindow(y);
	}
	
	@Override
	public double get(Matrix m, int wx, int wy){
		throw new UnsupportedOperationException();
	}

	@Override
	public double get(Matrix m, Pixel wp){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int diameter(){
		throw new UnsupportedOperationException();
	}

	@Override
	public int theoricalSize() {
		return windows[0].theoricalSize();
	}

	@Override
	public List<Pixel> removeDownList() {
		return windows[0].removeDownList();
	}

	@Override
	public List<Pixel> addDownList() {
		return windows[0].addDownList();
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

	@Override
	public void reinit() {
		for(Window w : windows){
			w.reinit();
		}
	}

	@Override
	public void display() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void export(int x, int y, Matrix m, String path) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void infos() {
	}
	
}
