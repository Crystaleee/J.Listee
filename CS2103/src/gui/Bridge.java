package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import parser.InputSuggestion;
import javafx.stage.DirectoryChooser;
import main.App;

/**
 * @@author  Zhu Bingjing
 */

// JavaScript interface object
public class Bridge {
    //the name of file storing tasks
    private static final String FILENAME="\\J.Listee.txt";
    
    //These are the specific specila commands
    private static ArrayList<String> helpCommands = new ArrayList<String>(
            Arrays.asList("help", "show help"));
    private static ArrayList<String> exitCommands = new ArrayList<String>(
            Arrays.asList("exit", "quit"));
    private static ArrayList<String> changeLocationCommands = new ArrayList<String>(
            Arrays.asList("change filepath", "change directory", "change location"));
    
    //the only instance of Bridge
    private static Bridge bridge;
    
    // this is the list of string storing user's commands
    private static List<String> userCmd =new ArrayList<String>();
    
    // Parser.InputSuggestion to get command suggestion from
    private static InputSuggestion suggestion=InputSuggestion.getInstance();
    
    // the presenting cmd's index
    private static int cmdIndex;
    
    private Bridge(){
    }
    
    public static Bridge getInstance(){
        if (bridge == null) {
            return new Bridge();
        }
        return bridge;
    }
    
    /**
     * receive use cmd, add cmd history and decide what to do
     * 
     * @param command
     */
    public void receiveCommand(String command) {
        String cmd = command.trim();

        //add cmd to cmd history
        setCmdHistory(cmd);
        
        //if it's not a special command, pass to GUIController
        if(!checkSpecialCmd(cmd)){
            GUIController.handelUserInput(cmd);
        }
    }

    /**
     * check if it's special command including "help", "change filepath", "exit"
     * 
     * @param cmd the user input command
     * @return boolean if it's a special command
     */
    private boolean checkSpecialCmd(String cmd) {
        if(helpCommands.contains(cmd)){
            GUIController.displayHelp();
            return true;
        }else if(changeLocationCommands.contains(cmd)){
            changeStorageLocation();
            return true;
        }else if(exitCommands.contains(cmd)){
            App.terminate();
            return true;
        }else{
            return false;
        }
    }

    /**
     *  get sommand suggestion from parser and pass to Javascript
     *  
     * @param cmd
     *              the user input command
     * @return String 
     * command suggestion
     */
    public String getCommandSuggestion(String cmd) {
        String cmdSuggestion = suggestion.getSuggestedInput(cmd.trim());
        return cmdSuggestion;
    }
    
    /**
     * set cmd history
     * 
     * @param cmd user input command
     */
    private void setCmdHistory(String cmd) {
        userCmd.add(cmd);
        cmdIndex = userCmd.size() - 1;
    }

    /**
     * pop up a file chooser and send new filepath
     */
    private void changeStorageLocation() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        File selectedFile = fileChooser.showDialog(App.stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath() + FILENAME;
            
            // create file under the file folder chosen by user
            GUIController.changeFilePath(filePath);
        }
    }

    /**
     * get previous entered command
     * 
     * @return previous command
     */
    public String getPreviousCmd() {
        if (cmdIndex > 0) {       
            return userCmd.get(cmdIndex--);
        } else {
            //return the first cmd when there's no previous one
            return userCmd.get(0);
        }
    }

    /**
     * get later command entered
     * 
     * @return later command
     */
    public String getLaterCmd() {
        if (cmdIndex < userCmd.size() - 1) {
            return userCmd.get(++cmdIndex);
        } else
          //return ""  when there's no later one
            return "";
    }
}
