package fr.inrae.act.bagap.raster;

import java.awt.Rectangle;

public class TabCoverage extends Coverage {
	
	public float[] datas;
	
	public TabCoverage(float[] datas, EnteteRaster entete){
		super(entete);
		this.datas = datas;
	}

	@Override
	public float[] getDatas(Rectangle roi) {
		if(roi.x == 0 && roi.y == 0 && (roi.width*roi.height) == datas.length){
			return datas;	
		}else{
			//System.out.println(roi.x+" "+roi.y+" "+roi.width+" "+roi.height);
			throw new IllegalArgumentException(roi+" not accepted yet");
		}
	}
	
	@Override
	public void dispose(){
		datas = null;
	}

}
