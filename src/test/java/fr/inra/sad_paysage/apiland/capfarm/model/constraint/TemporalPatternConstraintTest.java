package fr.inra.sad_paysage.apiland.capfarm.model.constraint;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintMode;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintType;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;

public class TemporalPatternConstraintTest extends ConstraintTest {

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitAlwaysTemporalPattern() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.TemporalPattern);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setParams("M-B");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitNeverTemporalPattern() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.TemporalPattern);
		cb.setMode(ConstraintMode.NEVER);
		cb.setParams("M-B");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitOnlyTemporalPattern() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.TemporalPattern);
		cb.setMode(ConstraintMode.ONLY);
		cb.setParams("M-B");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitAroundTemporalPattern() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.TemporalPattern);
		cb.setMode(ConstraintMode.AROUND);
		cb.setParams("M-B");
		cb.build();
		run();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsAlwaysTemporalPattern() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.TemporalPattern);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setParams("M-B");
		cb.build();
		run();
	}
	
	@Test
	public void testUnitsNeverTemporalPattern() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.TemporalPattern);
		cb.setMode(ConstraintMode.NEVER);
		cb.setParams("M-B");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsOnlyTemporalPattern() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.TemporalPattern);
		cb.setMode(ConstraintMode.ONLY);
		cb.setParams("M-B");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testUnitsAroundTemporalPattern() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M");
		cb.setType(ConstraintType.TemporalPattern);
		cb.setMode(ConstraintMode.AROUND);
		cb.setParams("M-B");
		cb.build();
		run();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupAlwaysTemporalPattern() {
		cb.setCode("C1"); 		
		cb.setCover("CE");
		cb.setType(ConstraintType.TemporalPattern);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setParams("M-B");
		cb.build();
		run();
	}
	
	@Test
	public void testGroupNeverTemporalPattern() {
		cb.setCode("C1"); 		
		cb.setCover("CE");
		cb.setType(ConstraintType.TemporalPattern);
		cb.setMode(ConstraintMode.NEVER);
		cb.setParams("M-B");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupOnlyTemporalPattern() {
		cb.setCode("C1"); 		
		cb.setCover("CE");
		cb.setType(ConstraintType.TemporalPattern);
		cb.setMode(ConstraintMode.ONLY);
		cb.setParams("M-B");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupAroundTemporalPattern() {
		cb.setCode("C1"); 		
		cb.setCover("CE");
		cb.setType(ConstraintType.TemporalPattern);
		cb.setMode(ConstraintMode.AROUND);
		cb.setParams("M-B");
		cb.build();
		run();
	}
}
