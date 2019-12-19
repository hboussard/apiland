package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.csvreader.CsvReader.CatastrophicException;
import com.csvreader.CsvReader.FinalizedException;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RefPoint;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RefPointWithID;

public class CoordinateManager {

	private static final int controlIndex = 500000;
	
	public static double getProjectedX(Matrix m, int x){
		return x * m.cellsize() + m.minX() + m.cellsize()/2;
	}
	
	public static double getProjectedY(Matrix m, int y){
		//return m.cellsize() * (m.height() - 1 - y) + m.minY();
		return m.cellsize() * (m.height() - y) + m.minY() - m.cellsize()/2;
	}
	
	public static int getLocalX(Matrix m, double x){
		return new Double(((x - m.minX()) / m.cellsize())).intValue() ;
	}
	
	public static int getLocalY(Matrix m, double y){
		return m.height() -1 - new Double((y - m.minY()) / m.cellsize()).intValue();
	}
	
	/**
	 * initialize a set of pixels using a text file of points
	 * according to a given matrix
	 * @param m a matrix
	 * @param f a text file of points
	 * @return a set of pixels
	 */
	public static Set<Pixel> initWithPoints(Matrix m, String f) {
		Set<Pixel> pixels = new TreeSet<Pixel>();
		try{
			CsvReader cr = new CsvReader(f);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			//double X, Y, pX1, pY1;
			//int localx1, localx2, localy1, localy2;
			double X, Y;
			int x, y;
			String id;
			while(cr.readRecord()){
				/* test
				X = Double.parseDouble(cr.get("X"));
				Y = Double.parseDouble(cr.get("Y"));
				
				localx1 = getLocalX(m, X);
				localy1 = getLocalY(m, Y);
				
				System.out.println(X+" "+localx1);
				System.out.println(Y+" "+localy1);
				
				pX1 = getProjectedX(m, localx1);
				pY1 = getProjectedY(m, localy1);
				
				System.out.println(X+" "+pX1+" --> "+(X-pX1));
				System.out.println(Y+" "+pY1+" --> "+(Y-pY1));
				
				localx2 = getLocalX(m, pX1);
				System.out.println(localx1 - localx2);
				localy2 = getLocalY(m, pY1);
				System.out.println(localy1 - localy2);
				*/
				
				if(cr.get("X") != ""){
					X = Double.parseDouble(cr.get("X"));
				}else{
					X = Double.parseDouble(cr.get("x"));
				}
				
				if(cr.get("Y") != ""){
					Y = Double.parseDouble(cr.get("Y"));
				}else{
					Y = Double.parseDouble(cr.get("y"));
				}
				
				x = getLocalX(m, X);
				y = getLocalY(m, Y);
				
				if(!(id = cr.get("id")).equals("")){
					pixels.add(PixelManager.get(x, y, id, X, Y));
				}else if(!(id = cr.get("ID")).equals("")){
					pixels.add(PixelManager.get(x, y, id, X, Y));
				}else if(!(id = cr.get("Id")).equals("")){
					pixels.add(PixelManager.get(x, y, id, X, Y));
				}else if(!(id = cr.get("iD")).equals("")){
					pixels.add(PixelManager.get(x, y, id, X, Y));
				}else{
					pixels.add(PixelManager.get(x, y));
				}
			}
			
			//System.out.println(points);
			
			cr.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}catch(FinalizedException ex){
			ex.printStackTrace();
		}catch(CatastrophicException ex){
			ex.printStackTrace();
		}
		return pixels;	
	}
	
	public static Set<RefPoint> initWithPoints(String f) {
		Set<RefPoint> points = new TreeSet<RefPoint>();
		try{
			CsvReader cr = new CsvReader(f);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			double X, Y;
			String id;
			while(cr.readRecord()){
				
				if(cr.get("X") != ""){
					X = Double.parseDouble(cr.get("X"));
				}else{
					X = Double.parseDouble(cr.get("x"));
				}
				
				if(cr.get("Y") != ""){
					Y = Double.parseDouble(cr.get("Y"));
				}else{
					Y = Double.parseDouble(cr.get("y"));
				}
				
				
				if(!(id = cr.get("id")).equals("")){
					points.add(new RefPointWithID(id, X, Y));
				}else if(!(id = cr.get("ID")).equals("")){
					points.add(new RefPointWithID(id, X, Y));
				}else if(!(id = cr.get("Id")).equals("")){
					points.add(new RefPointWithID(id, X, Y));
				}else if(!(id = cr.get("iD")).equals("")){
					points.add(new RefPointWithID(id, X, Y));
				}else{
					points.add(new RefPoint(X, Y));
				}
			}
			
			//System.out.println(points);
			
			cr.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}catch(FinalizedException ex){
			ex.printStackTrace();
		}catch(CatastrophicException ex){
			ex.printStackTrace();
		}
		return points;	
	}
	
	
	public static Set<Pixel> initWithPixels(String f) {
		Set<Pixel> pixels = new TreeSet<Pixel>();
		try{
			CsvReader cr = new CsvReader(f);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			int x;
			int y;
			
			while(cr.readRecord()){
				x = new Integer(cr.get("X"));
				y = new Integer(cr.get("Y"));
//				System.out.println(x+" "+y);
				pixels.add(PixelManager.get(x, y));
			}
			
			//System.out.println(points);
			
			cr.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}catch(FinalizedException ex){
			ex.printStackTrace();
		}catch(CatastrophicException ex){
			ex.printStackTrace();
		}
		return pixels;	
	}
	
	public static void savePoints(Set<Pixel> pixels, Matrix m, String file) {
		CsvWriter cw = new CsvWriter(file);
		try{
			cw.setDelimiter(';');
			cw.write("X");
			cw.write("Y");
			cw.endRecord();
			
			for(Pixel p : pixels){
				cw.write(CoordinateManager.getProjectedX(m, p.x())+"");
				cw.write(CoordinateManager.getProjectedY(m, p.y())+"");
				cw.endRecord();
			}
			
		}catch(com.csvreader.CsvWriter.FinalizedException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			cw.close();
		}
	}
	
	public static void savePixels(Set<Pixel> pixels, Matrix m, String file) {
		CsvWriter cw = new CsvWriter(file);
		try{
			cw.setDelimiter(';');
			cw.write("X");
			cw.write("Y");
			cw.endRecord();
			
			for(Pixel p : pixels){
				cw.write(p.x()+"");
				cw.write(p.y()+"");
				cw.endRecord();
			}
			
		}catch(com.csvreader.CsvWriter.FinalizedException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		} finally{
			cw.close();
		}
	}
	
	/**
	 * initialize a set of pixels using a number n of points 
	 * and minimum distance d between two of them
	 * @param m the matrix
	 * @param d the distance
	 * @param n the number of pixels
	 * @return a set of pixels
	 */
	public static void dispatch(Set<Pixel> pixels, Matrix m, int n){
		
		//System.out.println("nombre de points possibles : "+m.width()*m.height());
		
		//Set<Pixel> pixels = new TreeSet<Pixel>();
		pixels.add(getRandomPixel(m));
		Pixel pixel;
		boolean ok;
		int index = 0;
		while(pixels.size() < n){
			if((index++) > controlIndex){
				throw new IllegalArgumentException("incompatible parameters for matrix "+m+" : number = "+n+", maximum observed = "+pixels.size());
			}
			pixel = getRandomPixel(m);
			ok = true;
			
			if(pixels.contains(pixel)){
				ok = false;
			}
			
			if(ok){
				pixels.add(pixel);
			}
		}
		
//		for(Pixel p : pixels){
//			System.out.println(p);
//		}
		
		//return pixels;
	}
	
	public static void dispatch(Set<Pixel> pixels, Matrix m, int n, int d, Set<Integer> with, Set<Integer> without) throws IllegalArgumentException{
		pixels.add(getRandomPixel(m));
		Pixel pixel;
		double v;
		boolean ok;
		int index = 0;
		
		//System.out.println(with);
		
		while(pixels.size() < n){
			if((index++) > controlIndex){
				throw new IllegalArgumentException("incompatible parameters for matrix "+m+" : distance = "+d+",  number = "+n+", maximum observed = "+pixels.size());
			}
			pixel = getRandomPixel(m);
			v = m.get(pixel);
			ok = true;
			
			if(with != null){
				ok = false;
				for(Integer c : with){
					if(c == v){
						ok = true;
						break;
					}
				}
			}
			
			if(without != null){
				for(Integer c : without){
					if(c == v){
						ok = false;
						break;
					}
				}
			}
			
			if(ok){
				if(d != -1 && d != 0 && d != 1){
					for(Pixel p : pixels){
						if(Pixel.distance(p,  pixel) < d){
							ok = false;
							break;
						}
					}
				}
				
				if(ok){
					pixels.add(pixel);
				}
			}
		}
		
		/*for(Pixel p : pixels){
			System.out.println(p+" "+m.get(p));
		}*/
	}
	
	public static void dispatch(Set<Pixel> pixels, Set<Matrix> matrix, int n, int d, Set<Integer> with, Set<Integer> without) throws IllegalArgumentException{
		Matrix m = matrix.iterator().next();
		pixels.add(getRandomPixel(m));
		Pixel pixel;
		//double v;
		boolean ok;
		int index = 0;
		Set<Integer> values = new HashSet<Integer>();
		while(pixels.size() < n){
			if((index++) > controlIndex){
				throw new IllegalArgumentException("incompatible parameters for matrix "+m+" : distance = "+d+",  number = "+n+", maximum observed = "+pixels.size());
			}
			pixel = getRandomPixel(m);
			
			values.clear();
			for(Matrix mat : matrix){
				values.add(new Double(mat.get(pixel)).intValue());
			}
			//v = m.get(pixel);
			ok = true;
			
			if(with != null){
				ok = false;
				for(int c : with){
					for(int v : values){
						if(c == v){
							ok = true;
							break;
						}
					}
					if(ok){
						break;
					}
				}
			}
			
			if(without != null){
				for(int c : without){
					for(int v : values){
						if(c == v){
							ok = false;
							break;
						}
					}
					if(!ok){
						break;
					}
				}
			}
			
			if(ok){
				if(d != -1 && d != 0 && d != 1){
					for(Pixel p : pixels){
						if(Pixel.distance(p,  pixel) < d){
							ok = false;
							break;
						}
					}
				}
				
				if(ok){
					pixels.add(pixel);
				}
			}
		}
	}
	
	/**
	 * initialize a set of pixels using a number n of points 
	 * and minimum distance d between two of them
	 * @param m the matrix
	 * @param n the number of pixels
	 * @param d the distance
	 * @return a set of pixels
	 */
	public static void dispatch(Set<Pixel> pixels, Matrix m, int n, int d){
		
		//System.out.println("nombre de points possibles : "+m.width()*m.height());
		
		//Set<Pixel> pixels = new TreeSet<Pixel>();
		pixels.add(getRandomPixel(m));
		Pixel pixel;
		boolean ok;
		int index = 0;
		while(pixels.size() < n){
			if((index++) > controlIndex){
				throw new IllegalArgumentException("incompatible parameters for matrix "+m+" : distance = "+d+",  number = "+n+", maximum observed = "+pixels.size());
			}
			pixel = getRandomPixel(m);
			ok = true;
			for(Pixel p : pixels){
				if(Pixel.distance(p,  pixel) < d){
					ok = false;
					break;
				}
			}
			if(ok){
				pixels.add(pixel);
			}
		}
		
//		for(Pixel p : pixels){
//			System.out.println(p);
//		}
		
		//return pixels;
	}
	
	public static void dispatchWithFilter(Set<Pixel> pixels, Matrix m, int s, int n, int... cat){
		//Set<Pixel> pixels = new TreeSet<Pixel>();
		pixels.add(getRandomPixel(m));
		Pixel pixel;
		boolean ok;
		double v;
		int index = 0;
		while(pixels.size() < n){
			if((index++) > controlIndex){
				throw new IllegalArgumentException("incompatible parameters for matrix "+m+" : size = "+s+",  number = "+n+", maximum observed = "+pixels.size());
			}
			pixel = getRandomPixel(m);
			v = m.get(pixel);
			ok = false;
			for(Integer c : cat){
				if(c == v){
					ok = true;
					break;
				}
			}
			if(ok){
				for(Pixel p : pixels){
					if(Pixel.distance(p,  pixel) < s){
						ok = false;
						break;
					}
				}
				if(ok){
					pixels.add(pixel);
				}
			}
		}
		
		//return pixels;
	}
	
	public static void dispatchWithoutFilter(Set<Pixel> pixels, Matrix m, int s, int n, int... cat){
		//Set<Pixel> pixels = new TreeSet<Pixel>();
		pixels.add(getRandomPixel(m));
		Pixel pixel;
		boolean ok;
		double v;
		int index = 0;
		while(pixels.size() < n){
			if((index++) > controlIndex){
				throw new IllegalArgumentException("incompatible parameters for matrix "+m+" : size = "+s+",  number = "+n+", maximum observed = "+pixels.size());
			}
			pixel = getRandomPixel(m);
			v = m.get(pixel);
			ok = true;
			for(Integer c : cat){
				if(c == v){
					ok = false;
					break;
				}
			}
			if(ok){
				for(Pixel p : pixels){
					if(Pixel.distance(p,  pixel) < s){
						ok = false;
						break;
					}
				}
				if(ok){
					pixels.add(pixel);
				}
			}
		}
		
		//return pixels;
	}
	
	public static void dispatchWithoutNoData(Set<Pixel> pixels, Matrix m, int s, int n){
		//Set<Pixel> pixels = new TreeSet<Pixel>();
		pixels.add(getRandomPixel(m));
		Pixel pixel;
		boolean ok;
		int index = 0;
		while(pixels.size() < n){
			if((index++) > controlIndex){
				throw new IllegalArgumentException("incompatible parameters for matrix "+m+" : size = "+s+",  number = "+n+", maximum observed = "+pixels.size());
			}
			pixel = getRandomPixel(m);
			if(m.get(pixel) != Raster.getNoDataValue()){
				ok = true;
				for(Pixel p : pixels){
					if(Pixel.distance(p,  pixel) < s){
						ok = false;
						break;
					}
				}
				if(ok){
					pixels.add(pixel);
				}
			}
		}
		
		//return pixels;
	}
	
	public static void dispatchWithFilterWithoutNoData(Set<Pixel> pixels, Matrix m, int s, int n, int... cat){
		//Set<Pixel> pixels = new TreeSet<Pixel>();
		pixels.add(getRandomPixel(m));
		Pixel pixel;
		boolean ok;
		double v;
		int index = 0;
		while(pixels.size() < n){
			if((index++) > controlIndex){
				throw new IllegalArgumentException("incompatible parameters for matrix "+m+" : size = "+s+",  number = "+n+", maximum observed = "+pixels.size());
			}
			pixel = getRandomPixel(m);
			v = m.get(pixel);
			if(v != Raster.getNoDataValue()){
				ok = false;
				for(Integer c : cat){
					if(c == v){
						ok = true;
						break;
					}
				}
				if(ok){
					for(Pixel p : pixels){
						if(Pixel.distance(p,  pixel) < s){
							ok = false;
							break;
						}
					}
					if(ok){
						pixels.add(pixel);
					}
				}
			}
		}
		
		//return pixels;
	}
	
	public static void dispatchWithoutFilterWithoutNoData(Set<Pixel> pixels, Matrix m, int s, int n, int... cat){
		//Set<Pixel> pixels = new TreeSet<Pixel>();
		pixels.add(getRandomPixel(m));
		Pixel pixel;
		boolean ok;
		double v;
		int index = 0;
		while(pixels.size() < n){
			if((index++) > controlIndex){
				throw new IllegalArgumentException("incompatible parameters for matrix "+m+" : size = "+s+",  number = "+n+", maximum observed = "+pixels.size());
			}
			pixel = getRandomPixel(m);
			v = m.get(pixel);
			if(v != Raster.getNoDataValue()){
				ok = true;
				for(Integer c : cat){
					if(c == v){
						ok = false;
						break;
					}
				}
				if(ok){
					for(Pixel p : pixels){
						if(Pixel.distance(p,  pixel) < s){
							ok = false;
							break;
						}
					}
					if(ok){
						pixels.add(pixel);
					}
				}
			}
		}
		
		//return pixels;
	}
	
	private static Pixel getRandomPixel(Matrix m){
		return PixelManager.get(
				new Double(Math.random()*m.width()).intValue(), 
				new Double(Math.random()*m.height()).intValue());
	}
	
	
}
