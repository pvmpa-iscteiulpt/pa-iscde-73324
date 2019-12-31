package pt.iscte.pidesco.conventions.exttest;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import pt.iscte.pidesco.conventions.problems.conventions.ConventionViolation;
import pt.iscte.pidesco.conventions.problems.conventions.ViolationType;

public class TestViolation implements ViolationType {

	@Override
	public String getProperName() {
		return "Test Violation";
	}

	@Override
	public List<ConventionViolation> analyzeCode(ASTNode node, String filePath) {
		return new ArrayList<ConventionViolation>();
	}

}
