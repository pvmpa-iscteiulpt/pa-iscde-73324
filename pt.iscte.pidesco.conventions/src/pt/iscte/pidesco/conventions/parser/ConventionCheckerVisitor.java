package pt.iscte.pidesco.conventions.parser;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

import pt.iscte.pidesco.conventions.problems.Problem;
import pt.iscte.pidesco.conventions.problems.ProblemType;
import pt.iscte.pidesco.conventions.problems.conventions.ConventionViolation;
import pt.iscte.pidesco.conventions.problems.conventions.ConventionViolationType;

public class ConventionCheckerVisitor extends ASTVisitor {

	private ArrayList<ProblemType> problemsToCheck = new ArrayList<ProblemType>();
	private ArrayList<String> filesToAnalyze = new ArrayList<String>();
	private ListMultimap<String, Problem> detectedProblems;
	private String currentFile;

	public ConventionCheckerVisitor(ArrayList<ProblemType> problemsToCheck, ArrayList<String> filesToCheck) {
		this.problemsToCheck = problemsToCheck;
		this.filesToAnalyze = filesToCheck;
	}

	public ListMultimap<String, Problem> runChecker() {
		this.detectedProblems = MultimapBuilder.hashKeys().arrayListValues().build();
		for (String fileToCheck : this.filesToAnalyze) {
			this.currentFile = fileToCheck;
			JavaParser.parse(fileToCheck, this);
		}
		return this.detectedProblems;
	}

	/**
	 * Given any node of the AST, returns the source code line where it was parsed.
	 */
	private int sourceLine(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition());
	}
	
	/**
	 * @param name
	 * @param line
	 * @param potentialProblem
	 */
	private void addProblem(String name, int line, ProblemType potentialProblem) {
		ConventionViolation violation = new ConventionViolation(this.currentFile, line,
				(ConventionViolationType) potentialProblem, name);
		this.detectedProblems.put(this.currentFile, violation);
	}

	
	/**
	 * 
	 * @param name
	 * @param nodeType
	 * @param supposedToStartWithUnderscore
	 * @param line
	 * @param potentialProblem
	 */
	private void checkUnderscore(String name, String nodeType, boolean supposedToStartWithUnderscore, int line,
			ProblemType potentialProblem) {
		boolean isProblem = false;
		if (name.contains(new String("_")) && !supposedToStartWithUnderscore) {
			isProblem = true;
		} else if (!name.contains(new String("_")) && supposedToStartWithUnderscore) {
			isProblem = true;
		}
		if (isProblem) {
			addProblem(name, line, potentialProblem);
		}
	}

	/**
	 * 
	 * 
	 * @param name
	 * @param nodeType
	 * @param supposedToBeLowercaseAtStart
	 * @param line
	 * @param potentialProblem
	 */
	private void checkLowerCaseAtStart(String name, String nodeType, boolean supposedToBeLowercaseAtStart, int line,
			ProblemType potentialProblem) {
		Character firstChar = name.charAt(0);
		boolean isProblem = false;
		if (Character.isLetter(firstChar)) {
			if (Character.isLowerCase(firstChar) && !supposedToBeLowercaseAtStart) {
				isProblem = true;
			} else if (!Character.isLowerCase(firstChar) && supposedToBeLowercaseAtStart) {
				isProblem = true;
			}
		}
		if (isProblem) {
			addProblem(name, line, potentialProblem);
		}
	}

	/**
	 * METHOD DECLARATION
	 */
	@Override
	public boolean visit(MethodDeclaration node) {
		String name = node.getName().toString();

		ConventionViolationType problem = ConventionViolationType.NON_STATIC_FINAL_CASE_VIOLATION;
		if (this.problemsToCheck.contains(problem)) {
			checkLowerCaseAtStart(name, "Method", true, sourceLine(node), problem);
			checkUnderscore(name, "Method", false, sourceLine(node), problem);
		}
		return true;
	}

	/**
	 * CLASS/INTERFACE/ENUM NAME DECLARATION
	 */
	@Override
	public boolean visit(TypeDeclaration node) {
		String name = node.getName().toString();
		System.out.println("Parsing class " + name + ", starting on line " + this.sourceLine(node));

		ConventionViolationType problem = ConventionViolationType.CLASS_NAME_CASE_VIOLATION;
		if (this.problemsToCheck.contains(problem)) {
			checkLowerCaseAtStart(name, "Class", false, sourceLine(node), problem);
			checkUnderscore(name, "Class", false, sourceLine(node), problem);
		}
		return true;
	}

	/**
	 * CLASS FIELD DECLARATION
	 */
	@Override
	public boolean visit(FieldDeclaration node) {

		// loop for several variables in the same declaration
		for (Object o : node.fragments()) {
			VariableDeclarationFragment var = (VariableDeclarationFragment) o;
			String name = var.getName().toString();

			boolean isStatic = Modifier.isStatic(node.getModifiers());
			boolean isFinal = Modifier.isFinal(node.getModifiers());
			if (isStatic && isFinal) {
				for (char c : name.toCharArray()) {
					if (Character.isLowerCase(c)) {
						addProblem(name, sourceLine(node), ConventionViolationType.STATIC_FINAL_CASE_VIOLATION);
						break;
					}
				}
			}

		}
		return false; // false to avoid child VariableDeclarationFragment to be processed again
	}

	/**
	 * VARIABLE PARAMETER DECLARATION
	 */
	@Override
	public boolean visit(SingleVariableDeclaration node) {
		String name = node.getName().toString();

//		// another visitor can be passed to process the method (parent of parameter)
//		class AssignVisitor extends ASTVisitor {
//			// visits assignments (=, +=, etc)
//			@Override
//			public boolean visit(Assignment node) {
//				String varName = node.getLeftHandSide().toString();
//				if (varName.equals(name)) {
//					System.err.println("Parameter " + varName + " is being modified!! (assignment)");
//				}
//				return true;
//			}
//
//			// visits post increments/decrements (i++, i--)
//			@Override
//			public boolean visit(PostfixExpression node) {
//				String varName = node.getOperand().toString();
//				if (varName.equals(name)) {
//					System.err.println("Parameter " + varName + " is being modified!! (post increment/decrement)");
//				}
//				return true;
//			}
//
//			// visits pre increments/decrements (++i, --i)
//			@Override
//			public boolean visit(PrefixExpression node) {
//				String varName = node.getOperand().toString();
//				if (varName.equals(name)) {
//					System.err.println("Parameter " + varName + " is being modified!! (pre increment/decrement)");
//				}
//				return true;
//			}
//		}
//		AssignVisitor assignVisitor = new AssignVisitor();
//		node.getParent().accept(assignVisitor);

		ConventionViolationType problem = ConventionViolationType.NON_STATIC_FINAL_CASE_VIOLATION;
		if (this.problemsToCheck.contains(problem)) {
			checkLowerCaseAtStart(name, "Parameter", true, sourceLine(node), problem);
			checkUnderscore(name, "Parameter", false, sourceLine(node), problem);
		}
		return true;
	}

	/**
	 * VARIABLE DECLARATION
	 */
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		String name = node.getName().toString();

		ConventionViolationType problem = ConventionViolationType.NON_STATIC_FINAL_CASE_VIOLATION;
		if (this.problemsToCheck.contains(problem)) {
			checkLowerCaseAtStart(name, "Variable", true, sourceLine(node), problem);
			checkUnderscore(name, "Variable", false, sourceLine(node), problem);
		}

		return true;
	}

}
