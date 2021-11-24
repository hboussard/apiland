package fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.patch;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.counting.Counting;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetric;
import fr.inra.sad.bagap.apiland.analysis.process.metric.PatchMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.patch.Patch;
import fr.inra.sad.bagap.apiland.patch.PatchComposite;

public class MeffCUTIndex extends MatrixMetric implements PatchMetric {
	
	public MeffCUTIndex() {
		super(VariableManager.get("MeffCUT"));
	}
	
	@Override
	public void doCalculate(Counting co) {
		
		double surf_ter = co.validValues() * Raster.getCellSize() * Raster.getCellSize();
		
		value = 0;
		double surf_nat;
		for(Patch pa : ((PatchComposite) co.patches()).patches()){
			if(pa.getValue() != 0){
				surf_nat = pa.getArea();
				value += surf_nat * surf_nat;
			}	
		}
		
		value /= surf_ter;
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}
	}
	
	
	/*
	@Override
	public void doCalculate(Counting co) {
		
		double surf_ter = ((WindowMatrixProcess) co.process()).window().size() * Raster.getCellSize();
		
		value = 0;
		Pixel mp, p;
		int X, Y;
		double surf_nat;
		double s_natter;
		double prod_surf;
		p = ((CenteredWindow) ((WindowMatrixProcess) co.process()).window()).pixel();
		//System.out.println("metric : "+((CenteredWindow) ((WindowMatrixProcess) co.process()).window()).pixel());
		for(PatchInterface pa : ((PatchComposite) co.patches()).patches()){
			surf_nat = pa.getArea();
			mp = pa.pixel();
			
			X = ((WindowMatrixProcess) co.process()).window().outXWindow(mp.x());
			Y = ((WindowMatrixProcess) co.process()).window().outYWindow(mp.y());
			s_natter = areas.get(new Pixel(X, Y));
			//s_natter = 6.0;
			
			//System.out.println(mp+" --> "+X+" "+Y);
			//System.out.println("X : "+mp.x()+" --> "+X);
			//System.out.println("Y : "+mp.y()+" --> "+Y);
			//System.out.println(surf_nat+" "+s_natter);
			
			prod_surf = s_natter * surf_nat;
			
			//value += prod_surf;
			value += s_natter;
			//value += surf_nat;
		}
		
		value /= surf_ter;
		
		if(Double.isNaN(value)){
			value = Raster.getNoDataValue();
		}else{
			//System.out.println(value);
		}
		
		//System.out.println("meff = "+value);
	}*/

}
