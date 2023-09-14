package test;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import fr.inrae.act.bagap.raster.CoverageManager;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiTileGeoTIFFExporter {
	
    public static void main(String[] args) {
        try {
            // Liste des tuiles GridCoverage2D
            List<GridCoverage2D> tiles = new ArrayList<>();
            String inputPath = "G:/FRC_Pays_de_la_Loire/data/grain_bocager/TypeBoisement/";
            int index = 0;
            for(String file : new File(inputPath).list()){
            	if(file.endsWith(".tif")){
            		GridCoverage2D gc = CoverageManager.get(inputPath+file);
            		tiles.add(gc);
            		System.out.println(index++);
            	}
            }

            // Créez un GridCoverage2D qui représente la mosaïque
            // Vous devez implémenter cette fonction en fonction de vos besoins spécifiques
            GridCoverage2D mosaicCoverage = createMosaic(tiles);

            // Définir le nom du fichier GeoTIFF de sortie
            String outputFilePath = "G:/FRC_Pays_de_la_Loire/data/testRaster/type_boisement.tif";

            GeoTiffWriteParams wp = new GeoTiffWriteParams();
			wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
		    wp.setCompressionType("LZW");
		    ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
		    params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
		    GeoTiffWriter writer = new GeoTiffWriter(new File(outputFilePath));
		    writer.write(mosaicCoverage, (GeneralParameterValue[]) params.values().toArray(new GeneralParameterValue[1]));

            // Fermer le writer
            writer.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fonction pour créer une mosaïque à partir de tuiles adjacentes
    private static GridCoverage2D createMosaic(List<GridCoverage2D> tiles) {
      
    	// Utilisez la première tuile pour obtenir la référence spatiale (coordinate reference system)
    	GridCoverage2D firstTile = tiles.get(0);
    	ReferencedEnvelope mosaicEnvelope = new ReferencedEnvelope(firstTile.getEnvelope());

    	// Parcourez les autres tuiles pour étendre la référence spatiale de la mosaïque	
    	for (int i=1; i<tiles.size(); i++) {
    		GridCoverage2D tile = tiles.get(i);
    		ReferencedEnvelope tileEnvelope = new ReferencedEnvelope(tile.getEnvelope());
    		mosaicEnvelope.include(tileEnvelope);
    	}

    	// Créez une mosaïque vide avec la référence spatiale étendue
    	GridCoverage2D mosaic = new GridCoverageFactory().create("Mosaic", new float[10000][10000], mosaicEnvelope);

    	int index = 0;
    	// Parcourez les tuiles et copiez les données dans la mosaïque
    	for (GridCoverage2D tile : tiles) {
    		Envelope2D tileEnvelope = tile.getEnvelope2D();
    		int xOffset = (int) Math.round((tileEnvelope.getMinX() - mosaicEnvelope.getMinX()) / 5);
    		int yOffset = (int) Math.round((tileEnvelope.getMinY() - mosaicEnvelope.getMinY()) / 5);

    		//System.out.println((index++)+" "+xOffset+" "+yOffset);
    		
    		// Lire les données de la tuile
    		RenderedImage image = tile.getRenderedImage();

    		// Copier les données dans la mosaïque
    		Raster sourceRaster = image.getData();
    		WritableRaster targetRaster = (WritableRaster) mosaic.getRenderedImage().getData();
    		targetRaster.setRect(xOffset, yOffset, sourceRaster);
    		
    		System.out.println(targetRaster.getBounds().width+" "+targetRaster.getBounds().height);
    		
    	}

    	return mosaic;

       // } catch (IOException e) {
       //     e.printStackTrace();
       // }

    }
}
