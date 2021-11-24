package fr.inra.sad.bagap.apiland.analysis.matrix.util;

public class SearchAndReplaceCleaner implements AsciiGridCleaner {

	private String fromString;
	
	private String toString;
	
	public SearchAndReplaceCleaner(String fromString, String toString){
		this.fromString = fromString;
		this.toString = toString;
	}
	
	@Override
	public String clean(String line) {
		return line.replace(fromString, toString);
	}

}
