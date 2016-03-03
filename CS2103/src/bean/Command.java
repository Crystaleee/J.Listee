package bean;

import java.util.ArrayList;
import java.util.Calendar;

public class Command {
	private  String commandType;
	private  String taskDescription;
	private  String  recurringDescription = null;
	private  ArrayList<String> calendarDescription = new ArrayList<String>();
	private Calendar addStartDate;
	private Calendar addEndDate;
	private Calendar deleteStartDate;
	private Calendar deleteEndDate;
	//private  Calendar startDateTime = Calendar.getInstance();
	//private  Calendar endDateTime = Calendar.getInstance();
	private  Calendar date = Calendar.getInstance();
	private  Calendar time = Calendar.getInstance();
	private  String addLocation;
	private  String deleteLocation;
	private  ArrayList<String> tagLists = new ArrayList<String>();
	private ArrayList<Integer> taskNumber;
	private ArrayList<Integer> deleteTaskNumber = new ArrayList<Integer>();
	private Integer numberOfDaysToPostpone;
	private ArrayList<Integer> doneTaskNumber = new ArrayList<Integer>();
	
	public String getCommandType(){
		return commandType;
	}
	
	public void setDoneTaskNumber(Integer taskNum) {
		doneTaskNumber.add(taskNum);
	}
	
	public ArrayList<Integer> getDoneTaskNumber(){
		return doneTaskNumber;
	}

	public void setNumberOfDaysToPostpone(Integer days) {
		numberOfDaysToPostpone = days;
	}
	
	public int getNumberOfDaysToPostpone() {
		return numberOfDaysToPostpone;
	}

	public void setTaskNumber(Integer taskNum) {
		taskNumber = taskNum;		
	}

	public void setTags(String tag) {
		tagLists.add(tag);
	}

	public void setTime(int startHour, int startMin) {
		time.set(startHour,startMin); //double check later
	}
	
	public void setDate(int startDateYear, int startDateMonth, int startDateDay) {
		date.set(startDateYear, startDateMonth, startDateDay);
	}
	
	public Calendar getDate(){
		return date;
	}
	
	public void setEndDateTime(int endDateYear, int endDateMonth, int endDateDay, int endHour, int endMin) {
		endDateTime.set(endDateYear,endDateMonth,endDateDay,endHour,endMin);
		
	}

	public void setStartDateTime(int startDateYear, int startDateMonth, int startDateDay, int startHour, int startMin) {
		startDateTime.set(startDateYear,startDateMonth,startDateDay,startHour,startMin);
	}
	
	
	public void setCommandType(String commandType) {
		this.commandType = commandType;
		
	}
	
	public void setTaskDescription(String input){
		taskDescription.add(input);
	}
	public ArrayList<String> getTaskDescription(){
		return taskDescription;
	}
	
	public Calendar getStartDateTime(){
		return startDateTime;
	}

	public Calendar getEndDateTime(){
		return endDateTime;
	}
	
	public String getLocation(){
		return location;
	}
	
	public ArrayList<String> getTags(){
		return tagLists;
	}
	
	public void setLocation(String location){
		this.location = location;
		
	}

	public  int getTaskNumber() {
		return taskNumber;
	}

	public ArrayList<Integer> getDeleteTaskNumber(){
		return deleteTaskNumber;
	}
	
	public void setDeleteTaskNumber(Integer taskNum){
		deleteTaskNumber.add(taskNum);
	}
}
