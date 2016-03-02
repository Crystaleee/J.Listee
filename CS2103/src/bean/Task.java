package bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author  Zhu Bingjing
 * @date 2016年3月2日 上午11:11:38 
 * @version 1.0 
 */
public class Task {
	private String taskDescription;
	private String taskLocation;
	private List<String> tags=new ArrayList<String>();
	private Date startTime;
	private Date endTime;
	
	public Task(String des, String loc, Date start, Date end, String[] tags){
		this.setTaskDescription(des);
		this.setTaskLocation(loc);
		this.setStartTime(start);
		this.setEndTime(end);
		for(String tag:tags){
			this.tags.add(tag);
		}		
	}
	
	public List<String> getTaskTags() {
		return tags;
	}
	
	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getTaskLocation() {
		return taskLocation;
	}

	public void setTaskLocation(String taskLocation) {
		this.taskLocation = taskLocation;
	}

	public String getStartTime() {
		return this.startTime.getYear()+"-"+this.startTime.getMonth()+"-"+this.startTime.getDay()+" "+this.startTime.getHours()+":"+this.startTime.getMinutes();
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.endTime.getYear()+"-"+this.endTime.getMonth()+"-"+this.endTime.getDay()+" "+this.endTime.getHours()+":"+this.endTime.getMinutes();
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
