package gui;

import java.lang.reflect.Field;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue; 
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.w3c.dom.Document; 

import com.sun.webkit.WebPage;

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
		this.setStyle("-fx-background-color: rgba(0, 0, 0, 0); ");

		 webEngine.documentProperty().addListener(new DocListener());

		//load web page
		webEngine.load(WelcomeAndChooseStorage.class.getResource(
				this.html).toExternalForm());
		
		// add the web view to the scene
		this.getChildren().add(browser);
	}
	
	class DocListener implements ChangeListener<Document>{  
        @Override
        public void changed(ObservableValue<? extends Document> observable, Document oldValue, Document newValue) {
            try {

                // Use reflection to retrieve the WebEngine's private 'page' field. 
                Field f = webEngine.getClass().getDeclaredField("page"); 
                f.setAccessible(true); 
                com.sun.webkit.WebPage page =  (WebPage) f.get(webEngine);  
                page.setBackgroundColor((new java.awt.Color(0, 0, 0, 0)).getRGB()); 

            } catch (Exception e) {
            }

        }
    }  
}
