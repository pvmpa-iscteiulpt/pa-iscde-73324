package pt.iscte.pidesco.conventions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

import pt.iscte.pidesco.conventions.problems.Problem;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class ConventionsView implements PidescoView {

	ArrayList<String> filesToAnalyze = new ArrayList<String>();
	ListMultimap<String, Problem> filesAndProblems = MultimapBuilder.hashKeys().arrayListValues().build();

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

	private void createButtons(Composite viewArea) {
		
		

	}
}
