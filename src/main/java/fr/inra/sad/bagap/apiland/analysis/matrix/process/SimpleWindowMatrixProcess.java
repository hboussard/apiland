package fr.inra.sad.bagap.apiland.analysis.matrix.process;

import java.util.Arrays;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
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
		//System.out.println("création du process en "+p+" "+w.getClass()+" "+w.height()+" "+w.width());
		values = new double[w.height()][w.width()];
		for(int y=0; y<values.length; y++){
			Arrays.fill(values[y], Raster.getNoDataValue());
		}
	}
	
	public void resetValues(){
		values = null;
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
		
		//System.out.println("add "+x+" "+y);
		
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
			/*
		if(window().pixel().x() == 0){
			System.out.println(window().diameter()+" "+x+" "+y+" "+v);
		}*/
		
		if(window().accept(x/*+place*/, y/*+place*/)){
		/*
			if(window().pixel().x() == 0){
				System.out.println("accept");
			}*/
			
			ok = true;
			init(); // if needed
			
			addCurrentSize();
			
			int inX = window().toXWindow(x/*+place*/);
			int inY = window().toYWindow(y/*+place*/);
			
			if(values != null){
				values[inY][inX] = v; // stockage de la valeur au niveau du process
			}
			
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
		values = null;
	}

	@Override
	public void down(int delta) {
		//System.out.println("down delta");
		if(pixel().y() < processType().matrix().height()-delta){
			for(int d=0; d<delta; d++){
				doDown(d);
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
	
	private void doDown(int d){
		//System.out.println("down");
		window().locate(pixel()); // localisation de la fenêtre
		
		counting.down(d, 0); // descente du comptage
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
		
		Arrays.fill(values[values.length-1], Raster.getNoDataValue());
		
		// mise a jour du pixel central
		pixel().setY(pixel().y()+1);
	}
	
	public void downQuiet(int delta, double[][] vs, int place) { 
		//System.out.println("down quiet delta");
		if(pixel().y() < processType().matrix().height()-delta){
			for(int d=0; d<delta; d++){
				doDownQuiet(vs, place, d, delta);
			}
			
			window().locate(pixel()); // localisation de la fenêtre
			setMaxSize(window().size(processType().matrix().width(), processType().matrix().height()));
		}
	}
	
	private void doDownQuiet(double[][] vs, int place, int d, int delta){
		//System.out.println("down quiet");
		window().locate(pixel()); // localisation de la fenêtre
		//counting.down(d); // descente du comptage
		/*
		if(window().pixel().x() == 0){
			System.out.println("beguin down counting diameter "+window().diameter());
		}*/
		counting.down(d, place); // descente du comptage
		/*if(window().pixel().x() == 0){
			System.out.println("end down counting diameter "+window().diameter());
		}*/
		//counting.down(-10);
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
		//Arrays.fill(values[values.length-1], Raster.getNoDataValue());
		
		values[values.length-1] = Arrays.copyOfRange(vs[vs.length-1-delta-place+d+1], place, vs[vs.length-1].length-place);
		/*
		if(window().pixel().x() == 0){
			System.out.println("beguin down diameter "+window().diameter());
			System.out.println("parent");
			for(int jj=0; jj<vs.length; jj++){
				for(int ii=0; ii<vs.length; ii++){
					System.out.print(vs[jj][ii]+" ");
				}
				System.out.println();
			}
			System.out.println(window().diameter()+" values "+place+" "+d);
			for(int jj=0; jj<window().diameter(); jj++){
				for(int ii=0; ii<window().diameter(); ii++){
					System.out.print(values[jj][ii]+" ");
				}
				System.out.println();
			}
			
		}*/
		
		//if(place != 0)
		//System.out.println(d);
		
		// mise a jour du pixel central
		//pixel().setY(pixel().y()+1);
		//window().locate(pixel());
		
		if((place >= (1+d)) && (window().pixel().y()+window().diameter()/2+1) < processType().matrix().height()){
		//if(place < d){
			//for(int i=0; i<values[values.length-1].length; i++){
			
			for(int i=(window().pixel().x()-window().diameter()/2 < 0)?window().diameter()/2-window().pixel().x():0; 
				i<((window().pixel().x()+window().diameter()/2 >= processType().matrix().width())?values[values.length-1].length-((window().pixel().x()+window().diameter()/2)-processType().matrix().width())-1:values[values.length-1].length); 
				i++){
			
				int f = window().filter(i, values.length-1);
				if(f != 0){
					/*
					if(window().diameter() == 5 && window().pixel().x() == 0){
						System.out.println("la");
					}*//*
					if(window().pixel().x() == 0){
						System.out.println("y "+(values.length-1));
					}*/
					counting.add(values[values.length-1][i], i, values.length-1, f, 
							(i==0)?Raster.getNoDataValue():Couple.get(values[values.length-1][i-1], values[values.length-1][i]),
							Couple.get(values[values.length-2][i], values[values.length-1][i]));
					
				}
			}
		}
		/*
		if(window().pixel().x() == 0){
			System.out.println("end down diameter "+window().diameter());
		}*/
	
		
		// mise a jour du pixel central
		pixel().setY(pixel().y()+1);
	}

	
}
