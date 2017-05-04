package fr.inra.sad_paysage.apiland.capfarm.model.constraint;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintMode;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.ConstraintType;
import fr.inra.sad_paysage.apiland.capfarm.model.constraint.CoverAllocationConstraint;

public class OnNumericConditionConstraintTest extends ConstraintTest {

	@Test
	public void testUnitAlwaysOnNumericCondition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.OnNumericCondition);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[humidity>0.4]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitNeverOnNumericCondition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.OnNumericCondition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[humidity>0.4]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitOnlyOnNumericCondition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.OnNumericCondition);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[humidity>0.4]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitAroundOnNumericCondition() {
		cb.setCode("C1"); 		
		cb.setCover("M"); 
		cb.setType(ConstraintType.OnNumericCondition);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[humidity>0.4]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsAlwaysOnNumericCondition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.OnNumericCondition);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[humidity>0.4]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsNeverOnNumericCondition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.OnNumericCondition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[humidity>0.4]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsOnlyOnNumericCondition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.OnNumericCondition);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[humidity>0.4]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testUnitsAroundOnNumericCondition() {
		cb.setCode("C1"); 		
		cb.setCover("B","O","M"); 
		cb.setType(ConstraintType.OnNumericCondition);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[humidity>0.4]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupAlwaysOnNumericCondition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.OnNumericCondition);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[humidity>0.4]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupNeverOnNumericCondition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.OnNumericCondition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[humidity>0.4]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupOnlyOnNumericCondition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.OnNumericCondition);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[humidity>0.4]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
	
	@Test
	public void testGroupAroundOnNumericCondition() {
		cb.setCode("C1"); 		
		cb.setCover("CE"); 
		cb.setType(ConstraintType.OnNumericCondition);
		cb.setMode(ConstraintMode.AROUND);
		cb.setDomain("[humidity>0.4]");
		cb.build();
		run();
		
		CoverAllocationConstraint<?, ?> ca = farm.getFarmingSystem().getConstraint("C1");
		
		assertTrue(ca.check(start, end, false));
	}
}
