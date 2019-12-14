package pt.iscte.pidesco.conventions.problems;

/**
 * Defines a "problem" with some Java source element. These are used to keep
 * track of violations that are spotted in analyzed code.
 *
 * @author pvmpa-iscteiulpt
 *
 */
public abstract class Problem {

	private String filePath;
	private int line;
	
	private ProblemType problemType;

	private String elementName; // TODO see if this is necessary later.

	public Problem(String filePath, int line, ProblemType problemType) {
		setFilePath(filePath);
		this.line = line;
		this.problemType = problemType;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath.trim();
		// TODO initialise elementName here if necessary.
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

	public void setStartingLine(int startingLine) {
		this.line = startingLine;
	}





}
