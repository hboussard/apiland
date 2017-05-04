package fr.inra.sad_paysage.apiland.analysis.matrix.util;

public class IntegerAsciiGridCleaner implements AsciiGridCleaner {

	@Override
	public String clean(String line) {
		 return line.replace(".0 ", " ");
	}
	
}
