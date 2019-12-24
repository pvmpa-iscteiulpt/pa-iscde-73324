package pt.iscte.pidesco.conventions.problems;

import pt.iscte.pidesco.conventions.problems.conventions.ViolationType;

/**
 * Defines a "problem" with some Java source element. These are used to keep
 * track of violations that are spotted in analyzed code.<br>
 *
 * @author pvmpa-iscteiulpt
 *
 */
public abstract class Problem {

	private String filePath;
	private int line;
	
	private ViolationType problemType;

	private String elementName;

	public Problem(String filePath, int line, ViolationType problemType, String elementName) {
		setFilePath(filePath);
		this.line = line;
		this.problemType = problemType;
		this.elementName = elementName;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath.trim();
	}

	public String getFilePath() {
		return filePath;
	}

	public int getStartingLine() {
		return line;
	}
	
	public ViolationType getProblemType() {
		return problemType;
	}

	public String getElementName() {
		return elementName;
	}

	public void setStartingLine(int startingLine) {
		this.line = startingLine;
	}





}
