package fr.inra.sad_paysage.apiland.simul.operation;


public class OpRasterizationType extends OpTranslationType {

	private static final long serialVersionUID = 1L;

	private double cellsize;
	
	private double minX, maxX, minY, maxY;
	
	@Override
	public void reset(){
		super.reset();
		cellsize = 1;
		minX = -1;
		maxX = -1;
		minY = -1;
		maxY = -1;
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
	
	@Override
	public boolean setParameter(String name, Object value){
		if(name.equalsIgnoreCase("cellsize")){
			try{
				setCellSize((Integer)value);
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
		return super.setParameter(name, value);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public OpRasterization getOperation() {
		return new OpRasterization(this);
	}

}
