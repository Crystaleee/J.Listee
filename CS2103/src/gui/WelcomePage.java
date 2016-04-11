package gui;

import netscape.javascript.JSObject;

/**
 * @@author A0149527W
 */
public class WelcomePage extends AppPage {
    // This is the html path for help page and the file name
    private static final String PATH_HTML = "/view/html/launch.html";

    public WelcomePage() {
        super(PATH_HTML);

        // communicate Java and JavaScript
        JSObject win = (JSObject) webEngine.executeScript(SCRIPT_WINDOW);
        win.setMember(NAME_BRIDGE, Bridge.getInstance());
    }
}
