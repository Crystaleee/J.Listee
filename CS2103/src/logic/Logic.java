/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 22 Mar, 12:10AM
 * CS2103
 */
package logic;

import java.io.IOException;

import History.History;
import bean.Command;
import bean.Display;
import parser.Parser;
import storage.Storage;

public class Logic {

    public static String MESSAGE_ADD_SUCCESS = "added: \"%1$s\"";
    
    public static final String MESSAGE_FILE_CREATED = "File created and ready for use";
    public static final String MESSAGE_ERROR_FILE_EXISTS = "File already exists";
    public static final String MESSAGE_ERROR_READING_FILE = "Error occured while reading file";
    public static final String MESSAGE_NO_DESCRIPTION = "Pls enter a description";
    public static final String MESSAGE_ERROR_UPDATE_FILE = "Error occured while updating to file";

    private static Display display;
    private static String file;
    
    public static boolean createFile(String filePath){
        try{
            Storage.createFile(filePath);
            return true;
        }catch(IOException error){
            return false;
        }
    }
    
    public static Display initializeProgram(String filePath){
        file = filePath;
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
    
    public static Display executeUserCommand(String userInput) {
        History.saveUserInput(userInput);
        Parser myParser = new Parser();
        Command userCommand = myParser.ParseCommand(userInput);
        try {
            display = userCommand.execute(Storage.getDisplay(file));
        } catch (IOException e) {
            //fail to get display
        }
        
        if(userCommand.getUpdateFile()){
            if(successfullyUpdatesFile()){
                if(userCommand.getSaveHistory()){
                    History.saveDisplay(display);
                }
            }
            else{
                display = new Display(MESSAGE_ERROR_UPDATE_FILE);
            }
        }
        
        return display;
    }
    
    public static boolean successfullyUpdatesFile() {
        try{
            Storage.saveFile(display);
            return true;
        }catch(IOException error){
            return false;
        }
    }
}
