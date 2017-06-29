package fr.inra.sad_paysage.apiland.core.structure;

import java.util.List;

import fr.inra.sad_paysage.apiland.core.change.Changeable;
import fr.inra.sad_paysage.apiland.core.change.ChangeableObserver;
import fr.inra.sad_paysage.apiland.core.space.Geometry;
import fr.inra.sad_paysage.apiland.core.space.Point;
import fr.inra.sad_paysage.apiland.core.space.impl.raster.Raster;
import fr.inra.sad_paysage.apiland.core.time.Instant;
import fr.inra.sad_paysage.apiland.core.time.Time;
import fr.inra.sad_paysage.apiland.core.time.TimeException;

public class RepresentationRaster<G extends Geometry> extends Representation<G> {

	List<Raster> rasters;
	
	public RepresentationRaster(RepresentationType type) {
		super(type);
	}
	@Override
	public G getGeometry(Instant t) {
		return null;
	}

	@Override
	public void setGeometry(Time t, G g) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public boolean isActive(Instant t, Point g) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getArea(Instant t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLength(Instant t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addTemporal(TemporalEntity<G> t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTemporal(TemporalEntity<G> t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TemporalEntity<G> getActive(Instant t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TemporalEntity<G> get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIndex(Instant t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TemporalEntity<G> getFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TemporalEntity<G> getLast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTime(Time t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isActive(Instant t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void kill(Instant t) throws TimeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Instant getLastChange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addObserver(ChangeableObserver o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeObserver(ChangeableObserver o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyObservers(Instant t, Changeable c, Object o) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void display() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double minX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double maxX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double minY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double maxY() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
