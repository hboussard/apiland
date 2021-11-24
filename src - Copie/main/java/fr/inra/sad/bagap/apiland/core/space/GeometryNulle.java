package fr.inra.sad.bagap.apiland.core.space;

import fr.inra.sad.bagap.apiland.core.space.impl.GeometryImpl;

public class GeometryNulle extends Surface{

	public GeometryNulle(GeometryImpl impl) {
		super(impl);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public <G extends Geometry> Geometry addComplexGeometry(ComplexGeometry<G> g) {
		return g;
	}

	@Override
	public Geometry addCurve(Curve g) {
		return g;
	}

	@Override
	public Geometry addGeometry(Geometry g) {
		return g;
	}

	@Override
	public Geometry addPoint(Point g) {
		return g;
	}

	@Override
	public Geometry addSurface(Surface g) {
		return g;
	}

	@Override
	public Geometry smooth() {
		return this;
	}
	
	public double getArea(){
		return 0;
	}

}
