package pt.iscte.pidesco.conventions.problems.conventions;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import pt.iscte.pidesco.conventions.problems.CommonCodeChecks;

/**
 * Lists out the types of Convention Violations.
 * 
 * @author grovy
 *
 */
public class NonStaticFinalCaseViolation implements ViolationType {

	@Override
	public String getProperName() {
		return "camelCase for vars and procs / CamelCase for constructors";
	}

	@Override
	public ArrayList<ConventionViolation> analyzeCode(ASTNode node, String filePath) {
		ArrayList<ConventionViolation> result = new ArrayList<ConventionViolation>();
		if (!(node instanceof MethodDeclaration) && !(node instanceof SingleVariableDeclaration)
				&& !(node instanceof VariableDeclarationFragment)) {
			return result;
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
			result.add(violation);
			return result;
		} else {
			return result;
		}
	}

	private boolean isProblematic(String name, boolean shouldStartWithLowerCase, boolean shouldHaveUnderscore) {
		return CommonCodeChecks.checkLowerCaseAtStartProblem(name, shouldStartWithLowerCase)
				|| CommonCodeChecks.checkUnderscoreProblem(name, shouldHaveUnderscore);
	}
}
