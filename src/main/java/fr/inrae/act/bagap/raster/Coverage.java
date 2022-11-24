package fr.inrae.act.bagap.raster;

import java.awt.Rectangle;

public abstract class Coverage {
	
	private EnteteRaster entete;
	
	public Coverage(EnteteRaster entete){
		this.entete = entete;
	}

	public abstract float[] getDatas();
	
	public abstract float[] getDatas(Rectangle roi);

	public abstract void dispose();
	
	public EnteteRaster getEntete(){
		return entete;
	}
	
	public int width(){
		return entete.width();
	}
	
	public int height(){
		return entete.height();
	}
	
	public double minx(){
		return entete.minx();
	}
	
	public double maxx(){
		return entete.maxx();
	}
	
	public double miny(){
		return entete.miny();
	}
	
	public double maxy(){
		return entete.maxy();
	}
	
	public double cellsize(){
		return entete.cellsize();
	}
	
	public int noDataValue(){
		return entete.noDataValue();
	}
	
}
