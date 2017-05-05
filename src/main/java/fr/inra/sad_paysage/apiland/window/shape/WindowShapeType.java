package fr.inra.sad_paysage.apiland.window.shape;

import fr.inra.sad_paysage.apiland.analysis.process.ProcessType;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.matrix.Matrix;

public enum WindowShapeType {

	SQUARE,
	
	CIRCLE,
	
	FUNCTIONAL;
	
	//RING;
	
	public WindowShape create(int width){
		switch(this){
		case SQUARE : return new SquareWindow(width);
		case CIRCLE : return new CircleWindow(width);
		default : throw new IllegalArgumentException("not implemented yet");
		}
	}
	
	public WindowShape create(Matrix m, double d, Friction f, ProcessType<?> pt){
		switch(this){
		case FUNCTIONAL : 
			FunctionalWindow fw = new FunctionalWindowWithMap(m, d, f);
			pt.addObserver(fw);
			return fw;
		default : throw new IllegalArgumentException("not implemented yet");
		}
	}
	
	public WindowShape create(Matrix m, double d, Matrix f, ProcessType<?> pt){
		switch(this){
		case FUNCTIONAL : 
			FunctionalWindowWithMatrix fwwm = new FunctionalWindowWithMatrix(m, d, f);
			pt.addObserver(fwwm);
			return fwwm;
		default : throw new IllegalArgumentException("not implemented yet");
		}
	}
	
	public static WindowShapeType get(String type){
		if(type.equalsIgnoreCase("square")){
			return SQUARE;
		}else if(type.equalsIgnoreCase("circle")){
			return CIRCLE;
		}else if(type.equalsIgnoreCase("functional")){
			return FUNCTIONAL;
		}
		throw new IllegalArgumentException();
	}
	
	public static String getAbreviation(WindowShapeType type){
		switch(type){
		case SQUARE : return "sq";
		case CIRCLE : return "cr";
		case FUNCTIONAL : return "fn";
		}
		throw new IllegalArgumentException();
	}
}
