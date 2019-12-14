package pt.iscte.pidesco.conventions.parser;
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

public class ConventionChecker {

	/**
	 * Given any node of the AST, returns the source code line where it was parsed.
	 */
	private static int sourceLine(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition());
	}

	private static class CheckConventions extends ASTVisitor {

		/**
		 * @param name
		 */
		private void checkUnderscore(String name, String nodeType) {
			if (name.contains(new String("_"))) {
				System.err.println(nodeType + " " + name + " contains an underscore!");
			}
		}

		/**
		 * @param name
		 */
		private void checkLowerCaseAtStart(String name, String nodeType, boolean supposedToBeLowercaseAtStart) {
			Character firstChar = name.charAt(0);
			if (Character.isLetter(firstChar)) {
				if (Character.isLowerCase(firstChar) && !supposedToBeLowercaseAtStart) {
					System.err.println(nodeType + " " + name + " starts with lowercase!");
				} else if (!Character.isLowerCase(firstChar) && supposedToBeLowercaseAtStart) {
					System.err.println(nodeType + " " + name + " starts with uppercase!");
				}
			}
		}

		//visits method declaration
		@Override
		public boolean visit(MethodDeclaration node) {
			String name = node.getName().toString();

			checkLowerCaseAtStart(name, "Method", true);
			checkUnderscore(name, "Method");

			return true;
		}

		// visits class/interface declaration
		@Override
		public boolean visit(TypeDeclaration node) {
			String name = node.getName().toString();
			System.out.println("Parsing class " + name + ", starting on line " + sourceLine(node));

			checkLowerCaseAtStart(name, "Class", false);
			checkUnderscore(name, "Class");

			return true;
		}

		// visits attributes
		@Override
		public boolean visit(FieldDeclaration node) {

			// loop for several variables in the same declaration
			for(Object o : node.fragments()) {
				VariableDeclarationFragment var = (VariableDeclarationFragment) o;
				String name = var.getName().toString();

				boolean isStatic = Modifier.isStatic(node.getModifiers());
				boolean isFinal = Modifier.isFinal(node.getModifiers());
				if (isStatic && isFinal) {
					for (char c: name.toCharArray()) {
						if (Character.isLowerCase(c)) {
							System.err.println("Constant " + name + " has lowercase letters!");
							break;
						}
					}
				}

			}
			return false; // false to avoid child VariableDeclarationFragment to be processed again
		}

		// visits variable declarations in parameters
		@Override
		public boolean visit(SingleVariableDeclaration node) {
			String name = node.getName().toString();

			// another visitor can be passed to process the method (parent of parameter)
			class AssignVisitor extends ASTVisitor {
				// visits assignments (=, +=, etc)
				@Override
				public boolean visit(Assignment node) {
					String varName = node.getLeftHandSide().toString();
					if (varName.equals(name)) {
						System.err.println("Parameter " + varName + " is being modified!! (assignment)");
					}
					return true;
				}

				// visits post increments/decrements (i++, i--)
				@Override
				public boolean visit(PostfixExpression node) {
					String varName = node.getOperand().toString();
					if (varName.equals(name)) {
						System.err.println("Parameter " + varName + " is being modified!! (post increment/decrement)");
					}
					return true;
				}

				// visits pre increments/decrements (++i, --i)
				@Override
				public boolean visit(PrefixExpression node) {
					String varName = node.getOperand().toString();
					if (varName.equals(name)) {
						System.err.println("Parameter " + varName + " is being modified!! (pre increment/decrement)");
					}
					return true;
				}
			}
			AssignVisitor assignVisitor = new AssignVisitor();
			node.getParent().accept(assignVisitor);

			checkUnderscore(name, "Parameter");
			checkLowerCaseAtStart(name, "Parameter", true);

			return true;
		}

		// visits variable declarations
		@Override
		public boolean visit(VariableDeclarationFragment node) {
			String name = node.getName().toString();

			checkUnderscore(name, "Variable");
			checkLowerCaseAtStart(name, "Variable", true);

			return true;
		}

	}

	public static void main(String[] args) {
		CheckConventions checker = new CheckConventions();
		JavaParser.parse("TestExampleConventions.java", checker);
		System.out.println("---");
		JavaParser.parse("TestExampleOKConventions.java", checker);

	}
}
