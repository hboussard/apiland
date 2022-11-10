package fr.inra.sad.bagap.apiland.analysis.matrix.process;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.MultipleWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessState;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

import java.util.TreeMap;

public class MultipleWindowMatrixProcess extends WindowMatrixProcess {

	/** the values */
	private double[][] values;
	
	public boolean selected;
	
	private Map<Window, SimpleWindowMatrixProcess> processes;

	public MultipleWindowMatrixProcess(Window w, Pixel p, MultipleWindowMatrixProcessType wpt) {
		super(w, p, wpt);
		//System.out.println("création du process en "+p);
		values = new double[w.height()][w.width()];
		for(int y=0; y<values.length; y++){
			Arrays.fill(values[y], -1);
		}
		this.selected = false;
	}
	
	public MultipleWindowMatrixProcess(Window w, Pixel p, MultipleWindowMatrixProcessType wpt, boolean selected) {
		this(w, p, wpt);
		//values = null;
		this.selected = selected;
	}
	
	@Override
	public MultipleWindow window(){
		return (MultipleWindow) super.window();
	}

	public Window[] windows(){
		return window().windows();
	}
	
	public Map<Window, SimpleWindowMatrixProcess> processes(){
		return processes;
	}
	
	public void setProcesses(Map<Window, SimpleWindowMatrixProcess> p){
		this.processes = p;
	}
	
	@Override
	public void calculateMetrics(){
		assert state() == ProcessState.READY;
		for(Entry<Window, SimpleWindowMatrixProcess> p : processes().entrySet()){
			for(Metric m : processType().metrics()){
				((MatrixMetric) m).calculate(p.getValue(), "w"+p.getKey().width()+"_");
			}
			//notifyMetricsObservers("w"+p.getKey().width()+"_");
		}
		setState(ProcessState.DONE);
		return;
	}
	
	@Override
	public void unCalculateMetrics(){
		assert state() == ProcessState.READY;
		for(Entry<Window, SimpleWindowMatrixProcess> p : processes().entrySet()){
			for(Metric m : processType().metrics()){
				((MatrixMetric) m).unCalculate(p.getValue(), "w"+p.getKey().width()+"_");
			}
			//notifyMetricsObservers("w"+p.getKey().width()+"_");
		}
		setState(ProcessState.DONE);
		return;
	}

	@Override
	public void init() {
		if(state().equals(ProcessState.IDLE)){
			//setProcesses(new HashMap<Window, SimpleWindowProcess>(windows().length));
			setProcesses(new TreeMap<Window, SimpleWindowMatrixProcess>());
			SimpleWindowMatrixProcess process;
			for(Window w : windows()){
				process = new SimpleWindowMatrixProcess(w, new Pixel(pixel().x(), pixel().y()), processType());
				//process.clearObservers();
				if(selected){
					process.resetValues();
				}
				process.init();
				process.setMaxSize(w.size(processType().matrix().width(), processType().matrix().height()));
				processes().put(w, process);
			}
			setMaxSize(windows()[0].size(processType().matrix().width(), processType().matrix().height()));
			setState(ProcessState.INIT);
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
			values[inY][inX] = v; // stockage de la valeur au niveau du process
			
			//int place = 0;
			int old = -1;
			for(Window w : windows()){
				/*
				if(old != -1){
					place += (old - w.width()) / 2;
				}*/
				
				if(!processes.get(w).addQuiet(x, y, v)){
					break;
				}
				
				old = w.width();
				
				//System.out.println(w.width());
				/*
				 * int f, inX, inY;
				if(w.accept(x, y)){
					inX = w.toXWindow(x);
					inY = w.toYWindow(y);
					f = w.filter(inX, inY);
					processes().get(w).addCurrentSize();
					if(f != 0){
						processes().get(w).counting().add(v, inX, inY, f, processType().getHorizontal(null), processType().getVertical(null));
					}
				}else{
					break;
				}
				*/
			}
			if(currentSize() == maxSize()){
				setState(ProcessState.READY);
			}
		}
		return ok;
	}
	
	@Override
	public void delete() {
		for(WindowMatrixProcess p : processes().values()){
			p.delete();
			p = null;
		}
		values = null;
		processes().clear();
		setProcesses(null);
	}

	@Override
	public void down(int delta) { 
		if(pixel().y() < processType().matrix().height()-delta){
			for(int d=0; d<delta; d++){
				down();
			}
			/*
			if(pixel().x() == 0){
				System.out.println("--> beguin down multiple");
			}*/
			int place = 0;
			int old = -1;
			for(Entry<Window, SimpleWindowMatrixProcess> p : processes().entrySet()){
				if(old != -1){
					place += (old - p.getKey().width()) / 2;
				}
				p.getValue().downQuiet(delta, values, place);
				old = p.getKey().width();
			}
			/*
			if(pixel().x() == 0){
				System.out.println("--> end down multiple");
			}*/
			
			window().locate(pixel()); // localisation de la fenêtre
			setMaxSize(windows()[0].size(processType().matrix().width(), processType().matrix().height()));
				
			if(currentSize() == maxSize()){
				setState(ProcessState.FINISH);
			}else{
				setState(ProcessState.INIT);
			}
		}
	}
	
	private void down(){
		
		window().locate(pixel()); // localisation de la fenêtre
	
		// descente du process
		if(pixel().y() >= windows()[0].height()/2){
			for(int x=0; x<windows()[0].width(); x++){ 
				if((pixel().x() - windows()[0].width()/2 + x >= 0)
						&& (pixel().x() - windows()[0].width()/2 + x < processType().matrix().width())){
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
	
}
