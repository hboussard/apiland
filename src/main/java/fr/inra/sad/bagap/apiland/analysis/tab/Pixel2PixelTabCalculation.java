package fr.inra.sad.bagap.apiland.analysis.tab;

public abstract class Pixel2PixelTabCalculation {
	
	private float[] out;
	
	private float[][] in;
	
	public Pixel2PixelTabCalculation(float[] outTab, float[]... inTab){
		this.out = outTab;
		this.in = inTab;
	}
	
	public void run(){
		float[] v = new float[in.length];
		for(int i=0; i<out.length; i++){
			for(int j=0; j<v.length; j++){
				v[j] = in[j][i];
			}
			out[i] = doTreat(v);
		}
	}

	protected abstract float doTreat(float[] v);
	
}
