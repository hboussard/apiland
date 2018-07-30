package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import com.sun.media.jai.widget.DisplayJAI;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.util.Couple;

public class JaiMatrix implements Matrix {

	private static final long serialVersionUID = 1L;

	private PlanarImage pi;
	
	private WritableRaster wr;
	
	private RandomIter ite;
	//private RectIter ite;
	
	private double[] pixel;
	
	private double minX, maxX, minY, maxY, cellsize;
	
	private double minV, maxV;
	
	private int width, height;
	
	private String file;
	
	private Set<Integer> values;
	
	public JaiMatrix(String image){
		this(JAI.create("fileload", image));
		setFile(image);
	}
	
	public JaiMatrix(PlanarImage pi){
		this(pi, true);
	}
	
	public JaiMatrix(PlanarImage pi, boolean read){
		setPlanarImage(pi);
		ite = RandomIterFactory.create(this.pi, null);
		//ite = RectIterFactory.create(pi, null);
		pixel = new double[1];
		if(read){
			System.out.println("read");
			initValues();
		}
	}
	
	public JaiMatrix(PlanarImage pi, Set<Integer> values){
		setPlanarImage(pi);
		ite = RandomIterFactory.create(this.pi, null);
		//ite = RectIterFactory.create(this.pi, null);
		pixel = new double[1];
		this.values = values;
	}
	
	public JaiMatrix(JaiMatrix matrix){
		this(new TiledImage(matrix.pi, matrix.pi.getTileWidth(), matrix.pi.getTileHeight()));
		this.minX = matrix.minX();
		this.maxX = matrix.maxX();
		this.minY = matrix.minY();
		this.maxY = matrix.maxY();
		this.cellsize = matrix.cellsize;
		Raster.setCellSize(this.cellsize);
	}
	
	public JaiMatrix(JaiMatrix matrix, int divisor){
		this(new TiledImage(matrix.pi.getMinX(), matrix.pi.getMinY(), 
				matrix.width()%divisor==0?matrix.width()/divisor:(matrix.width()/divisor)+1, 
				matrix.height()%divisor==0?matrix.height()/divisor:(matrix.height()/divisor)+1,
				matrix.pi.getTileGridXOffset(), matrix.pi.getTileGridYOffset(),
				matrix.pi.getSampleModel(), matrix.pi.getColorModel()));
		this.minX = matrix.minX();
		this.maxX = matrix.maxX();
		this.minY = matrix.minY();
		this.maxY = matrix.maxY();
		this.cellsize = matrix.cellsize * divisor;
		Raster.setCellSize(this.cellsize);
	}
	
	public JaiMatrix(double cellsize, double minx, double maxx, double miny, double maxy, PlanarImage pi) {
		this(pi);
		this.minX = minx;
		this.maxX = maxx;
		this.minY = miny;
		this.maxY = maxy;
		this.cellsize = cellsize;
		Raster.setCellSize(this.cellsize);
	}
	
	public JaiMatrix(double cellsize, double minx, double maxx, double miny, double maxy, PlanarImage pi, boolean read) {
		this(pi, read);
		this.minX = minx;
		this.maxX = maxx;
		this.minY = miny;
		this.maxY = maxy;
		this.cellsize = cellsize;
		Raster.setCellSize(this.cellsize);
		//this.values = new TreeSet<Integer>();
	}
	
	public JaiMatrix(double cellsize, double minx, double maxx, double miny, double maxy, PlanarImage pi, Set<Integer> values) {
		this(pi, values);
		this.minX = minx;
		this.maxX = maxx;
		this.minY = miny;
		this.maxY = maxy;
		this.cellsize = cellsize;
		Raster.setCellSize(this.cellsize);
	}

	private void setPlanarImage(PlanarImage pi){
		this.pi = pi;
		this.width = pi.getWidth();
		this.height = pi.getHeight();
		if(pi instanceof TiledImage){
			this.wr = ((TiledImage) this.pi).getWritableTile(0, 0);
		}
	}
	
	/*
	public PlanarImage getPI(){
		return pi;
	}*/
	
	@Override 
	public String toString(){
		return file;
	}
	
	@Override
	public Iterator<Pixel> iterator() {
		return new TiledMatrixIterator(pi);
	}
	
	private void initValues(){
		values = new HashSet<Integer>();
		/*
		RectIter iterator = RectIterFactory.create(pi, null);
		//RectIter iterator = RectIterFactory.create(pi, new Rectangle(0, 0, pi.getWidth(), pi.getHeight()));
		//RectIter iterator = RectIterFactory.create(pi, new Rectangle(0, 0, pi.getWidth(), 200));
		
		for(int h=0; h<height; h++){
			for(int w=0; w<width; w++){
				//values.add((int) ite.getSampleDouble(w, h, 0));
				values.add((int) iterator.getSampleDouble());
				iterator.nextPixel();
			}
			System.out.println(h+" "+height);
		}
		
		for(int v : values){
			if(v != Raster.getNoDataValue()){
				minV = Math.min(minV, v);
				maxV = Math.max(maxV, v);
			}
		}
		*/
		
		/*
		//RandomIter iterator = RandomIterFactory.create(pi, null);
		for(int h=0; h<height; h++){
			for(int w=0; w<width; w++){
				values.add((int) ite.getSampleDouble(w, h, 0));
			}
			//System.out.println(h+"/"+height());
		}
		*/
		/*
		for(int yt=0; yt<pi.getNumYTiles(); yt++){
			for(int xt=0; xt<pi.getNumXTiles(); xt++){
				//System.out.println(xt+" "+yt);
				for(int y=yt*pi.getTileHeight(); y<(yt+1)*pi.getTileHeight() && y<height(); y++) {
					//System.out.println(y);
					for(int x=xt*pi.getTileWidth(); x<(xt+1)*pi.getTileWidth() && x<width(); x++){
						int v = new Double(get(x, y)).intValue();
						if(v != Raster.getNoDataValue()){
							//System.out.println(x+" "+y+" "+v);
							values.add(v);
						}
					}
				}
			}
		}
		*/
		
		RectIter ite = RectIterFactory.create(pi, new Rectangle(0, 0, pi.getWidth(), pi.getHeight()));
		double v;
		minV = Integer.MAX_VALUE;
		maxV = Integer.MIN_VALUE;
		int line = 0;
		do {
			do {
				v = ite.getSampleDouble();
				values.add((int) v);
				minV = Math.min(minV, v);
				maxV = Math.max(maxV, v);
			} while (!ite.nextPixelDone());
			ite.startPixels();
			System.out.println((line++)+"/"+height());
		} while (!ite.nextLineDone());
		
		/*
		for(int v1 : values){
			if(v1 != Raster.getNoDataValue()){
				minV = Math.min(minV, v1);
				maxV = Math.max(maxV, v1);
			}
		}*/
		
		//System.out.println(pi.getClass());
		/*
		int width = width();
		int height = height();
		java.awt.image.Raster r;
		int[] pixels = new int[width];
		minV = Integer.MAX_VALUE;
		maxV = Integer.MIN_VALUE;
		int v;
		for(int h=0;h<height;h++){
			r = pi.getTile(0, h);
			r.getPixels(0,h,width,1,pixels);
			for(int w=0;w<width;w++){
				v = pixels[w];
				if(v != -1){
					minV = Math.min(minV, v);
					maxV = Math.max(maxV, v);
				}
				values.add(v);
				//System.out.println(h+" "+w+" "+v+" ");
			}
			System.out.println(h+" "+height);
		}
		*/
	}
	
	@Override
	public double get(int x, int y) {
		if(x>=0 && x<width()
				&& y>=0 && y<height()){
			//return ite.getPixel(x, y, pixel)[0];
			return ite.getSampleDouble(x, y, 0);
		}
		return Raster.getNoDataValue();
	}

	@Override
	public double get(Pixel p) {
		return get(p.x(), p.y());
	}
	
	@Override
	public void put(int x, int y, double value) {
		pixel[0] = value;
		wr.setPixel(x, y, pixel);
	}

	@Override
	public void put(Pixel p, double value) {
		put(p.x(), p.y(), value);
	}
	
	private void put(int x, int y, double[] value) {
		((TiledImage)pi).getWritableTile(x/tileWidth(), y/tileHeight()).setPixel(x, y, value);
	}

	@Override
	public void put(Raster r, double value) {
		pixel[0] = value;
		for(Pixel p : r){
			put(p.x(), p.y(), pixel);
		}
	}

	@Override
	public void init(double v) {
		double[] pixels;
		WritableRaster wraster;
		int index;
		for(int yt=0; yt<pi.getNumYTiles(); yt++){
			for(int xt=0; xt<pi.getNumXTiles(); xt++){
				wraster = ((TiledImage) pi).getWritableTile(xt, yt);
				pixels = new double[pi.getTileWidth()*pi.getTileHeight()];
				for(int y=0; y<pi.getTileHeight() && (y+yt*pi.getTileHeight())<height(); y++) {
					for(int x=0; x<pi.getTileWidth() && (x+xt*pi.getTileWidth())<width(); x++){
						//System.out.println(x+" "+y);
						index = x + y*pi.getTileWidth();
						pixels[index] = v;
					}
				}
				
				wraster.setPixels(xt*pi.getTileWidth(), yt*pi.getTileHeight(), pi.getTileWidth(), pi.getTileHeight(), pixels);
			}
		}
		if(values != null){
			values.clear();
		}else{
			values = new TreeSet<Integer>();
		}
		
		values.add(new Double(v).intValue());
	}
	
	@Override
	public int height() {
		//return pi.getHeight();
		return height;
	}

	@Override
	public int width() {
		//return pi.getWidth();
		return width;
	}

	@Override
	public double minX() {
		return minX;
	}

	@Override
	public double maxX() {
		return maxX;
	}

	@Override
	public double minY() {
		return minY;
	}

	@Override
	public double maxY() {
		return maxY;
	}

	@Override
	public double cellsize() {
		return cellsize;
	}

	@Override
	public int noDataValue() {
		return Raster.getNoDataValue();
	}

	@Override
	public void display() {
		for(int yt=0; yt<pi.getNumYTiles(); yt++){
			for(int xt=0; xt<pi.getNumXTiles(); xt++){
				for(int y=yt*pi.getTileHeight(); y<(yt+1)*pi.getTileHeight() && y<height(); y++) {
					for(int x=xt*pi.getTileWidth(); x<(xt+1)*pi.getTileWidth() && x<width(); x++){
						System.out.println(x+" "+y+" "+get(x, y));
					}
				}
			}
		}
	}

	@Override
	public String getFile() {
		return file;
	}

	@Override
	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public Set<Integer> values() {
		return values;
	}

	@Override
	public void visualize() {
		// Get some information about the image
		String imageInfo = "Dimensions: "+width()+"x"+height()+" Bands:"+pi.getNumBands();
		//System.out.println(imageInfo);
					
		// Create a frame for display.
		JFrame frame = new JFrame();
		frame.setTitle("DisplayJAI: ");
					
		// Get the JFrame’s ContentPane.
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
					
		// Create an instance of DisplayJAI.
		DisplayJAI dj = new DisplayJAI(pi);
				
		// Add to the JFrame’s ContentPane an instance of JScrollPane
		// containing the DisplayJAI instance.
		contentPane.add(new JScrollPane(dj),BorderLayout.CENTER);
					
		// Add a text label with the image information.
		contentPane.add(new JLabel(imageInfo),BorderLayout.SOUTH);
		
		// Set the closing operation so the application is finished.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 400); // adjust the frame size.
		frame.setVisible(true); // show the frame.
	}	

	@Override
	public MatrixType getType() {
		return MatrixType.JAI;
	}

	@Override
	public double getActiveArea() {
		double area = 0.0;
		double pow = Math.pow(cellsize, 2);
		for(int yt=0; yt<pi.getNumYTiles(); yt++){
			for(int xt=0; xt<pi.getNumXTiles(); xt++){
				for(int y=yt*pi.getTileHeight(); y<(yt+1)*pi.getTileHeight() && y<height(); y++) {
					for(int x=xt*pi.getTileWidth(); x<(xt+1)*pi.getTileWidth() && x<width(); x++){
						if(get(x, y) != Raster.getNoDataValue()){
							area += pow;
						}
					}
				}
			}
		}
		return area;
	}

	@Override
	public void getCouples(Matrix horizontals, Matrix verticals) {
		
		for(Pixel p : this){
			if(p.x() < width())
				horizontals.put(p.x(), p.y(), Couple.get(get(p.x(), p.y()), get(p.x()+1, p.y())));
			if(p.y() < height())
				verticals.put(p.x(), p.y(), Couple.get(get(p.x(), p.y()), get(p.x(), p.y()+1)));
		}
		
		for(int yt=0; yt<pi.getNumYTiles(); yt++){
			for(int xt=0; xt<pi.getNumXTiles(); xt++){
				for(int y=yt*pi.getTileHeight(); y<(yt+1)*pi.getTileHeight() && y<height(); y++) {
					for(int x=xt*pi.getTileWidth(); x<(xt+1)*pi.getTileWidth() && x<width(); x++){
						//System.out.println(x+" "+y);
						verticals.put(x, y, Couple.get(get(x, y), get(x, y+1)));
						horizontals.put(x, y, Couple.get(get(x, y), get(x+1, y)));
					}
				}
				for(int y=yt*pi.getTileHeight(); y<(yt+1)*pi.getTileHeight() && y<height(); y++){
					verticals.put(width()-1, y, Couple.get(get(width()-1, y), get(width()-1, y+1)));
				}
				for(int x=0; x<width()-1; x++){
					horizontals.put(x, height()-1, Couple.get(get(x, height()-1), get(x+1, height()-1)));
				}
			}
		}
	}

	@Override
	public boolean contains(Pixel p) {
		return contains(p.x(), p.y());
	}
	
	@Override
	public boolean contains(int x, int y) {
		if(x >= 0 && x < width()
				&& y >= 0 && y < height()){
			return true;	
		}
		return false;
	}

	@Override
	public int tileWidth() {
		return pi.getTileWidth();
	}

	@Override
	public int tileHeight() {
		return pi.getTileHeight();
	}

	@Override
	public int numXTiles() {
		return pi.getNumXTiles();
	}

	@Override
	public int numYTiles() {
		return pi.getNumYTiles();
	}

	@Override
	public double minV() {
		return minV;
	}

	@Override
	public double maxV() {
		return maxV;
	}

	
	public int size(){
		return height * width;
	}

	/*
	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}
	*/

}

/*
public static RenderedImage convert(RenderedOp renderedOp) {
    ColorModel colorModel = renderedOp.getColorModel();

    SampleModel sampleModel = renderedOp.getSampleModel();

    if (colorModel instanceof IndexColorModel) {
        IndexColorModel indexColorModel = (IndexColorModel)colorModel;
        
        int mapSize = indexColorModel.getMapSize();
        
        byte[][] data = new byte[4][mapSize];
        
        indexColorModel.getReds(data[0]);
        indexColorModel.getGreens(data[1]);
        indexColorModel.getBlues(data[2]);
        indexColorModel.getAlphas(data[3]);

        LookupTableJAI lookupTableJAI = new LookupTableJAI(data);
        
        renderedOp = LookupDescriptor.create(renderedOp, lookupTableJAI, null);
    }

    // Grays with alpha

    else if (sampleModel.getNumBands() == 2) {
        int bandsCount = 4;
        int width = renderedOp.getWidth();
        int height = renderedOp.getHeight();

        java.awt.image.Raster oldData = renderedOp.getData();
        DataBuffer oldDataBuffer = oldData.getDataBuffer();

        List<Byte> byteList = new ArrayList<Byte>(width * height * bandsCount);

        List<Byte> tempByteList = new ArrayList<Byte>(4);

        for (int i = 0; i < oldDataBuffer.getSize(); i++) {
            int mod = (i + 1) % 2;

            int elemPos = i;

            if (mod == 0) {
                tempByteList.add((byte)oldDataBuffer.getElem(elemPos - 1));
                tempByteList.add((byte)oldDataBuffer.getElem(elemPos - 1));
            }

            tempByteList.add((byte)oldDataBuffer.getElem(elemPos));

            if (mod == 0) {
                Collections.reverse(tempByteList);

                byteList.addAll(tempByteList);

                tempByteList.clear();
            }
        }

        byte[] data = new byte[byteList.size()];

        for (int i = 0; i < byteList.size(); i++) {
            data[i] = byteList.get(i);
        }

        DataBuffer dataBuffer = new DataBufferByte(data, data.length);

        SampleModel newSampleModel = RasterFactory.createPixelInterleavedSampleModel(
            DataBuffer.TYPE_BYTE, width, height, bandsCount);
        ColorModel newColorModel = PlanarImage.createColorModel(newSampleModel);
        WritableRaster raster = RasterFactory.createWritableRaster(newSampleModel, dataBuffer, new Point(0, 0));

        TiledImage tiledImage = new TiledImage(0, 0, width, height, 0, 0, newSampleModel, newColorModel);

        tiledImage.setData(raster);

        return tiledImage;
    }
    else if (colorModel.getTransparency() != Transparency.TRANSLUCENT) {
        int bandsCount = 4;
        int width = renderedOp.getWidth();
        int height = renderedOp.getHeight();

        java.awt.image.Raster oldData = renderedOp.getData();
        DataBuffer oldDataBuffer = oldData.getDataBuffer();

        List<Byte> byteList = new ArrayList<Byte>(width * height * bandsCount);

        List<Byte> tempByteList = new ArrayList<Byte>(4);

        for (int i = 0; i < oldDataBuffer.getSize(); i++) {
            int mod = (i + 1) % 3;

            int elemPos = i;

            tempByteList.add((byte)oldDataBuffer.getElem(elemPos));

            if (mod == 0) {
                tempByteList.add((byte)255);

                Collections.reverse(tempByteList);

                byteList.addAll(tempByteList);

                tempByteList.clear();
            }
        }

        byte[] data = new byte[byteList.size()];

        for (int i = 0; i < byteList.size(); i++) {
            data[i] = byteList.get(i);
        }

        DataBuffer dataBuffer = new DataBufferByte(data, data.length);

        SampleModel newSampleModel = RasterFactory.createPixelInterleavedSampleModel(
            DataBuffer.TYPE_BYTE, width, height, bandsCount);
        ColorModel newColorModel = PlanarImage.createColorModel(newSampleModel);
        WritableRaster raster = RasterFactory.createWritableRaster(newSampleModel, dataBuffer, new Point(0, 0));

        TiledImage tiledImage = new TiledImage(0, 0, width, height, 0, 0, newSampleModel, newColorModel);
        
        tiledImage.setData(raster);
        
        //JAI.create("filestore", tiledImage, "test.png", "PNG");

        //printImage(renderedOp);
        //printImage(tiledImage);

        return tiledImage;
    }
    else {
        //printImage(renderedOp);
    }
    
    return renderedOp;
}*/

/*
public void init2(double v) {
	double[] pixels = new double[pi.getTileWidth()*pi.getTileHeight()];
	WritableRaster wraster = null;
	int index;
	for(int yt=0; yt<pi.getNumYTiles(); yt++){
		for(int xt=0; xt<pi.getNumXTiles(); xt++){
			//System.out.println("XTile : "+xt+", YTile : "+yt);
			wraster = (WritableRaster)pi.getData(
					new Rectangle(xt*pi.getTileWidth(), yt*pi.getTileHeight(), (xt+1)*pi.getTileWidth(), (yt+1)*pi.getTileHeight()));

			//System.out.println(xt*pi.getTileWidth()+" "+yt*pi.getTileHeight()+" "+((xt+1)*pi.getTileWidth()-1)+" "+((yt+1)*pi.getTileHeight()-1));
			
			wraster.getPixels(xt*pi.getTileWidth(), yt*pi.getTileHeight(), pi.getTileWidth(), pi.getTileHeight(), pixels);
			for(int y=0; y<pi.getTileHeight() && (y+yt*pi.getTileHeight())<height(); y++) {
				for(int x=0; x<pi.getTileWidth() && (x+xt*pi.getTileWidth())<width(); x++){
					System.out.println(x+" "+y);
					index = x + y*pi.getTileWidth();
					pixels[index] = v;
				}
			}
			
			wraster.setPixels(xt*pi.getTileWidth(), yt*pi.getTileHeight(), pi.getTileWidth(), pi.getTileHeight(), pixels);
			((TiledImage)pi).setData(wraster);
		}
	}
	values.clear();
	values.add(new Double(v).intValue());
}
*/
