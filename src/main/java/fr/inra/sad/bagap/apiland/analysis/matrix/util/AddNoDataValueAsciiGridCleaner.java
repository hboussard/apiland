package fr.inra.sad.bagap.apiland.analysis.matrix.util;

import java.io.BufferedWriter;
import java.io.IOException;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class AddNoDataValueAsciiGridCleaner implements AsciiGridCleaner {

	private int index = 0;
	
	private BufferedWriter bw;
	
	public AddNoDataValueAsciiGridCleaner(BufferedWriter bw){
		this.bw = bw;
	}
	
	@Override
	public String clean(String line) {
		index++;
		if(index == 6){
			if(!line.startsWith("NODATA_value")){
				try {
					bw.write("NODATA_value "+Raster.getNoDataValue());
					bw.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return line;
	}

}
