package ui;

import javax.swing.JFileChooser;

import storage.LogStorage;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserFunction;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

/**
 * @author Zhu Bingjing
 * @date 2016年3月1日 下午10:08:56
 * @version 1.0
 */
public class WelcomeAndChooseStorage extends Browser {
	BrowserView browserView;

	public WelcomeAndChooseStorage() {
		browserView = new BrowserView(this);
		this.loadURL(View.programPath + "\\src\\html\\launch.html");

		// Register Java callback function that will be invoked from JavaScript
		// when user clicks choose filefolder button.
		this.registerFunction("onChooseFolder", new BrowserFunction() {
			@Override
			public JSValue invoke(JSValue... args) {
				JFileChooser fileChooser = new JFileChooser("D:\\");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fileChooser.showOpenDialog(fileChooser);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					LogStorage.filePath = fileChooser.getSelectedFile().getAbsolutePath();
					LogStorage.writeLogFile();
				}
				// Return undefined (void) to JavaScript.
				return JSValue.createUndefined();
			}
		});
	}
}
