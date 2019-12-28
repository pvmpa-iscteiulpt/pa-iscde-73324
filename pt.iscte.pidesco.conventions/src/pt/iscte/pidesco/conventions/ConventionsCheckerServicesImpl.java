package pt.iscte.pidesco.conventions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.widgets.Button;

import pt.iscte.pidesco.conventions.problems.Problem;
import pt.iscte.pidesco.conventions.problems.conventions.ViolationType;
import pt.iscte.pidesco.conventions.service.ConventionsCheckerServices;

public class ConventionsCheckerServicesImpl implements ConventionsCheckerServices {

	@Override
	public void addFilesToAnalyze(String[] absoluteFilePaths) {
		activateView();
		ConventionsView instance = ConventionsView.getInstance();
		
		ArrayList<String> currentList = instance.getServiceFiles();
		for (String filePath: absoluteFilePaths) {
			if (!currentList.contains(filePath) && new File(filePath).canRead()) {
				currentList.add(filePath);
			}
		}

	}

	@Override
	public List<Problem> getFileProblems(String absoluteFilePath) {
		activateView();
		ConventionsView instance = ConventionsView.getInstance();
		return instance.getFilesAndProblems().get(absoluteFilePath);
	}

	@Override
	public HashMap<ViolationType, Button> getAvailableProblems() {
		activateView();
		ConventionsView instance = ConventionsView.getInstance();
		return instance.getProblemCheckboxes();
	}

	
	private void activateView() {
		ConventionsCheckerActivator.getInstance().getPidescoServices().openView(ConventionsView.VIEW_ID);
	}
}
