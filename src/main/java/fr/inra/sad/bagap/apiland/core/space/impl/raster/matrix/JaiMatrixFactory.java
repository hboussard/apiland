package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.plugin.AscReader;
import ij.process.ImageProcessor;

import java.awt.RenderingHints;
import java.awt.image.DataBuffer;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.TiledImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.gce.arcgrid.ArcGridReader;

import com.csvreader.CsvReader;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class JaiMatrixFactory extends MatrixFactory {

	private static double minx, maxx, miny, maxy, cellsize;

	private static int width, height, noDataValue = -1;

	private static JaiMatrixFactory factory = new JaiMatrixFactory();

	private JaiMatrixFactory() {}

	public static JaiMatrixFactory get() {
		return factory;
	}

	@Override
	public Matrix create(Matrix mRef) {
		return new JaiMatrix((JaiMatrix) mRef);
	}

	@Override
	public Matrix create(Matrix mRef, int divisor) {
		return new JaiMatrix((JaiMatrix) mRef, divisor);
	}
	
	@Override
	public Matrix create(int width, int height, double cellsize, double minX, double maxX, double minY, double maxY, int noData) {
		//System.out.println("create pi");
		int bands[] = {0};
		SampleModel sm = new PixelInterleavedSampleModel(DataBuffer.TYPE_DOUBLE, width, height, 1, width, bands);
		TiledImage pi = new TiledImage(0, 0, width, height, 0, 0, sm, null);
		//System.out.println("fait");
		return new JaiMatrix(cellsize, minX, maxX, minY, maxY, pi, false);
		//throw new UnsupportedOperationException();
	}

	@Override
	public Matrix create(int width, int height, double cellsize, double minX, double maxX, double minY, double maxY, int noData, PlanarImage ref) {
		return new JaiMatrix(cellsize, minX, maxX, minY, maxY, new TiledImage(0, 0, width, height, 1, 1, ref.getSampleModel(), ref.getColorModel()));
	}
	
	public Matrix createWithTiff(String tiff, String entete, boolean read) throws NumberFormatException, IOException {
		initWithEntete(entete);
		PlanarImage pi = JAI.create("fileload", tiff);
		Matrix m = new JaiMatrix(cellsize, minx, maxx, miny, maxy, pi, read);
		m.setFile(tiff);
		return m;
	}
	
	public Matrix createWithAsciiGrid(String ascii) throws NumberFormatException, IOException {
		return createWithAsciiGrid(ascii, true);
	}
	
	public Matrix createWithAsciiGrid(String ascii, boolean read) throws NumberFormatException, IOException {
		
		String tiff = deleteExtension(ascii)+".tif";
		if(!new File(tiff).exists()){
			AscReader tr = new AscReader();
			ImageProcessor ip = tr.open(ascii);
	        if (ip!=null){
	        	String nom = deleteExtension(getNomCourt(ascii));
	        	ImagePlus imp = new ImagePlus(nom, ip);
	        	FileSaver saver = new FileSaver(imp);
	    		saver.saveAsTiff(tiff);
	        }
		}
		
		return createWithTiff(tiff, ascii, read);
	}
	
	/**
	 * supression de l'extension d'un fichier
	 * @param fichier : fichier
	 * @return le nom du fichier sans l'extension
	 */
	private static String deleteExtension(String file){
		String line="";
		StringTokenizer st = new StringTokenizer(file,".");
		String last="";
		while(st.hasMoreTokens()){
			last = last + line;
			line = st.nextToken();
		}
		return last;
	}
	
	/**
	 * récupération du nom court d'un fichier
	 * @param nom : nom absolu du fichier
	 * @return le nom court
	 */
	private static String getNomCourt(String nom){
		File f = new File(nom);
		return f.getName();
	}
	
	public Matrix createWithAsciiGridOld(String ascii, boolean read) throws NumberFormatException, IOException {
		initWithEntete(ascii);
		//PlanarImage pi = (PlanarImage) readTiled(ascii, width, height);
		PlanarImage pi = (PlanarImage) readTiled(ascii, width, 1);
		//System.out.println(pi.getHeight()+" "+pi.getWidth());
		//Matrix m = new JaiMatrix(cellsize, minx, maxx, miny, maxy, pi, values);
		Matrix m = new JaiMatrix(cellsize, minx, maxx, miny, maxy, pi, read);
		m.setFile(ascii);
		return m;
	}
	
	public Matrix createWithAsciiGridTest(String asciiref, int width) throws NumberFormatException, IOException {
		initWithEntete(asciiref);
		//PlanarImage pi = (PlanarImage) readTiled(ascii, width, height);
		PlanarImage pi = (PlanarImage) readTiled(asciiref, width, 1);
		System.out.println(pi.getHeight()+" "+pi.getWidth());
		//Matrix m = new JaiMatrix(cellsize, minx, maxx, miny, maxy, pi, values);
		Matrix m = new JaiMatrix(cellsize, minx, maxx, miny, maxy, pi, true);
		return m;
	}
	

	private void initWithEntete(String ascii) throws NumberFormatException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(ascii));
			String line = br.readLine();
			String sep = String.valueOf(line.charAt(5));
			
			String[] s = line.split(sep);
			width = Integer.parseInt(s[s.length-1]);
			//System.out.println(width);

			s = br.readLine().split(sep);
			height = Integer.parseInt(s[s.length-1]);
			//System.out.println("and "+height);
			
			s = br.readLine().split(sep);
			minx = Double.parseDouble(s[s.length-1]);

			s = br.readLine().split(sep);
			miny = Double.parseDouble(s[s.length-1]);

			s = br.readLine().split(sep);
			cellsize = Double.parseDouble(s[s.length-1]);

			maxx = minx + width * cellsize;
			maxy = miny + height * cellsize;

			s = br.readLine().split(sep);
			if (s[0].equalsIgnoreCase("NODATA_value")) {
				noDataValue = Integer.parseInt(s[s.length-1]);
				Raster.setNoDataValue(noDataValue);
			} else {
				noDataValue = Raster.getNoDataValue();
				br.close();
				throw new NumberFormatException();
			}
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public Set<Integer> findValues(String ascii) {
		Set<Integer> values = new HashSet<Integer>();
		try {
			CsvReader cr = new CsvReader(ascii);
			cr.setDelimiter(' ');

			cr.readRecord();
			cr.readRecord();
			cr.readRecord();
			cr.readRecord();
			cr.readRecord();
			cr.readRecord();

			int v;
			//int index = 0;
			while (cr.readRecord()) {
				//System.out.println(++index);
				for (int i = 0; i < width; i++) {
					if (!cr.get(i).equalsIgnoreCase("")) {
						try {
							v = Integer.parseInt(cr.get(i));
							if (v != noDataValue) {
								values.add(v);
							}
						} catch (NumberFormatException ex) {
							values.add((new Double(cr.get(i))).intValue());
						}
					}
				}
			}

			cr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}

	/*
	 * public JaiMatrix createWithTiff(String tfw, String tiff, int tileSize){
	 * //initWithTfw(tfw); PlanarImage pi = (PlanarImage)readTiled(tiff,
	 * tileSize, tileSize); Set<Integer> values = new HashSet<Integer>();
	 * values.add(1); values.add(2); values.add(3); return new JaiMatrix(10,
	 * 51025.00, 2444806.4999998705, pi, values); }
	 */

	private void initWithTfw(String tfw) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(tfw));

			String line = br.readLine();
			System.out.println(line);
			StringTokenizer st = new StringTokenizer(line, " ");
			st.nextToken();
			cellsize = Double.parseDouble(st.nextToken());

			System.out.println(cellsize);

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private RenderedImage readTiled(String file, int tileWidth, int tileHeight) throws IOException {
		try {
			ParameterBlockJAI pbj = new ParameterBlockJAI("imageread");
			pbj.setParameter("Input", ImageIO.createImageInputStream(new File(file)));
			
			ImageLayout layout = new ImageLayout();
			layout.setTileWidth(tileWidth);
			layout.setTileHeight(tileHeight);
			RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT,	layout);

			return JAI.create("imageread", pbj, hints);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}

	private RenderedImage readAsciiFile(String ascii) {
		ArcGridReader agr = null;
		try {
			agr = new ArcGridReader(new File(ascii));
			GridCoverage2D g = (GridCoverage2D) agr.read(null);
			
			return g.getRenderedImage();
		} catch (DataSourceException ex) {
			ex.printStackTrace();
			return null;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			agr.dispose();
		}
	}



}
