package fr.inra.sad.bagap.apiland.analysis.matrix;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class RCMDistanceTabAnalysis extends Analysis {
	
	private float[] inSource, inFriction, output, inEdges;

	private int width, height;

	private float cellSize;

	private int[] codes;

	private float threshold;
	
	public RCMDistanceTabAnalysis(float[] output, float[] inSource, float[] inFriction, int width, int height, float cellSize, int[] codes) {
		this(output, inSource, inFriction, null, width, height, cellSize, codes, Raster.getNoDataValue());
	}
	
	public RCMDistanceTabAnalysis(float[] output, float[] inSource, float[] inFriction, float[] inEdges, int width, int height, float cellSize, int[] codes) {
		this(output, inSource, inFriction, inEdges, width, height, cellSize, codes, Raster.getNoDataValue());
	}

	public RCMDistanceTabAnalysis(float[] output, float[] inSource, float[] inFriction, float[] inEdges, int width, int height, float cellSize,	int[] codes, float threshold) {
		this.output = output;
		this.inSource = inSource;
		this.inFriction = inFriction;
		this.inEdges = inEdges;
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.codes = codes;
		this.threshold = threshold;
	}

	@Override
	protected void doInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doRun() {
		//System.out.println("1");
		boolean hasValue = false;
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				float v = inSource[y*width+x];
				boolean ok = false;
				if (v != Raster.getNoDataValue()) {
					for (int c : codes) {
						if (c == v) {
							ok = true;
							hasValue = true;
							break;
						}
					}
					if (ok) {
						output[y*width+x] = 0; // inside the object -> distance=0
					} else {
						output[y*width+x] = -2; // outside the object -> to be computed
					}
				}else{
					output[y*width+x] = Raster.getNoDataValue(); // nodata_value -> to be not computed
				}
			}
		}
		
		// pour la gestion des pixels a traiter en ordre croissant de distance
		Map<Float, Set<Pixel>> waits = new TreeMap<Float, Set<Pixel>>();
		//System.out.println("2");
		boolean maj = false;
		// afin de limiter le nombre de calculs de diffusion, ne diffuser qu'à partir des bords d'habitats
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				if (output[y*width+x] == 0) {
					maj = true;
					if(x== 0 || output[(x-1)+y*width] == 0){
						if(x==0 || y==0 || output[(x-1)+(y-1)*width] == 0){
							if(y==0 || output[x+(y-1)*width] == 0){
								if(x==(width-1) || y==0 || output[(x+1)+(y-1)*width] == 0){
									if(x==(width-1) || output[(x+1)+y*width] == 0){
										if(x==(width-1) || y==(height-1) || output[(x+1)+(y+1)*width] == 0){
											if(y==(height-1) || output[x+(y+1)*width] == 0){
												if(x==0 || y==(height-1) || output[(x-1)+(y+1)*width] == 0){
													// do nothing
													maj = false;
												}
											}
										}
									}
								}
							}
						}
					}
					if(maj){
						setPixelAndValue(waits, new Pixel(x, y), 0.0f);
					}
				}
				
			}
		}
		//System.out.println("3");
		// gestion des bords si maj
		if(inEdges != null){
			
			for (int x=0; x<width; x++) {
				output[x] = inEdges[x];
				setPixelAndValue(waits, new Pixel(x, 0), inEdges[x]);
				
				output[(height-1)*width+x] = inEdges[(height-1)*width+x];
				setPixelAndValue(waits, new Pixel(x, (height-1)), inEdges[(height-1)*width+x]);
			}
			for (int y=1; y<height-1; y++) {
				output[y*width] = inEdges[y*width];
				setPixelAndValue(waits, new Pixel(0, y), inEdges[y*width]);
				
				output[y*width+(width-1)] = inEdges[y*width+(width-1)];
				setPixelAndValue(waits, new Pixel((width-1), y), inEdges[y*width+(width-1)]);
			}
		}
		//System.out.println("4");
		if(hasValue){
			//ArrayRCMDistanceAnalysis rcm = new ArrayRCMDistanceAnalysis(output, inFriction, width, height, cellSize, waits);
			ArrayRCMDistanceAnalysis rcm = new ArrayRCMDistanceAnalysis(output, inFriction, width, height, cellSize, Raster.getNoDataValue(), waits, threshold);
			setResult(rcm.allRun());
		}
		
	}
	
	private void setPixelAndValue(Map<Float, Set<Pixel>> waits, Pixel pixel, float value) {
		if (!waits.containsKey(value)) {
			waits.put(value, new HashSet<Pixel>());
		}
		waits.get(value).add(pixel);
	}

	@Override
	protected void doClose() {
		// TODO Auto-generated method stub
		
	}

}
