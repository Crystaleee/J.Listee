package gui;

import java.io.File;
import java.io.IOException;

import javafx.stage.DirectoryChooser;

import javax.swing.JOptionPane;

import netscape.javascript.JSObject;
import main.App;

/**
 * @@author A0149527W
 */
public class WelcomeAndChooseStorage extends AppPage {
	
	public WelcomeAndChooseStorage() {
		super("/view/html/launch.html");
		JSObject win = (JSObject) webEngine
				.executeScript("window");
		win.setMember("app", new WelcomeBridge());
	}

	// JavaScript interface object
	public class WelcomeBridge {

		public void chooseFolder()  {
			DirectoryChooser fileChooser = new DirectoryChooser();
			File selectedFile = fileChooser.showDialog(App.stage);
			fileChooser.setTitle("Please select a folder for storage location");
			
			if(selectedFile!=null){			
				App.filePath=selectedFile.getAbsolutePath()+"\\J.Listee.txt";
				// create file under the file folder chosen by user
				try {
					GUIController.createFile(App.filePath);
					//display starting page
					GUIController.initializeList(App.filePath);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		}
	}

}
