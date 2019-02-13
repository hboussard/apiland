package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;
import fr.inra.sad.bagap.apiland.analysis.process.ProcessType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;

public enum WindowShapeType {

	CIRCLE,
	
	SQUARE,
	
	FUNCTIONAL;
	
	//RING;
	
	public WindowShape create(int width, DistanceFunction function){
		switch(this){
		case SQUARE : return new SquareWindow(width, function);
		case CIRCLE : return new CircleWindow(width, function);
		default : throw new IllegalArgumentException("not implemented yet");
		}
	}
	
	public WindowShape create(double radius, DistanceFunction function){
		switch(this){
		//case SQUARE : return new SquareWindow(width, function);
		//case CIRCLE : return new CircleWindow(width, function);
		case CIRCLE : return new CircleWindow2(radius);
		default : throw new IllegalArgumentException("not implemented yet");
		}
	}
	
	public WindowShape create(Matrix m, double dmax, Friction f, ProcessType<?> pt, DistanceFunction function){
		switch(this){
		case FUNCTIONAL : 
			FunctionalWindowWithMap fw = new FunctionalWindowWithMap(m, dmax, f, function);
			pt.addObserver(fw);
			return fw;
		default : throw new IllegalArgumentException("not implemented yet");
		}
	}
	
	public WindowShape create(Matrix m, double dmax, Matrix f, ProcessType<?> pt, DistanceFunction function){
		switch(this){
		case FUNCTIONAL : 
			FunctionalWindowWithMatrix fwwm = new FunctionalWindowWithMatrix(m, dmax, f, function);
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
