/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/4/2016, 4:30AM
 * CS2103
 */
 package bean;

import java.util.ArrayList;

public abstract class Task{
    private String description;
    private String location;
    private ArrayList<String> tags;
    
    public Task(String description, String location, ArrayList<String> tags){
        this.description = description;
        this.location=location;
        this.tags = tags;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setTags(ArrayList<String> tags){
        this.tags= tags;
    }
    
    public ArrayList<String> getTags(){
        return tags;
    }

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
