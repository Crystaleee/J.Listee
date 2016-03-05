/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 8:00pm
 * CS2103
 */
package bean;
import java.util.ArrayList;

public class Display{

    private String message;
    private ArrayList<EventTask> events;
    private ArrayList<DeadlineTask> deadlineTasks;
    private ArrayList<FloatingTask> floatTasks;
    //private ArrayList<FloatingTask> reservedTasks;
    //private ArrayList<FloatingTask> completedTasks;

    public Display(){
        message = "";
        events = null;
        deadlineTasks = null;
        floatTasks = null;
        //reservedTasks = null;
        //completedTasks = null;
    }
    
    public Display(String message){
        this.message = message;
        events = null;
        deadlineTasks = null;
        floatTasks = null;
        //reservedTasks = null;
        //completedTasks = null;
    }
    
    public Display(String message, ArrayList<EventTask> events, ArrayList<DeadlineTask> deadlineTasks, 
            ArrayList<FloatingTask> floatTasks, ArrayList<FloatingTask> reservedTasks, ArrayList<FloatingTask> completedTasks){
        this.message = "";
        this.events = events;
        this.deadlineTasks = deadlineTasks;
        this.floatTasks = floatTasks;
        //this.reservedTasks = reservedTasks;
        //this.completedTasks = completedTasks;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
    public String getMessage(){
        return message;
    }
    
    public void setEvents(ArrayList<EventTask> events){
        this.events = events;
    }
    
    public ArrayList<EventTask> getEvents(){
        return events;
    }
    
    public void setDeadlineTasks(ArrayList<DeadlineTask> deadlineTasks){
        this.deadlineTasks = deadlineTasks;
    }
    
    public ArrayList<DeadlineTask> getDeadlineTasks(){
        return deadlineTasks;
    }
    
    public void setFloatTasks(ArrayList<FloatingTask> floatTasks){
        this.floatTasks = floatTasks;
    }
    
    public ArrayList<FloatingTask> getFloatTasks(){
        return floatTasks;
    }
    
    /*public void setReservedTasks(ArrayList<FloatingTask> reservedTasks){
        this.reservedTasks = reservedTasks;
    }
    
    public ArrayList<FloatingTask> getReservedTasks(){
        return reservedTasks;
    }
    
    public ArrayList<FloatingTask> getCompletedTasks(){
        return events;
    }
    
    public void setCompletedTasks(ArrayList<FloatingTask> completedTasks){
        this.completedTasks = completedTasks;
    }*/
}
