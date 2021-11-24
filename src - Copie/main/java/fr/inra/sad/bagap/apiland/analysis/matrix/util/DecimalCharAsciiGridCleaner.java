package fr.inra.sad.bagap.apiland.analysis.matrix.util;

public class DecimalCharAsciiGridCleaner implements AsciiGridCleaner {
	
	@Override
	public String clean(String line) {
		return line = line.replace(',', '.');
	}

}
