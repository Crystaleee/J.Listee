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
public class WelcomePage extends AppPage {
  //This is the html path for help page and the file name
    private static final String PATH_HTML="/view/html/launch.html";
    private static final String FILENAME="\\J.Listee.txt";
    
    public WelcomePage() {
        super(PATH_HTML);
        
        //communicate Java and JavaScript
        JSObject win = (JSObject) webEngine.executeScript(SCRIPT_WINDOW);
        win.setMember(NAME_BRIDGE, new WelcomeBridge());
    }

    // JavaScript interface object
    public class WelcomeBridge {

        public void chooseFolder() {
            DirectoryChooser fileChooser = new DirectoryChooser();
            File selectedFile = fileChooser.showDialog(App.stage);
            
            String  filePath =null;
            if (selectedFile != null) {
               filePath = selectedFile.getAbsolutePath() + FILENAME;
                // create file under the file folder chosen by user
                try {
                    GUIController.createFile(filePath);
                    // display starting page
                    GUIController.initializeList(filePath);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        }
    }
}
