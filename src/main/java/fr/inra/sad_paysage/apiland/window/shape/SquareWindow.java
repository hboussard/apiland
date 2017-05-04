package fr.inra.sad_paysage.apiland.window.shape;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.CoordinateManager;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public class SquareWindow extends WindowShape {

	private int size;
	
	private List<Pixel> addDownList;
	
	private List<Pixel> removeDownList;
	
	private List<Pixel> removeHorizontalDownList;
	
	private List<Pixel> removeVerticalDownList;
	
	public SquareWindow(int s){
		this.size = s;
	}

	@Override
	public int width() {
		return size;
	}

	@Override
	public int height() {
		return size;
	}

	private Pixel pixelByIndex(int index){
		return new Pixel(index%size, index/size);
	}
	
	private void initLists(){
		if(addDownList == null){
			addDownList = new LinkedList<Pixel>();
			removeDownList = new LinkedList<Pixel>();
			removeHorizontalDownList = new LinkedList<Pixel>();
			removeVerticalDownList = new LinkedList<Pixel>();
			Pixel p;
			for(int x=0; x<size; x++){
				p = new Pixel(x, 0);
				removeDownList.add(p);
				if(x > 0){
					removeHorizontalDownList.add(p);
				}
			}
			for(int x=size; x<2*size; x++){
				removeVerticalDownList.add(pixelByIndex(x));
			}
		}
	}
	
	@Override
	public List<Pixel> removeDownList() {
		initLists();
		return removeDownList;
	}
	
	@Override
	public List<Pixel> addDownList() {
		initLists();
		return addDownList;
	}

	@Override
	public List<Pixel> removeHorizontalDownList() {
		initLists();
		return removeHorizontalDownList;
	}
	
	@Override
	public List<Pixel> removeVerticalDownList() {
		initLists();
		return removeVerticalDownList;
	}

	@Override
	public List<Pixel> addHorizontalDownList() {
		initLists();
		return addDownList;
	}

	@Override
	public List<Pixel> addVerticalDownList() {
		initLists();
		return addDownList;
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public void export(int x, int y, Matrix m, String path) {
		try {
			String file = path+"square_"+size+"_"+CoordinateManager.getProjectedX(m, x)+"-"+CoordinateManager.getProjectedY(m, y)+".asc";
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
		
			double delta = size/2.0;
			double X = CoordinateManager.getProjectedX(m, x);
			double nX = X - delta * Raster.getCellSize();
			
			double Y = CoordinateManager.getProjectedY(m, y);
			double nY = Y - delta * Raster.getCellSize();
			
			out.write("ncols ");
			out.write(size+"");
			out.newLine();
			out.write("nrows ");
			out.write(size+"");
			out.newLine();
			out.write("xllcorner ");
			out.write(nX+"");
			out.newLine();
			out.write("yllcorner ");
			out.write(nY+"");
			out.newLine();
			out.write("cellsize ");
			out.write(Raster.getCellSize()+"");
			out.newLine();
			out.write("NODATA_value  ");
			out.write(Raster.getNoDataValue()+"");
			out.newLine();
		
			
			for(int j=y-size/2; j<y+1+size/2; j++){
				for(int i=x-size/2; i<x+1+size/2; i++){
					out.write(m.get(i, j)+" ");
				}
				out.newLine();
			}
			/*
			for(int j=0; j<size; j++){
				for(int i=0; i<size; i++){
					out.write("1 ");
				}
				out.newLine();
			}
			*/
		
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void infos() {
		// TODO Auto-generated method stub
		
	}
	
}
