/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 22 Mar, 12:10AM
 * CS2103
 */
package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import History.History;
import bean.Command;
import bean.CommandAddDeadlineTask;
import bean.CommandUndo;
import bean.Display;
import parser.JListeeParser;
import storage.Storage;

public class Logic {

    public static String MESSAGE_ADD_SUCCESS = "added: \"%1$s\"";
    
    public static final String MESSAGE_FILE_CREATED = "File created and ready for use";
    public static final String MESSAGE_ERROR_FILE_EXISTS = "File already exists";
    public static final String MESSAGE_ERROR_READING_FILE = "Error occured while reading file";
    public static final String MESSAGE_NO_DESCRIPTION = "Pls enter a description";
    public static final String MESSAGE_ERROR_UPDATE_FILE = "Error occured while updating to file";

    private static Storage storage = Storage.getInstance();
    private static Display display;
    private static String file;
    
    public static boolean createFile(String filePath){
        file = filePath;
        try{
            storage.createFile(filePath);
            return true;
        }catch(IOException error){
            return false;
        }
    }
    
    public static Display initializeProgram(String filePath){
            file = filePath;
            getDisplayFromStorage();
            display.setMessage(null);
            History.saveDisplay(display);
            return display;
    }
    
    public static Display executeUserCommand(String userInput) {
        History.saveUserInput(userInput);
        Command userCommand = parseUserInput(userInput);
        display = executeCommand(userCommand);
        
        return display;
    }

    private static void saveToHistory(Command userCommand) {
        if(userCommand.getSaveHistory()){
            History.saveDisplay(display);
        }
    }

    public static Display executeCommand(Command userCommand) {
        getDisplayFromStorage();
        display = userCommand.execute(display);
        
        if(userCommand.getUpdateFile()){
            if(successfullyUpdatesFile()){
                saveToHistory(userCommand);
            }
            else{
                display = new Display(MESSAGE_ERROR_UPDATE_FILE);
            }
        }
        return display;
    }

    private static Command parseUserInput(String userInput) {
    	JListeeParser myParser = new JListeeParser();
        Command userCommand = myParser.ParseCommand(userInput);
        return userCommand;
    }

    private static void getDisplayFromStorage() {
        try {
            display = storage.getDisplay(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private static boolean successfullyUpdatesFile() {
        try{
            storage.saveFile(display);
            return true;
        }catch(IOException error){
            return false;
        }
    }
}
