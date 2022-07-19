package fr.inra.sad.bagap.apiland.analysis.matrix.window;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcess;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.process.Process;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessState;
import fr.inra.sad.bagap.apiland.analysis.process.metric.Metric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public class NaiveSelectedWindowMatrixAnalysis extends WindowMatrixAnalysis implements VolatileWindowAnalysis {

	private Set<Pixel> pixels;
	
	private Set<WindowMatrixProcess> processes;
	
	private Set<WindowMatrixProcess> dones;
	
	private boolean update;
	
	private int xp, yp;
	
	private String path;
	
	private boolean exportFilters;
	
	public NaiveSelectedWindowMatrixAnalysis(Matrix m, Window w, WindowMatrixProcessType pType, double minRate, Set<Pixel> pixels, String path, boolean exportFilters) {
		super(m, w, pType);
		Metric.setMinRate(minRate);
		this.pixels = pixels;
		this.path = path;
		this.exportFilters = exportFilters;
		/*
		for(Pixel p : pixels){
			System.out.println("##"+p);
		}
		*/
	}
	
	@Override
	public Pixel next(Pixel pixel){
		Pixel p = PixelManager.get(pixel.x()+1, pixel.y());
		if(matrix().contains(p)){
			return p;
		}
		p = PixelManager.get(0, pixel.y()+1);
		if(matrix().contains(p)){
			return p;
		}
		return null;
	}

	@Override
	public void doInit() {
		update = false;
		if(path != null){
			new File(path+"filters/").mkdirs();
		}
	}

	@Override
	protected void doRun() {
		dones = new HashSet<WindowMatrixProcess>();
		processes = new TreeSet<WindowMatrixProcess>();
		int ypt;
		for(int yt=0; yt<matrix().numYTiles(); yt++){
			if(yt == 0){
				yp = 0;
			}else{
				yp ++;
			}
			ypt = yp;
			for(int xt=0; xt<matrix().numXTiles(); xt++){
				if(xt == 0){
					xp = 0;
				}else{
					xp ++;
				}
				yp = ypt;
				//System.out.println(xt+" "+yt);
				createProcesses(xt, yt);
				//distributeValues(xt, yt);
			}
		}
	}
	
	@Override
	protected void doClose() {
		// do nothing 
	}

	private void createProcesses(int xt, int yt){
		//System.out.println("creation des processus");
		WindowMatrixProcess wp;
		boolean yi = false;
		boolean xi = false;
		int x = xp;
		Pixel p;
		for(; yp<(yt+1)*matrix().tileHeight()+(window().height()/2)+1 && yp<matrix().height(); yp++){
			yi = true;
			
			for(xp = x; xp<(xt+1)*matrix().tileWidth()+(window().width()/2)+1 && xp<matrix().width(); xp++){
				xi = true;
				
				p = PixelManager.get(xp, yp);
				
				if(pixels.contains(p)){
					//System.out.println("création du processus en "+p);
					wp = processType().create(window(), p, true);
					processes.add(wp);
					distributeValues(wp, xt, yt);
				}
			}
		}
		
		if(yi){
			yp --;
		}
		if(xi){
			xp --;
		}
		//System.out.println("fin creation des processus");
	}
	
	private void distributeValues(WindowMatrixProcess wp, int xt, int yt) {
		//System.out.println("distribution des valeurs de la tuile "+xt+" "+yt);
		//System.out.println("nombre de processus : "+processes.size());
		double v;
		int total = pixels.size();
		
		for(int y=Math.max(0, wp.y()-((wp.height()-1)/2)); y<Math.min(matrix().height(), 1+wp.y()+((wp.height()-1)/2)); y++){
			for(int x=Math.max(0, wp.x()-((wp.width()-1)/2)); x<Math.min(matrix().width(), 1+wp.x()+((wp.width()-1)/2)); x++){
				
				//Pixel p = PixelManager.get(x, y);
				//System.out.println("distribution du pixel "+x+" "+y);
				
				v = matrix().get(x, y);
				processType().setValue(x, y, v, null);
				
				wp.add(x, y, v);
			}
		}
		
		//System.out.println("calcul des métriques");
		//wp.calculateMetrics();
		
		update();
		updateProgression(total);
		
		//System.out.println("fin distribution des valeurs");
	}

	private void update(){
		if(update){
			update = false;
			
			if(dones.size() > 0){
				for(WindowMatrixProcess wp : dones){
					processes.remove(wp);
				}
				dones.clear();
			}
		}
		
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void notify(Process p, ProcessState state) {
		if(p instanceof WindowMatrixProcess && p.processType().equals(processType())){
			//System.out.println(state);
			switch(state){
			case INIT : notifyProcessInit((WindowMatrixProcess)p); break;
			case READY : notifyProcessReady((WindowMatrixProcess) p); break;
			case DONE : notifyProcessDone((WindowMatrixProcess)p); break;
			}
		}
	}
	
	private void notifyProcessInit(WindowMatrixProcess wp) {
		update = true;
	}
	
	private void notifyProcessReady(WindowMatrixProcess p) {
		p.calculateMetrics();
		if(path != null){
			Pixel pixel = null;
			for(Pixel pix : pixels){
				if(pix.x() == p.x() && pix.y() == p.y()){
					pixel = pix;
					break;
				}
			}if(exportFilters){
				if(pixel != null){
					p.window().export(pixel, matrix(), path+"filters/");
				}else{
					p.window().export(p.pixel(), matrix(), path+"filters/");
				}
			}
		}
	}

	private void notifyProcessDone(WindowMatrixProcess wp) {
		//System.out.println(wp.getClass());
		dones.add(wp);
		wp.delete();
		//System.out.println(wp.getClass());
		//System.out.println("destruction du processus "+wp.pixel());
		//for(WindowMatrixProcess wmp : processes){
		//	System.out.println(wmp.pixel());
		//}
		//System.out.println("nombre de processus avant : "+processes.size());
		//processes.remove(wp);
		//System.out.println("nombre de processus après : "+processes.size());
		wp = null;
		update = true;
	}

}
