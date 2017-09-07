package fr.inra.sad.bagap.apiland.core.element;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.core.change.Changeable;
import fr.inra.sad.bagap.apiland.core.change.ChangeableObserver;
import fr.inra.sad.bagap.apiland.core.element.iterator.ActiveElementsIterator;
import fr.inra.sad.bagap.apiland.core.element.iterator.ActiveFeaturesIterator;
import fr.inra.sad.bagap.apiland.core.element.iterator.ElementsIterator;
import fr.inra.sad.bagap.apiland.core.element.iterator.FeaturesIterator;
import fr.inra.sad.bagap.apiland.core.element.type.DynamicElementType;
import fr.inra.sad.bagap.apiland.core.element.type.DynamicLayerType;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.apiland.core.time.TimeNulle;

public abstract class AbstractDynamicLayer<E extends DynamicElement> extends AbstractDynamicElement implements DynamicLayer<E>, Set<E>{

	private static final long serialVersionUID = 1L;

	private Set<E> elements;
	
	private String name;
	
	public AbstractDynamicLayer(DynamicLayerType type){
		super(type);
		elements = new TreeSet<E>(); 
		setId("");
		setName("layer");
	}
	
	@Override
	public AbstractDynamicLayer<E> clone(){
		AbstractDynamicLayer<E> clone = (AbstractDynamicLayer<E>)super.clone();
		clone.elements = new TreeSet<E>();
		for(E e : elements){
			clone.elements.add((E)e.clone());
		}
		return clone;
	}
	
	@Override
	public DynamicLayerType getType(){
		return (DynamicLayerType)super.getType();
	}

	@Override
	public void addObserver(ChangeableObserver o) {
		for(E e : this){
			e.addObserver(o);
		}
		super.addObserver(o);
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		for(E e : this){
			e.addObserver(o);
		}
		super.removeObserver(o);
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		super.notifyObservers(t,c,o);
	}

	@Override
	public Iterator<E> iterator() {
		return elements.iterator();
	}
	
	@Override
	public Iterator<E> activeIterator(Instant t) {
		return new ActiveElementsIterator<E>(this,t);
	}
	
	@Override
	public Iterator<E> completeIterator() {
		return new ElementsIterator<E>(iterator());
	}
	
	@Override
	public <D extends DynamicFeature> Iterator<D> deepIterator(){
		return new FeaturesIterator<D>(iterator());
	}
	
	@Override
	public <D extends DynamicFeature> Iterator<D> activeDeepIterator(Instant t){
		return new ActiveFeaturesIterator<D>((Iterator<D>)deepIterator(),t);
	}

	@Override
	public int count(Instant t) {
		int count = 0;
		for(DynamicElement de : this){
			count += de.count(t);
		}
		return count;
	}
	
	@Override
	public int count(Instant t, DynamicElementType... types) {
		for(DynamicElementType type : types){
			if(getType().equals(type)){
				if(isActive(t)){
					return 1;
				}
				return 0;
			}
		}
		int count = 0;
		for(DynamicElement e : this){
			count += e.count(t,types);
		}
		return count;
	}

	@Override
	public boolean add(E e){
		e.setLayer(this);
		return elements.add(e);
	}

	@Override
	public E get(String id) {
		for(E e : this){
			if(e.getId().equalsIgnoreCase(id)){
				return e;
			}
		}
		return null;
	}
	
	@Override
	public DynamicFeature getDeep(String id){
		Iterator<DynamicFeature> ite = deepIterator();
		DynamicFeature f;
		while(ite.hasNext()){
			f = ite.next();
			if(f.getId().equalsIgnoreCase(id)){
				return f;
			}
		}
		return null;
	}
	
	@Override
	public boolean contains(String id) {
		for(E e : this){
			if(e.getId().equalsIgnoreCase(id)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void display() {
		System.out.println("dynamiclayer id "+getId()+" and type : "+this.getClass().getSimpleName());
		System.out.println(this.getType());
		for(E e : this){
			e.display();
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for(E e : c){
			e.setLayer(this);
		}
		return elements.addAll(c);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		return elements.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return elements.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}


	@Override
	public boolean remove(Object o) {
		((DynamicElement)o).setLayer(null);
		return elements.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		for(Object e : c){
			((DynamicElement)e).setLayer(null);
		}
		return elements.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return elements.retainAll(c);
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public Object[] toArray() {
		return elements.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return elements.toArray(a);
	}
	
	@Override
	public Instant getLastChange(){
		Instant t = super.getLastChange();
		Instant n;
		for(E e : this){
			n = e.getLastChange(); 
			if(t == null || t instanceof TimeNulle || (n != null && n.isAfter(t))){
				t = n;
			}
		}
		return t;
	}
	
	@Override
	public void setName(String name){
		this.name = name;
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	@Override
	public void delete(){
		super.delete();
		name = null;
		for(E e : elements){
			e.delete();
		}
		elements.clear();
		elements = null;
	}
	
}
