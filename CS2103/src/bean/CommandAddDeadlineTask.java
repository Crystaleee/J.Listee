/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 9:27pm
 * CS2103
 */
package bean;
import java.util.ArrayList;
import java.util.Calendar;

import History.History;

public class CommandAddDeadlineTask extends CommandAddFloatTask{
    private Calendar endDate;
    
    public CommandAddDeadlineTask(){
        this.setDescription(null);
        this.setLocation(null);
        this.endDate = null;
        this.setTags(null);
    }
    
    public CommandAddDeadlineTask(String description, String location, Calendar endDate, ArrayList<String> tags){
        this.setDescription(description);
        this.setLocation(location);
        this.endDate = endDate;
        this.setTags(tags);
    }
    
    public Calendar getEndDate(){
        return endDate;
    }
    
    public void setEndDate(Calendar endDate){
        this.endDate = endDate;
    } 
    
    public Display execute(Display display){
        if(getDescription() == null){
            display.setMessage(getMessageNoDescription());
            return display;
        }
        ArrayList<TaskDeadline> deadlineTasks = addDeadlineTask(display.getDeadlineTasks());
        display.setDeadlineTasks(deadlineTasks);
        if(hasUpdateFile(display)){
            display.setMessage(getSuccessMessage());
            History.saveDisplay(display);
        }
        else{
            display = new Display(getMessageErrorUpdateFile());
        }
        return display;
    }
    
    public ArrayList<TaskDeadline> addDeadlineTask(ArrayList<TaskDeadline> taskList) {
        int index = getIndex(taskList);
        taskList.add(index, new TaskDeadline(getDescription(), getLocation(), endDate, getTags()));
        return taskList;
    }
    
    /*
     * This method searches for the index to slot the deadline task in since we
     * are sorting the list in order of earliest deadline first
     */
    public int getIndex(ArrayList<TaskDeadline> taskList) {
        int i = 0;
        for(i = 0; i < taskList.size(); i++){
            if(endDate.compareTo(taskList.get(i).getEndDate()) < 0){
                break;
            }
        }
        return i;
    }
}
