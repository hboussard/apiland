package fr.inra.sad.bagap.apiland.simul.operation;

import java.io.Serializable;
import java.util.Iterator;

import fr.inra.sad.bagap.apiland.core.element.DynamicElement;
import fr.inra.sad.bagap.apiland.core.element.DynamicFeature;
import fr.inra.sad.bagap.apiland.core.element.DynamicLayer;
import fr.inra.sad.bagap.apiland.core.element.manager.DynamicElementBuilder;
import fr.inra.sad.bagap.apiland.core.space.Surface;
import fr.inra.sad.bagap.apiland.core.spacetime.SpatioTemporal;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.Interval;
import fr.inra.sad.bagap.apiland.core.time.Time;
import fr.inra.sad.bagap.apiland.core.time.TimeException;

public class OpDecoupage extends OpComposition{

	private static final long serialVersionUID = 1L;
	
	private Surface surface;
	
	private Serializable value;
	
	private String id;
	
	private DynamicElementBuilder builder;

	public OpDecoupage(OpTypeDecoupage type) {
		super(type);
		this.builder = type.getBuilder();
	}
	
	public void setGeometry(Surface surface){
		this.surface = surface;
	}
	
	public void setValue(Serializable value){
		this.value = value;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	@Override
	public boolean make(Instant t, DynamicElement... e) {
		//Iterator<DynamicFeature> ite = ((DynamicLayer<?>)getElement()).deepIterator();
		Iterator<DynamicFeature> ite = ((DynamicLayer<?>)e[0]).deepIterator();
		DynamicElement de;
		while(ite.hasNext()){
			de = ite.next();
			if(de.isActive(t)){
				//System.out.println(f.getId());
				if(de.getGeometry(t).equals(surface)){
					de.getAttribute(getAttribute()).setValue(t, value);
					return true;
				}else if(de.getGeometry(t).contains(surface)
						|| de.getGeometry(t).within(surface)){
					try {
						de.kill(t);
					} catch (TimeException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		
		//ite = ((DynamicLayer<?>)getElement()).deepIterator();
		ite = ((DynamicLayer<?>)e[0]).deepIterator();
		while(ite.hasNext()){
			de = ite.next();
			if(((SpatioTemporal)de.getDefaultRepresentation().get(0)).getGeometry().equals(surface)){
				de.setTime(Time.add(de.getTime(), new Interval(t)));
				de.getAttribute(getAttribute()).setValue(t, value);
				return true;
			}
		}
		
		builder.setTime(new Interval(t));
		builder.setAttribute(getAttribute(), t, value);
		builder.setRepresentation("the_geom",t, surface);
		DynamicFeature f = builder.build();
		f.setId(id);
		
		//System.out.println("ajout de l'�l�ment "+e.getId());
		//((DynamicLayer<DynamicFeature>)getElement()).add(f);
		((DynamicLayer<DynamicFeature>)e[0]).add(f);
		
		return true;
	}

	@Override
	public void delete(){
		super.delete();
		surface = null;
		value = null;
		id = null;
		builder = null;
	}
	
	
}
