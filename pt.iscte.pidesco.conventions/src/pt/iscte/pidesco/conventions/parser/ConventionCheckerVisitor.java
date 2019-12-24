package pt.iscte.pidesco.conventions.parser;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
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

import pt.iscte.pidesco.conventions.problems.CommonCodeChecks;
import pt.iscte.pidesco.conventions.problems.Problem;
import pt.iscte.pidesco.conventions.problems.ProblemType;
import pt.iscte.pidesco.conventions.problems.conventions.ConventionViolation;
import pt.iscte.pidesco.conventions.problems.conventions.NonStaticFinalCaseViolation;

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
	 * Adds a problem to the problem list.
	 * 
	 * @param violation - the committed violation.
	 */
	private void addProblem(ConventionViolation violation) {
		this.detectedProblems.put(this.currentFile, violation);
	}

	/**
	 * Delegates the processing of a potential problem to the available problem type
	 * processors.
	 * 
	 * @param node - the current AST node
	 */
	private void delegateProblemProcessing(ASTNode node) {
		for (ProblemType problem : this.problemsToCheck) {
			ConventionViolation violation = problem.analyzeCode(node, this.currentFile);
			if (violation != null) {
				addProblem(violation);
			}
		}
	}

	/**
	 * METHOD DECLARATION
	 */
	@Override
	public boolean visit(MethodDeclaration node) {
		delegateProblemProcessing(node);
		return true;
	}

	/**
	 * CLASS/INTERFACE/ENUM NAME DECLARATION
	 */
	@Override
	public boolean visit(TypeDeclaration node) {
		String name = node.getName().toString();
		System.out.println("Parsing class " + name + ", starting on line " + CommonCodeChecks.sourceLine(node));

		delegateProblemProcessing(node);
		return true;
	}

	/**
	 * CLASS FIELD DECLARATION
	 */
	@Override
	public boolean visit(FieldDeclaration node) {
		delegateProblemProcessing(node);
		return false; // false to avoid child VariableDeclarationFragment to be processed again
	}

	/**
	 * VARIABLE PARAMETER DECLARATION
	 */
	@Override
	public boolean visit(SingleVariableDeclaration node) {
		// another visitor can be passed to process the method (parent of parameter)
		class AssignVisitor extends ASTVisitor {
			// visits assignments (=, +=, etc)
			@Override
			public boolean visit(Assignment node) {
				delegateProblemProcessing(node);
				return true;
			}

			// visits post increments/decrements (i++, i--)
			@Override
			public boolean visit(PostfixExpression node) {
				delegateProblemProcessing(node);
				return true;
			}

			// visits pre increments/decrements (++i, --i)
			@Override
			public boolean visit(PrefixExpression node) {
				delegateProblemProcessing(node);
				return true;
			}
		}
		AssignVisitor assignVisitor = new AssignVisitor();
		node.getParent().accept(assignVisitor);

		delegateProblemProcessing(node);
		return true;
	}

	/**
	 * VARIABLE DECLARATION
	 */
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		delegateProblemProcessing(node);
		return true;
	}

}
