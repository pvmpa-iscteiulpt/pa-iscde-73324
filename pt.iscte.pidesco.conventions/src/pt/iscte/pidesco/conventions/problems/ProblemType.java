package pt.iscte.pidesco.conventions.problems;

public interface ProblemType {
	/**
	 * Defines a proper name for the Problem Type, so it can be sorted out by the
	 * Conventions program without much hassle.
	 * 
	 * @return The problem's name.
	 */
	public String getProperName();
}
