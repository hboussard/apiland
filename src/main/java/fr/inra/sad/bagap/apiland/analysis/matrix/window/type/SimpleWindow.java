package fr.inra.sad.bagap.apiland.analysis.matrix.window.type;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.WindowShape;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public abstract class SimpleWindow extends Window {

	private WindowShape shape;
	
	public SimpleWindow(WindowShape ws){
		super();
		this.shape = ws;
		this.shape.setWindow(this);
	}
	
	@Override
	public double[][] weighted(){
		return shape.weighted();
	}
	
	@Override
	public double[][] weightedH(){
		return shape.weightedH();
	}
	
	@Override
	public double[][] weightedV(){
		return shape.weightedV();
	}
	
	@Override
	public void reinit() {
		shape.reinit();
	}
	
	@Override
	public void locate(Pixel p) {
		if(pixel() != p){
			setPixel(p);
			shape.locate(p.x(), p.y());
		}
	}
	
	@Override
	public void locate(int x, int y) {
		locate(new Pixel(x, y));
	}
	
	@Override
	public int filter(Pixel wp){
		return shape.filter(wp.x(), wp.y());
	}
	
	@Override
	public int filter(int x, int y){
		return shape.filter(x, y);
	}
	
	@Override
	public int width(){
		return shape.width();
	}
	
	@Override
	public int height(){
		return shape.height();
	}
	
	public WindowShape shape(){
		return shape;
	}
	
	@Override
	public int diameter(){
		return shape.diameter();
	}

	@Override
	public int theoreticalSize() {
		return shape.theoreticalSize();
	}

	@Override
	public void close() {
		shape.close();
	}
	
	@Override
	public void display() {
		shape.display();
	}

	@Override
	public void export(Pixel p, Matrix m, String path) {
		shape.export(p, m, path);
	}
	
	public void infos(){
		shape.infos();
	}
}
