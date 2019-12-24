package pt.iscte.pidesco.conventions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

import pt.iscte.pidesco.conventions.parser.ConventionCheckerVisitor;
import pt.iscte.pidesco.conventions.problems.Problem;
import pt.iscte.pidesco.conventions.problems.conventions.NonStaticFinalCaseViolation;
import pt.iscte.pidesco.conventions.problems.conventions.ViolationType;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class ConventionsView implements PidescoView {

	ArrayList<String> filesToAnalyze = new ArrayList<String>();
	ListMultimap<String, Problem> filesAndProblems = MultimapBuilder.hashKeys().arrayListValues().build();
	HashMap<ViolationType, Button> problemCheckboxes = new HashMap<ViolationType, Button>();

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		viewArea.setLayout(new RowLayout(SWT.VERTICAL));
		BundleContext context = Activator.getContext();
		initializeProjectBrowserService(context);
		initializeJavaEditorService(context);
		createButtons(viewArea);
		// TODO createExtensions();
	}

	/**
	 * Initialises the Java Editor Service for the Java Conventions Checker, so it
	 * can annotate convention violations/code smells in problematic files.
	 *
	 * @param context
	 */
	private void initializeJavaEditorService(BundleContext context) {
		ServiceReference<JavaEditorServices> serviceReference = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices javaServ = context.getService(serviceReference);
		// TODO actually modify the java source files to annotate problems
	}

	/**
	 * Initialises the Project Browser Service for the Java Conventions Checker, in
	 * order to gather a list of classes that are meant to be checked. Each class is
	 * represented by their absolute file path for loading simplicity's sake.
	 *
	 * @param context
	 */
	private void initializeProjectBrowserService(BundleContext context) {
		ServiceReference<ProjectBrowserServices> serviceReference = context
				.getServiceReference(ProjectBrowserServices.class);
		ProjectBrowserServices projServ = context.getService(serviceReference);

		projServ.addListener(new ProjectBrowserListener() {
			@Override
			public void selectionChanged(Collection<SourceElement> selection) {
				ArrayList<String> newFiles = new ArrayList<String>();
				for (SourceElement element : selection) {
					String filePath = element.getFile().getAbsolutePath();
					if (filePath.endsWith(".java")) { // we only want classes, interfaces, etc
						newFiles.add(filePath);
					}
				}
				filesToAnalyze = newFiles;
			}
		});

		// TODO do more fancy stuff when the conventions checker does things
		// maybe add a button on the conventions checker to filter classes that have
		// problems
	}
	
	private ArrayList<ViolationType> createExtensionsViolations(Composite viewArea) {
		IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pt.iscte.pidesco.conventions.xtraconventions");

		IExtension[] extensions = extensionPoint.getExtensions();
		ArrayList<ViolationType> problemTypes = new ArrayList<ViolationType>();
		for(IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for(IConfigurationElement c : confElements) {
				String s = c.getAttribute("name");
				try {
					ViolationType o = (ViolationType) c.createExecutableExtension("class");
					problemTypes.add(o);
					System.out.println(o);
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		return problemTypes;

	}

	private void createButtons(Composite viewArea) {
		// TODO selection buttons for:
		// all of the available convention violation types
		// all of the code smell types

		ArrayList<ViolationType> problems = createExtensionsViolations(viewArea);
		problems.add(new NonStaticFinalCaseViolation());

		for (ViolationType problem : problems) {
			Button b = new Button(viewArea, SWT.CHECK);
			b.setText(problem.getProperName());
			this.problemCheckboxes.put(problem, b);
		}





		// TODO action buttons for:
		// pull the lever, kronk (run the checker)
		// filter classes in the Project Browser so that only those with problems are
		// displayed
		
		/*
		 * Button to Run Checker
		 */
		createRunCheckerButton(viewArea);

		viewArea.layout();

	}

	private void createRunCheckerButton(Composite viewArea) {
		Button b = new Button(viewArea, SWT.PUSH);
		b.setText("Run Checker");
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				b.setEnabled(false); // we don't want the user to run the checker multiple times at once
				ArrayList<ViolationType> problemsToCheck = getProblemsToCheck();
				ConventionCheckerVisitor checker = new ConventionCheckerVisitor(problemsToCheck, filesToAnalyze);
				try {
					filesAndProblems = checker.runChecker();
					warnUserAboutFoundProblems();
				} finally {
					b.setEnabled(true); // whatever happens, the button MUST be enabled again
				}
			}

			private void warnUserAboutFoundProblems() {
				String barrier = "===================================";
				System.out.println(barrier);
				System.out.println("Java Convention Checker");
				Map<String, Collection<Problem>> fileProblemMap = filesAndProblems.asMap();
				for (String problematicFile: fileProblemMap.keySet()) {
					System.out.println(barrier);
					System.out.println(problematicFile);
					Collection<Problem> problemsInFile = fileProblemMap.get(problematicFile);
					for (Problem problemInFile: problemsInFile) {
						System.out.println(problemInFile.getProblemType().getProperName() + " on Line " + problemInFile.getStartingLine() + ": " + problemInFile.getElementName());
					}
				}
				System.out.println(barrier);
				System.out.println("Done.");
			}

			private ArrayList<ViolationType> getProblemsToCheck() {
				ArrayList<ViolationType> problemsToCheck = new ArrayList<ViolationType>();
				for (ViolationType problem : problemCheckboxes.keySet()) {
					if (problemCheckboxes.get(problem).getSelection()) {
						problemsToCheck.add(problem);
					}
				}
				return problemsToCheck;
			}
		});
	}
}
