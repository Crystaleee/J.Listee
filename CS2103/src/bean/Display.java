/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 8:00pm
 * CS2103
 */
package bean;
import java.util.ArrayList;

class Display{

    private String message;
    private ArrayList<TaskEvent> events;
    private ArrayList<TaskDeadline> deadlineTasks;
    private ArrayList<TaskFloat> floatTasks;
    //private ArrayList<TaskFloat> reservedTasks;
    //private ArrayList<TaskFloat> completedTasks;

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
    
    public Display(String message, ArrayList<TaskEvent> events, ArrayList<TaskDeadline> deadlineTasks, 
            ArrayList<TaskFloat> floatTasks, ArrayList<TaskFloat> reservedTasks, ArrayList<TaskFloat> completedTasks){
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
    
    public void setEvents(ArrayList<TaskEvent> events){
        this.events = events;
    }
    
    public ArrayList<TaskEvent> getEvents(){
        return events;
    }
    
    public void setDeadlineTasks(ArrayList<TaskDeadline> deadlineTasks){
        this.deadlineTasks = deadlineTasks;
    }
    
    public ArrayList<TaskDeadline> getDeadlineTasks(){
        return deadlineTasks;
    }
    
    public void setFloatTasks(ArrayList<TaskFloat> floatTasks){
        this.floatTasks = floatTasks;
    }
    
    public ArrayList<TaskFloat> getFloatTasks(){
        return floatTasks;
    }
    
    /*public void setReservedTasks(ArrayList<TaskFloat> reservedTasks){
        this.reservedTasks = reservedTasks;
    }
    
    public ArrayList<TaskFloat> getReservedTasks(){
        return reservedTasks;
    }
    
    public ArrayList<TaskFloat> getCompletedTasks(){
        return events;
    }
    
    public void setCompletedTasks(ArrayList<TaskFloat> completedTasks){
        this.completedTasks = completedTasks;
    }*/
}
