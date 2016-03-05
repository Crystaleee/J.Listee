/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/6/2016, 1:38am
 * CS2103
 */
package bean;
import java.io.IOException;

import History.History;
import storage.Storage;

public class CommandRedo extends Command{
    private final String MESSAGE_REDO = "Redid last command";
    private final String MESSAGE_ERROR_REDO = "You have reached the latest point possible";
    private final String MESSAGE_ERROR = "Error occured while updating to file";
    
    public CommandRedo(){
    }
    
    public Display execute(Display display){
        if(History.atLastState()){
            display = new Display(MESSAGE_ERROR_REDO);
            return display;
        }
        
        display = History.getDisplay(1);
        if(updateFile(display)){
            display.setMessage(MESSAGE_REDO);
            History.incrementIndex();
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
