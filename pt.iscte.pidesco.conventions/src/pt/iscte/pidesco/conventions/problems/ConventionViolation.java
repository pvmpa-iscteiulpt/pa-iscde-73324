package pt.iscte.pidesco.conventions.problems;

public class ConventionViolation extends Problem {
	
	public ConventionViolation(String filePath, int startingLine, int endingLine, int startingColumn,
			int endingColumn, ConventionViolationType violation) {
		super(filePath, startingLine, endingLine, startingColumn, endingColumn, violation);
	}

}
