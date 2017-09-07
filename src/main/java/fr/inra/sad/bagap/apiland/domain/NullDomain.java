package fr.inra.sad.bagap.apiland.domain;

public class NullDomain extends SimpleDomain {

	public NullDomain() {
		super(null);
	}

	@Override
	public boolean accept(Object e) {
		return false;
	}

	@Override
	public Domain inverse() {
		return new AllDomain();
	}

}
