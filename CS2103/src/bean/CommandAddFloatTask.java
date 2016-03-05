/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 8:06pm
 * CS2103
 */
package bean;
import java.io.IOException;
import java.util.ArrayList;

import History.History;
import storage.Storage;

public class CommandAddFloatTask extends Command{
    private String description;
    private String location;
    private ArrayList<String> tags;
    private final String MESSAGE_NO_DESCRIPTION = "Pls enter a description";
    private final String MESSAGE_SUCCESS = "added: \"%2$s\"";
    private final String MESSAGE_ERROR_UPDATE_FILE = "Error occured while updating to file";
    
    public CommandAddFloatTask(){
        this.description = null;
        this.location = null;
        this.tags = null;
    }
    
    public CommandAddFloatTask(String description, String location, ArrayList<String> tags){
        this.description = description;
        this.location = location;
        this.tags = tags;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setDescription(String description){
        this.description = description;
    } 
    
    public String getLocation(){
        return location;
    }
    
    public void setLocation(String location){
        this.location = location;
    } 
    
    public String getMessageNoDescription(){
        return MESSAGE_NO_DESCRIPTION;
    }
    
    public String getSuccessMessage(){
        return MESSAGE_SUCCESS;
    }
    
    public String getMessageErrorUpdateFile(){
        return MESSAGE_ERROR_UPDATE_FILE;
    }
    
    public void setTags(ArrayList<String> tags){
        this.tags = tags;
    }
    
    public ArrayList<String> getTags(){
        return tags;
    }
    
    public Display execute(Display display){
        if(description == null){
            display.setMessage(MESSAGE_NO_DESCRIPTION);
            return display;
        }
        display.getFloatTasks().add(new Task(description, location, tags));
        if(hasUpdateFile(display)){
            display.setMessage(MESSAGE_SUCCESS);
            History.saveDisplay(display);
        }
        else{
            display = new Display(MESSAGE_ERROR_UPDATE_FILE);
        }
        return display;
    }
    
    public boolean hasUpdateFile(Display display) {
        try{
            Storage.saveFile(display);
            return true;
        }catch(IOException error){
            return false;
        }
    }
}
