package fr.inra.sad.bagap.apiland.patch;

public class Envelope {

	private int xmin;
	
	private int xmax;
	
	private int ymin;
	
	private int ymax;
	
	public Envelope(int xmin, int xmax, int ymin, int ymax){
		//System.out.println(xmin+" "+xmax+" "+ymin+" "+ymax);
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}

	public int getXmin() {
		return xmin;
	}

	public int getXmax() {
		return xmax;
	}

	public int getYmin() {
		return ymin;
	}

	public int getYmax() {
		return ymax;
	}
	
	public static double distance(Envelope env1, Envelope env2) {
		
		double dx = 0.0;
		if (env1.xmax < env2.xmin) 
			dx = env2.xmin - env1.xmax;
		else if (env1.xmin > env2.xmax) 
			dx = env1.xmin - env2.xmax;
	    
		double dy = 0.0;
		if (env1.ymax < env2.ymin) 
			dy = env2.ymin - env1.ymax;
		else if (env1.ymin > env2.ymax) dy = env1.ymin - env2.ymax;

		//System.out.println(dx+" "+dy);
	    // if either is zero, the envelopes overlap either vertically or horizontally
		if (dx == 0.0 && dy == 0.0) return 0.0;
	    if (dx == 0.0) return dy;
	    if (dy == 0.0) return dx;
	    return Math.sqrt(dx * dx + dy * dy);
	  }
	
}
