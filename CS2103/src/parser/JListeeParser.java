package parser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.joestelmach.natty.*;

import bean.Command;
import bean.CommandAddDeadlineTask;
import bean.CommandAddEvent;
import bean.CommandAddFloatTask;
import bean.CommandAddReserved;
import bean.CommandDelete;
import bean.CommandInvalid;
import bean.CommandRedo;
import bean.CommandShow;
import bean.CommandUndo;

public class JListeeParser{
	private static final String COMMAND_SHOW = "show";
	private static final String COMMAND_REDO = "redo";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_UPDATE = "update";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_RESERVE = "reserve";

	private static final int DEFAULT_START_HOUR = 0;
	private static final int DEFAULT_START_MINUTE = 0;
	private static final int DEFAULT_START_SECOND = 0;
	private static final int DEFAULT_START_MILLISECOND = 0;
	private static final int DEFAULT_END_HOUR = 23;
	private static final int DEFAULT_END_MINUTE = 59;
	private static final int DEFAULT_END_SECOND = 0;
	private static final int DEFAULT_END_MILLISECOND = 0;
	private static final int DAYS_IN_WEEK = 7;
	private static final int DESC_NO_OR = 1;
	private static final int NO_DATE_PARSED = -1;


	private static Logger logger = Logger.getGlobal();
	private static FileHandler fh; 
	private com.joestelmach.natty.Parser dateParser;

	public JListeeParser() {
		dateParser = new com.joestelmach.natty.Parser();
	}



/* public static void main(String[] separateInputLine){ // for testing
		try {
			fh = new FileHandler("/Users/kailin/Desktop/IVLE/CS2103/for proj/cs2103 proj/CS2103/src/MyLogFile.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);  

		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		} 


		JListeeParser testEvent = new JListeeParser();
		testEvent.ParseCommand("add event  (03/17/2016 2pm to 03/27/2016 5pm) add event @LT27 #hihi #me");


		JListeeParser testFloat = new JListeeParser();
		testFloat.ParseCommand("add floatingtask @zzz #arghhhhh #hi");

		JListeeParser testDeadLine = new JListeeParser();
		testDeadLine.ParseCommand("add deadLine date only @whatthe 12/2/14 #hey");

		JListeeParser testDeadLine2 = new JListeeParser();
		testDeadLine2.ParseCommand("add deadLine date and time @location today 3:00 #hashtag");

		JListeeParser testDelete = new JListeeParser();
		testDelete.ParseCommand("delete 1,2,3,4");
		
		JListeeParser testShow = new JListeeParser();
		testShow.ParseCommand("show hiiiiii 23rd march 2016 to 12/4/16 #hihi @location ");
		
		JListeeParser testReserve = new JListeeParser();
		testReserve.ParseCommand("reserve meeting with boss 12/2/15 4pm to 5pm and 11/4/15 4pm to 5pm #hwork @icube");

		JListeeParser testUpdateTask = new JListeeParser();
		testUpdateTask.ParseCommand("update 2 hello hello hello"); 
	} 
*/


	public Command ParseCommand(String inputLine){

		String[] separateInputLine = inputLine.split(" ");	
		String commandType = determineCommandType(separateInputLine);	

		switch (commandType){
		case COMMAND_ADD:
			return parseAdd(inputLine);

		case COMMAND_DELETE:
			return parseDelete(inputLine);
		
		case COMMAND_UNDO:		
			return parseUndo();

		case COMMAND_REDO:
			return parseRedo();
			
		case COMMAND_SHOW:
			return parseShow(inputLine);
						
		case COMMAND_RESERVE:
			return parseReserve(inputLine);

		/*case COMMAND_UPDATE:
			return parseUpdate(separateInputLine);

			 */
		default : 
			return parseInvalid();
		}
	}

	private Command parseShow(String inputLine) {
		Calendar startDate = null;
		Calendar endDate = null;
		String location = null;
		ArrayList<String> tagLists = new ArrayList<String>();
		String taskDescription = null;

		inputLine = inputLine.replaceFirst("show", "").trim();
		//natty library to extract dates
		List<DateGroup> groups = dateParser.parse(inputLine);

		for(DateGroup group:groups) {
			List<Date> dates = group.getDates();

			/*has start date and end date, search inbetween 2 dates*/
			if (dates.size() == 2){
				startDate = dateToCalendar(dates.get(0));
				endDate = dateToCalendar(dates.get(1));

				/* Swap date if necessary */
				if (startDate.after(endDate)) {
					Calendar temp = endDate;
					endDate = startDate;
					startDate = temp;
				}

				if (group.isTimeInferred()) {
					setStartDateTimeDefault(startDate);
					setEndDateTimeDefault(endDate);
				}	
			}

			/*only 1 set of date, search for that specific dates*/
			else if(dates.size() == 1){
				endDate = dateToCalendar(dates.get(0));

				/*set default end date if no time specified*/
				if (group.isTimeInferred()) {
					setEndDateTimeDefault(endDate);
				}
			} 

			/*remove group of dates from inputLine*/
			if (inputLine.contains(group.getText())){
				inputLine = inputLine.replace(group.getText(), "");
			}
		}

		tagLists = findHashTags(inputLine);	

		location = findLocation(inputLine);

		if (!inputLine.contains("all")){
		 taskDescription = trimInputLineToDescriptionOnly(inputLine, location, tagLists);
		}
		return new CommandShow(taskDescription);

	}



	public Command parseInvalid() {
		return new CommandInvalid();
	}

	public Command parseRedo() {
		return new CommandRedo();
	}

	public Command parseUndo() {
		return new CommandUndo();
	}

	/*public Command parseUpdate(String[] separateInputLine) {
		boolean isTaskDescription = false;

		updateTaskNumber = (Integer.valueOf(separateInputLine[1]));

		if (separateInputLine[2].contains("-")) {
			if (separateInputLine[2].contains("@")){
				System.out.println("Comes in remove location");
				//remove location
			}

			else if (separateInputLine[2].contains("#")){
				System.out.println("Comes in remove hashtags");
				//remove hashtag
			}
		}

		else if (separateInputLine[2].contains("@")){
			System.out.println("comes in add new location");
		}

		else if (separateInputLine[2].contains("#")){
			System.out.println("comes in add new location");
		}


		else {
			for (int i = 2; i<separateInputLine.length; i++){
				taskDescription += separateInputLine[i] + " ";
			}

			taskDescription = taskDescription.substring(4,taskDescription.length());
			isTaskDescription = true;
		}

		Command update = new CommandUpdate(updateTaskNumber, taskDescription);
		return update;
	}
*/

	public Command parseReserve(String inputLine) {
		Calendar startDate = null;
		Calendar endDate = null;
		String location = null;
		ArrayList<String> tagLists = new ArrayList<String>();
		ArrayList<Calendar> startDates = new ArrayList<Calendar>();
		ArrayList<Calendar> endDates = new ArrayList<Calendar>();

		inputLine = inputLine.replaceFirst("reserve", "").trim();

		System.out.println(inputLine);

		//natty library to extract dates
		List<DateGroup> groups = dateParser.parse(inputLine);

		for(DateGroup group:groups) {
			List<Date> dates = group.getDates();
			System.out.println(dates.toString());
			/*has start date and end date, event task*/

			if (dates.size() >= 2){
				for (int i=0;i<dates.size()-1;i+=2){
					startDate = dateToCalendar(dates.get(i));
					endDate = dateToCalendar(dates.get(i+1));
					
					/* Swap date if necessary */
					if (startDate.after(endDate)) {
						Calendar temp = endDate;
						endDate = startDate;
						startDate = temp;
					}
					
					startDates.add(startDate);
					endDates.add(endDate);
				}
				
				if (group.isTimeInferred()) {
					setStartDateTimeDefault(startDate);
					setEndDateTimeDefault(endDate);
				}	
			}

			/*remove group of dates from inputLine*/
			if (inputLine.contains(group.getText())){
				inputLine = inputLine.replace(group.getText(), "");
			}
		}

		tagLists = findHashTags(inputLine);	

		location = findLocation(inputLine);

		String taskDescription = trimInputLineToDescriptionOnly(inputLine, location, tagLists);
		
		return new CommandAddReserved(taskDescription, location, startDates, endDates, tagLists);
	
	}


	public Command parseDelete(String inputLine) {
		ArrayList<Integer> taskNumbers = new ArrayList<Integer>();
		inputLine = inputLine.replace("delete","").trim();
		
		if (inputLine.contains("all")){
			taskNumbers = null;
		}
		
		else {
			String [] separateInputLine = inputLine.split(",");
			for (int i=0; i<separateInputLine.length; i++){
				taskNumbers.add(Integer.valueOf(separateInputLine[i]));
			}
		}
		
		return new CommandDelete(taskNumbers);
	}

	public Command parseAdd(String inputLine) {		
		boolean isEvent = false;
		boolean isDeadline = false;
		Calendar startDate = null;
		Calendar endDate = null;
		String location = null;
		ArrayList<String> tagLists = new ArrayList<String>();

		inputLine = inputLine.replaceFirst("add", "").trim();


		//natty library to extract dates
		List<DateGroup> groups = dateParser.parse(inputLine);

		for(DateGroup group:groups) {
			List<Date> dates = group.getDates();

			/*has start date and end date, event task*/

			if (dates.size() == 2){
				isEvent = true; 
				startDate = dateToCalendar(dates.get(0));
				endDate = dateToCalendar(dates.get(1));

				/* Swap date if necessary */
				if (startDate.after(endDate)) {
					Calendar temp = endDate;
					endDate = startDate;
					startDate = temp;
				}

				if (group.isTimeInferred()) {
					setStartDateTimeDefault(startDate);
					setEndDateTimeDefault(endDate);
				}	
			}

			/*only 1 set of date, deadlineTask*/
			else if(dates.size() == 1){
				isDeadline = true;
				endDate = dateToCalendar(dates.get(0));

				/*set default end date if no time specified*/
				if (group.isTimeInferred()) {
					setEndDateTimeDefault(endDate);
				}
			} 

			/*remove group of dates from inputLine*/
			if (inputLine.contains(group.getText())){
				inputLine = inputLine.replace(group.getText(), "");
			}
		}

		tagLists = findHashTags(inputLine);	

		location = findLocation(inputLine);

		String taskDescription = trimInputLineToDescriptionOnly(inputLine, location, tagLists);


		if (isEvent){
			return new CommandAddEvent(taskDescription, location, startDate, endDate, tagLists);
		}

		else if (isDeadline){
			return new CommandAddDeadlineTask(taskDescription, location, endDate, tagLists);
		}

		return new CommandAddFloatTask(taskDescription, location, tagLists);
	}

	public String trimInputLineToDescriptionOnly(String inputLine, String location, ArrayList<String> tagLists) {
		for (int i =0;i <tagLists.size(); i++){
			inputLine = inputLine.replace("#" + tagLists.get(i), "").trim();
		}

		inputLine = inputLine.replace("@" + location, "").trim();
		inputLine = inputLine.replace("(", "").replace(")", "").replace("@", "");

		return inputLine;
	}


	public String findLocation(String inputLine) {
		String location = null;
		Matcher retrieveLocation = Pattern.compile("@\\s*(\\w+)").matcher(inputLine);
		while (retrieveLocation.find()) {
			location = retrieveLocation.group(1);
		}
		return location;
	}

	public ArrayList<String> findHashTags(String inputLine) {
		ArrayList<String> tags = new ArrayList<String>();
		Matcher retrieveHashTags = Pattern.compile("#\\s*(\\w+)").matcher(inputLine);

		while (retrieveHashTags.find()) {
			tags.add(retrieveHashTags.group(1));
		}

		return tags;
	}



	public void setEndDateTimeDefault(Calendar endDate) {
		endDate.set(Calendar.HOUR_OF_DAY,
				DEFAULT_END_HOUR);
		endDate.set(Calendar.MINUTE,
				DEFAULT_END_MINUTE);
		endDate.set(Calendar.SECOND,
				DEFAULT_END_SECOND);
		endDate.set(Calendar.MILLISECOND,
				DEFAULT_END_MILLISECOND);
	}



	public void setStartDateTimeDefault(Calendar startDate) {
		startDate.set(Calendar.HOUR_OF_DAY,
				DEFAULT_START_HOUR);
		startDate.set(Calendar.MINUTE,
				DEFAULT_START_MINUTE);
		startDate.set(Calendar.SECOND,
				DEFAULT_START_SECOND);
		startDate.set(Calendar.MILLISECOND,
				DEFAULT_START_MILLISECOND);
	}

	public String determineCommandType(String[] separateInputLine) {
		return separateInputLine[0].trim();
	}

	private static Calendar dateToCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}


}
