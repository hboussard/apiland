package fr.inra.sad_paysage.apiland.analysis.matrix.calculation.perso;

import java.io.IOException;

import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.MatrixManager;

public class MyTest {

	public static void main(String[] args){
		script();
	}
	
	private static void script(){
		try {
			System.out.println("initialisation");
			Matrix m = JaiMatrixFactory.get().createWithAsciiGrid("D:/projets/CATI_ACTION/seminaire_avignon/chloe2012/data/rpg/output/ilot2009_bzh44495053_EA_0100_rate_valid_value.asc");
			
			System.out.println("calculation");
			Matrix r = new MyCalculation(m).allRun();
			
			System.out.println("export");
			MatrixManager.exportAsciiGrid(r, "D:/projets/CATI_ACTION/seminaire_avignon/chloe2012/data/rpg/friction/friction2.asc");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
