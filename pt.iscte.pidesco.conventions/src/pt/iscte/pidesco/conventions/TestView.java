package pt.iscte.pidesco.conventions;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import pt.iscte.pidesco.extensibility.PidescoView;

/**
 * delete
 * @author grovy
 *
 */
public class TestView implements PidescoView {

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		viewArea.setLayout(new RowLayout(SWT.HORIZONTAL));
		Label label = new Label(viewArea, SWT.NONE);
		label.setImage(imageMap.get("smiley.png"));
		Text text = new Text(viewArea, SWT.BORDER);
		text.setText("Hello World");

	}

}
