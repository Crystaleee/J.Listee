/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/6/2016, 1:28am
 * CS2103
 */

package bean;
import java.io.IOException;

import History.History;
import storage.Storage;

public class CommandUndo extends Command{
    private final String MESSAGE_UNDO = "Undid last command";
    private final String MESSAGE_ERROR_UNDO = "You have reached the earliest point possible";
    private final String MESSAGE_ERROR = "Error occured while updating to file";
    
    public CommandUndo(){
    }
    
    public Display execute(Display display){
        if(History.atFirstState()){
            display = new Display(MESSAGE_ERROR_UNDO);
            return display;
        }
        
        display = History.getDisplay(-1);
        if(updateFile(display)){
            display.setMessage(MESSAGE_UNDO);
            History.decrementIndex();
        }
        else{
            display = new Display(MESSAGE_ERROR);
        }
        return display;
    }
    
    public boolean updateFile(Display display) {
        try{
            Storage.saveFile(display);
            return true;
        }catch(IOException error){
            return false;
        }
    }
}
