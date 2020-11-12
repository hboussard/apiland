package fr.inra.sad.bagap.apiland.analysis.matrix.pixel.ascii;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public abstract class Pixel2PixelAsciiGridCalculation {

	private String out;
	
	private String[] in;
	
	public Pixel2PixelAsciiGridCalculation(String outAscii, String... inAscii){
		this.out = outAscii;
		this.in = inAscii;
	}
	
	public void run(){
		try {
			
			BufferedReader[] br = new BufferedReader[in.length];
			int i = 0;
			for(String sin : in){
				br[i++] = new BufferedReader(new FileReader(new File(sin)));
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(out));

			String[] lines = new String[in.length];
			i=0;
			while ((lines[0] = br[0].readLine()) != null) {
				
				for(int ibr=1; ibr<br.length; ibr++){
					lines[ibr] = br[ibr].readLine();
				}
				
				if(i>=6){
					bw.write(treat(i, lines));
					bw.newLine();
				}else if(i == 5){
					bw.write("NODATA_value "+Raster.getNoDataValue());
					bw.newLine();
				}else{
					bw.write(lines[0]);
					bw.newLine();
				}
				i++;
			}

			for(int ibr=0; ibr<br.length; ibr++){
				br[ibr].close();
			}
			bw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String treat(int ind, String[] lines) {
		String[][] ss = new String[lines.length][];
		double[][] vs = new double[lines.length][];
		int width = -1;
		for(int i=0; i<lines.length; i++){
			ss[i] = lines[i].split(" ");
			//System.out.println((ind-6)+" "+ss[i].length);
			width = ss[i].length; 
			vs[i] = new double[width];
			for(int ii=0; ii<width; ii++){
				//try{
					vs[i][ii] = Double.parseDouble(ss[i][ii]);
				//}catch(NumberFormatException ex){
					//System.err.println(ss[i][ii]);
					//throw new Exception();
				//	vs[i][ii] = Raster.getNoDataValue();
				//}
			}
		}
		StringBuffer line = new StringBuffer();
		for(int i=0; i<width; i++){
			double[] v = new double[in.length];
			for(int ii=0; ii<lines.length; ii++){
				v[ii] = vs[ii][i];
			}
			line.append(doTreat(v)+" ");
		}
		
		return line.toString();
	}

	protected abstract double doTreat(double[] v);
	
}
