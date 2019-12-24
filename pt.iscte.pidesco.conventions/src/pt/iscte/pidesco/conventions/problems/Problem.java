package pt.iscte.pidesco.conventions.problems;

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
	
	private ProblemType problemType;

	private String elementName;

	public Problem(String filePath, int line, ProblemType problemType, String elementName) {
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
	
	public ProblemType getProblemType() {
		return problemType;
	}

	public String getElementName() {
		return elementName;
	}

	public void setStartingLine(int startingLine) {
		this.line = startingLine;
	}





}
