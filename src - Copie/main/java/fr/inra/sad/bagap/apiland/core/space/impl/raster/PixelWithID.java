package fr.inra.sad.bagap.apiland.core.space.impl.raster;

public class PixelWithID extends Pixel {
	
	private static final long serialVersionUID = 1L;

	private String id;
	
	private double X, Y;
	
	public PixelWithID(int x, int y, String id, double X, double Y){
		super(x, y);
		this.id = id;
		this.X = X;
		this.Y = Y;
	}
	
	public String getId(){
		return id;
	}
	
	public double getX(){
		return X;
	}
	
	public double getY(){
		return Y;
	}
	
	@Override
	public int compareTo(Pixel p) {
		int comp = super.compareTo(p);
		if(comp == 0 && p instanceof PixelWithID){
			if(this.Y < ((PixelWithID) p).Y){
				return 1;
			}else if(this.Y > ((PixelWithID) p).Y){
				return -1;
			}else{
				if(this.X < ((PixelWithID) p).X){
					return -1;
				}else if(this.X > ((PixelWithID) p).X){
					return 1;
				}else{
					return 0;
				}
			}
		}
		return comp;
	}
	
}
