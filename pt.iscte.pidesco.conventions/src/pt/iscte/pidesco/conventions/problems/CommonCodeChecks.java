package pt.iscte.pidesco.conventions.problems;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Defines common code checks that can be used by many code analysis processes.
 * 
 * @author grovy
 *
 */
public class CommonCodeChecks {

	private CommonCodeChecks() {
		// access denied
	}

	/**
	 * Checks whether a given node starts with a lower case, and depending on
	 * whether it's supposed to or not, reports if it's a problem.
	 * 
	 * @param name                         - node as a string
	 * @param supposedToBeLowercaseAtStart - true/false
	 * @return true if it's not supposed to start with a lower case and starts with
	 *         one, OR if it's supposed to start with a lower case and it
	 *         doesn't.<br>
	 *         <br>
	 *         false otherwise.
	 */
	public static boolean checkLowerCaseAtStartProblem(String name, boolean supposedToBeLowercaseAtStart) {
		Character firstChar = name.charAt(0);
		boolean isProblem = false; // innocent until proven guilty!
		if (Character.isLetter(firstChar)) {
			if (Character.isLowerCase(firstChar) && !supposedToBeLowercaseAtStart) {
				isProblem = true;
			} else if (!Character.isLowerCase(firstChar) && supposedToBeLowercaseAtStart) {
				isProblem = true;
			}
		}
		return isProblem;
	}

	/**
	 * Checks if a given node has an underscore in it, and depending on whether it's
	 * supposed to or not, reports if it's a problem.
	 * 
	 * @param name                          - node as a string
	 * @param supposedToStartWithUnderscore - true/false
	 * @return true if it's not supposed to have an underscore and has one, OR if
	 *         it's supposed to have an underscore and doesn't have one.<br>
	 *         <br>
	 *         false otherwise.
	 */
	public static boolean checkUnderscoreProblem(String name, boolean supposedToStartWithUnderscore) {
		boolean isProblem = false;
		if (name.contains(new String("_")) && !supposedToStartWithUnderscore) {
			isProblem = true;
		} else if (!name.contains(new String("_")) && supposedToStartWithUnderscore) {
			isProblem = true;
		}
		return isProblem;
	}

	/**
	 * Given any node of the AST, returns the source code line where it was parsed.
	 * 
	 * @param node - The node that was parsed.
	 * @return the source code line as an integer.
	 */
	public static int sourceLine(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition());
	}

}
