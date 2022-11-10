package fr.inrae.act.bagap.raster;

import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Pixel2PixelCoverage2AsciiCalculation {

	private static final int buffer = 1000;
	
	private String asciiOut;
	
	private EnteteRaster entete;
	
	private Coverage[] coverages;
	
	public Pixel2PixelCoverage2AsciiCalculation(String asciiOut, EnteteRaster entete, Coverage... coverages){
		this.asciiOut = asciiOut;
		this.entete = entete;
		this.coverages = coverages;
	}
	
	public void run(){
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(asciiOut));
			writer.write("ncols "+entete.width());
			writer.newLine();
			writer.write("nrows "+entete.height());
			writer.newLine();
			writer.write("xllcorner "+entete.minx());
			writer.newLine();
			writer.write("yllcorner "+entete.miny());
			writer.newLine();
			writer.write("cellsize "+entete.cellsize());
			writer.newLine();
			writer.write("NODATA_value "+entete.noDataValue());
			//writer.newLine();
		
			Rectangle roi;
			int h;
			float[] v = new float[coverages.length];
			float vOut;
			float[][] in = new float[v.length][];
			
			for(int theY=0; theY<entete.height(); theY+=buffer){
				h = Math.min(buffer, entete.height()-theY);
				roi = new Rectangle(0, theY, entete.width(), h);
				
				//out = new float[entete.width() * h];
				
				for(int j=0; j<v.length; j++){
					in[j] = coverages[j].getDatas(roi);
				}
				
				for(int i=0; i<(entete.width() * h); i++){
					for(int j=0; j<v.length; j++){
						v[j] = in[j][i];
					}
					//out[i] = doTreat(v);
					vOut = doTreat(v);
					if(i%entete.width() == 0){
						writer.newLine();
					}
					writer.append(vOut+" ");
				}
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	protected abstract float doTreat(float[] v);
	
}
