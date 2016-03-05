/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 8:55pm
 * CS2103
 */


package bean;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskReserved extends Task{
    private ArrayList<Calendar> startDates;
    private ArrayList<Calendar> endDates;
    
    public TaskReserved() {
        this.setDescription(null);
        this.setLocation(null);
        this.setTags(null);
        startDates = null;
        endDates = null;
    }
    
    public TaskReserved(String description, String location, ArrayList<Calendar> startDates, 
            ArrayList<Calendar> endDates, ArrayList<String> tags) {
        this.setDescription(description);
        this.setLocation(location);
        this.setTags(tags);
        this.startDates = startDates;
        this.endDates = endDates;
    }
    
    public void setStartDates(ArrayList<Calendar> startDates){
        this.startDates = startDates;
    }
    
    public ArrayList<Calendar> getStartDates(){
        return startDates;
    }
    
    public void setEndDates(ArrayList<Calendar> endDates){
        this.endDates = endDates;
    }
    
    public ArrayList<Calendar> getEndDates(){
        return endDates;
    }
}
