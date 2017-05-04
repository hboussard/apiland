package fr.inra.sad_paysage.apiland.capfarm.model.constraint;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintMode;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintType;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;

public class DistanceFromFacilitiesConstraintTest extends ConstraintTest {

	@Test
	public void testUnitAlwaysDistanceFromFacilities() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[,2]");
		cb.setParams("head");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitNeverDistanceFromFacilities() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[,2]");
		cb.setParams("head");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitOnlyDistanceFromFacilities() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,2]");
		cb.setParams("head");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitAroundDistanceFromFacilities() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[,2]");
		cb.setParams("head");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		assertTrue(ca.check(start, end, false));
	}

	@Test
	public void testUnitsAlwaysDistanceFromFacilities() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[,2]");
		cb.setParams("head");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsNeverDistanceFromFacilities() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[,2]");
		cb.setParams("head");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsOnlyDistanceFromFacilities() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,2]");
		cb.setParams("head");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsAroundDistanceFromFacilities() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[,2]");
		cb.setParams("head");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}

	@Test
	public void testGroupAlwaysDistanceFromFacilities() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[,2]");
		cb.setParams("head");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupNeverDistanceFromFacilities() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[,2]");
		cb.setParams("head");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupOnlyDistanceFromFacilities() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,2]");
		cb.setParams("head");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupAroundDistanceFromFacilities() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[,2]");
		cb.setParams("head");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}


}
