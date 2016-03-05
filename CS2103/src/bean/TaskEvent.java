/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 9:12pm
 * CS2103
 */
package bean;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskEvent extends TaskDeadline{
    private Calendar startDate;
    
    public TaskEvent(){
        this.setDescription(null);
        this.setLocation(null);
        this.setTags(null);
        this.setEndDate(null);
        this.startDate = null;
    }
    
    public TaskEvent(String description, String location, Calendar startDate, Calendar endDate, ArrayList<String> tags){
        this.setDescription(description);
        this.setLocation(location);
        this.setTags(tags);
        this.setEndDate(endDate);
        this.startDate = startDate;
    }
    
    public void setStartDate(Calendar startDate){
        this.startDate = startDate;
    }
    
    public Calendar getStartDate(){
        return startDate;
    }
}