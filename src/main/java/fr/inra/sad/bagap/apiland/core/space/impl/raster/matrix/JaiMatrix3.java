package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

import com.sun.media.jai.widget.DisplayJAI;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.util.Couple;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.image.WritableRaster;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.TiledImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class JaiMatrix3 implements Matrix {
	private static final long serialVersionUID = 1L;
	private PlanarImage pi;
	private RandomIter ite;
	private double[] pixel;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private double cellsize;
	private double minV;
	private double maxV;
	private String file;
	private Set<Integer> values;

	public JaiMatrix3(String image) {
		this(JAI.create("fileload", image));
		setFile(image);
	}

	public JaiMatrix3(PlanarImage pi) {
		this(pi, true);
	}

	public JaiMatrix3(PlanarImage pi, boolean read) {
		this.pi = pi;
		ite = RandomIterFactory.create(this.pi, null);

		pixel = new double[1];
		if (read) {
			initValues();
		}
	}

	public JaiMatrix3(PlanarImage pi, Set<Integer> values) {
		this.pi = pi;
		ite = RandomIterFactory.create(this.pi, null);

		pixel = new double[1];
		this.values = values;
	}

	/*
	public JaiMatrix(JaiMatrix matrix) {
		this(new TiledImage(pi, pi.getTileWidth(), pi.getTileHeight()));
		minX = matrix.minX();
		maxX = matrix.maxX();
		minY = matrix.minY();
		maxY = matrix.maxY();
		cellsize = cellsize;
		Raster.setCellSize(cellsize);
	}*/
/*
	public JaiMatrix(JaiMatrix matrix, int divisor) {
		this(new TiledImage(pi.getMinX(), pi.getMinY(),
				matrix.width() % divisor == 0 ? matrix.width() / divisor : matrix.width() / divisor + 1,
				matrix.height() % divisor == 0 ? matrix.height() / divisor : matrix.height() / divisor + 1,
				pi.getTileGridXOffset(), pi.getTileGridYOffset(), pi.getSampleModel(), pi.getColorModel()));
		minX = matrix.minX();
		maxX = matrix.maxX();
		minY = matrix.minY();
		maxY = matrix.maxY();
		cellsize *= divisor;
		Raster.setCellSize(cellsize);
	}*/

	public JaiMatrix3(double cellsize, double minx, double maxx, double miny, double maxy, PlanarImage pi) {
		this(pi);
		minX = minx;
		maxX = maxx;
		minY = miny;
		maxY = maxy;
		this.cellsize = cellsize;
		Raster.setCellSize(this.cellsize);
	}

	public JaiMatrix3(double cellsize, double minx, double maxx, double miny, double maxy, PlanarImage pi,
			boolean read) {
		this(pi, read);
		minX = minx;
		maxX = maxx;
		minY = miny;
		maxY = maxy;
		this.cellsize = cellsize;
		Raster.setCellSize(this.cellsize);
	}

	public JaiMatrix3(double cellsize, double minx, double maxx, double miny, double maxy, PlanarImage pi,
			Set<Integer> values) {
		this(pi, values);
		minX = minx;
		maxX = maxx;
		minY = miny;
		maxY = maxy;
		this.cellsize = cellsize;
		Raster.setCellSize(this.cellsize);
	}

	public String toString() {
		return file;
	}

	public Iterator<Pixel> iterator() {
		return new TiledMatrixIterator(pi);
	}

	private void initValues() {
		values = new HashSet();

		RectIter ite = RectIterFactory.create(pi, new Rectangle(0, 0, pi.getWidth(), pi.getHeight()));

		minV = 2.147483647E9D;
		maxV = -2.147483648E9D;
		int line = 0;
		do {
			do {
				double v = ite.getSampleDouble();
				if (v != Raster.getNoDataValue()) {
					minV = Math.min(minV, v);
					maxV = Math.max(maxV, v);
				}
				values.add(Integer.valueOf((int) v));
			} while (!ite.nextPixelDone());
			ite.startPixels();
			System.out.println(line++ + "/" + height());
		} while (!ite.nextLineDone());
	}

	public double get(int x, int y) {
		if ((x >= 0) && (x < width()) && (y >= 0) && (y < height())) {
			return ite.getPixel(x, y, pixel)[0];
		}
		return Raster.getNoDataValue();
	}

	public double get(Pixel p) {
		return get(p.x(), p.y());
	}

	public void put(int x, int y, double value) {
		pixel[0] = value;

		((TiledImage) pi).getWritableTile(x / tileWidth(), y / tileHeight()).setPixel(x, y, pixel);
	}

	public void put(Pixel p, double value) {
		put(p.x(), p.y(), value);
	}

	private void put(int x, int y, double[] value) {
		((TiledImage) pi).getWritableTile(x / tileWidth(), y / tileHeight()).setPixel(x, y, value);
	}

	public void put(Raster r, double value) {
		pixel[0] = value;
		for (Pixel p : r) {
			put(p.x(), p.y(), pixel);
		}
	}

	public void init(double v) {
		for (int yt = 0; yt < pi.getNumYTiles(); yt++) {
			for (int xt = 0; xt < pi.getNumXTiles(); xt++) {
				WritableRaster wraster = ((TiledImage) pi).getWritableTile(xt, yt);
				double[] pixels = new double[pi.getTileWidth() * pi.getTileHeight()];
				for (int y = 0; (y < pi.getTileHeight()) && (y + yt * pi.getTileHeight() < height()); y++) {
					for (int x = 0; (x < pi.getTileWidth()) && (x + xt * pi.getTileWidth() < width()); x++) {
						int index = x + y * pi.getTileWidth();
						pixels[index] = v;
					}
				}

				wraster.setPixels(xt * pi.getTileWidth(), yt * pi.getTileHeight(), pi.getTileWidth(),
						pi.getTileHeight(), pixels);
			}
		}
		values.clear();
		values.add(Integer.valueOf(new Double(v).intValue()));
	}

	public int height() {
		return pi.getHeight();
	}

	public int width() {
		return pi.getWidth();
	}

	public double minX() {
		return minX;
	}

	public double maxX() {
		return maxX;
	}

	public double minY() {
		return minY;
	}

	public double maxY() {
		return maxY;
	}

	public double cellsize() {
		return cellsize;
	}

	public int noDataValue() {
		return Raster.getNoDataValue();
	}

	public void display() {
		for (int yt = 0; yt < pi.getNumYTiles(); yt++) {
			for (int xt = 0; xt < pi.getNumXTiles(); xt++) {
				for (int y = yt * pi.getTileHeight(); (y < (yt + 1) * pi.getTileHeight()) && (y < height()); y++) {
					for (int x = xt * pi.getTileWidth(); (x < (xt + 1) * pi.getTileWidth()) && (x < width()); x++) {
						System.out.println(x + " " + y + " " + get(x, y));
					}
				}
			}
		}
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Set<Integer> values() {
		return values;
	}

	public void visualize() {
		String imageInfo = "Dimensions: " + width() + "x" + height() + " Bands:" + pi.getNumBands();

		JFrame frame = new JFrame();
		frame.setTitle("DisplayJAI: ");

		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		DisplayJAI dj = new DisplayJAI(pi);

		contentPane.add(new JScrollPane(dj), "Center");

		contentPane.add(new JLabel(imageInfo), "South");

		frame.setDefaultCloseOperation(3);
		frame.setSize(300, 400);
		frame.setVisible(true);
	}

	public MatrixType getType() {
		return MatrixType.JAI;
	}

	public double getActiveArea() {
		double area = 0.0D;
		double pow = Math.pow(cellsize, 2.0D);
		for (int yt = 0; yt < pi.getNumYTiles(); yt++) {
			for (int xt = 0; xt < pi.getNumXTiles(); xt++) {
				for (int y = yt * pi.getTileHeight(); (y < (yt + 1) * pi.getTileHeight()) && (y < height()); y++) {
					for (int x = xt * pi.getTileWidth(); (x < (xt + 1) * pi.getTileWidth()) && (x < width()); x++) {
						if (get(x, y) != Raster.getNoDataValue()) {
							area += pow;
						}
					}
				}
			}
		}
		return area;
	}

	public void getCouples(Matrix horizontals, Matrix verticals) {
		for (Pixel p : this) {
			if (p.x() < width())
				horizontals.put(p.x(), p.y(), Couple.get(get(p.x(), p.y()), get(p.x() + 1, p.y())));
			if (p.y() < height()) {
				verticals.put(p.x(), p.y(), Couple.get(get(p.x(), p.y()), get(p.x(), p.y() + 1)));
			}
		}
		for (int yt = 0; yt < pi.getNumYTiles(); yt++) {
			for (int xt = 0; xt < pi.getNumXTiles(); xt++) {
				for (int y = yt * pi.getTileHeight(); (y < (yt + 1) * pi.getTileHeight()) && (y < height()); y++) {
					for (int x = xt * pi.getTileWidth(); (x < (xt + 1) * pi.getTileWidth()) && (x < width()); x++) {
						verticals.put(x, y, Couple.get(get(x, y), get(x, y + 1)));
						horizontals.put(x, y, Couple.get(get(x, y), get(x + 1, y)));
					}
				}
				for (int y = yt * pi.getTileHeight(); (y < (yt + 1) * pi.getTileHeight()) && (y < height()); y++) {
					verticals.put(width() - 1, y, Couple.get(get(width() - 1, y), get(width() - 1, y + 1)));
				}
				for (int x = 0; x < width() - 1; x++) {
					horizontals.put(x, height() - 1, Couple.get(get(x, height() - 1), get(x + 1, height() - 1)));
				}
			}
		}
	}

	public boolean contains(Pixel p) {
		return contains(p.x(), p.y());
	}

	public boolean contains(int x, int y) {
		if ((x >= 0) && (x < width()) && (y >= 0) && (y < height())) {
			return true;
		}
		return false;
	}

	public int tileWidth() {
		return pi.getTileWidth();
	}

	public int tileHeight() {
		return pi.getTileHeight();
	}

	public int numXTiles() {
		return pi.getNumXTiles();
	}

	public int numYTiles() {
		return pi.getNumYTiles();
	}

	public double minV() {
		return minV;
	}

	public double maxV() {
		return maxV;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
}
