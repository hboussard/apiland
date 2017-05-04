package fr.inra.sad_paysage.apiland.analysis.matrix.process;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.window.Window;
import fr.inra.sad_paysage.apiland.analysis.process.Process;

public abstract class WindowMatrixProcess extends MatrixProcess {

	/** the refereed window */
	private Window window;
	
	/** the position pixel */
	private Pixel pixel;
	
	public WindowMatrixProcess(Window w, Pixel p, WindowMatrixProcessType wpt){
		super(wpt);
		this.window = w;
		this.pixel = p;
	}
	
	@Override
	public String toString(){
		return pixel.toString();
	}
	
	@Override
	public WindowMatrixProcessType processType(){
		return (WindowMatrixProcessType) super.processType();
	}
	
	@Override
	public int compareTo(Process other) { 
		if(other instanceof SimpleWindowMatrixProcess){
			if(((WindowMatrixProcess) other).pixel.y() > this.pixel.y()){
				return -1;
			}else if(((WindowMatrixProcess) other).pixel.y() < this.pixel.y()){
				return 1;
			}else{
				if(((WindowMatrixProcess) other).pixel.x() > this.pixel.x()){
					return -1;
				}else if(((WindowMatrixProcess) other).pixel.x() < this.pixel.x()){
					return 1;
				}else{
					return window.compareTo(((WindowMatrixProcess) other).window);
				}
			}
		}
		return 1;
	} 
	
	@Override
	public boolean equals(Object other){
		return super.equals(other)
				&& ((WindowMatrixProcess) other).pixel.y() == this.pixel.y() 
				&& ((WindowMatrixProcess) other).pixel.x() == this.pixel.x() 
				&& ((WindowMatrixProcess) other).window.equals(this.window);
	}
	
	/** @return the refereed window */
	public Window window(){
		return window;
	}
	
	/** @return the window width */
	public int width(){
		return window.width();
	}
	
	/** @return the window height */
	public int height(){
		return window.height(); 
	}
	
	/** @return the position pixel */
	public Pixel pixel(){
		return pixel;
	}
	
	protected void setPixel(Pixel p){
		this.pixel = p;
	}
	
	/** @return the x location */
	public int x(){
		return pixel.x();
	}
	
	/** @return the y location */
	public int y(){
		return pixel.y();
	}

	/**
	 * to add a specific value
	 * @param x x pixel position
	 * @param y y pixel position
	 * @param v value
	 * @return true if added
	 */
	public abstract boolean add(int x, int y, double v);
	
	public abstract void down(int delta);
	
}
