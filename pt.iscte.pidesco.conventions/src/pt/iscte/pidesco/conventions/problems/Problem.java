package pt.iscte.pidesco.conventions.problems;

import org.eclipse.jdt.core.dom.ASTNode;

import pt.iscte.pidesco.conventions.problems.conventions.ViolationType;

/**
 * Defines a "problem" with some Java source element. These are used to keep
 * track of violations that are spotted in analyzed code.<br>
 *
 * @author pvmpa-iscteiulpt
 *
 */
public abstract class Problem {

	private String absoluteFilePath;
	private int line;

	private ViolationType problemType;

	private String elementName;
	private ASTNode node;

	public Problem(String filePath, int line, ViolationType problemType, String elementName, ASTNode node) {
		setAbsoluteFilePath(filePath);
		this.line = line;
		this.problemType = problemType;
		this.elementName = elementName;
		this.node = node;
	}

	/**
	 * Sets a problem's file path. It should be absolute.
	 * 
	 * @param absoluteFilePath
	 */
	public void setAbsoluteFilePath(String absoluteFilePath) {
		this.absoluteFilePath = absoluteFilePath.trim();
	}

	/**
	 * Returns the problem's absolute file path.
	 * 
	 * @return absolute file path
	 */
	public String getAbsoluteFilePath() {
		return absoluteFilePath;
	}

	/**
	 * Returns the line in the source file where the problem begins.
	 * 
	 * @return line number
	 */
	public int getStartingLine() {
		return line;
	}

	/**
	 * Returns the type of problem that this Problem is about.
	 * 
	 * @return instance of a ViolationType implementation
	 */
	public ViolationType getProblemType() {
		return problemType;
	}

	/**
	 * Returns the name of the problematic element as a String. This is mostly
	 * useful if the element in question is a variable, a method, a class - as in,
	 * anything that actually does have a name.
	 * 
	 * @return the problematic element's name
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * Returns the problematic node.
	 * @return the instance of ASTNode that is problematic
	 */
	public ASTNode getNode() {
		return node;
	}

}
