package fr.inra.sad_paysage.apiland.core.util;

public class Interface<E extends Comparable<E>> implements Comparable<Interface<E>> {

	private E e1;
	
	private E e2;
	
	public Interface(E e1, E e2){
		if(e1.compareTo(e2) < 1){
			this.e1 = e1;
			this.e2 = e2;
		}else{
			this.e2 = e1;
			this.e1 = e2;
		}
	}

	public String toString(){
		return e1+"/"+e2;
	}
	
	public boolean same(){
		return e1.equals(e2);
	}
	
	public E getOne(){
		return e1;
	}
	
	public E getOther(){
		return e2;
	}
	
	@Override
	public boolean equals(Object inter) {
		if(inter instanceof Interface<?>){
			return e1.equals(((Interface<?>) inter).e1) && e2.equals(((Interface<?>) inter).e2);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		int hash = 31 * e1.hashCode() * e2.hashCode();
		return hash;
	}
	
	public boolean contains(E e1, E e2){
		return (this.e1.equals(e1) && this.e2.equals(e2)) 
		|| (this.e2.equals(e1) && this.e1.equals(e2));
	}

	@Override
	public int compareTo(Interface<E> o) {
		if(e1.equals(o.e1)){
			return e2.compareTo(o.e2);
		}
		return e1.compareTo(o.e1);
	}
	
}
