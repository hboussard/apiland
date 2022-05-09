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
			float[] d = new float[roi.width*roi.height];
			int x, y;
			for(int i=0; i<d.length; i++){
				x = i%roi.width;
				y = i/roi.width;
				d[i] = datas[((roi.y+y)*width()) + roi.x + x];
			}
			return d;
		}
	}
	
	@Override
	public void dispose(){
		datas = null;
	}

}
