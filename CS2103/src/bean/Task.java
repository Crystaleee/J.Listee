package bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Task{

    private String description;
    private Calendar startDate;
    private Calendar endDate;
    private String location;
    private List<String> tags=new ArrayList<String>();

    public Task(){
        description = "Default";
        endDate = null;
        startDate = null;
    }
    
    public Task(String description){
        this.description = description;
        this.endDate = null;
        this.startDate = null;
    }
    
    public Task(String description, Calendar startDate, Calendar endDate){
        this.description = description;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}