package pt.iscte.pidesco.conventions.service;

import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.widgets.Button;

import pt.iscte.pidesco.conventions.problems.Problem;
import pt.iscte.pidesco.conventions.problems.conventions.ViolationType;

/**
 * Contains the services provided by the Conventions Checker view.
 * 
 * @author grovy
 *
 */
public interface ConventionsCheckerServices {

	/**
	 * Adds files to queue up for the analysis program. Notice that the list used
	 * for these files is unique, and does not override (or is overridden by) the
	 * default file selection method of selecting files through the ProjectBrowser.
	 * 
	 * @param absoluteFilePaths - <u><b>ABSOLUTE</b></u> file paths to valid *.java files.
	 */
	void addFilesToAnalyze(String[] absoluteFilePaths);

	/**
	 * Returns the problems that a file has.
	 * 
	 * @param absoluteFilePath - <u><b>ABSOLUTE</b></u> file path to a valid *.java file.
	 * @return non-null List of Problems that a *.java file has.
	 */
	List<Problem> getFileProblems(String absoluteFilePath);

	/**
	 * Returns the loaded types of violations and the Eclipse SWT Widget button
	 * associated with it. They can be freely manipulated to cause changes to the
	 * GUI.
	 * 
	 * @return non-null hash map of Violations and the buttons associated with them.
	 */
	HashMap<ViolationType, Button> getAvailableProblems();
}
