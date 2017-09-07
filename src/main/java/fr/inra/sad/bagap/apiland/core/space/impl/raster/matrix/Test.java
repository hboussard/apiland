package fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix;

public class Test{
	
	public static void main(String[] args) {
		String in;
		String out;
		for(int i=5; i<7; i++){
			in = "/home/sad20/agents/camille/carto/raster"+i+".asc";
			out = "/home/sad20/agents/camille/carto/raster"+i+"_bis.asc";
			MatrixManager.findAndReplace(in, out, "-9999", "-1");
		}

	}

}
