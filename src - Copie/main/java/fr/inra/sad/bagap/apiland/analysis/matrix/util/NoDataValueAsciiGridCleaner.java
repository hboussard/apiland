package fr.inra.sad.bagap.apiland.analysis.matrix.util;

public class NoDataValueAsciiGridCleaner implements AsciiGridCleaner {

	private int index = 0;
	
	private int noData;
	
	public NoDataValueAsciiGridCleaner(Integer value){
		this.noData = value;
	}
	
	@Override
	public String clean(String line) {
		index++;
		if(index == 6){
			line = "NODATA_value "+noData;
		}
		return line;
	}

}
