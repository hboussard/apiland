package fr.inra.sad.bagap.apiland.analysis.matrix.util;

public class SpaceAsciiGridCleaner implements AsciiGridCleaner {

	private int index = 0;
	
	private boolean ok = true;
	
	private String sep;
	
	@Override
	public String clean(String line) {
		index++;
		if(index == 1){
			if(line.split(" ").length == 2){
				ok = true;
			}else{
				ok = false;
				sep = String.valueOf(line.charAt(5));
			}
		}
		if(!ok){
			if(index < 6){
				String[] els = line.split(sep);
				return els[0]+" "+els[els.length-1];
			}
			if(index == 6 && line.startsWith("NODATA_value")){
				String[] els = line.split(sep);
				return els[0]+" "+els[els.length-1];
			}
		}
		
		return line;
	}

	
}
