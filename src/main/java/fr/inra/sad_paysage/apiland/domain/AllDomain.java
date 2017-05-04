package fr.inra.sad_paysage.apiland.domain;

public class AllDomain extends SimpleDomain {

	public AllDomain() {
		super(null);
	}

	@Override
	public boolean accept(Object e) {
		return true;
	}

	@Override
	public Domain inverse() {
		return new NullDomain();
	}

}
