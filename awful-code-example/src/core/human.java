package core;

/**
 * This is an example of code with clear convention violations to try the
 * Conventions Checker out.
 * 
 * @author grovy
 *
 */
public class human {

	private int AGE;
	String full_name;

	public static final int superpower = 0;

	public human(int one, String two) {
		this.AGE = one;
		this.full_name = two;
	}

	public int return_AGE() {
		return AGE;
	}
}
