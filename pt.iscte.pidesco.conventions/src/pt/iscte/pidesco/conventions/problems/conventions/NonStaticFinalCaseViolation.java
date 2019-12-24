package pt.iscte.pidesco.conventions.problems.conventions;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import pt.iscte.pidesco.conventions.problems.CommonCodeChecks;
import pt.iscte.pidesco.conventions.problems.ProblemType;

/**
 * Lists out the types of Convention Violations.
 * 
 * @author grovy
 *
 */
public class NonStaticFinalCaseViolation implements ProblemType {

	@Override
	public String getProperName() {
		return "camelCase for vars and procs / CamelCase for constructors";
	}

	@Override
	public ConventionViolation analyzeCode(ASTNode node, String filePath) {
		System.out.println(node + " - " + node.getClass().getCanonicalName());
		if (!(node instanceof MethodDeclaration) && !(node instanceof SingleVariableDeclaration)
				&& !(node instanceof VariableDeclarationFragment)) {
			return null;
		}
		String name = new String();
		int line = CommonCodeChecks.sourceLine(node);
		boolean shouldStartWithLowerCase = true;
		boolean shouldHaveUnderscore = false;

		if (node instanceof MethodDeclaration) {
			MethodDeclaration actualNode = (MethodDeclaration) node;
			if (actualNode.isConstructor()) {
				shouldStartWithLowerCase = false;
			}
			name = actualNode.getName().toString();
		}
		if (node instanceof SingleVariableDeclaration) {
			SingleVariableDeclaration actualNode = (SingleVariableDeclaration) node;
			name = actualNode.getName().toString();
		}
		if (node instanceof VariableDeclarationFragment) {
			VariableDeclarationFragment actualNode = (VariableDeclarationFragment) node;
			name = actualNode.getName().toString();
		}

		boolean isBadCode = isProblematic(name, shouldStartWithLowerCase, shouldHaveUnderscore);
		if (isBadCode) {
			ConventionViolation violation = new ConventionViolation(filePath, line, this, name);
			return violation;
		} else {
			return null;
		}
	}

	private boolean isProblematic(String name, boolean shouldStartWithLowerCase, boolean shouldHaveUnderscore) {
		return CommonCodeChecks.checkLowerCaseAtStartProblem(name, shouldStartWithLowerCase)
				|| CommonCodeChecks.checkUnderscoreProblem(name, shouldHaveUnderscore);
	}
}
