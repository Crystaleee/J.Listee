/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 9:46pm
 * CS2103
 */
 package bean;
import java.util.ArrayList;
import java.util.Calendar;

import History.History;

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
        System.out.println("Entering execute 0");
        ArrayList<TaskEvent> events = addEvent(display.getEventTasks());
        System.out.println("Entering execute 1");
        display.setEvents(events);
        System.out.println("Entering execute 2");
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
        System.out.println("Entering execute 0.1");
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
        System.out.println("Entering execute 0.2");
        for(i = 0; i < taskList.size(); i++){
            System.out.println("Loop" + i);
            if(startDate.compareTo(taskList.get(i).getStartDate()) < 0){
                break;
            }
        }
        System.out.println("Entering execute 0.5");
        return i;
    }
}
