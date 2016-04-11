package gui;

import java.lang.reflect.Field;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.w3c.dom.Document;

import com.sun.webkit.WebPage;

// @@author A0149527W

/**
 * abstract class for every page in the app
 * 
 * @extends StackPane
 */
public abstract class AppPage extends StackPane {
    // this is the javafx statement to set background to transparent
    private static final String JAVAFX_BACKGROUND_TRANSPARENT = "-fx-background-color: rgba(0, 0, 0, 0); ";

    // common bridge name and script name
    protected static final String NAME_BRIDGE = "app";
    protected static final String SCRIPT_WINDOW = "window";

    private WebView browser; // the web view to put in the scene
    protected WebEngine webEngine; // the web engine to load web page
    protected String html; // the web page

    public AppPage(String html) {
        // initilize parameters
        this.browser = new WebView();
        this.webEngine = browser.getEngine();
        this.html = html;

        // disable context menu
        this.browser.setContextMenuEnabled(false);

        // set transparent background
        setTransparentBackground();

        // load web page
        webEngine.load(WelcomePage.class.getResource(this.html).toExternalForm());

        // add the web view to the scene
        this.getChildren().add(browser);
    }

    /**
     * set transparent background by seting StakePane's background and adding
     * listener to the web page
     */
    private void setTransparentBackground() {
        this.setStyle(JAVAFX_BACKGROUND_TRANSPARENT);
        this.webEngine.documentProperty().addListener(new DocListener());
    }
    
    private class DocListener implements ChangeListener<Document> {
        @Override
        public void changed(ObservableValue<? extends Document> observable, Document oldValue,
                Document newValue) {
            try {
                // Use reflection to retrieve the WebEngine's private 'page'
                // field.
                Field f = webEngine.getClass().getDeclaredField("page");
                f.setAccessible(true);
                com.sun.webkit.WebPage page = (WebPage) f.get(webEngine);
                page.setBackgroundColor((new java.awt.Color(0, 0, 0, 0)).getRGB());

            } catch (Exception e) {
            }
        }
    }
}
