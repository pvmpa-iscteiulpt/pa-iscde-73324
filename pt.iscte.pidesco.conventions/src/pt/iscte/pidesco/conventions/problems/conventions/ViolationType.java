package pt.iscte.pidesco.conventions.problems.conventions;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Defines required behaviour for specific types of Violations.
 * 
 * @author grovy
 *
 */
public interface ViolationType {
	/**
	 * Defines a proper name for the Problem Type, so it can be sorted out by the
	 * Conventions program without much hassle.
	 * 
	 * @return The problem's name.
	 */
	public String getProperName();

	/**
	 * Defines the analysis of an AST node. You MUST define behaviour in order to
	 * discriminate between a generic AST node or a specialized node (i.e. a
	 * TypeDeclaration node) unless you want your code analysis to be generic.<br>
	 * <br>
	 * Specialization of nodes can be checked using "instanceof".
	 * 
	 * @param ASTNode
	 * @param filePath
	 * @return empty list if no Violation is found, or a list with convention
	 *         violations if any is found
	 */
	public List<ConventionViolation> analyzeCode(ASTNode node, String filePath);
}
