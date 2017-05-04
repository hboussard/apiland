package fr.inra.sad_paysage.apiland.analysis.matrix.calculation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.inra.sad_paysage.apiland.analysis.matrix.MatrixAnalysis;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.PixelComposite;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.RasterComposite;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.MatrixFactory;

public class IdClusteringAnalysis extends MatrixAnalysis {

	private int value; 
	
	public IdClusteringAnalysis(Matrix m){
		super(m);
	}
	
	@Override
	public void doInit() {
		value = Raster.getNoDataValue(); 
	}

	@Override
	public void doRun() {
		RasterComposite r = new RasterComposite();
		Matrix ever = MatrixFactory.get(matrix().getType()).create(matrix());
		ever.init(0);
		for(Pixel p : matrix()){
			if(ever.get(p) == 0){
				ever.put(p, 1);
				if(condition1(matrix(), p)){
					// diffusion
					r.addSimplePixelComposite(diffuseFromPixel(matrix(),p,ever,new ArrayList<Pixel>(),new PixelComposite(p)));
				}
			}
		}
		setResult(r.smooth());
	}
	
	@Override
	public Raster getResult(){
		return (Raster) super.getResult();
	}
	
	private boolean condition1(Matrix m, Pixel p){
		if(m.get(p) != Raster.getNoDataValue()){
			value = (int) m.get(p);
			return true;
		}
		return false;
	}

	private PixelComposite diffuseFromPixel(Matrix m, Pixel p, Matrix ever, List<Pixel> next, PixelComposite pc) {
		Iterator<Pixel> ite = p.getMargins();
		Pixel np;
		while(ite.hasNext()){
			np = ite.next();
			if(ever.get(np) == 0){
				if(m.get(np) == value){
					pc.addSimplePixel(np);
					next.add(np);
					ever.put(np, 1);
				}
			}
		}
		if(next.size() == 0){
			value = Raster.getNoDataValue();
			return pc;
		}
		return diffuseFromPixel(m,next.remove(0),ever,next,pc);
	}
	
	@Override
	public void doClose() {
		// do nothing
	}
	
	
	
}
