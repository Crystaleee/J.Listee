/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 8:57pm
 * CS2103
 */
package bean;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskDeadline extends Task{
    private Calendar endDate;
    
    public TaskDeadline(){
        this.setDescription(null);
        this.setLocation(null);
        this.setTags(null);
        this.endDate = null;
    }
    
    public TaskDeadline(String description, String location, Calendar endDate, ArrayList<String> tags){
        this.setDescription(description);
        this.setLocation(location);
        this.setTags(tags);
        this.endDate = endDate;
    }
    
    public void setEndDate(Calendar endDate){
        this.endDate = endDate;
    }
    
    public Calendar getEndDate(){
        return endDate;
    }
}