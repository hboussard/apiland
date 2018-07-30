package fr.inra.sad.bagap.apiland.capfarm.model.economic;

import fr.inra.sad.bagap.apiland.capfarm.model.CoverUnit;

public class MaeliaManagmentProfil extends ManagmentProfil {

	public MaeliaManagmentProfil(CoverUnit[] covers, int[] works) {
		super(covers, works);
	}

	@Override
	protected int work(int index) {
		return getWorks()[index];
	}

}
