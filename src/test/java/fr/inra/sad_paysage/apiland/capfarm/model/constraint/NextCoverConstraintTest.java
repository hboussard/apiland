package fr.inra.sad_paysage.apiland.capfarm.model.constraint;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintMode;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintType;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;

public class NextCoverConstraintTest extends ConstraintTest {

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitAlwaysNextCover() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.NextCover);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[M,B,C]");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitNeverNextCover() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.NextCover);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[M,B,C]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitOnlyNextCover() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.NextCover);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[M,B,C]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitAroundNextCover() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.NextCover);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[M,B,C]");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsAlwaysNextCover() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.NextCover);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[M,B,C]");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitsNeverNextCover() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.NextCover);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[M,B,C]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsOnlyNextCover() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.NextCover);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[M,B,C]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsAroundNextCover() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.NextCover);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[M,B,C]");
		cb.build();
		run();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupAlwaysNextCover() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.NextCover);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[M,B,C]");
		cb.build();
		run();
	}
	
	@Test
	public void testGroupNeverNextCover() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.NextCover);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[M,B,C]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupOnlyNextCover() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.NextCover);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[M,B,C]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupAroundNextCover() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.NextCover);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[M,B,C]");
		cb.build();
		run();
	}
}
