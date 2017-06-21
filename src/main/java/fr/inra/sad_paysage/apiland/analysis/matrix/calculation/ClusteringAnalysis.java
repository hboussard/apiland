package fr.inra.sad_paysage.apiland.analysis.matrix.calculation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import fr.inra.sad_paysage.apiland.analysis.matrix.MatrixAnalysis;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.PixelComposite;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.RasterComposite;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public abstract class ClusteringAnalysis extends MatrixAnalysis {
	
	private int value;
	
	private Set<Integer> interest;
	
	
	//private int index = 0;

	private int nbPixel = 0;
	
	private int size;
	
	public ClusteringAnalysis(Matrix m, Set<Integer> interest){
		super(m);
		this.interest = interest;
		size = m.height() * m.width();
	}
	
	@Override
	public void doInit() {
		value = Raster.getNoDataValue();
	}	
	
	@Override
	public void doRun() {
		RasterComposite r = new RasterComposite();
		Set<Pixel> ever = new TreeSet<Pixel>();
		double v;
		int oldy = -1;
		int index = 0;
		for(Pixel p : matrix()){
			//System.out.println(p);
			if(p.y() > oldy){ // nettoyage du trop plein 
				Iterator<Pixel> ite = ever.iterator();
				while(ite.hasNext()){
					if(ite.next().y() == oldy){
						ite.remove();
					}else{
						break;
					}
				}
				// fin nettoyage
				oldy = p.y();
			}
			
			if(!ever.contains(p)){
				ever.add(p);
				v = matrix().get(p);
				if(v != Raster.getNoDataValue()){
					if(interest == null || interest.contains((int) v)){
						value = (int) v;
						PixelComposite pc = diffuseFromPixel(matrix(), p, ever, new PixelComposite(p, ++index, value));
						r.addSimplePixelComposite(pc);
					}else{
						value = (int) v;
						//r.addSimplePixelComposite(diffuseFromPixel(matrix(), p, ever, new PixelComposite(p, 0)));
					}
				}
			}
		}
		setResult(r.smooth());
		//setResult(r);
	}

	private PixelComposite diffuseFromPixel(Matrix m, Pixel p, Set<Pixel> ever, PixelComposite pc) {
				
		LinkedList<Pixel> next = new LinkedList<Pixel>();
		next.add(p);
		
		while(!next.isEmpty()){
			Pixel np = next.poll();
			treatPixel(m, np, ever, next);
			pc.addSimplePixel(np);
		}
		
		pc.setUserData(value);
		value = Raster.getNoDataValue();
				
		return pc;
	}
	
	private boolean treatPixel(Matrix m, Pixel p, Set<Pixel> ever, LinkedList<Pixel> next){
		System.out.println(++nbPixel+" / "+size);
		Iterator<Pixel> ite = getMargins(p);
		Pixel np;
		while(ite.hasNext()){
			np = ite.next();
			if(!ever.contains(np) && m.contains(np)){
				if((int) m.get(np) == value){
					next.addLast(np);
					ever.add(np);
				}
			}
		}
		ite = null;
		return true;
	}
	
	/*
	private PixelComposite diffuseFromPixel(Matrix m, Pixel p, Set<Pixel> ever, LinkedList<Pixel> next, PixelComposite pc) {
		//System.out.println(++index+" / "+size);
		Iterator<Pixel> ite = getMargins(p);
		Pixel np;
		while(ite.hasNext()){
			np = ite.next();
			if(!ever.contains(np) && m.contains(np)){
				if((int) m.get(np) == value){
					pc.addSimplePixel(np);
					next.addLast(np);
					ever.add(np);
				}
			}
		}
	
		ite = null;
		
		if(next.size() == 0){
			pc.setUserData(value);
			value = Raster.getNoDataValue();
			return pc;
		}
		
		return diffuseFromPixel(m, next.poll(), ever, next, pc);
	}*/
	
	@Override
	public Raster getResult(){
		return (Raster) super.getResult();
	}

	@Override
	public void doClose() {
		// do nothing
	}
	
	public abstract Iterator<Pixel> getMargins(Pixel p);

	/*
	@Override
	public void doRun() {
		double last, v;
		PixelComposite pc = new PixelComposite();
		Pixel p;
		RasterComposite rc = new RasterComposite();
		Matrix m = matrix();
		// parcours 1 horizontal
		for(int j=0; j<m.height(); j++){
			last = Raster.getNoDataValue();
			for(int i=0; i<m.width(); i++){
				p = PixelManager.get(i, j);
				v = m.get(p);
				
				if(last == Raster.getNoDataValue()){
					if(v != Raster.getNoDataValue()){
						pc.addSimplePixel(p);
					}else if(pc.size() > 0){
						rc.addSimplePixelComposite(pc);
					}
				}else{
					if(last == v){
						pc.addSimplePixel(p);
					}else{
						rc.addSimplePixelComposite(pc);
						pc = new PixelComposite();
						if(v != Raster.getNoDataValue()){
							pc.addSimplePixel(p);
						}
					}
				}
				
				last = v;
			}
			if(pc.size() > 0){
				rc.addSimplePixelComposite(pc);
				pc = new PixelComposite();
			}
		}
		
		
		PixelComposite lastPC;
		// parcours 2 vertical
		for(int i=0; i<m.width(); i++){
			last = Raster.getNoDataValue();
			lastPC = null;
			for(int j=0; j<m.height(); j++){
				p = PixelManager.get(i, j);
				v = m.get(p);
				
				if(last != Raster.getNoDataValue()){
					if(last == v){
						if(lastPC == null){
							for(Raster r : rc.getRasters()){
								if(((PixelComposite) r).containsPixel(PixelManager.get(i, j-1))){
									lastPC = (PixelComposite) r;
									break;
								}
							}	
						}
						if(!lastPC.containsPixel(p)){
							rc.getRasters().remove(lastPC);
							for(Raster r : rc.getRasters()){
								if(((PixelComposite) r).containsPixel(p)){
									pc = (PixelComposite) r;
									break;
								}
							}
							rc.getRasters().remove(pc);
							for(Pixel pp : pc){
								lastPC.addSimplePixel(pp);
							}
							rc.addSimplePixelComposite(lastPC);
						}
					}else{
						lastPC = null;
					}
				}
				
				last = v;
			}
		}
		
		setResult(rc);
	}
	*/
	
}
