package bean;

import java.util.ArrayList;
import java.util.Calendar;

class Task{

    private String description;
    private Calendar startDate;
    private Calendar endDate;
    private ArrayList<String> tags;

    public Task(){
        description = "Default";
        endDate = null;
        startDate = null;
        tags = null;
    }
    
    public Task(String description, ArrayList<String> tags){
        this.description = description;
        this.endDate = null;
        this.startDate = null;
        this.tags = tags;
    }
    
    public Task(String description, Calendar startDate, Calendar endDate){
        this.description = description;
        this.endDate = endDate;
        this.startDate = startDate;
        this.tags = null;
    }
    
    public Task(String description, Calendar startDate, Calendar endDate, ArrayList<String> tags){
        this.description = description;
        this.endDate = endDate;
        this.startDate = startDate;
        this.tags = tags;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setStartDate(Calendar startDate){
        this.startDate= startDate;
    }
    
    public Calendar getStartDate(){
        return startDate;
    }
    
    public void setEndDate(Calendar endDate){
        this.endDate= endDate;
    }
    
    public Calendar getEndDate(){
        return endDate;
    }
    
    public void setTags(ArrayList<String> tags){
        this.tags= tags;
    }
    
    public ArrayList<String> getTags(){
        return tags;
    }
}
