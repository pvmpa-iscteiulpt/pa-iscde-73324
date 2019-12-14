package pt.iscte.pidesco.conventions.problems;

/**
 * Defines a convention violation. These are specialised instances of Problems
 * that warn about syntax oddities that may make the code harder to read and
 * maintain, since they defy expectations set by most developers.
 * 
 * @author grovy
 *
 */
public class ConventionViolation extends Problem {

	public ConventionViolation(String filePath, int line, ConventionViolationType violation) {
		super(filePath, line, violation);
	}

}
