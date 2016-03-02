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
	
	public String getCommandType(){
		return commandType;
	}
	
	public void setTaskNumber(int[] taskNum) {
		for(int num:taskNum){
			taskNumber.add(num);		
		}	
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
	
//	public void setEndDateTime(int endDateYear, int endDateMonth, int endDateDay, int endHour, int endMin) {
//		endDateTime.set(endDateYear,endDateMonth,endDateDay,endHour,endMin);
//		
//	}
//
//	public void setStartDateTime(int startDateYear, int startDateMonth, int startDateDay, int startHour, int startMin) {
//		startDateTime.set(startDateYear,startDateMonth,startDateDay,startHour,startMin);
//	}
	
	
	public void setCommandType(String commandType) {
		this.commandType = commandType;
		
	}
	
	public void setTaskDescription(String input){
		this.taskDescription=input;
	}
	public String getTaskDescription(){
		return taskDescription;
	}
	
//	public Calendar getStartDateTime(){
//		return startDateTime;
//	}
//
//	public Calendar getEndDateTime(){
//		return endDateTime;
//	}
//	
//	public String getLocation(){
//		return location;
//	}
	
	public ArrayList<String> getTags(){
		return tagLists;
	}
	
//	public void setLocation(String location){
//		this.location = location;	
//	}

	public  ArrayList<Integer> getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(Integer valueOf) {
		this.taskNumber.set(0, valueOf);
	}

	public Calendar getAddStartDate() {
		return addStartDate;
	}

	public Calendar getAddEndDate() {
		return addEndDate;
	}

	public Calendar getDeleteStartDate() {
		return deleteStartDate;
	}

	public void setDeleteStartDate(Calendar deleteStartDate) {
		this.deleteStartDate = deleteStartDate;
	}

	public Calendar getDeleteEndDate() {
		return deleteEndDate;
	}

	public void setDeleteEndDate(Calendar deleteEndDate) {
		this.deleteEndDate = deleteEndDate;
	}

	public String getAddLocation() {
		return addLocation;
	}

	public void setAddLocation(String addLocation) {
		this.addLocation = addLocation;
	}

	public String getDeleteLocation() {
		return deleteLocation;
	}

	public void setDeleteLocation(String deleteLocation) {
		this.deleteLocation = deleteLocation;
	}

	public void setAddStartDateTime(int startDateYear, int startDateMonth,
			int startDateDay, int startHour, int startMin) {
		this.addStartDate.set(startDateYear, startDateMonth, startDateDay, startHour, startMin);
	}
	
	public void setAddEndDateTime(int endDateYear, int endDateMonth,
			int endDateDay, int endHour, int endMin) {
		this.addEndDate.set(endDateYear, endDateMonth, endDateDay, endHour, endMin);
	}
}
