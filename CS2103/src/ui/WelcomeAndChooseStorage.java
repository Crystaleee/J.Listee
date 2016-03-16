package ui;

import java.io.IOException;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import main.App;
import netscape.javascript.JSObject;

/**
 * @author Zhu Bingjing
 * @date 2016年3月1日 下午10:08:56
 * @version 1.0
 */
public class WelcomeAndChooseStorage extends Pane {
	private WebView browser = new WebView();
	private WebEngine webEngine = browser.getEngine();
	public static WelcomeAndChooseStorage welcome;

	public static WelcomeAndChooseStorage getInstance(){
		if(welcome==null)
			welcome=new WelcomeAndChooseStorage();
		return welcome;
	}
	
	private WelcomeAndChooseStorage() {
		// load the web page
		webEngine.load(WelcomeAndChooseStorage.class.getResource(
				"/html/launch.html").toExternalForm());
		// process page loading
		webEngine
				.getLoadWorker()
				.stateProperty()
				.addListener(
						(ObservableValue<? extends State> ov, State oldState,
								State newState) -> {
							if (newState == State.SUCCEEDED) {
								JSObject win = (JSObject) webEngine
										.executeScript("window");
								win.setMember("app", new WelcomeBridge());
							}
						});
		// add the web view to the scene
		this.getChildren().add(browser);
	}

	// JavaScript interface object
	public class WelcomeBridge {

		public void chooseFolder()  {
			JFileChooser fileChooser = new JFileChooser("D:\\");
			//TODO directories only
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = fileChooser.showOpenDialog(fileChooser);
			if (returnVal == JFileChooser.APPROVE_OPTION) {				
				App.filePath=fileChooser.getSelectedFile().getAbsolutePath()+"\\J.Listee.txt";
				// create file under the file folder chosen by user
				UI ui=UI.getInstance();
				//TODO EXCEPTION
				try {
					ui.createFile(App.filePath);
					//display starting page
					ui.initializeList(App.stage, App.filePath);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(fileChooser, e.getMessage());
				}
			}
		}
	}

}
