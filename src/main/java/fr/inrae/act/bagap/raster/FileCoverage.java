package fr.inrae.act.bagap.raster;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.image.util.ImageUtilities;

// recuperation des donnees depuis le coverage
// attention bug de la récupération des données dans le coverage2D si le Y dépasse une certaine valeur
// bizarement ce bug influence les données en X
// ce bug n'est effectif que sur les coverage issus de fichiers AsciiGrid
// pas de problème sur fichier TIF
public class FileCoverage extends Coverage {

	private GridCoverage2D coverage;
	
	public FileCoverage(GridCoverage2D coverage, EnteteRaster entete){
		super(entete);
		this.coverage = coverage;
	}
	
	public void setCoverage2D(GridCoverage2D gc2d){
		this.coverage = gc2d;
	}
	
	@Override
	public float[] getDatas(){
		float[] inDatas = new float[getEntete().width() * getEntete().height()];
		Rectangle roi = new Rectangle(0, 0, getEntete().width(), getEntete().height());
		inDatas = coverage.getRenderedImage().getData(roi).getSamples(0, 0, getEntete().width(), getEntete().height(), 0, inDatas);
		return inDatas;
	}
	
	@Override
	public float[] getDatas(Rectangle roi){
		
		float[] inDatas = new float[roi.width * roi.height];
		//System.out.println(roi.x+" "+roi.y+" "+roi.width+" "+roi.height);
		
		inDatas = coverage.getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas);
		
		return inDatas;
	}
	
	@Override
	public void dispose(){
		PlanarImage planarImage = (PlanarImage) coverage.getRenderedImage();
		ImageUtilities.disposeImage(planarImage);
		planarImage = null;
		coverage.dispose(true);
		coverage = null;
	}
	
}
