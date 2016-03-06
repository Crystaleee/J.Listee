/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 10:57pm
 * CS2103
 */
package bean;
import java.util.ArrayList;

public class Display{

    private String message;
    private ArrayList<TaskEvent> events;
    private ArrayList<TaskDeadline> deadlineTasks;
    private ArrayList<Task> floatTasks;
    private ArrayList<TaskReserved> reservedTasks;
    private ArrayList<Task> completedTasks;

    public Display(){
        message = "";
        events = new ArrayList<TaskEvent>();
        deadlineTasks = new ArrayList<TaskDeadline>();
        floatTasks = new ArrayList<Task>();
        reservedTasks = new ArrayList<TaskReserved>();
        completedTasks = new ArrayList<Task>();
    }
    
    public Display(String message){
        this.message = message;
        events = new ArrayList<TaskEvent>();
        deadlineTasks = new ArrayList<TaskDeadline>();
        floatTasks = new ArrayList<Task>();
        reservedTasks = new ArrayList<TaskReserved>();
        completedTasks = new ArrayList<Task>();
    }
    
    public Display(String message, ArrayList<TaskEvent> events, ArrayList<TaskDeadline> deadlineTasks, 
            ArrayList<Task> floatTasks, ArrayList<TaskReserved> reservedTasks, ArrayList<Task> completedTasks){
        this.message = "";
        this.events = events;
        this.deadlineTasks = deadlineTasks;
        this.floatTasks = floatTasks;
        this.reservedTasks = reservedTasks;
        this.completedTasks = completedTasks;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
    public int getNumberOfTasks(){
        return (events.size() + deadlineTasks.size() + floatTasks.size() + reservedTasks.size() + completedTasks.size());
    }
    
    public String getMessage(){
        return message;
    }
    
    public void setEvents(ArrayList<TaskEvent> events){
        this.events = events;
    }
    
    public ArrayList<TaskEvent> getEventTasks(){
        return events;
    }
    
    public void setDeadlineTasks(ArrayList<TaskDeadline> deadlineTasks){
        this.deadlineTasks = deadlineTasks;
    }
    
    public ArrayList<TaskDeadline> getDeadlineTasks(){
        return deadlineTasks;
    }
    
    public void setFloatTasks(ArrayList<Task> floatTasks){
        this.floatTasks = floatTasks;
    }
    
    public ArrayList<Task> getFloatTasks(){
        return floatTasks;
    }
    
    public void setReservedTasks(ArrayList<TaskReserved> reservedTasks){
        this.reservedTasks = reservedTasks;
    }
    
    public ArrayList<TaskReserved> getReservedTasks(){
        return reservedTasks;
    }
    
    public ArrayList<Task> getCompletedTasks(){
        return completedTasks;
    }
    
    public void setCompletedTasks(ArrayList<Task> completedTasks){
        this.completedTasks = completedTasks;
    }
}
