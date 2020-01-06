package pt.iscte.pidesco.conventions.minimap;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import pa.iscde.minimap.extensibility.MinimapInspection;
import pa.iscde.minimap.service.InspectionContext;
import pa.iscde.minimap.utils.Colors;
import pt.iscte.pidesco.conventions.problems.Problem;

public class MinimapConventionViolationInspector implements MinimapInspection {

	@Override
	public void inspect(ASTNode node, InspectionContext context) {
		String absoluteFilePath = context.getFile().toAbsolutePath().toString();
		List<Problem> fileProblems = Activator.getConventionsCheckerServices().getFileProblems(absoluteFilePath);
		for (Problem problem: fileProblems) {
			if (problem.getStartingLine() == context.getLineStart()) {
				context.setBackground(Colors.YELLOW);
			}
		}

	}

}
