package fr.inrae.act.bagap.raster;

import java.awt.Rectangle;
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
		return getDatas(new Rectangle(0, 0, getEntete().width(), getEntete().height()));
	}
	
	@Override
	public float[] getDatas(Rectangle roi){
		float[] datas = new float[roi.width * roi.height];
		datas = coverage.getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, datas);
		return datas;
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
