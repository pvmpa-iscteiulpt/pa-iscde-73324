package pt.iscte.pidesco.conventions.problems.smells;

import pt.iscte.pidesco.conventions.problems.Problem;

/**
 * Defines a code smell. These are specialised instances of Problems that warn
 * about code design oddities that at best may compromise the maintainability of
 * a project, and at worst goad developers to introduce bugs to the code due to
 * its design (or, usually, lack thereof).
 * 
 * @author grovy
 *
 */
public class CodeSmell extends Problem {

	public CodeSmell(String filePath, int line, CodeSmellType smell, String elementName) {
		super(filePath, line, smell, elementName);
	}

}
