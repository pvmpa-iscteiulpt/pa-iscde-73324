package pt.iscte.pidesco.conventions.exttest;

import pt.iscte.pidesco.conventions.problems.CommonCodeChecks;
import pt.iscte.pidesco.conventions.problems.conventions.ConventionViolation;
import pt.iscte.pidesco.conventions.problems.conventions.ViolationType;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class StaticFinalSnakeCaseViolation implements ViolationType {

	@Override
	public String getProperName() {
		return "SNAKE_CASE for static final fields";
	}

	@Override
	public ArrayList<ConventionViolation> analyzeCode(ASTNode node, String filePath) {
		ArrayList<ConventionViolation> result = new ArrayList<ConventionViolation>();
		if (!(node instanceof FieldDeclaration)) {
			return result;
		}
		FieldDeclaration actualNode = (FieldDeclaration) node;
		for (Object o : actualNode.fragments()) {
			VariableDeclarationFragment field = (VariableDeclarationFragment) o;
			String name = field.getName().toString();
			int line = CommonCodeChecks.sourceLine(actualNode);
			int modifiers = actualNode.getModifiers();

			boolean isStatic = Modifier.isStatic(modifiers);
			boolean isFinal = Modifier.isFinal(modifiers);
			if (isStatic && isFinal) {
				boolean foundUnderscore = false;
				boolean isAlreadyBad = false;
				for (char c : name.toCharArray()) {
					if (Character.isLowerCase(c)) {
						result.add(new ConventionViolation(filePath, line, this, name, node));
						isAlreadyBad = true;
						break; // we don't want to keep adding the same field to the naughty list over and
									// over!
					}
					if (c == '_') {
						foundUnderscore = true;
					}
				}
				if (!foundUnderscore && !isAlreadyBad) {
					result.add(new ConventionViolation(filePath, line, this, name, node));
				}
			}
		}
		return result;
	}

}
