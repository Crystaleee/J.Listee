/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 9:46pm
 * CS2103
 */
 
import java.util.ArrayList;
import java.util.Calendar;

public class CommandAddEvent extends CommandAddDeadlineTask{
    private Calendar startDate;
    
    public CommandAddEvent(){
        this.setDescription(null);
        this.setLocation(null);
        this.startDate = null;
        this.setEndDate(null);
        this.setTags(null);
    }
    
    public CommandAddEvent(String description, String location, Calendar startDate, Calendar endDate, ArrayList<String> tags){
        this.setDescription(description);
        this.setLocation(location);
        this.startDate = startDate;
        this.setEndDate(endDate);
        this.setTags(tags);
    }
    
    public Display execute(Display display){
        if(getDescription() == null){
            display.setMessage(getMessageNoDescription());
            return display;
        }
        ArrayList<TaskEvent> events = addEvent(display.getEvents());
        display.setEvents(events);
        if(hasUpdateFile(display)){
            display.setMessage(getSuccessMessage());
            History.saveDisplay(display);
        }
        else{
            display = new Display(getMessageErrorUpdateFile());
        }
        return display;
    }
    
    
    public ArrayList<TaskEvent> addEvent(ArrayList<TaskEvent> taskList) {
        int index = getAddIndex(taskList);
        taskList.add(index, new TaskEvent(getDescription(), getLocation(), startDate, getEndDate(), getTags()));
        return taskList;
    }
    
    /*
     * This method searches for the index to slot the deadline task in since we
     * are sorting the list in order of earliest start time first
     */
    public int getAddIndex(ArrayList<TaskEvent> taskList) {
        int i = 0;
        for(i = 0; taskList.get(i).getStartDate()!= null; i++){
            if(startDate.compareTo(taskList.get(i).getStartDate()) < 0){
                break;
            }
        }
        return i;
    }
}