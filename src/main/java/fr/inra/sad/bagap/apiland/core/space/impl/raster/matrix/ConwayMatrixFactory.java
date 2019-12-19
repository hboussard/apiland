package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.media.jai.PlanarImage;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class ConwayMatrixFactory extends MatrixFactory {

	private static double minx, maxx, miny, maxy, cellsize;

	private static int width, height, noDataValue = -1;
	
	private static ConwayMatrixFactory factory = new ConwayMatrixFactory();

	private ConwayMatrixFactory() {}

	public static ConwayMatrixFactory get() {
		return factory;
	}
	
	public Matrix createWithAsciiGrid(String ascii){
		Matrix m = initWithEntete(ascii);
		m.setFile(ascii);
		return m;
	}
	
	private Matrix initWithEntete(String ascii) throws NumberFormatException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(ascii));
			String line = br.readLine();
			String sep = String.valueOf(line.charAt(5));
			//System.out.println(line);
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
			
			Double[][] values = new Double[height][];
			List<Double> lValues = new ArrayList<Double>();
			String[] datas;
			int j=0;
			while(br.ready()){
				System.out.println(j);
				lValues.clear();
				line = br.readLine();
				datas = line.split(" ");
				double lastv = 0;
				double count = 0;
				double v;
				for(int i=0; i<width; i++){
					v = Double.parseDouble(datas[i]);
					if(i == 0){
						lastv = v;
						count++;
					}else if(v == lastv){
						count++;
					}else{
						lValues.add(count);
						lValues.add(lastv);
						lastv = v;
						count = 1;
					}
				}
				values[j++] = lValues.toArray(new Double[lValues.size()]);
			}
			br.close();
			
			Matrix m = new ConwayMatrix(cellsize, minx, maxx, miny, maxy, width, height, values);
			
			return m;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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

	@Override
	public Matrix create(int width, int height, double cellsize, double minX, double maxX, double minY, double maxY,
			int noData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix create(int width, int height, double cellsize, double minX, double maxX, double minY, double maxY,
			int noData, PlanarImage ref) {
		// TODO Auto-generated method stub
		return null;
	}

}
