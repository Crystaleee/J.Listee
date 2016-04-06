// @@author Her Kai Lin (A0126070U)

// @@author Her Kai Lin (A0126070U)

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
import bean.CommandConfirm;
import bean.CommandDelete;
import bean.CommandDone;
import bean.CommandInvalid;
import bean.CommandRedo;
import bean.CommandShow;
import bean.CommandUndo;
import bean.CommandUndone;
import bean.CommandUpdate;

public class JListeeParser {
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_CFM = "cfm";
	private static final String COMMAND_RES = "res";
	private static final String COMMAND_DEL = "del";
	private static final String COMMAND_CONFIRM = "confirm";
	private static final String CONTAINING_DELETE = "del";
	private static final String CONTAINING_BOTH = "both";
	private static final String CONTAINING_ALLTIME = "alltime";
	private static final String COMMAND_UNDONE = "undone";
	private static final String COMMAND_DONE = "done";
	private static final String CONTAINING_END = "end";
	private static final String CONTAINING_ENDTIME = "etime";
	private static final String CONTAINING_STARTTIME = "stime";

	private static final String CONTAINING_START = "start";
	private static final int STARTING_INDEX = 0;
	private static final String CONTAINS_COMMA = ",";
	private static final String CONTAINS_ALL = "all";
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
	
    private static final String[] DATE_WORDS =
            new String[]{"by", "on", "at", "due", "during", "in", "for", "from", "at", "in", "this", "before", "after"};
	
    private static final String[] SEARCH_TASKS =
            new String[]{"today", "tomorrow", "overdue", "done", "reserved", "deadlines", "events", "untimed"};
    
    private static final String[] CONTAIN_TIME = new String[]{"end", "start", CONTAINING_ALLTIME, "etime", "stime", "both", CONTAINING_DELETE};
    
	private static Logger logger = Logger.getGlobal();
	private static FileHandler fh;
	private com.joestelmach.natty.Parser dateParser;

	private Calendar startDate = null;
	private Calendar endDate = null; 
	
	public JListeeParser() {
		dateParser = new com.joestelmach.natty.Parser();
	}

 public static void main(String[] separateInputLine){ // for testing
		try {
			fh = new FileHandler("logs\\log.txt");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);  

		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		} 


		JListeeParser testEvent = new JListeeParser();
		testEvent.ParseCommand("add today to hi whhat friday from 5th april to 9th april #hi @location  # ");

		JListeeParser testFloat = new JListeeParser();
		testFloat.ParseCommand("add cs2105 @zzz #arghhhhh #hi");

		JListeeParser testDeadLine = new JListeeParser();
		testDeadLine.ParseCommand("add good friday this friday");

		JListeeParser testDeadLine2 = new JListeeParser();
		testDeadLine2.ParseCommand("add deadLine date and time on saturday 2359 #hashtag");

		JListeeParser testDelete = new JListeeParser();
		testDelete.ParseCommand("delete 1-2 ,  4,3"); 
		
		JListeeParser testDone = new JListeeParser();
		testDone.ParseCommand("done 1, 2-4 ,3,4"); 
		
		JListeeParser testUndone = new JListeeParser();
		testUndone.ParseCommand("undone 1-2,5,  2-4"); 
		
		JListeeParser testShow = new JListeeParser();
		testShow.ParseCommand("show assignment due friday ");
			
		JListeeParser testReserve = new JListeeParser();
		testReserve.ParseCommand("reserve from huh @hi #asda #ahskdjashd r1 from 12/4/16 3pm to 5pm and today 7pm to 8pm and sunday 2 to 3pm"); 

		JListeeParser testUpdateTask = new JListeeParser();
		testUpdateTask.ParseCommand("update 3 /del 1,2,3"); 	

		JListeeParser testConfirm = new JListeeParser();
		testConfirm.ParseCommand("confirm 3 1"); 

	} 

	
		/*
	 * public static void main(String[] separateInputLine){ // for testing try {
	 * fh = new FileHandler(
	 * "/Users/kailin/Desktop/IVLE/CS2103/for proj/cs2103 proj/CS2103/src/MyLogFile.log"
	 * ); logger.addHandler(fh); SimpleFormatter formatter = new
	 * SimpleFormatter(); fh.setFormatter(formatter);
	 * 
	 * } catch (SecurityException e) { e.printStackTrace(); } catch (IOException
	 * e) { e.printStackTrace(); }
	 * 
	 * 
	 * JListeeParser testEvent = new JListeeParser(); testEvent.ParseCommand(
	 * "add event  (03/17/2016 2pm to 03/27/2016 5pm) add event @LT27 #hihi #me"
	 * );
	 * 
	 * 
	 * JListeeParser testFloat = new JListeeParser(); testFloat.ParseCommand(
	 * "add floatingtask @zzz #arghhhhh #hi");
	 * 
	 * JListeeParser testDeadLine = new JListeeParser();
	 * testDeadLine.ParseCommand("add deadLine date only @whatthe 12/2/14 #hey"
	 * );
	 * 
	 * JListeeParser testDeadLine2 = new JListeeParser();
	 * testDeadLine2.ParseCommand(
	 * "add deadLine date and time @location today 3:00 #hashtag");
	 * 
	 * JListeeParser testDelete = new JListeeParser(); testDelete.ParseCommand(
	 * "delete 1,2,3,4");
	 * 
	 * JListeeParser testShow = new JListeeParser(); testShow.ParseCommand(
	 * "show hiiiiii 23rd march 2016 to 12/4/16 #hihi @location ");
	 * 
	 * JListeeParser testReserve = new JListeeParser();
	 * testReserve.ParseCommand("reserve r1 tomorrow 3pm - 5pm and 7pm - 8pm");
	 * 
	 * JListeeParser testUpdateTask = new JListeeParser();
	 * testUpdateTask.ParseCommand(
	 * "update 2 what -tomorrow  @location #hashtag -#deleteHashtag -#sigh #hi "
	 * ); }
	 */
	

	public Command ParseCommand(String inputLine) {

		String[] separateInputLine = inputLine.split(" ");
		String commandType = determineCommandType(separateInputLine);
		inputLine = deleteKeyword(COMMAND_REDO, inputLine);
		inputLine = inputLine.trim().replaceAll(" +", "");


		switch (commandType) {

		case COMMAND_ADD:
			return parseAdd(inputLine);

		case COMMAND_DEL:
		case COMMAND_DELETE:
			return parseDelete(inputLine);

		case COMMAND_UNDO:
			
			return parseUndo();

		case COMMAND_REDO:
			return parseRedo();

		case COMMAND_SHOW:
			return parseShow(inputLine);
		
		case COMMAND_RES:
		case COMMAND_RESERVE:
			return parseReserve(inputLine);
		
		case COMMAND_CFM:
		case COMMAND_CONFIRM:
			return parseConfirm(inputLine);
		
		case COMMAND_EDIT:
		case COMMAND_UPDATE:
			return parseUpdate(inputLine);

		case COMMAND_DONE:
			return parseDone(inputLine);
			
		case COMMAND_UNDONE:
			return parseUndone(inputLine);
			
		default:
			return parseInvalid();
		}
	}

	public Command parseConfirm(String inputLine) {
		Integer taskNumber;
		Integer timeSlotIndex;
		

		taskNumber = extractTaskNumber(inputLine);
		
		if (inputLine.contains(String.valueOf(taskNumber))) {
			inputLine = deleteKeyword(String.valueOf(taskNumber), inputLine);
		}
		
		timeSlotIndex = extractTaskNumber(inputLine);		
		
		return new CommandConfirm(taskNumber, timeSlotIndex) ;
	}

	public Command parseAdd(String inputLine) {
		boolean isEvent = false;
		boolean isDeadline = false;
		String location = null;
		ArrayList<String> tagLists = new ArrayList<String>();
		int firstDateIndex = -1;
		int prepositionIndex = -1;

		
		//find the index where the preposition starts
		prepositionIndex = getPrepositionIndex(inputLine, prepositionIndex);
		

		//if contain the list of words means there is date to extract
		if (prepositionIndex != -1){
			List<DateGroup> groups = dateParser.parse(inputLine.substring(prepositionIndex));

			for (DateGroup group : groups) {
				List<Date> dates = group.getDates();

				/* has start date and end date, event task */
				if (dates.size() == 2) {
					isEvent = true;
					setAllDates(inputLine, groups);
				}

				/* only 1 set of date, deadlineTask */
				else if (dates.size() == 1) {
					isDeadline = true;
					setAllDates(inputLine, groups);

				}
				
				firstDateIndex = getFirstDateIndex(prepositionIndex, group);
				inputLine = removeWordBeforeDateAndDate(inputLine, firstDateIndex, group).trim();
			}
		}
			

		tagLists = findHashTags(inputLine);

		location = findLocation(inputLine);

		String taskDescription = trimInputLineToDescriptionOnly(inputLine, location, tagLists);


		if (isEvent) {
			return new CommandAddEvent(taskDescription, location, startDate, endDate, tagLists);
		}
	
		else if (isDeadline) {
			return new CommandAddDeadlineTask(taskDescription, location, endDate, tagLists);
		}
	

		return new CommandAddFloatTask(taskDescription, location, tagLists);
	}



	public Command parseDelete(String inputLine) {
		ArrayList<Integer> taskNumbers = new ArrayList<Integer>();

		if (inputLine.contains(CONTAINS_ALL)) {
			taskNumbers = null;
		}
	
		else {
			String[] separateInputLine = inputLine.split(CONTAINS_COMMA);
			for (int startIndex = STARTING_INDEX; startIndex < separateInputLine.length; startIndex++) {
				if (separateInputLine[startIndex].contains("-")){
					String[] splitRange = separateInputLine[startIndex].split("-");
					int startDeleteIndex = Integer.valueOf(splitRange[0]);
					int endDeleteIndex = Integer.valueOf(splitRange[1]);

					while (startDeleteIndex <= endDeleteIndex){
						taskNumbers.add(startDeleteIndex);
						startDeleteIndex++;
					}
				}

				else{
					taskNumbers.add(Integer.valueOf(separateInputLine[startIndex]));
				}
			}
		}
		
		detectDuplicates(taskNumbers);
		
		return new CommandDelete(taskNumbers);
	}

	public String deleteKeyword(String keyword, String inputLine) {
		return inputLine;
	}



	public Command parseUndo() {
		return new CommandUndo();
	}

	public Command parseRedo() {
		return new CommandRedo();
	}

	public Command parseShow(String inputLine) {
		String location = null;
		ArrayList<String> tagLists = new ArrayList<String>();
		String taskDescription = null;
		ArrayList<String> task = new ArrayList<String>();
		int prepositionIndex = -1;
		int firstDateIndex = -1;
		
		for (int i = 0; i< SEARCH_TASKS.length; i++){
			if (inputLine.toLowerCase().contains("/" + SEARCH_TASKS[i])){

				inputLine = deleteKeyword("/" + SEARCH_TASKS[i], inputLine);

				switch (SEARCH_TASKS[i]){
					case ("today"):
						startDate = Calendar.getInstance();
						setStartDateTimeDefault();
						endDate = Calendar.getInstance();
						setEndDateTimeDefault();
						break;

					case ("tomorrow"):
						startDate = Calendar.getInstance();
						startDate.add(startDate.DATE,1);
						setStartDateTimeDefault();
						endDate = Calendar.getInstance();
						endDate.add(endDate.DATE,1);
						setEndDateTimeDefault();
						break;
						
					case ("overdue"):
						setStartTimeInMilliZero();
						endDate = Calendar.getInstance();
						break;
					
					case ("done"):
						task.add("done");
						break;
						
					case ("deadlines"):
						task.add("deadline");
						break;
						
					case ("reserved"):
						task.add("reserved");
						break;
						
					case ("events"):
						task.add("event");
						break;
						
					case ("untimed"):
						task.add("untimed");
						break;
				}
			}
		}	

		//find the index where the preposition starts
		prepositionIndex = getPrepositionIndex(inputLine, prepositionIndex);
		//if contain the list of words means there is date to extract
		if (prepositionIndex != -1){
			List<DateGroup> groups = dateParser.parse(inputLine.substring(prepositionIndex));
			for (DateGroup group : groups) {
				setAllDates(inputLine, groups);

				if (startDate == null){
					startDateToCalendar(endDate.getTime());
					setStartDateTimeDefault();
				}
				
				firstDateIndex = getFirstDateIndex(prepositionIndex, group);
				inputLine = removeWordBeforeDateAndDate(inputLine, firstDateIndex, group).trim();
			}
		}
		
		tagLists = findHashTags(inputLine);		
		location = findLocation(inputLine);
		taskDescription = trimInputLineToDescriptionOnly(inputLine, location , tagLists);

		
		return new CommandShow(taskDescription, location, startDate, endDate, tagLists, task);
	}

	public Command parseReserve(String inputLine) {
		
		String location = null;
		ArrayList<String> tagLists = new ArrayList<String>();
		ArrayList<Calendar> startDates = new ArrayList<Calendar>();
		ArrayList<Calendar> endDates = new ArrayList<Calendar>();
		int prepositionIndex = -1;
		int firstDateIndex = -1;
		
		prepositionIndex = getPrepositionIndex(inputLine, prepositionIndex);
		
		//if contain the list of words means there is date to extract
		if (prepositionIndex != -1){
			List<DateGroup> groups = dateParser.parse(inputLine.substring(prepositionIndex));
			for (DateGroup group : groups) {
				List<Date> dates = group.getDates();

				extractMultiplePairDates(startDates, endDates, group, dates);
				
				firstDateIndex = getFirstDateIndex(prepositionIndex, group);
				inputLine = removeWordBeforeDateAndDate(inputLine, firstDateIndex, group).trim();			}
		}
	
		tagLists = findHashTags(inputLine);
	
		location = findLocation(inputLine);
	
		String taskDescription = trimInputLineToDescriptionOnly(inputLine, location, tagLists);
	
		
		return new CommandAddReserved(taskDescription, location, startDates, endDates, tagLists);
	
	}

	public Command parseUpdate(String inputLine) {

		String location = null;
		String taskDescription = null;
		ArrayList<String> removeTagLists = new ArrayList<String>();
		ArrayList<String> tagLists = new ArrayList<String>();
		ArrayList<Integer> taskNumbers = new ArrayList<Integer>();
		Integer taskNumber;
		Integer reservedTaskIndex = null; 
	
		taskNumber = extractTaskNumber(inputLine);
	
		if (inputLine.contains(String.valueOf(taskNumber))) {
			inputLine = inputLine.replaceFirst(String.valueOf(taskNumber), "").trim();
		}
		

		Matcher retrieveLocation = Pattern.compile("-@").matcher(inputLine);
		if (retrieveLocation.find()){
			inputLine = inputLine.replace("-@", "");
			location  = "";
		}

		else {
			location = findLocation(inputLine);
		}
		
		
		for (String preposition : CONTAIN_TIME){
			if (inputLine.contains("/"+ preposition)){
				if (!preposition.equals("both")){
				reservedTaskIndex = extractTaskNumber(inputLine);
				inputLine = inputLine.replaceFirst(String.valueOf(reservedTaskIndex), "").trim();
				}
			}
		}

		List<DateGroup> groups = dateParser.parse(inputLine);

			for (int i=0; i<CONTAIN_TIME.length; i++){

				if (inputLine.toLowerCase().contains("-" + CONTAIN_TIME[i])){
					inputLine = inputLine.replace("-" + CONTAIN_TIME[i], "").trim();

					switch (CONTAIN_TIME[i]){
					case CONTAINING_END:
						setEndTimeInMilliZero();
						break;
					case CONTAINING_START:
						setStartTimeInMilliZero();
						break;
					case CONTAINING_ALLTIME:
						setStartTimeInMilliZero();
						setEndTimeInMilliZero();
						break;	
					}	

				}

				else if (inputLine.toLowerCase().contains("/" + CONTAIN_TIME[i])){ //adding date, time
					inputLine = inputLine.replace("/" + CONTAIN_TIME[i], "").trim();
					switch (CONTAIN_TIME[i]){
					case CONTAINING_END:
					case CONTAINING_BOTH:
						setAllDates(inputLine, groups);
						break;

					case CONTAINING_START:
						inputLine = setStartDateTimeAndTrimInputLine(inputLine, groups);

						break;
					case CONTAINING_ENDTIME:
						setAllDates(inputLine, groups);
						endDate.set(Calendar.YEAR, 1);

						break;	

					case CONTAINING_STARTTIME:
						inputLine = setStartDateTimeAndTrimInputLine(inputLine, groups);
						startDate.set(Calendar.YEAR, 1);
						break;
						
					case CONTAINING_DELETE:
						if (inputLine.contains(CONTAINS_ALL)) {
							taskNumbers = null;
						}
					
						else {
							String[] separateInputLine = inputLine.split(CONTAINS_COMMA);
							for (int startIndex = STARTING_INDEX; startIndex < separateInputLine.length; startIndex++) {
								if (separateInputLine[startIndex].contains("-")){
									String[] splitRange = separateInputLine[startIndex].split("-");
									Integer startDeleteIndex = Integer.valueOf(splitRange[0]);
									Integer endDeleteIndex = Integer.valueOf(splitRange[1]);

									while (startDeleteIndex <= endDeleteIndex){
										taskNumbers.add(startDeleteIndex);
										inputLine = inputLine.replaceFirst(startDeleteIndex.toString(), "");

										startDeleteIndex++;
									}
								}

								else{
									taskNumbers.add(Integer.valueOf(separateInputLine[startIndex]));
									inputLine = inputLine.replaceFirst(separateInputLine[startIndex], "");
									inputLine = inputLine.replace(",", "");


								}
							}
						}
						
						detectDuplicates(taskNumbers);
						
					
					}

					for (DateGroup group : groups) {
						inputLine = removeDateFromInputLine(inputLine, group);		
					}
				}

		}

			if (inputLine.contains("-")){
				removeTagLists = findHashTags(inputLine);
				inputLine = trimInputLineWithoutRemoveHashTags(inputLine, removeTagLists);
			}
		
			tagLists = findHashTags(inputLine);

			taskDescription = trimInputLineToDescriptionOnly(inputLine, location, tagLists);

			if (taskDescription.equals("")){
				taskDescription = null;
			}
			
			return new CommandUpdate(taskNumber, reservedTaskIndex, taskDescription, location, startDate, endDate, tagLists, removeTagLists, taskNumbers);
	}

	public Command parseDone(String inputLine){
		ArrayList<Integer> taskNumbers = new ArrayList<Integer>();

		if (inputLine.contains(CONTAINS_ALL)) {
			taskNumbers = null;
		}
	
		else {
			String[] separateInputLine = inputLine.split(CONTAINS_COMMA);
			for (int startIndex = STARTING_INDEX; startIndex < separateInputLine.length; startIndex++) {
				if (separateInputLine[startIndex].contains("-")){
					String[] splitRange = separateInputLine[startIndex].split("-");
					int startDeleteIndex = Integer.valueOf(splitRange[0]);
					int endDeleteIndex = Integer.valueOf(splitRange[1]);

					while (startDeleteIndex <= endDeleteIndex){
						taskNumbers.add(startDeleteIndex);
						startDeleteIndex++;
					}
				}

				else{
					taskNumbers.add(Integer.valueOf(separateInputLine[startIndex]));
				}
			}
		}
		
		detectDuplicates(taskNumbers);
		
		
		return new CommandDone(taskNumbers);
	}

	public Command parseUndone(String inputLine) {
		ArrayList<Integer> taskNumbers = new ArrayList<Integer>();
	

		if (inputLine.contains(CONTAINS_ALL)) {
			taskNumbers = null;
		}
	
		else {
			String[] separateInputLine = inputLine.split(CONTAINS_COMMA);
			for (int startIndex = STARTING_INDEX; startIndex < separateInputLine.length; startIndex++) {
				if (separateInputLine[startIndex].contains("-")){
					String[] splitRange = separateInputLine[startIndex].split("-");
					int startDeleteIndex = Integer.valueOf(splitRange[0]);
					int endDeleteIndex = Integer.valueOf(splitRange[1]);

					while (startDeleteIndex <= endDeleteIndex){
						taskNumbers.add(startDeleteIndex);
						startDeleteIndex++;
					}
				}

				else{
					taskNumbers.add(Integer.valueOf(separateInputLine[startIndex]));
				}
			}
		}
		
		detectDuplicates(taskNumbers);
				
		return new CommandUndone(taskNumbers);		
	}

	public Command parseInvalid() {
		return new CommandInvalid();
	}

	private int getPrepositionIndex(String inputLine, int prepositionIndex) {
		for (int i = 0; i< DATE_WORDS.length; i++){
			int temp = -1;
	
			Matcher matcher = Pattern.compile("\\b"+ DATE_WORDS[i]+"\\b").matcher(inputLine);
	
			while(matcher.find()){
				temp = matcher.start();
			}		
			
			if (prepositionIndex < temp){
				prepositionIndex = temp;
			}
		}
		return prepositionIndex;
	}

	public void detectDuplicates(ArrayList<Integer> taskNumbers) {
		if (taskNumbers!= null){
			for (int i=0; i<taskNumbers.size();i++){
				for (int j = i+1; j<taskNumbers.size(); j++){
					if (taskNumbers.get(i) == taskNumbers.get(j)){
						taskNumbers.remove(j);
					}
				}
			}
		}
	}

	private String removeWordBeforeDateAndDate(String input, int dateIndex, DateGroup group) {
		String textBeforeDate = input.substring(0, dateIndex).trim();

		String textAfter = input.substring(dateIndex, input.length());	
		textAfter =  removeDateFromInputLine(textAfter,group);

		String lastWord = textBeforeDate. substring(textBeforeDate.lastIndexOf(" ") + 1);

		for (String preposition : DATE_WORDS) {
			if (lastWord.equalsIgnoreCase(preposition)) {
				textBeforeDate = textBeforeDate.
						substring(0, textBeforeDate.length() - lastWord.length());
				break;
			}
		}

		return textBeforeDate + " " + textAfter;
	}

	private int getFirstDateIndex(int prepositionIndex, DateGroup group) {
		int firstDateIndex;
		firstDateIndex = group.getPosition()-1 + prepositionIndex;
		return firstDateIndex;
	}


	private String setEndDateTimeAndTrimInputLine(String inputLine, List<DateGroup> groups) {
		for (DateGroup group : groups) {
			List<Date> dates = group.getDates();
			endDateToCalendar(dates.get(0));	
	
			if (group.isTimeInferred()) {
				setEndDateTimeDefault();
			} 
			inputLine = removeDateFromInputLine(inputLine, group);
	
		}
		return inputLine;
	}

	
	private void extractMultiplePairDates(ArrayList<Calendar> startDates, ArrayList<Calendar> endDates, DateGroup group,
			List<Date> dates) {
		
		for (int i = STARTING_INDEX; i < dates.size() - 1; i += 2) {
			startDateToCalendar(dates.get(i));
			endDateToCalendar(dates.get(i + 1));
			
			swapDates();
			
			startDates.add(startDate);
			endDates.add(endDate);
		}
	
		if (group.isTimeInferred()) {
			setStartDateTimeDefault();
			setEndDateTimeDefault();
		}
	}



	private String removeDateFromInputLine(String inputLine, DateGroup group) {
		if (inputLine.contains(group.getText())) {
			inputLine = inputLine.replace(group.getText(), "");
		}
		return inputLine;
	}



	private Integer extractTaskNumber(String inputLine) {
		
		String[] splitInputLine = inputLine.split(" ");

		for (int i = 0; i < splitInputLine[0].length(); i++) {
		      if (!Character.isDigit(splitInputLine[0].charAt(i))){
		    	  return null;
		      }
		}
		      	
		return Integer.valueOf(splitInputLine[0]);
	}

	

	
	public void setEndTimeInMilliZero() {
		endDate = Calendar.getInstance();
		endDate.setTimeInMillis(0);
	}



	public void setStartTimeInMilliZero() {
		startDate = Calendar.getInstance();
		startDate.setTimeInMillis(0);
	}



	private void setAllDates(String inputLine, List<DateGroup> groups) {
		
		for (DateGroup group : groups) {
			List<Date> dates = group.getDates();
			/* has start date and end date, search inbetween 2 dates */
			if (dates.size() == 2) {
				setStartDateTimeandEndDateTime(group, dates);
			}
	
			/* only 1 set of date, search for that specific dates */
			else if (dates.size() == 1) {
				setEndDateAndTime(group, dates);
			}
		}
	}



	private String setStartDateTimeAndTrimInputLine(String inputLine, List<DateGroup> groups) {
		for (DateGroup group : groups) {
			List<Date> dates = group.getDates();
			startDateToCalendar(dates.get(0));
	
			if (group.isTimeInferred()) {
				setStartDateTimeDefault();
			}
			
			inputLine = removeDateFromInputLine(inputLine, group);
				
		}
		return inputLine;
	}



	private void setEndDateAndTime(DateGroup group, List<Date> dates) {
		endDateToCalendar(dates.get(0));
		/* set default end time if no time specified */
		if (group.isTimeInferred()) {
			setEndDateTimeDefault();
		}

	}


	private void setStartDateTimeandEndDateTime(DateGroup group, List<Date> dates) {
		startDateToCalendar(dates.get(0));
		endDateToCalendar(dates.get(1));

		swapDates();
		
		/* set default start end time if no time specified */
		if (group.isTimeInferred()) {
			setStartDateTimeDefault();
			setEndDateTimeDefault();
		}
	}

	private void setEndDateTimeDefault() {
		endDate.set(Calendar.HOUR_OF_DAY, DEFAULT_END_HOUR);
		endDate.set(Calendar.MINUTE, DEFAULT_END_MINUTE);
		endDate.set(Calendar.SECOND, DEFAULT_END_SECOND);
		endDate.set(Calendar.MILLISECOND, DEFAULT_END_MILLISECOND);
	}



	private void setStartDateTimeDefault() {
		startDate.set(Calendar.HOUR_OF_DAY, DEFAULT_START_HOUR);
		startDate.set(Calendar.MINUTE, DEFAULT_START_MINUTE);
		startDate.set(Calendar.SECOND, DEFAULT_START_SECOND);
		startDate.set(Calendar.MILLISECOND, DEFAULT_START_MILLISECOND);
	}



	private String trimInputLineWithoutRemoveHashTags(String inputLine, ArrayList<String> removeTagLists) {

		for (int i = 0; i < removeTagLists.size(); i++) {
			inputLine = inputLine.replace("-#" + removeTagLists.get(i), "").trim();
		}

		return inputLine;
	}

	private String trimInputLineToDescriptionOnly(String inputLine, String location, ArrayList<String> tagLists) {
		if (tagLists != null) {
			for (int i = 0; i < tagLists.size(); i++) {
				inputLine = inputLine.replace("-#" + tagLists.get(i), "").trim();
				inputLine = inputLine.replace("#" + tagLists.get(i), "").trim();

			}
		}
		
		inputLine = inputLine.replace("@" + location, "").trim();
		inputLine = inputLine.replace("-@" + location, "").trim();
		inputLine = inputLine.trim().replaceAll(" +", " ");

		return inputLine;
	}



	private String findLocation(String inputLine) {
		String location = null;
		

		Matcher retrieveLocation = Pattern.compile("@\\w*\\w+").matcher(inputLine);
		
		while (retrieveLocation.find()) {
				location = retrieveLocation.group(0);
				location = location.substring(1,location.length());
		}
		
		return location;
	}

	private ArrayList<String> findHashTags(String inputLine) {
		ArrayList<String> tags = new ArrayList<String>();
		Matcher retrieveHashTags;
		String hashtag = null;
	
		if (inputLine.contains("-")) {
			retrieveHashTags = Pattern.compile("-#\\w*\\w+").matcher(inputLine);
			while (retrieveHashTags.find()) {
				hashtag = retrieveHashTags.group(0);
				hashtag = hashtag.substring(2, hashtag.length());
				tags.add(hashtag);
			}

		}

		else{
			retrieveHashTags = Pattern.compile("#\\w*\\w+").matcher(inputLine);

			while (retrieveHashTags.find()) {
				hashtag = retrieveHashTags.group(0);
				hashtag = hashtag.substring(1, hashtag.length());
				tags.add(hashtag);
			}
		}

		return tags;
	}

	private String determineCommandType(String[] separateInputLine) {
		return separateInputLine[0].toLowerCase().trim();
	}

	private void startDateToCalendar(Date date) {
		startDate = Calendar.getInstance();
		startDate.setTime(date);
	}
	
	private void endDateToCalendar(Date date) {
		endDate = Calendar.getInstance();
		endDate.setTime(date);
	}

	private void swapDates(){
		if (startDate.after(endDate)) {
			Calendar temp = endDate;
			endDate = startDate;
			startDate = temp;
		}
	}
	
}

