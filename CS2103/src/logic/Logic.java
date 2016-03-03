/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/4/2016, 4:30AM
 * CS2103
 */
package logic

import java.io.IOException;
import java.util.ArrayList;
public class Logic {
    
    private static final String MESSAGE_FILE_CREATED = "File created and ready for use";
    private static final String MESSAGE_ERROR_FILE_EXISTS = "File already exists";
    private static final String MESSAGE_ERROR_READING_FILE = "Error occured while reading file";

    private static Display display;
    
    public static Display createFile(String filePath){
        try{
            Storage.createFile(filePath);
            setDisplay(MESSAGE_FILE_CREATED, null);
            return display;
        }catch(IOException error){
            setDisplay(MESSAGE_ERROR_FILE_EXISTS, null);
            return display;
        }
    }
    
    public static Display initialiseProgram(){
        try{
            ArrayList<Task> taskList = Storage.getList();
            History.saveList(taskList);
            setDisplay(null, taskList);
            return display;
        }catch(IOException error){
            setDisplay(MESSAGE_ERROR_READING_FILE, null);
            return display;
        }
    }
    
    public static Display handleCommand(String userInput) {
        History.saveUserInput(userInput);
        display = Parser.parseCommand(userInput).execute(History.getTaskList(0));
        
        return display;
    }
    
    public static void setDisplay(String message, ArrayList<Task> list) {
        display.setMessage(message);
        display.setList(list);
    }
}
