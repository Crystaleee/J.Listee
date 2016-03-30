package gui;

import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * @@author A0149527W
 */
public abstract class AppPage extends Pane{
	WebView browser;
	WebEngine webEngine;
	String html;
	
	public AppPage(String html){
		this.browser = new WebView();
		this.webEngine = browser.getEngine();
		this.html=html;
		browser. setContextMenuEnabled(false);

		//load web page
		webEngine.load(WelcomeAndChooseStorage.class.getResource(
				this.html).toExternalForm());
		
		// add the web view to the scene
		this.getChildren().add(browser);
	}
	
}
