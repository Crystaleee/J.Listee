// @@author A0126070U

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
import bean.GlobalLogger;
import bean.CommandPostpone;


public class JListeeParser {
	private static final String MATCH_LOCATION = "@\\w+\\S+";
	private static final String MATCH_DELETE_HASHTAGS = "-#\\w+\\S+";
	private static final String MATCH_HASHTAGS = "#\\w+\\S+";
	private static final String SYMBOL_LOCATION = "@";
	private static final String SYMBOL_HASHTAG = "#";
	private static final String SYMBOL_REMOVE_HASHTAG = "-#";
	private static final String CONTAINING_TO = "to";
	private static final String CONTAINING_FROM = "from";
	private static final String MINS = "mins";
	private static final String MIN = "min";
	private static final String MINUTES = "minutes";
	private static final String MINUTE = "minute";
	private static final String HOUR = "hour";
	private static final String HOURS = "hours";
	private static final String HRS = "hrs";
	private static final String HR = "hr";
	private static final String DAY = "day";
	private static final String DAYS = "days";
	private static final String MTHS = "mths";
	private static final String MTH = "mth";
	private static final String MONTHS = "months";
	private static final String MONTH = "month";
	private static final String YEARS = "years";
	private static final String YR = "yr";
	private static final String YRS = "yrs";
	private static final String YEAR = "year";
	private static final String WHITE_SPACE = "\\b";
	private static final String DIGIT = "\\d";
	private static final String SYMBOL_DELETE_LOCATION = "-@";
	private static final String CONTAINING_UNTIMED = "untimed";
	private static final String CONTAINING_EVENTS = "events";
	private static final String CONTAINING_RESERVED = "reserved";
	private static final String CONTAINING_DEADLINES = "deadlines";
	private static final String CONTAINING_DONE = "done";
	private static final String CONTAINING_OVERDUE = "overdue";
	private static final String CONTAINING_TOMORROW = "tomorrow";
	private static final String CONTAINING_TODAY = "today";
	private static final String SLASH = "/";
	private static final String EMPTY_SPACE = "";
	private static final String INVALID_TASK_NUMBERS = "Invalid TaskNumbers";
	private static final String CONTAINS_DASH = "-";
	private static final String MORE_THAN_ONE_SPACE = " +";
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int DONT_EXIST = -1;
	private static final String SPLIT_BY_SPACE = " ";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_PP = "pp";
	private static final String COMMAND_POSTPONE = "postpone";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_CFM = "cfm";
	private static final String COMMAND_RES = "res";
	private static final String COMMAND_DEL = "del";
	private static final String COMMAND_CONFIRM = "confirm";
	private static final String CONTAINING_DEL = "del";
	private static final String CONTAINING_BOTH = "both";
	private static final String CONTAINING_ALLTIME = "alltime";
	private static final String COMMAND_UNDONE = "undone";
	private static final String COMMAND_DONE = CONTAINING_DONE;
	private static final String CONTAINING_END = "end";
	private static final String CONTAINING_ENDTIME = "etime";
	private static final String CONTAINING_STARTTIME = "stime";

	private static final int STARTING_INDEX = 0;

	private static final String CONTAINING_START = "start";
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
			new String[]{"by", "on", "at", "due", "during", "in", "for", CONTAINING_FROM, "at", "in", "this", "before", "after", "next"};

	private static final String[] SEARCH_TASKS =
			new String[]{CONTAINING_TODAY, CONTAINING_TOMORROW, CONTAINING_OVERDUE, CONTAINING_DONE, CONTAINING_RESERVED, CONTAINING_DEADLINES, CONTAINING_EVENTS, "untimed"};

	private static final String[] CONTAIN_TIME = new String[]{"end", "start", "alltime" , "etime", "stime", "both", "del"};

	private static final String[] CONTAIN_YEAR_MONTH_DAY_HOUR_MIN =
			new String[]{YEARS, YEAR, YR, YRS, MONTHS, MONTH, MTH, MTHS, DAY, DAYS, HOURS, HOUR, HR, HRS, MINUTE, MINUTES, MIN, MINS};

	private static Logger logger = GlobalLogger.getLogger();
	private com.joestelmach.natty.Parser dateParser;

	private Calendar startDate = null;
	private Calendar endDate = null; 

	public JListeeParser() {
		dateParser = new com.joestelmach.natty.Parser();
	}

	public static void main(String[] separateInputLine){ // for testing
		try {
			//	fh = new FileHandler("logs\\log.txt");
			//logger.addHandler(fh);
			//	SimpleFormatter formatter = new SimpleFormatter();  
			//	fh.setFormatter(formatter);  

		} catch (SecurityException e) {  
			e.printStackTrace();  
		}

		JListeeParser testEvent = new JListeeParser();
		testEvent.ParseCommand("add today to hi whhat friday from 5th april to 9th april #hi @location  # ");

		JListeeParser testFloat = new JListeeParser();
		testFloat.ParseCommand("add  from april @zzz #arghhhhh #hi");

		JListeeParser testDeadLine = new JListeeParser();
		testDeadLine.ParseCommand("add lunch at tomorrow @biz");

		JListeeParser testDeadLine2 = new JListeeParser();
		testDeadLine2.ParseCommand("add deadLine date and time on saturday 2359 #hashtag");

		JListeeParser testDelete = new JListeeParser();
		testDelete.ParseCommand("delete 1-2 ,  4,3"); 

		JListeeParser testDone = new JListeeParser();
		testDone.ParseCommand("done 1, 2-4 ,3,4"); 

		JListeeParser testUndone = new JListeeParser();
		testUndone.ParseCommand("undone 1-2,5,  2-4"); 

		JListeeParser testShow = new JListeeParser();
		testShow.ParseCommand("show /EVENTS due friday ");

		JListeeParser testReserve = new JListeeParser();
		testReserve.ParseCommand("reserve from huh @hi #asda #ahskdjashd r1 from 12/4/16 3pm to 5pm and today 7pm to 8pm and sunday 2 to 3pm"); 

		JListeeParser testUpdateTask = new JListeeParser();
		testUpdateTask.ParseCommand("update 3 /del 1,2,3 "); 	

		JListeeParser testConfirm = new JListeeParser();
		testConfirm.ParseCommand("confirm 3 1"); 

		JListeeParser testPostpone = new JListeeParser();
		testPostpone.ParseCommand("postpone 1 4days 3hours 6minutes"); 

	} 
	
	public Command ParseCommand(String inputLine) {

		String[] separateInputLine = inputLine.split(SPLIT_BY_SPACE);
		String commandType = determineCommandType(separateInputLine);
		inputLine = deleteKeyword(commandType, inputLine);

		switch (commandType) {

		case COMMAND_ADD :
			return parseAdd(inputLine);

		case COMMAND_DEL :
		case COMMAND_DELETE :
			return parseDelete(inputLine);

		case COMMAND_UNDO :	
			return parseUndo();

		case COMMAND_REDO :
			return parseRedo();

		case COMMAND_SEARCH :
		case COMMAND_SHOW :
			return parseShow(inputLine);

		case COMMAND_RES :
		case COMMAND_RESERVE : 
			return parseReserve(inputLine);

		case COMMAND_CFM :
		case COMMAND_CONFIRM :
			return parseConfirm(inputLine);

		case COMMAND_EDIT :
		case COMMAND_UPDATE :
			return parseUpdate(inputLine);

		case COMMAND_DONE :
			return parseDone(inputLine);

		case COMMAND_UNDONE :
			return parseUndone(inputLine);

		case COMMAND_PP :
		case COMMAND_POSTPONE :
			return parsePostpone(inputLine);

		default :
			return parseInvalid();
		}
	}

	public Command parseConfirm(String inputLine) {
		Integer taskNumber = null;
		Integer timeSlotIndex;

		taskNumber = extractTaskNumber(inputLine);

		if (taskNumber != null) {
			inputLine = deleteKeyword(String.valueOf(taskNumber), inputLine);
		}

		timeSlotIndex = extractTaskNumber(inputLine);	

		return new CommandConfirm(taskNumber, timeSlotIndex);
	}

	public Command parseAdd(String inputLine) {
		boolean isEvent = false;
		boolean isDeadline = false;
		String location = null;
		ArrayList<String> tagLists = new ArrayList<String>();
		int firstDateIndex = DONT_EXIST;
		int prepositionIndex = DONT_EXIST;

		prepositionIndex = getPrepositionIndex(inputLine, prepositionIndex);

		if (prepositionIndex != DONT_EXIST){
			List<DateGroup> groups = dateParser.parse(inputLine.substring(prepositionIndex));

			for (DateGroup group : groups) {
				List<Date> dates = group.getDates();

				if (dates.size() == TWO) {
					isEvent = true;
				}

				else if (dates.size() == ONE) {
					isDeadline = true;
				}

				setAllDates(inputLine, groups);
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

		taskNumbers = extractTaskNumbers(inputLine, taskNumbers);

		detectDuplicates(taskNumbers);

		return new CommandDelete(taskNumbers);
	}

	public ArrayList<Integer> extractTaskNumbers(String inputLine, ArrayList<Integer> taskNumbers) {
		inputLine = inputLine.trim().replaceAll(MORE_THAN_ONE_SPACE, EMPTY_SPACE);

		if (inputLine.contains(CONTAINS_ALL)) {
			taskNumbers = null;
		}

		else {
			String[] taskNumberLine = inputLine.split(CONTAINS_COMMA);

			for (int startIndex = STARTING_INDEX; startIndex < taskNumberLine.length; startIndex++) {
				checkUsingDashOrCommaAndExtract(taskNumbers, taskNumberLine, startIndex);
			}
		}

		return taskNumbers;
	}

	public void checkUsingDashOrCommaAndExtract(ArrayList<Integer> taskNumbers, String[] taskNumberLine,
			int startIndex) {

		/* Split the code with "-" , index zero represents the first task index to delete
		 * index 1 refers to last task index to delete
		 */

		if (taskNumberLine[startIndex].contains(CONTAINS_DASH)) {
			String[] splitRange = taskNumberLine[startIndex].split(CONTAINS_DASH);

			try{
				int startDeleteIndex = Integer.valueOf(splitRange[STARTING_INDEX]);
				int endDeleteIndex = Integer.valueOf(splitRange[ONE]);

				extractTaskNumbersContainingDash(taskNumbers, startDeleteIndex, endDeleteIndex);

			} catch (NumberFormatException e) {
				System.out.println(INVALID_TASK_NUMBERS);
			}

		}

		else {
			taskNumbers.add(Integer.valueOf(taskNumberLine[startIndex]));
		}
	}

	public void extractTaskNumbersContainingDash(ArrayList<Integer> taskNumbers, int startDeleteIndex, int endDeleteIndex) {
		while (startDeleteIndex <= endDeleteIndex){
			taskNumbers.add(startDeleteIndex);
			startDeleteIndex++;
		}
	}

	public String deleteKeyword(String keyword, String inputLine) {
		inputLine = inputLine.replaceFirst(keyword, EMPTY_SPACE).trim();
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
		int prepositionIndex = DONT_EXIST;
		int firstDateIndex = DONT_EXIST;

		for (int keyword = STARTING_INDEX; keyword < SEARCH_TASKS.length; keyword++) { 
			inputLine = checkTaskToDisplay(inputLine, task, keyword);
		}	

		prepositionIndex = getPrepositionIndex(inputLine, prepositionIndex);

		if (prepositionIndex != DONT_EXIST) {
			List<DateGroup> groups = dateParser.parse(inputLine.substring(prepositionIndex));

			for (DateGroup group : groups) {	
				setAllDates(inputLine, groups);
				checkStartDateDontExistAndInitialise();

				firstDateIndex = getFirstDateIndex(prepositionIndex, group);
				inputLine = removeWordBeforeDateAndDate(inputLine, firstDateIndex, group).trim();
			}
		}

		tagLists = findHashTags(inputLine);		
		location = findLocation(inputLine);
		taskDescription = trimInputLineToDescriptionOnly(inputLine, location , tagLists);

		return new CommandShow(taskDescription, location, startDate, endDate, tagLists, task);
	}

	public void checkStartDateDontExistAndInitialise() {
		if (startDate == null) {
			startDateToCalendar(endDate.getTime());
			setStartDateTimeDefault();
		}
	}

	public String checkTaskToDisplay(String inputLine, ArrayList<String> task, int keyword) {
		if (inputLine.toLowerCase().contains(SLASH + SEARCH_TASKS[keyword])){
			inputLine = deleteKeyword(SLASH + SEARCH_TASKS[keyword], inputLine);

			switch (SEARCH_TASKS[keyword]){
			case CONTAINING_TODAY :
				startDate = Calendar.getInstance();
				setStartDateTimeDefault();

				endDate = Calendar.getInstance();
				setEndDateTimeDefault();		
				break;

			case CONTAINING_TOMORROW :
				startDate = Calendar.getInstance();
				startDate.add(startDate.DATE, ONE);
				setStartDateTimeDefault();

				endDate = Calendar.getInstance();
				endDate.add(endDate.DATE,ONE);
				setEndDateTimeDefault();			
				break;

			case CONTAINING_OVERDUE :
				setStartTimeInMilliZero();
				endDate = Calendar.getInstance();
				break;

			case CONTAINING_DONE :
				task.add(CONTAINING_DONE);
				break;

			case CONTAINING_DEADLINES :
				task.add(CONTAINING_DEADLINES);
				break;

			case CONTAINING_RESERVED :
				task.add(CONTAINING_RESERVED);
				break;

			case CONTAINING_EVENTS :
				task.add("event");
				break;

			case CONTAINING_UNTIMED : 
				task.add("untimed");
				break;
			}
		}

		return inputLine;
	}

	public Command parseReserve(String inputLine) {
		String location = null;
		ArrayList<String> tagLists = new ArrayList<String>();
		ArrayList<Calendar> startDates = new ArrayList<Calendar>();
		ArrayList<Calendar> endDates = new ArrayList<Calendar>();
		int prepositionIndex = DONT_EXIST;
		int firstDateIndex = DONT_EXIST;

		prepositionIndex = getPrepositionIndex(inputLine, prepositionIndex);

		if (prepositionIndex != DONT_EXIST) {
			List<DateGroup> groups = dateParser.parse(inputLine.substring(prepositionIndex));

			for (DateGroup group : groups) {
				List<Date> dates = group.getDates();

				extractMultiplePairDates(startDates, endDates, group, dates);

				firstDateIndex = getFirstDateIndex(prepositionIndex, group);
				inputLine = removeWordBeforeDateAndDate(inputLine, firstDateIndex, group).trim();	
			}
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

		if (taskNumber != null) {
			inputLine = deleteKeyword(String.valueOf(taskNumber), inputLine);
		}


		Matcher retrieveLocation = Pattern.compile(SYMBOL_DELETE_LOCATION).matcher(inputLine);

		if (retrieveLocation.find()) {
			inputLine = deleteKeyword(SYMBOL_DELETE_LOCATION, inputLine);
			location = EMPTY_SPACE;
		}

		else {
			location = findLocation(inputLine);
		}

		for (String preposition : CONTAIN_TIME) {
			if (inputLine.contains(SLASH + preposition) && (!preposition.equals(CONTAINING_BOTH))) {
				reservedTaskIndex = extractTaskNumber(inputLine);
				inputLine = deleteKeyword(String.valueOf(reservedTaskIndex),inputLine);
			}
		}

		List<DateGroup> groups = dateParser.parse(inputLine);

		for (int keyword = STARTING_INDEX; keyword < CONTAIN_TIME.length; keyword++) {

			if (inputLine.toLowerCase().contains(CONTAINS_DASH + CONTAIN_TIME[keyword])){
				inputLine = inputLine.replace(CONTAINS_DASH + CONTAIN_TIME[keyword], EMPTY_SPACE).trim();
				deleteTime(keyword);	
			}


			else if (inputLine.toLowerCase().contains(SLASH + CONTAIN_TIME[keyword])) { 
				inputLine = inputLine.replace(SLASH + CONTAIN_TIME[keyword], EMPTY_SPACE).trim();

				switch (CONTAIN_TIME[keyword]) {

				case CONTAINING_END :
				case CONTAINING_BOTH :
					setAllDates(inputLine, groups);
					break;

				case CONTAINING_START :
					inputLine = setStartDateTimeAndTrimInputLine(inputLine, groups);
					break;

				case CONTAINING_ENDTIME :
					setAllDates(inputLine, groups);
					endDate.set(Calendar.YEAR, ONE);
					break;	

				case CONTAINING_STARTTIME :
					inputLine = setStartDateTimeAndTrimInputLine(inputLine, groups);
					startDate.set(Calendar.YEAR, ONE);
					break;

				case CONTAINING_DEL :
					taskNumbers = extractTaskNumbers(inputLine, taskNumbers);
					inputLine = removeAllDeletedNumberAndSymbols(inputLine, taskNumbers);
					detectDuplicates(taskNumbers);
					break;
				}

				for (DateGroup group : groups) {
					inputLine = removeDateFromInputLine(inputLine, group);		
				}

			}

		}

		if (inputLine.contains(CONTAINS_DASH)) {
			removeTagLists = findHashTags(inputLine);
			inputLine = trimInputLineWithoutRemoveHashTags(inputLine, removeTagLists);
		}

		tagLists = findHashTags(inputLine);

		taskDescription = trimInputLineToDescriptionOnly(inputLine, location, tagLists);

		if (taskDescription.equals(EMPTY_SPACE)){
			taskDescription = null;
		}

		return new CommandUpdate(taskNumber, reservedTaskIndex, taskDescription, location, startDate, endDate, tagLists, removeTagLists, taskNumbers);
	}

	public void deleteTime(int keyword) {
		switch (CONTAIN_TIME[keyword]) {
		case CONTAINING_END :
			setEndTimeInMilliZero();
			break;

		case CONTAINING_START :
			setStartTimeInMilliZero();
			break;

		case CONTAINING_ALLTIME :
			setStartTimeInMilliZero();
			setEndTimeInMilliZero();
			break;	
		}
	}

	public String removeAllDeletedNumberAndSymbols(String inputLine, ArrayList<Integer> taskNumbers) {
		for (int i = STARTING_INDEX; i<taskNumbers.size(); i++){
			if (inputLine.contains(String.valueOf(taskNumbers.get(i)))){
				inputLine = deleteKeyword(CONTAINS_COMMA, inputLine);
				inputLine = deleteKeyword(CONTAINS_DASH, inputLine);
				inputLine = deleteKeyword(String.valueOf(taskNumbers.get(i)), inputLine);
			}
		}
		return inputLine;
	}

	public Command parseDone(String inputLine){
		ArrayList<Integer> taskNumbers = new ArrayList<Integer>();
		taskNumbers = extractTaskNumbers(inputLine, taskNumbers);
		detectDuplicates(taskNumbers);

		return new CommandDone(taskNumbers);
	}

	public Command parseUndone(String inputLine) {
		ArrayList<Integer> taskNumbers = new ArrayList<Integer>();
		taskNumbers = extractTaskNumbers(inputLine, taskNumbers);
		detectDuplicates(taskNumbers);

		return new CommandUndone(taskNumbers);		
	}

	public Command parsePostpone(String inputLine) {
		Integer taskNumber;
		ArrayList<String> parameters = new ArrayList<String>();
		Calendar time = Calendar.getInstance();

		taskNumber = extractTaskNumber(inputLine);

		if (inputLine.contains(String.valueOf(taskNumber))) {
			inputLine = deleteKeyword(String.valueOf(taskNumber), inputLine);
		}

		for (int keyword = STARTING_INDEX; keyword< CONTAIN_YEAR_MONTH_DAY_HOUR_MIN.length; keyword++){

			Matcher matcher = Pattern.compile(DIGIT+ CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword]+WHITE_SPACE).matcher(inputLine);

			while(matcher.find()){
				switch(CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword]){
				case YEAR:
				case YRS:
				case YR:
				case YEARS:
					inputLine = setTimeYears(inputLine, parameters, time, keyword);
					break;

				case MONTH:
				case MONTHS:
				case MTH:
				case MTHS:
					inputLine = setTimeMonth(inputLine, parameters, time, keyword);
					break;

				case DAYS:
				case DAY:
					inputLine = setTimeDay(inputLine, parameters, time, keyword);
					break;

				case HR:
				case HRS:
				case HOURS:
				case HOUR:
					inputLine = setTimeHour(inputLine, parameters, time, keyword);
					break;


				case MINUTE:
				case MINUTES:
				case MIN:
				case MINS:
					inputLine = setTimeMinute(inputLine, parameters, time, keyword);
					break;
				}
			}	
		}

		return new CommandPostpone(taskNumber, time,  parameters);
	}

	public String setTimeMinute(String inputLine, ArrayList<String> parameters, Calendar time, int keyword) {
		Integer timeToPostpone;
		parameters.add(MINUTE);
		inputLine = deleteKeyword(CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword], inputLine);
		timeToPostpone = extractTaskNumber(inputLine);
		inputLine = deleteKeyword(String.valueOf(timeToPostpone), inputLine);
		time.set(Calendar.MINUTE, timeToPostpone);
		return inputLine;
	}

	public String setTimeHour(String inputLine, ArrayList<String> parameters, Calendar time, int keyword) {
		Integer timeToPostpone;
		parameters.add(HOUR);
		inputLine = deleteKeyword(CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword], inputLine);
		timeToPostpone = extractTaskNumber(inputLine);
		inputLine = deleteKeyword(String.valueOf(timeToPostpone), inputLine);					
		time.set(Calendar.HOUR_OF_DAY, timeToPostpone);
		return inputLine;
	}

	public String setTimeDay(String inputLine, ArrayList<String> parameters, Calendar time, int keyword) {
		Integer timeToPostpone;
		parameters.add(DAY);
		inputLine = deleteKeyword(CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword], inputLine);
		timeToPostpone = extractTaskNumber(inputLine);
		inputLine = deleteKeyword(String.valueOf(timeToPostpone), inputLine);
		time.set(Calendar.DATE, timeToPostpone);
		return inputLine;
	}

	public String setTimeMonth(String inputLine, ArrayList<String> parameters, Calendar time, int keyword) {
		Integer timeToPostpone;
		parameters.add(MONTH);
		inputLine = deleteKeyword(CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword], inputLine);
		timeToPostpone = extractTaskNumber(inputLine);
		inputLine = deleteKeyword(String.valueOf(timeToPostpone), inputLine);
		time.set(Calendar.MONTH, timeToPostpone);
		return inputLine;
	}

	public String setTimeYears(String inputLine, ArrayList<String> parameters, Calendar time, int keyword) {
		Integer timeToPostpone;
		parameters.add(YEAR);
		inputLine = deleteKeyword(CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword], inputLine);
		timeToPostpone = extractTaskNumber(inputLine);
		inputLine = deleteKeyword(String.valueOf(timeToPostpone), inputLine);
		time.set(Calendar.YEAR, timeToPostpone);
		return inputLine;
	}

	public Command parseInvalid() {
		return new CommandInvalid();
	}

	private int getPrepositionIndex(String inputLine, int prepositionIndex) {

		prepositionIndex = checkPrepositionIndexAndInitialise(inputLine, prepositionIndex);
		prepositionIndex = checkFromAndTo(inputLine, prepositionIndex);

		return prepositionIndex;
	}

	public int checkPrepositionIndexAndInitialise(String inputLine, int prepositionIndex) {
		for (int keyword = STARTING_INDEX; keyword< DATE_WORDS.length; keyword++){
			int temp = DONT_EXIST;

			Matcher matcher = Pattern.compile(WHITE_SPACE+ DATE_WORDS[keyword]+WHITE_SPACE).matcher(inputLine);

			while(matcher.find()){
				temp = matcher.start();
			}		

			if (prepositionIndex < temp){
				prepositionIndex = temp;
			}
		}
		return prepositionIndex;
	}

	public int checkFromAndTo(String inputLine, int prepositionIndex) {
		if (prepositionIndex != DONT_EXIST){
			if (inputLine.substring(prepositionIndex).contains(CONTAINING_FROM)){
				if (!inputLine.substring(prepositionIndex).contains(CONTAINING_TO)){
					prepositionIndex = DONT_EXIST;
				}

			}
		}
		return prepositionIndex;
	}

	public void detectDuplicates(ArrayList<Integer> taskNumbers) {
		if (taskNumbers!= null){
			iterateAndDetectDuplicates(taskNumbers);
		}
	}

	public void iterateAndDetectDuplicates(ArrayList<Integer> taskNumbers) {
		for (int keyword=STARTING_INDEX; keyword<taskNumbers.size();keyword++){
			checkDuplicatesForAllElements(taskNumbers, keyword);
		}
	}

	public void checkDuplicatesForAllElements(ArrayList<Integer> taskNumbers, int keyword) {
		for (int j = keyword + ONE; j<taskNumbers.size(); j++){
			if (taskNumbers.get(keyword) == taskNumbers.get(j)){
				taskNumbers.remove(j);
			}
		}
	}

	private String removeWordBeforeDateAndDate(String input, int dateIndex, DateGroup group) {
		String textBeforeDate = input.substring(STARTING_INDEX, dateIndex).trim();
		String textAfter = input.substring(dateIndex, input.length());	
		textAfter =  removeDateFromInputLine(textAfter,group);

		String lastWord = textBeforeDate. substring(textBeforeDate.lastIndexOf(SPLIT_BY_SPACE) + ONE);

		for (String preposition : DATE_WORDS) {
			if (lastWord.equalsIgnoreCase(preposition)) {
				textBeforeDate = textBeforeDate.
						substring(STARTING_INDEX, textBeforeDate.length() - lastWord.length());
				break;
			}
		}

		return textBeforeDate + SPLIT_BY_SPACE + textAfter;
	}

	private int getFirstDateIndex(int prepositionIndex, DateGroup group) {
		int firstDateIndex;
		firstDateIndex = group.getPosition() - ONE + prepositionIndex;
		return firstDateIndex;
	}

	private void extractMultiplePairDates(ArrayList<Calendar> startDates, ArrayList<Calendar> endDates, DateGroup group,
			List<Date> dates) {

		extractPairDates(startDates, endDates, dates);

		if (group.isTimeInferred()) {
			setStartDateTimeDefault();
			setEndDateTimeDefault();
		}
	}

	public void extractPairDates(ArrayList<Calendar> startDates, ArrayList<Calendar> endDates, List<Date> dates) {
		for (int keyword = STARTING_INDEX; keyword < dates.size() - ONE; keyword += TWO) {
			startDateToCalendar(dates.get(keyword));
			endDateToCalendar(dates.get(keyword + ONE));

			swapDates();

			startDates.add(startDate);
			endDates.add(endDate);
		}
	}

	private String removeDateFromInputLine(String inputLine, DateGroup group) {
		if (inputLine.contains(group.getText())) {
			inputLine = inputLine.replace(group.getText(), EMPTY_SPACE);
		}
		return inputLine;
	}

	private Integer extractTaskNumber(String inputLine) {	
		String[] splitInputLine = inputLine.split(SPLIT_BY_SPACE);

		for (int keyword = STARTING_INDEX; keyword < splitInputLine[STARTING_INDEX].length(); keyword++) {
			if (!Character.isDigit(splitInputLine[STARTING_INDEX].charAt(keyword))){
				return null;
			}
		}

		return Integer.valueOf(splitInputLine[STARTING_INDEX]);
	}


	public void setEndTimeInMilliZero() {
		endDate = Calendar.getInstance();
		endDate.setTimeInMillis(STARTING_INDEX);
	}


	public void setStartTimeInMilliZero() {
		startDate = Calendar.getInstance();
		startDate.setTimeInMillis(STARTING_INDEX);
	}

	private void setAllDates(String inputLine, List<DateGroup> groups) {
		for (DateGroup group : groups) {
			List<Date> dates = group.getDates();
			/* has start date and end date, search inbetween 2 dates */
			if (dates.size() == TWO) {
				setStartDateTimeandEndDateTime(group, dates);
			}

			/* only 1 set of date, search for that specific dates */
			else if (dates.size() == ONE) {
				setEndDateAndTime(group, dates);
			}
		}
	}


	private String setStartDateTimeAndTrimInputLine(String inputLine, List<DateGroup> groups) {
		for (DateGroup group : groups) {
			List<Date> dates = group.getDates();
			startDateToCalendar(dates.get(STARTING_INDEX));

			if (group.isTimeInferred()) {
				setStartDateTimeDefault();
			}

			inputLine = removeDateFromInputLine(inputLine, group);

		}
		return inputLine;
	}

	private void setEndDateAndTime(DateGroup group, List<Date> dates) {
		endDateToCalendar(dates.get(STARTING_INDEX));

		/* set default end time if no time specified */
		if (group.isTimeInferred()) {
			setEndDateTimeDefault();
		}
	}

	private void setStartDateTimeandEndDateTime(DateGroup group, List<Date> dates) {
		startDateToCalendar(dates.get(STARTING_INDEX));
		endDateToCalendar(dates.get(ONE));

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
		for (int keyword = STARTING_INDEX; keyword < removeTagLists.size(); keyword++) {
			inputLine = inputLine.replace(SYMBOL_REMOVE_HASHTAG + removeTagLists.get(keyword), EMPTY_SPACE).trim();
		}

		return inputLine;
	}

	private String trimInputLineToDescriptionOnly(String inputLine, String location, ArrayList<String> tagLists) {
		if (tagLists != null) {
			for (int keyword = STARTING_INDEX; keyword < tagLists.size(); keyword++) {
				inputLine = inputLine.replace(SYMBOL_REMOVE_HASHTAG + tagLists.get(keyword), EMPTY_SPACE).trim();
				inputLine = inputLine.replace(SYMBOL_HASHTAG + tagLists.get(keyword), EMPTY_SPACE).trim();

			}
		}

		inputLine = inputLine.replace(SYMBOL_LOCATION + location, EMPTY_SPACE).trim();
		inputLine = inputLine.replace(SYMBOL_DELETE_LOCATION + location, EMPTY_SPACE).trim();
		inputLine = inputLine.trim().replaceAll(MORE_THAN_ONE_SPACE, SPLIT_BY_SPACE);

		return inputLine;
	}


	private String findLocation(String inputLine) {
		String location = null;
		Matcher retrieveLocation = Pattern.compile(MATCH_LOCATION).matcher(inputLine);
		location = addLocation(location, retrieveLocation);

		return location;
	}

	public String addLocation(String location, Matcher retrieveLocation) {
		while (retrieveLocation.find()) {
			location = retrieveLocation.group(STARTING_INDEX);
			location = location.substring(ONE,location.length());
		}
		return location;
	}

	private ArrayList<String> findHashTags(String inputLine) {
		ArrayList<String> tags = new ArrayList<String>();
		Matcher retrieveHashTags;

		if (inputLine.contains(CONTAINS_DASH)) {
			retrieveHashTags = Pattern.compile(MATCH_DELETE_HASHTAGS).matcher(inputLine);
			addRemoveHashTags(tags, retrieveHashTags);

		}

		else{
			retrieveHashTags = Pattern.compile(MATCH_HASHTAGS).matcher(inputLine);
			addHashTags(tags, retrieveHashTags);
		}

		return tags;
	}

	public void addHashTags(ArrayList<String> tags, Matcher retrieveHashTags) {
		String hashtag;
		while (retrieveHashTags.find()) {
			hashtag = retrieveHashTags.group(STARTING_INDEX);
			hashtag = hashtag.substring(ONE, hashtag.length());
			tags.add(hashtag);
		}
	}

	public void addRemoveHashTags(ArrayList<String> tags, Matcher retrieveHashTags) {
		String hashtag;
		while (retrieveHashTags.find()) {
			hashtag = retrieveHashTags.group(STARTING_INDEX);
			hashtag = hashtag.substring(TWO, hashtag.length());
			tags.add(hashtag);
		}
	}

	private String determineCommandType(String[] separateInputLine) {
		return separateInputLine[STARTING_INDEX].toLowerCase().trim();
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

