package fr.inra.sad.bagap.apiland.simul.operation;


public class OpRasterizationType extends OpTranslationType {

	private static final long serialVersionUID = 1L;

	private double cellsize;
	
	private double minX, maxX, minY, maxY;
	
	private String raster;
	
	@Override
	public void reset(){
		super.reset();
		cellsize = 1;
		minX = -1;
		maxX = -1;
		minY = -1;
		maxY = -1;
		raster = "raster";
	}
	
	private void setCellSize(double cellsize){
		this.cellsize = cellsize;
	}
	
	public double getCellSize() {
		return cellsize;
	}
	
	public double minX(){
		return minX;
	}
	
	public double maxX(){
		return maxX;
	}
	
	public double minY(){
		return minY;
	}
	
	public double maxY(){
		return maxY;
	}
	
	public String raster(){
		return raster;
	}
	
	@Override
	public boolean setParameter(String name, Object value){
		if(name.equalsIgnoreCase("cellsize")){
			try{
				if(value instanceof Integer){
					setCellSize(new Integer((int) value).doubleValue());
				}else{
					setCellSize((Double) value);	
				}

				return true;
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}
		}
		if(name.equalsIgnoreCase("minX")){
			minX = (double) value;
			return true;
		}
		if(name.equalsIgnoreCase("maxX")){
			maxX = (double) value;
			return true;
		}
		if(name.equalsIgnoreCase("minY")){
			minY = (double) value;
			return true;
		}
		if(name.equalsIgnoreCase("maxY")){
			maxY = (double) value;
			return true;
		}
		if(name.equalsIgnoreCase("raster")){
			raster = (String) value;
			return true;
		}
		return super.setParameter(name, value);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public OpRasterization getOperation() {
		return new OpRasterization(this);
	}

}
