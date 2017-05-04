package fr.inra.sad_paysage.apiland.capfarm.model.constraint;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintMode;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintType;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;

public class DistanceBetweenCoversConstraintTest extends ConstraintTest {

	@Test
	public void testUnitUnitAlwaysDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[,0.4]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitUnitNeverDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[,0.4]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitUnitOnlyDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,0.4]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitUnitAroundDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[,0.4]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}

	@Test
	public void testUnitsUnitAlwaysDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[,0.4]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsUnitNeverDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[,0.4]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsUnitOnlyDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,0.4]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsUnitAroundDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[,0.4]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}

	@Test
	public void testGroupUnitAlwaysDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[,0.4]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupUnitNeverDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[,0.4]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupUnitOnlyDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,0.4]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupUnitAroundDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[,0.4]");
		cb.setParams("M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}

	@Test
	public void testUnitUnitsAlwaysDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[,0.4]");
		cb.setParams("B","O","M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitUnitsNeverDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[,0.4]");
		cb.setParams("B","O","M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitUnitsOnlyDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,0.4]");
		cb.setParams("B","O","M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitUnitsAroundDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[,0.4]");
		cb.setParams("B","O","M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}

	@Test
	public void testUnitsUnitsAlwaysDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[,0.4]");
		cb.setParams("B","O","M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsUnitsNeverDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[,0.4]");
		cb.setParams("B","O","M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsUnitsOnlyDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,0.4]");
		cb.setParams("B","O","M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsUnitsAroundDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[,0.4]");
		cb.setParams("B","O","M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}

	@Test
	public void testGroupUnitsAlwaysDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[,0.4]");
		cb.setParams("B","O","M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupUnitsNeverDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[,0.4]");
		cb.setParams("B","O","M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupUnitsOnlyDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,0.4]");
		cb.setParams("B","O","M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupUnitsAroundDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[,0.4]");
		cb.setParams("B","O","M");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitGroupAlwaysDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[,0.4]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitGroupNeverDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[,0.4]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitGroupOnlyDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,0.4]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitGroupAroundDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[,0.4]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}

	@Test
	public void testUnitsGroupAlwaysDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[,0.4]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsGroupNeverDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[,0.4]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsGroupOnlyDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,0.4]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsGroupAroundDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[,0.4]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}

	@Test
	public void testGroupGroupAlwaysDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[,0.4]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupGroupNeverDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[,0.4]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupGroupOnlyDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,0.4]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupGroupAroundDistanceBetweenCovers() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.DistanceBetweenCovers);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[,0.4]");
		cb.setParams("CE");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}

	
}
