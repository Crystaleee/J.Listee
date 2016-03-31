package gui;

import main.App;
import netscape.javascript.JSObject;

/**
 * @@author  A0149527W
 */
public class Help extends AppPage{

	public Help() {
		super("/view/html/help.html");		
		JSObject win = (JSObject) webEngine
				.executeScript("window");
		win.setMember("app", new HelpBridge());
	}
	
	// JavaScript interface object
	public class HelpBridge {

		public void receiveCommand(String command){
			GUIController.displayList(App.stage, ((ShowList)(GUIController.getShowList())).getDisplay());
		}
	}

}
	
