package alice.tuprolog;

import junit.framework.TestCase;

public class TheoryManagerTestCase extends TestCase {
	
	public void testUnknownDirective() throws InvalidTheoryException {
		String theory = ":- unidentified_directive(unknown_argument).";
		Prolog engine = new Prolog();
		TestWarningListener warningListener = new TestWarningListener();
		engine.addWarningListener(warningListener);
		engine.setTheory(new Theory(theory));
		assertTrue(warningListener.warning.indexOf("unidentified_directive/1") > 0);
		assertTrue(warningListener.warning.indexOf("is unknown") > 0);
	}
	
	public void testFailedDirective() throws InvalidTheoryException {
		String theory = ":- load_library('UnknownLibrary').";
		Prolog engine = new Prolog();
		TestWarningListener warningListener = new TestWarningListener();
		engine.addWarningListener(warningListener);
		engine.setTheory(new Theory(theory));
		assertTrue(warningListener.warning.indexOf("load_library/1") > 0);
		assertTrue(warningListener.warning.indexOf("InvalidLibraryException") > 0);
	}
	
	public void testAssertNotBacktrackable() throws PrologException {
		Prolog engine = new Prolog();
		SolveInfo firstSolution = engine.solve("assertz(a(z)).");
		assertTrue(firstSolution.isSuccess());
		assertFalse(firstSolution.hasOpenAlternatives());
	}
	
	// TODO test abolish: not only the predicate is no more defined, but
	// it should also disappear completely from the clause database, i.e.
	// ClauseDatabase#get(f/a) should return null instead of an empty list
	// (it returns an empty list as a result of a retract_all, please test
	// also this one, thank you).
	
}
