package gui;

import parser.InputSuggestion;
import netscape.javascript.JSObject;

/**
 * @@author A0149527W
 */
public class Help extends AppPage {
	private InputSuggestion suggestion = InputSuggestion.getInstance();

	public Help() {
		super("/view/html/help.html");
		JSObject win = (JSObject) webEngine.executeScript("window");
		win.setMember("app", new HelpBridge());
	}

	// JavaScript interface object
	public class HelpBridge {

		public void receiveCommand(String command) {
			String cmd = command.trim();
			GUIController.handelUserInput(cmd);
		}

		public String getCommandsuggestion(String cmd) {
			String cmdSuggestion = suggestion.getSuggestedInput(cmd.trim());
			if (cmdSuggestion.equals(new String("null")) == false) {
				return cmdSuggestion;
			} else {
				return "";
			}
		}
	}

}
