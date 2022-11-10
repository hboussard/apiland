package fr.inra.sad.bagap.apiland.analysis.matrix;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ChamferDistanceTabAnalysis extends Analysis {

	private final static int[][] chamfer13 = new int[][] { new int[] { 1, 0, 68 }, new int[] { 1, 1, 96 },
			new int[] { 2, 1, 152 }, new int[] { 3, 1, 215 }, new int[] { 3, 2, 245 }, new int[] { 4, 1, 280 },
			new int[] { 4, 3, 340 }, new int[] { 5, 1, 346 }, new int[] { 6, 1, 413 } };

	private float[] input, output;

	private int width, height;

	private float cellSize;

	private int[][] chamfer = null;

	private int normalizer = 0;

	private int[] codes;

	private float threshold;

	public ChamferDistanceTabAnalysis(float[] output, float[] input, int width, int height, float cellSize, int[] codes) {
		this(output, input, width, height, cellSize, codes, Raster.getNoDataValue());
	}

	public ChamferDistanceTabAnalysis(float[] output, float[] input, int width, int height, float cellSize, int[] codes, float threshold) {
		this.chamfer = ChamferDistanceTabAnalysis.chamfer13;
		this.normalizer = this.chamfer[0][2];
		this.output = output;
		this.input = input;
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.codes = codes;
		this.threshold = threshold;
	}

	private void testAndSet(float[] output, int x, int y, float newvalue) {
		if (x < 0 || x >= width) {
			return;
		}
		if (y < 0 || y >= height) {
			return;
		}
		double v = output[y * width + x];
		if (v >= 0 && v < newvalue) {
			return;
		}
		output[y * width + x] = newvalue;
	}

	@Override
	protected void doRun() {

		// System.out.println("1");
		// initialize distance
		boolean hasCode = false;
		for (int y = 0; y < height; y++) {
			// System.out.println("1 : "+y);
			for (int x = 0; x < width; x++) {
				double v = input[y * width + x];
				boolean ok = false;
				if (v != Raster.getNoDataValue()) {
					for (int c : codes) {
						if (c == v) {
							ok = true;
							hasCode = true;
							break;
						}
					}
				}
				if (ok) {
					output[y * width + x] = 0; // inside the object ->
												// distance=0
				} else {
					output[y * width + x] = Raster.getNoDataValue(); // outside
																		// the
																		// object
																		// -> to
																		// be
																		// computed
				}
			}
		}
		if (!hasCode) {
			setResult(output);
			return;
		}
		// System.out.println("2");
		// forward
		for (int y = 0; y <= height - 1; y++) {
			// System.out.println("2 : "+y);
			for (int x = 0; x <= width - 1; x++) {
				float v = output[y * width + x];
				if (v == Raster.getNoDataValue()) {
					continue;
				}
				for (int k = 0; k < chamfer.length; k++) {
					int dx = chamfer[k][0];
					int dy = chamfer[k][1];
					int dt = chamfer[k][2];

					testAndSet(output, x + dx, y + dy, v + dt);
					if (dy != 0) {
						testAndSet(output, x - dx, y + dy, v + dt);
					}
					if (dx != dy) {
						testAndSet(output, x + dy, y + dx, v + dt);
						if (dy != 0) {
							testAndSet(output, x - dy, y + dx, v + dt);
						}
					}
				}
			}
		}
		// System.out.println("3");
		// backward
		for (int y = height - 1; y >= 0; y--) {
			// System.out.println("3 : "+y);
			for (int x = width - 1; x >= 0; x--) {
				float v = output[y * width + x];
				if (v == Raster.getNoDataValue()) {
					continue;
				}
				for (int k = 0; k < chamfer.length; k++) {
					int dx = chamfer[k][0];
					int dy = chamfer[k][1];
					int dt = chamfer[k][2];

					testAndSet(output, x - dx, y - dy, v + dt);
					if (dy != 0) {
						testAndSet(output, x + dx, y - dy, v + dt);
					}
					if (dx != dy) {
						testAndSet(output, x - dy, y - dx, v + dt);
						if (dy != 0)
							testAndSet(output, x + dy, y - dx, v + dt);
					}
				}
			}
		}
		// System.out.println("4");
		// normalize
		for (int y = 0; y < height; y++) {
			// System.out.println("4 : "+y);
			for (int x = 0; x < width; x++) {
				float v = output[y * width + x];
				if (v == Raster.getNoDataValue()) {
					continue;
				}
				output[y * width + x] = (v / normalizer) * cellSize;
			}
		}
		// System.out.println("5");
		// nettoyage
		for (int y = 0; y < height; y++) {
			// System.out.println("1 : "+y);
			for (int x = 0; x < width; x++) {
				double v = input[y * width + x];
				if (v == Raster.getNoDataValue()) {
					output[y * width + x] = Raster.getNoDataValue();
				} else {
					double v2 = output[y * width + x];
					if (threshold != Raster.getNoDataValue() && v2 > threshold) {
						output[y * width + x] = threshold;
					}
				}
			}
		}

		setResult(output);
	}

	@Override
	protected void doInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doClose() {
		// TODO Auto-generated method stub
		
	}
}
