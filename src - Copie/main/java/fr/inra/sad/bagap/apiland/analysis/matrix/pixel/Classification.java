package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;

import java.util.Map;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.domain.Domain;

public class Classification extends Pixel2PixelMatrixCalculation {

	private Map<Domain<Double, Double>, Integer> domains;
	
	public Classification(Matrix m, Map<Domain<Double, Double>, Integer> domains){
		super(m);
		this.domains = domains;
	}
	
	@Override
	protected double treatPixel(Pixel p) {
		double v = matrix().get(p);
		//System.out.println(p);
		//if(p.y()%100 == 0 && p.x() == 0){
		//	System.out.println(p.y());
		//}
		if(v != Raster.getNoDataValue()){
			for(Entry<Domain<Double, Double>, Integer> e : domains.entrySet()) {
				if(e.getKey().accept(v)){
					return e.getValue();
				}
			}
		}
		return Raster.getNoDataValue();
	}

}
