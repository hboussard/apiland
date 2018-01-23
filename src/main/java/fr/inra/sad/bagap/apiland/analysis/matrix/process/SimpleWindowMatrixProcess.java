package fr.inra.sad.bagap.apiland.analysis.matrix.process;

import java.util.Arrays;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.Window;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessState;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.util.Couple;

/**
 * modeling class of a window process
 * @author H.Boussard
 */
public class SimpleWindowMatrixProcess extends WindowMatrixProcess {
	
	/** the values */
	private double[][] values;
	
	/**
	 * constructor
	 * @param w the refereed window
	 * @param p the position pixel
	 * @param wpt the window process type
	 */
	public SimpleWindowMatrixProcess(Window w, Pixel p, WindowMatrixProcessType wpt){
		super(w, p, wpt);
		values = new double[w.height()][w.width()];
		for(int y=0; y<values.length; y++){
			Arrays.fill(values[y], -1);
		}
	}
	
	public double[][] values(){
		return values;
	}
	
	@Override
	public void init(){
		if(state().equals(ProcessState.IDLE)){
			setMaxSize(window().size(processType().matrix().width(), processType().matrix().height()));
			super.init();
		}
	}
	
	@Override
	public boolean add(int x, int y, double v) {
		
		window().locate(pixel());
		boolean ok = false;
		
		if(window().accept(x, y)){
	
			ok = true;
			init(); // if needed
			addCurrentSize();
			
			int inX = window().toXWindow(x);
			int inY = window().toYWindow(y);
			//System.out.println(pixel().x()+" "+pixel().y()+" "+inX+" "+x+" "+inY+" "+y+" "+v);
			values[inY][inX] = v; // stockage de la valeur au niveau du process
			
			int f = window().filter(inX, inY);
			if(f != 0){
				counting.add(v, inX, inY, f, processType().getHorizontal(null), processType().getVertical(null));
			}
			
			if(currentSize() == maxSize()){
				setState(ProcessState.READY);
			}
		}
		return ok;
	}
	
	public boolean addQuiet(int x, int y, double v) {
		window().locate(pixel());
		boolean ok = false;
		if(window().accept(x, y)){
			ok = true;
			init(); // if needed
			
			addCurrentSize();
			
			int inX = window().toXWindow(x);
			int inY = window().toYWindow(y);
			values[inY][inX] = v; // stockage de la valeur au niveau du process
			
			int f = window().filter(inX, inY);
			if(f != 0){
				counting.add(v, inX, inY, f, processType().getHorizontal(null), processType().getVertical(null));
			}
		}
		return ok;
	}

	@Override
	public void delete(){
		super.delete();
	}

	@Override
	public void down(int delta) {
		//System.out.println("down "+pixel());
		if(pixel().y() < processType().matrix().height()-delta){
			for(int d=0; d<delta; d++){
				down();
			}
			window().locate(pixel()); // localisation de la fenêtre
			setMaxSize(window().size(processType().matrix().width(), processType().matrix().height()));
			
			if(currentSize() == maxSize()){
			//if(y() + delta >= processType().matrix().height()){
				//System.out.println("pass1");
				setState(ProcessState.FINISH);
				//System.out.println("pass2");
			}else{
				setState(ProcessState.INIT);
			}
		}
	}
	
	private void down(){
		
		window().locate(pixel()); // localisation de la fenêtre
		counting.down(); // descente du comptage
		window().reinit();
		
		// descente du process
		if(pixel().y() >= window().height()/2){
			for(int x=0; x<window().width(); x++){ 
				if((pixel().x() - window().width()/2 + x >= 0)
						&& (pixel().x() - window().width()/2 + x < processType().matrix().width())){
					minusCurrentSize();
				}
			}
		}
		
		for(int y=1; y<values.length; y++){
			for(int x=0; x<values[y].length; x++){
				values[y-1][x] = values[y][x];
			}
		}
		
		Arrays.fill(values[values.length-1], -1);
		
		// mise a jour du pixel central
		pixel().setY(pixel().y()+1);
	}
	
	public void downQuiet(int delta, double[][] vs, int place) { 
		if(pixel().y() < processType().matrix().height()-delta){
			for(int d=0; d<delta; d++){
				downQuiet(vs, place);
			}
			
			window().locate(pixel()); // localisation de la fenêtre
			setMaxSize(window().size(processType().matrix().width(), processType().matrix().height()));
		}
	}
	
	private void downQuiet(double[][] vs, int place){
		
		window().locate(pixel()); // localisation de la fenêtre
		counting.down(); // descente du comptage
		window().reinit();
		
		// descente du process
		if(pixel().y() >= window().height()/2){
			for(int x=0; x<window().width(); x++){ 
				if((pixel().x() - window().width()/2 + x >= 0)
						&& (pixel().x() - window().width()/2 + x < processType().matrix().width())){
					minusCurrentSize();
				}
			}
		}
		
		for(int y=1; y<values.length; y++){
			for(int x=0; x<values[y].length; x++){
				values[y-1][x] = values[y][x];
			}
		}
		//Arrays.fill(values[values.length-1], -1);
		
		values[values.length-1] = Arrays.copyOfRange(vs[vs.length-1-place], place, vs[vs.length-1].length-place);
		
		if(place != 0){
			for(int i=0; i<values[values.length-1].length; i++){
				int f = window().filter(i, values.length-1);
				if(f != 0){
					counting.add(values[values.length-1][i], i, values.length-1, f, 
							(i==0)?Raster.getNoDataValue():Couple.get(values[values.length-1][i-1], values[values.length-1][i]),
							Couple.get(values[values.length-2][i], values[values.length-1][i]));
				}
			}
		}
		
		// mise a jour du pixel central
		pixel().setY(pixel().y()+1);
	}

	
}
