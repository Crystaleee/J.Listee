/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/4/2016, 4:30AM
 * CS2103
 */
package logic;

import java.io.IOException;

import History.History;
import bean.Display;
import parser.Parser;
import storage.Storage;

public class Logic {
    
    private static final String MESSAGE_FILE_CREATED = "File created and ready for use";
    private static final String MESSAGE_ERROR_FILE_EXISTS = "File already exists";
    private static final String MESSAGE_ERROR_READING_FILE = "Error occured while reading file";

    private static Display display;
    
    public static boolean createFile(String filePath){
        try{
            Storage.createFile(filePath);
            return true;
        }catch(IOException error){
            return false;
        }
    }
    
    public static Display initializeProgram(String filePath){
        try{
            display = Storage.getDisplay(filePath);
            display.setMessage(null);
            History.saveDisplay(display);
            return display;
        }catch(IOException error){
            display = new Display(MESSAGE_ERROR_FILE_EXISTS);
            return display;
        }
    }
    
    public static Display handleCommand(String userInput) {
        History.saveUserInput(userInput);
        Parser myParser = new Parser();
        display = myParser.ParseCommand(userInput).execute(History.getDisplay(0));
        //display = Parser.ParseCommand(userInput).execute(History.getDisplay(0));
        
        return display;
    }
}
