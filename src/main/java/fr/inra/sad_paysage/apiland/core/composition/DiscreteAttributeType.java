package fr.inra.sad_paysage.apiland.core.composition;

import java.util.HashMap;
import java.util.Map;

import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.Interval;
import fr.inra.sad_paysage.apiland.core.time.delay.Delay;

public class DiscreteAttributeType extends AttributeType{

	private static final long serialVersionUID = 1L;
	
	private Interval time;
	
	private Delay d;
	
	private int internalSize;
	
	private Map<Instant, Integer> index;
	
	public DiscreteAttributeType(String name, Class<?> binding, Interval time, Delay d){
		super(name,binding);
		this.time = time;
		this.d = d;
		init();
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("discrete attribute type : ");
		sb.append(name);
		sb.append(" (");
		sb.append(" object type : ");
		sb.append(binding.getName());
		sb.append(")\n");
		return sb.toString();
	}
	
	public Interval getTime(){
		return time;
	}
	
	public Delay delay(){
		return d;
	}
	
	private void init(){
		int i = 0;
		index = new HashMap<Instant, Integer>();
		Instant tt = time.start();
		Instant end = time.end();
		while(tt.isBefore(end) || tt.equals(end)){
			//System.out.println(tt+" "+i);
			index.put(tt, i);
			tt = d.next(tt);
			i++;
		}
		internalSize = i;
	}
	
	public int getInternalSize(){
		return internalSize;
	}
	
	public int getIndex(Instant t){
		try{
			return index.get(t);
		}catch(NullPointerException ex){
			if(t.equals(time.end()) || t.isAfter(time.end())){
				return getIndex(d.previous(time.end()));
			}
			Instant tt = time.start();
			int i = 0;
			while(tt.isBefore(t)){
				tt = d.next(tt);
				i++;
			}
			index.put(t, i);
			return i;
		}
	}
	
}
