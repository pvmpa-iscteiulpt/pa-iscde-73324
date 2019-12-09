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
	private int startingLine;
	private int endingLine;
	private int startingColumn;
	private int endingColumn;

	private String elementName; // TODO see if this is necessary later.

	public Problem(String filePath, int startingLine, int endingLine, int startingColumn, int endingColumn) {
		setFilePath(filePath);
		this.startingLine = startingLine;
		this.endingLine = endingLine;
		this.startingColumn = startingColumn;
		this.endingColumn = endingColumn;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath.trim();
		// TODO initialize elementName here if necessary.
	}

	public String getFilePath() {
		return filePath;
	}

	public int getStartingLine() {
		return startingLine;
	}

	public int getEndingLine() {
		return endingLine;
	}

	public int getStartingColumn() {
		return startingColumn;
	}

	public int getEndingColumn() {
		return endingColumn;
	}

	public void setStartingLine(int startingLine) {
		this.startingLine = startingLine;
	}

	public void setEndingLine(int endingLine) {
		this.endingLine = endingLine;
	}

	public void setStartingColumn(int startingColumn) {
		this.startingColumn = startingColumn;
	}

	public void setEndingColumn(int endingColumn) {
		this.endingColumn = endingColumn;
	}





}
