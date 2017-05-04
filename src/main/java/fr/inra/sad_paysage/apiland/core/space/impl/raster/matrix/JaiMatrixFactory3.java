package fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix;

import com.csvreader.CsvReader;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;
import java.util.StringTokenizer;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.gce.arcgrid.ArcGridReader;

public class JaiMatrixFactory3 extends MatrixFactory {
	private static double minx;
	private static double maxx;
	private static double miny;
	private static double maxy;
	private static double cellsize;
	private static int width;
	private static int height;
	private static int noDataValue = -1;

	private static JaiMatrixFactory3 factory = new JaiMatrixFactory3();

	private JaiMatrixFactory3() {
	}

	public static JaiMatrixFactory3 get() {
		return factory;
	}

	/*public Matrix create(Matrix mRef) {
		return new JaiMatrix((JaiMatrix) mRef);
	}

	public Matrix create(Matrix mRef, int divisor) {
		return new JaiMatrix((JaiMatrix) mRef, divisor);
	}*/

	public Matrix create(int width, int height, double cellsize, double minX, double maxX, double minY, double maxY,
			int noData) {
		throw new UnsupportedOperationException();
	}

	public Matrix createWithAsciiGrid(String ascii, boolean read) throws NumberFormatException, IOException {
		return createWithAsciiGrid(ascii, 1, read);
	}

	public Matrix createWithAsciiGrid(String ascii) throws NumberFormatException, IOException {
		return createWithAsciiGrid(ascii, 1, true);
	}

	public Matrix createWithAsciiGrid(String ascii, int tileSize, boolean read)
			throws NumberFormatException, IOException {
		initWithAsciiGrid(ascii);
		PlanarImage pi = (PlanarImage) readTiled(ascii, width, tileSize);

		Matrix m = new JaiMatrix(cellsize, minx, maxx, miny, maxy, pi, read);
		m.setFile(ascii);
		return m;
	}

	private void initWithAsciiGrid(String ascii) throws NumberFormatException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(ascii));
			String line = br.readLine();
			String sep = String.valueOf(line.charAt(5));

			String[] s = line.split(sep);
			width = Integer.parseInt(s[(s.length - 1)]);

			s = br.readLine().split(sep);
			height = Integer.parseInt(s[(s.length - 1)]);

			s = br.readLine().split(sep);
			minx = Double.parseDouble(s[(s.length - 1)]);

			s = br.readLine().split(sep);
			miny = Double.parseDouble(s[(s.length - 1)]);

			s = br.readLine().split(sep);
			cellsize = Double.parseDouble(s[(s.length - 1)]);

			maxx = minx + width * cellsize;
			maxy = miny + height * cellsize;

			s = br.readLine().split(sep);
			if (s[0].equalsIgnoreCase("NODATA_value")) {
				noDataValue = Integer.parseInt(s[(s.length - 1)]);
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
    Set<Integer> values = new java.util.HashSet();
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
			javax.imageio.stream.ImageInputStream iis = javax.imageio.ImageIO.createImageInputStream(new File(file));

			ImageLayout layout = new ImageLayout();
			layout.setTileWidth(tileWidth);
			layout.setTileHeight(tileHeight);

			ParameterBlockJAI pbj = new ParameterBlockJAI("imageread");
			pbj.setParameter("Input", iis);

			RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);

			return JAI.create("imageread", pbj, hints);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private RenderedImage readAsciiFile(String ascii) {
		ArcGridReader agr = null;
		try {
			agr = new ArcGridReader(new File(ascii));
			GridCoverage2D g = agr.read(null);

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

	@Override
	public Matrix create(int width, int height, double cellsize, double minX, double maxX, double minY, double maxY,
			int noData, PlanarImage ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix create(Matrix mRef) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix create(Matrix mRef, int divisor) {
		// TODO Auto-generated method stub
		return null;
	}
}
