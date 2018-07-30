package fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance;

import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.CenteredWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.CircleWindow;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class DistanceCircleWindow extends CircleWindow implements DistanceWindow {

	private final double[][] distances;
	
	public DistanceCircleWindow(DistanceFunction function) {
		super(function.getDiameter(Raster.getCellSize()));
		distances = new double[width()][width()];
		initDistance();
	}
	
	@Override
	public double[][] getDistances(){
		return distances;
	}
	
	private int getCenterX(){
		return ((CenteredWindow) window).getCenterX();
	}
	
	private int getCenterY(){
		return ((CenteredWindow) window).getCenterY();
	}

	private void initDistance() {
		for(int y=0; y<height(); y++){
			for(int x=0; x<width(); x++){
				if(filter(x , y) == 0){
					distances[y][x] = -1;
				}else{
					distances[y][x] = Math.sqrt(Math.pow(y - getCenterY(), 2) + Math.pow(x - getCenterX(), 2));
				}
			}
		}
	}
	
	
	
}
