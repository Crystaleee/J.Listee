// @@author A0126070U

package parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.joestelmach.natty.*;

import entity.Command;
import entity.CommandAddDeadlineTask;
import entity.CommandAddEvent;
import entity.CommandAddFloatTask;
import entity.CommandAddReserved;
import entity.CommandConfirm;
import entity.CommandDelete;
import entity.CommandDone;
import entity.CommandInvalid;
import entity.CommandPostpone;
import entity.CommandRedo;
import entity.CommandShow;
import entity.CommandUndo;
import entity.CommandUndone;
import entity.CommandUpdate;
import entity.GlobalLogger;

public class JListeeParser {
	private static final String LOG_COMMAND_TYPE = "commandType: ";
	private static final String LOG_ISDEADLINE = "is deadline?: ";
	private static final String LOG_IS_EVENT = "is event?: ";
	private static final String MATCH_LOCATION = "@\\w+\\S+";
	private static final String MATCH_DELETE_HASHTAGS = "-#\\w+\\S+";
	private static final String MATCH_HASHTAGS = "#\\w+\\S+";
	private static final String WHITE_SPACE = "\\b";
	private static final String DIGIT = "\\d";
	private static final String EMPTY_SPACE = "";
	private static final String INVALID_TASK_NUMBERS = "Invalid TaskNumbers";
	private static final String MORE_THAN_ONE_SPACE = " +";
	private static final String SPLIT_BY_SPACE = " ";
	
	private static final String SYMBOL_LOCATION = "@";
	private static final String SYMBOL_HASHTAG = "#";
	private static final String SYMBOL_REMOVE_HASHTAG = "-#";
	private static final String SYMBOL_DELETE_LOCATION = "-@";
	private static final String SYMBOL_SLASH = "/";
	private static final String SYMBOL_PLUS = "+";
	private static final String SYMBOL_DASH = "-";

	private static final String CONTAINING_TO = "to";
	private static final String CONTAINING_FROM = "from";
	private static final String CONTAINING_UNTIMED = "untimed";
	private static final String CONTAINING_EVENTS = "events";
	private static final String CONTAINING_RESERVED = "reserved";
	private static final String CONTAINING_DEADLINES = "deadlines";
	private static final String CONTAINING_DONE = "done";
	private static final String CONTAINING_OVERDUE = "overdue";
	private static final String CONTAINING_TOMORROW = "tomorrow";
	private static final String CONTAINING_TODAY = "today";
	private static final String CONTAINING_DEL = "del";
	private static final String CONTAINING_BOTH = "both";
	private static final String CONTAINING_ALLTIME = "alltime";
	private static final String CONTAINING_END = "end";
	private static final String CONTAINING_ENDTIME = "etime";
	private static final String CONTAINING_STARTTIME = "stime";
	private static final String CONTAINING_START = "start";
	private static final String CONTAINING_COMMA = ",";
	private static final String CONTAINING_ALL = "all";

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
	
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_PP = "pp";
	private static final String COMMAND_POSTPONE = "postpone";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_CFM = "cfm";
	private static final String COMMAND_RES = "res";
	private static final String COMMAND_DEL = "del";
	private static final String COMMAND_CONFIRM = "confirm";
	private static final String COMMAND_UNDONE = "undone";
	private static final String COMMAND_DONE = CONTAINING_DONE;
	private static final String COMMAND_SHOW = "show";
	private static final String COMMAND_REDO = "redo";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_UPDATE = "update";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_RESERVE = "reserve";
	
	private static final int STARTING_INDEX = 0;
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int DONT_EXIST = -1;
	
	private static final int DEFAULT_START_HOUR = 0;
	private static final int DEFAULT_START_MINUTE = 0;
	private static final int DEFAULT_START_SECOND = 0;
	private static final int DEFAULT_START_MILLISECOND = 0;
	private static final int DEFAULT_END_HOUR = 23;
	private static final int DEFAULT_END_MINUTE = 59;
	private static final int DEFAULT_END_SECOND = 0;
	private static final int DEFAULT_END_MILLISECOND = 0;

	/** Prepositions for date */
	private static final String[] DATE_WORDS =
			new String[]{"by", "on", "at", "due", "during", "in", "for", CONTAINING_FROM, "at", "in", "this", "before", "after", "next"};
	
	/** Terms for searching a specific type of task */
	private static final String[] SEARCH_TASKS =
			new String[]{CONTAINING_TODAY, CONTAINING_TOMORROW, CONTAINING_OVERDUE, CONTAINING_DONE, CONTAINING_RESERVED, CONTAINING_DEADLINES, CONTAINING_EVENTS, "untimed"};

	/** Terms for updating the time of a task */
	private static final String[] CONTAIN_TIME = new String[]{CONTAINING_END, CONTAINING_START , CONTAINING_ALLTIME , CONTAINING_ENDTIME , CONTAINING_STARTTIME, CONTAINING_BOTH, CONTAINING_DEL};

	/** Terms for postponing a task */
	private static final String[] CONTAIN_YEAR_MONTH_DAY_HOUR_MIN =
			new String[]{YEARS, YEAR, YR, YRS, MONTHS, MONTH, MTH, MTHS, DAY, DAYS, HOURS, HOUR, HR, HRS, MINUTE, MINUTES, MIN, MINS};

    /** Global logger to log information and exception. */
	private static Logger logger = GlobalLogger.getLogger();
	
	/** Library to parse date for natural command language */
	private com.joestelmach.natty.Parser dateParser;

	/** Start dates of task */
	private Calendar startDate = null;
	
	/** End Dates of task */
	private Calendar endDate = null; 

	public JListeeParser() {
		dateParser = new com.joestelmach.natty.Parser();
	}
	
	/**
	 * Return Command object of given inputLine
	 * 
	 * @param inputLine Inputline of user
	 * @return Command object
	 */
	public Command ParseCommand(String inputLine) {
		String[] separateInputLine = inputLine.split(SPLIT_BY_SPACE);
		String commandType = determineCommandType(separateInputLine);
		inputLine = deleteKeyword(commandType, inputLine);
		logger.info((LOG_COMMAND_TYPE + commandType));
		
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

	/**
	 * Parses Add command from user with natural language support 
	 * 
	 * @param inputLine
	 * @return CommandAddEvent or CommandAddDeadlineTask or CommandAddFloatTask
	 */
	
	private Command parseAdd(String inputLine) {
		assert inputLine != null;

		boolean isEvent = false;
		boolean isDeadline = false;
		String location = null;
		ArrayList<String> tagLists = new ArrayList<String>();
		int firstDateIndex = DONT_EXIST;
		int prepositionIndex = DONT_EXIST;

		try {
		prepositionIndex = getPrepositionIndex(inputLine, prepositionIndex);
		logger.info(("Preposition Index for parseAdd: " + prepositionIndex));
		
		if (prepositionIndex != DONT_EXIST){
			List<DateGroup> dateGroups = dateParser.parse(inputLine.substring(prepositionIndex));

			for (DateGroup group : dateGroups) {
				List<Date> dates = group.getDates();

				if (dates.size() == TWO) {
					isEvent = true;
				}

				else if (dates.size() == ONE) {
					isDeadline = true;

				}

				setAllDates(inputLine, dateGroups);
				firstDateIndex = getFirstDateIndex(prepositionIndex, group);
				inputLine = removeWordBeforeDateAndDate(inputLine, firstDateIndex, group).trim();
			}
		}

		logger.info(LOG_IS_EVENT + isEvent);
		logger.info(LOG_ISDEADLINE + isEvent);

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
		} catch (NumberFormatException e) {
			return new CommandInvalid();
		}	
	}

	/**
	 * Parses delete command from user with natural language support 
	 * 
	 * @param inputLine
	 * @return CommandDelete or CommandInvalid()
	 */
	private Command parseDelete(String inputLine) {
		assert inputLine != null;

		try {
			ArrayList<Integer> taskNumbers = new ArrayList<Integer>();

			taskNumbers = extractTaskNumbers(inputLine, taskNumbers);

			return new CommandDelete(taskNumbers);
		} catch (NumberFormatException e) {
			return new CommandInvalid();
		} 
	}

	/**
	 * Parses command undo 
	 * 
	 * @return CommandUndo
	 */
	private Command parseUndo() {
		return new CommandUndo();
	}

	/**
	 * Parses command Redo 
	 * 
	 * @return CommandRedo
	 */
	private Command parseRedo() {
		return new CommandRedo();
	}

	/**
	 * Parses show either group or keywords 
	 * @param inputLine
	 * @return
	 */
	private Command parseShow(String inputLine) {
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
		
		//date exist as preposition index is initialise
		//initialise respective dates
		if (prepositionIndex != DONT_EXIST) {
			List<DateGroup> dateGroups = dateParser.parse(inputLine.substring(prepositionIndex));

			for (DateGroup group : dateGroups) {	
				setAllDates(inputLine, dateGroups);
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
	
	/**
	 * Parses in commandReserve with natural language variation
	 * 
	 * @param inputLine
	 * @return CommandAddReserved or CommandInvalid
	 */
	private Command parseReserve(String inputLine) {
		assert inputLine != null;
		
		String location = null;
		ArrayList<String> tagLists = new ArrayList<String>();
		ArrayList<Calendar> startDates = new ArrayList<Calendar>();
		ArrayList<Calendar> endDates = new ArrayList<Calendar>();
		int prepositionIndex = DONT_EXIST;
		int firstDateIndex = DONT_EXIST;

		prepositionIndex = getPrepositionIndex(inputLine, prepositionIndex);

		try {
			if (prepositionIndex != DONT_EXIST) {
				List<DateGroup> dateGroups = dateParser.parse(inputLine.substring(prepositionIndex));

				for (DateGroup group : dateGroups) {
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
		
		} catch (NumberFormatException e) {
			return new CommandInvalid();
		}
	}
	
	/**
	 * Parses Confirm command from user for reserved task
	 * 
	 * @param inputLine
	 * @return CommandConfirm or CommandInvalid
	 *
	 */
	private Command parseConfirm(String inputLine) {
		
		try {
			Integer taskNumber = null;
			Integer timeSlotIndex;
	
			taskNumber = extractNumber(inputLine);
			inputLine = deleteReservedTaskIndexFromInputLine(inputLine, taskNumber);
	
			timeSlotIndex = extractNumber(inputLine);	
	
			return new CommandConfirm(taskNumber, timeSlotIndex);
		} catch (NumberFormatException e) {
			return new CommandInvalid();
		}
	}

	/**
	 * Parses commandUpdate with natural language variation
	 * 
	 * @param inputLine
	 * @return CommandUpdate
	 */
	private Command parseUpdate(String inputLine) {
		String location = null;
		String taskDescription = null;
		ArrayList<String> removeTagLists = new ArrayList<String>();
		ArrayList<String> tagLists = new ArrayList<String>();
		ArrayList<Integer> taskNumbers = new ArrayList<Integer>();
		Integer taskNumber;
		Integer reservedTaskIndex = null; 

		taskNumber = extractNumber(inputLine);

		inputLine = deleteReservedTaskIndexFromInputLine(inputLine, taskNumber);

		Matcher retrieveLocation = Pattern.compile(SYMBOL_DELETE_LOCATION).matcher(inputLine);

		location = setLocationToEmptySpace(location, retrieveLocation);
		
		if (location!= null && location.equals(EMPTY_SPACE)){
			inputLine = deleteKeyword(SYMBOL_DELETE_LOCATION, inputLine);
		}
		
		else {
			location = findLocation(inputLine);
		}
		
		for (String preposition : CONTAIN_TIME) {
			reservedTaskIndex = checkReservedTaskIndexExist(inputLine, reservedTaskIndex, preposition);
			inputLine = deleteReservedTaskIndexFromInputLine(inputLine, reservedTaskIndex);
		}

		List<DateGroup> dateGroups = dateParser.parse(inputLine);

		for (int keyword = STARTING_INDEX; keyword < CONTAIN_TIME.length; keyword++) {
			if (inputLine.toLowerCase().contains(SYMBOL_DASH + CONTAIN_TIME[keyword])){
				inputLine = deleteTimeAndTrimInputLine(inputLine, keyword);	
			}

			else if (inputLine.toLowerCase().contains(SYMBOL_PLUS + CONTAIN_TIME[keyword])) { 
				logger.info("Editing / Adding time");

				inputLine = inputLine.replace(SYMBOL_PLUS + CONTAIN_TIME[keyword], EMPTY_SPACE).trim();

				switch (CONTAIN_TIME[keyword]) {

				case CONTAINING_END :
				case CONTAINING_BOTH :
					setAllDates(inputLine, dateGroups);
					break;

				case CONTAINING_START :
					inputLine = setStartDateTimeAndTrimInputLine(inputLine, dateGroups);
					break;

				case CONTAINING_ENDTIME :
					changingEndTimeOnly(inputLine, dateGroups);
					break;	

				case CONTAINING_STARTTIME :
					inputLine = changingStartTimeOnly(inputLine, dateGroups);
					break;

				case CONTAINING_DEL :
					taskNumbers = extractTaskNumbers(inputLine, taskNumbers);
					inputLine = removeAllDeletedNumberAndSymbols(inputLine, taskNumbers);
					break;
				}

				for (DateGroup group : dateGroups) {
					inputLine = removeDateFromInputLine(inputLine, group);		
				}

			}

		}

		if (inputLine.contains(SYMBOL_DASH)) {
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
	
	/**
	 * Parses CommandDone 
	 * 
	 * @param inputLine
	 * @return CommandDone or CommandInvalid
	 */
	private Command parseDone(String inputLine){
		assert inputLine != null;
		try {
			
			ArrayList<Integer> taskNumbers = new ArrayList<Integer>();
			taskNumbers = extractTaskNumbers(inputLine, taskNumbers);
			return new CommandDone(taskNumbers);
			
		} catch (NumberFormatException e) {
			return new CommandInvalid();
		}
	}

	/**
	 * Parses CommandUndone
	 * 
	 * @param inputLine
	 * @return CommandUndone or CommandInvalid
	 */
	private Command parseUndone(String inputLine) {
		assert inputLine != null;
		try {
			
			ArrayList<Integer> taskNumbers = new ArrayList<Integer>();
			taskNumbers = extractTaskNumbers(inputLine, taskNumbers);	
			return new CommandUndone(taskNumbers);	
			
		} catch (NumberFormatException e) {
			return new CommandInvalid();
		}
	}

	/**
	 * Parses Command Postpone 
	 * 
	 * @param inputLine
	 * @return CommandPostpone or Command Invalid
	 */
	private Command parsePostpone(String inputLine) {
		Integer taskNumber;
		ArrayList<String> parameters = new ArrayList<String>();
		Calendar time = Calendar.getInstance();

		try{
			taskNumber = extractNumber(inputLine);

			if (inputLine.contains(String.valueOf(taskNumber))) {
				inputLine = deleteKeyword(String.valueOf(taskNumber), inputLine);
			}

			for (int keyword = STARTING_INDEX; keyword < CONTAIN_YEAR_MONTH_DAY_HOUR_MIN.length; keyword++){

				Matcher matcher = Pattern.compile(DIGIT+ CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword]+WHITE_SPACE).matcher(inputLine);

				while(matcher.find()) {
					switch(CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword]){
					case YEAR :
					case YRS :
					case YR :
					case YEARS : 
						inputLine = setTimeYears(inputLine, parameters, time, keyword);
						break;

					case MONTH :
					case MONTHS :
					case MTH :
					case MTHS :
						inputLine = setTimeMonth(inputLine, parameters, time, keyword);
						break;

					case DAYS :
					case DAY :
						inputLine = setTimeDay(inputLine, parameters, time, keyword);
						break;

					case HR :
					case HRS :
					case HOURS :
					case HOUR :
						inputLine = setTimeHour(inputLine, parameters, time, keyword);
						break;

					case MINUTE :
					case MINUTES : 
					case MIN :
					case MINS :
						inputLine = setTimeMinute(inputLine, parameters, time, keyword);
						break;
					}
				}	
			}

			return new CommandPostpone(taskNumber, time,  parameters);
		} catch (NumberFormatException e) {
			return new CommandInvalid();
		}
	}

	/**
	 * Returns invalid command
	 * @return CommandInvalid
	 */
	private Command parseInvalid() {
		return new CommandInvalid();
	}

	/**
	 * add hash tags into list
	 * @param tags
	 * @param retrieveHashTags
	 */
	private void addHashTags(ArrayList<String> tags, Matcher retrieveHashTags) {
		String hashtag;
		while (retrieveHashTags.find()) {
			hashtag = retrieveHashTags.group(STARTING_INDEX);
			hashtag = hashtag.substring(ONE, hashtag.length());
			tags.add(hashtag);
		}
	}

	/**
	 * adds location 
	 * @param location
	 * @param retrieveLocation
	 * @return
	 */
	private String addLocation(String location, Matcher retrieveLocation) {
		while (retrieveLocation.find()) {
			location = retrieveLocation.group(STARTING_INDEX);
			location = location.substring(ONE,location.length());
		}
		return location;
	}

	/**
	 * add remove to be remvoed hash tags into list
	 * @param tags
	 * @param retrieveHashTags
	 */
	private void addRemoveHashTags(ArrayList<String> tags, Matcher retrieveHashTags) {
		String hashtag;
		while (retrieveHashTags.find()) {
			hashtag = retrieveHashTags.group(STARTING_INDEX);
			hashtag = hashtag.substring(TWO, hashtag.length());
			tags.add(hashtag);
		}
	}

	private void changingEndTimeOnly(String inputLine, List<DateGroup> dateGroups) {
		setAllDates(inputLine, dateGroups);
		endDate.set(Calendar.YEAR, ONE);
	}

	/**
	 * check if preposition is a valid from..to.. if not, return -1;
	 * 
	 * @param inputLine
	 * @param prepositionIndex
	 * @return prepositionIndex
	 */
	private int checkFromAndTo(String inputLine, int prepositionIndex) {
		if (prepositionIndex != DONT_EXIST){
			prepositionIndex = checkInputLineFromAndTo(inputLine, prepositionIndex);
		}
		
		return prepositionIndex;
	}

	/**
	 * check if inputLine exists "from... to.."
	 * @param inputLine
	 * @param prepositionIndex
	 * @return
	 */
	private int checkInputLineFromAndTo(String inputLine, int prepositionIndex) {
		if (inputLine.substring(prepositionIndex).contains(CONTAINING_FROM)){
			if (!inputLine.substring(prepositionIndex).contains(CONTAINING_TO)){
				prepositionIndex = DONT_EXIST;
			}

		}
		return prepositionIndex;
	}

	/**
	 * brute force to check if task numbers are duplicate and remove if duplicates
	 * @param taskNumbers
	 * @param keyword
	 */
	private void checkDuplicatesForAllElements(ArrayList<Integer> taskNumbers, int keyword) {
		for (int index = keyword + ONE; index < taskNumbers.size(); index++) {
			checkIfDuplicateExist(taskNumbers, keyword, index);
		}
	}

	/**
	 * compares and check if duplicate exist
	 * @param taskNumbers
	 * @param keyword
	 * @param index
	 */
	private void checkIfDuplicateExist(ArrayList<Integer> taskNumbers, int keyword, int index) {
		if (taskNumbers.get(keyword) == taskNumbers.get(index)) {
			taskNumbers.remove(index);
		}
	}

	/**
	 * find if preposition exists and return the preposition index
	 * 
	 * @param inputLine
	 * @param prepositionIndex
	 * @return prepositionIndex
	 */
	private int checkPrepositionIndexAndInitialise(String inputLine, int prepositionIndex) {
		for (int keyword = STARTING_INDEX; keyword < DATE_WORDS.length; keyword++){
			int temp = DONT_EXIST;
	
			Matcher matcher = Pattern.compile(WHITE_SPACE+ DATE_WORDS[keyword]+WHITE_SPACE).matcher(inputLine);
	
			while(matcher.find()) {
				temp = matcher.start();
			}		
	
			if (prepositionIndex < temp) {
				prepositionIndex = temp;
			}
		}
		return prepositionIndex;
	}

	/**
	 * if index of task reserve exist 
	 * @param inputLine
	 * @param reservedTaskIndex
	 * @param preposition
	 * @return task index for the reserved
	 */
	private Integer checkReservedTaskIndexExist(String inputLine, Integer reservedTaskIndex, String preposition) {
		if (inputLine.contains(SYMBOL_PLUS + preposition) && (!preposition.equals(CONTAINING_BOTH))) {
			reservedTaskIndex = extractNumber(inputLine);
			logger.info("Editing reserve slot index time: " + reservedTaskIndex);		
		}
		return reservedTaskIndex;
	}

	private String changingStartTimeOnly(String inputLine, List<DateGroup> dateGroups) {
		inputLine = setStartDateTimeAndTrimInputLine(inputLine, dateGroups);
		startDate.set(Calendar.YEAR, ONE);
		return inputLine;
	}

	/**
	 * Initialise start date to end date and default time if don't exist
	 */
	private void checkStartDateDontExistAndInitialise() {
		if (startDate == null) {
			startDateToCalendar(endDate.getTime());
			setStartDateTimeDefault();
		}
	}

	/**
	 * Check for task to display with input /search_task 
	 * delete keyword and initialise time and return inputline
	 * 
	 * @param inputLine
	 * @param task
	 * @param keyword
	 * @return inputLine 	
	 */
	private String checkTaskToDisplay(String inputLine, ArrayList<String> task, int keyword) {
		assert inputLine != null;
		
		if (inputLine.toLowerCase().contains(SYMBOL_SLASH + SEARCH_TASKS[keyword])) {
			inputLine = deleteKeyword(SYMBOL_SLASH + SEARCH_TASKS[keyword], inputLine);
	
			switch (SEARCH_TASKS[keyword]) {
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

	/**
	 * Check if user inputs numbers and adds them to taskNumbers
	 * 
	 * @param taskNumbers
	 * @param taskNumberLine
	 * @param startIndex
	 */
	private void checkUsingDashOrCommaAndExtract(ArrayList<Integer> taskNumbers, String[] taskNumberLine,
			int startIndex) {
	
		/* Split the code with "-" , index zero represents the first task index to delete
		 * index 1 refers to last task index to delete
		 */
	
		if (taskNumberLine[startIndex].contains(SYMBOL_DASH)) {
			String[] splitRange = taskNumberLine[startIndex].split(SYMBOL_DASH);
	
			try{
				int startDeleteIndex = Integer.valueOf(splitRange[STARTING_INDEX]);
				int endDeleteIndex = Integer.valueOf(splitRange[ONE]);
	
				extractTaskNumbersContainingDash(taskNumbers, startDeleteIndex, endDeleteIndex);
	
			} catch (NumberFormatException e) {
				System.out.println(INVALID_TASK_NUMBERS);
			}
	
		}
	
		else {
			try {
				taskNumbers.add(Integer.valueOf(taskNumberLine[startIndex]));
			} catch (NumberFormatException e) {
				System.out.println(INVALID_TASK_NUMBERS);
			}
		}
	}

	/**
	 * delete word from inputLine
	 * 
	 * @param keyword
	 * @param inputLine
	 * @return inputLine 
	 */
	private String deleteKeyword(String keyword, String inputLine) {
		inputLine = inputLine.replaceFirst(keyword, EMPTY_SPACE).trim();
		return inputLine;
	}

	/**
	 * CommandUpdate deleting time 
	 * 
	 * @param keyword
	 */
	private void deleteTime(int keyword) {
		
		/* set time to zero to represent deleting time*/
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

	/**
	 * deleting time and remove from input line
	 * @param inputLine
	 * @param keyword
	 * @return inputLine after removing
	 */
	private String deleteTimeAndTrimInputLine(String inputLine, int keyword) {
		logger.info("Deleting time");
		inputLine = inputLine.replace(SYMBOL_DASH + CONTAIN_TIME[keyword], EMPTY_SPACE).trim();
		deleteTime(keyword);
		return inputLine;
	}

	/**
	 * removee reserve task index from inputLine
	 * @param inputLine
	 * @param reservedTaskIndex
	 * @return input line after revmogin
	 */
	private String deleteReservedTaskIndexFromInputLine(String inputLine, Integer reservedTaskIndex) {
		if (reservedTaskIndex != null){
			inputLine = deleteKeyword(String.valueOf(reservedTaskIndex),inputLine);
		}
		
		return inputLine;
	}

	/**
	 * check if taskNumbers are duplicates
	 * @param taskNumbers
	 */
	private void detectDuplicates(ArrayList<Integer> taskNumbers) {
		if (taskNumbers!= null){
			iterateAndDetectDuplicates(taskNumbers);
		}
	}

	/**
	 * determined user input command 
	 * @param separateInputLine
	 * @return command type 
	 */
	private String determineCommandType(String[] separateInputLine) {
		return separateInputLine[STARTING_INDEX].toLowerCase().trim();
	}

	/**
	 * set end date to calendar
	 * @param date
	 */
	private void endDateToCalendar(Date date) {
		endDate = Calendar.getInstance();
		endDate.setTime(date);
	}

	/**
	 * get task number of inputLine
	 * @param inputLine
	 * @return task number or don't exist
	 */
	private Integer extractNumber(String inputLine) {	
		String[] splitInputLine = inputLine.split(SPLIT_BY_SPACE);
		try{
			return Integer.valueOf(splitInputLine[STARTING_INDEX]);
		} catch (NumberFormatException e) {
			return null;
		}
	
	}

	/**
	 * extract 2 days only 
	 * @param startDates
	 * @param endDates
	 * @param group
	 * @param dates
	 */
	private void extractMultiplePairDates(ArrayList<Calendar> startDates, ArrayList<Calendar> endDates, DateGroup group,
			List<Date> dates) {
	
		extractPairDates(startDates, endDates, dates);
	
		if (group.isTimeInferred()) {
			setStartDateTimeDefault();
			setEndDateTimeDefault();
		}
	}

	/**
	 * set dates for pair dates 
	 * @param startDates
	 * @param endDates
	 * @param dates
	 */
	private void extractPairDates(ArrayList<Calendar> startDates, ArrayList<Calendar> endDates, List<Date> dates) {
		for (int keyword = STARTING_INDEX; keyword < dates.size() - ONE; keyword += TWO) {
			startDateToCalendar(dates.get(keyword));
			endDateToCalendar(dates.get(keyword + ONE));
	
			swapDates();
	
			startDates.add(startDate);
			endDates.add(endDate);
		}
	}

	/**
	 * Extract Task Numbers from a input line and add to ArrayList taskNumbers
	 * @param inputLine
	 * @param taskNumbers
	 * @return ArrayList<Integer> taskNumbers
	 */
	private ArrayList<Integer> extractTaskNumbers(String inputLine, ArrayList<Integer> taskNumbers) {
		inputLine = inputLine.trim().replaceAll(MORE_THAN_ONE_SPACE, EMPTY_SPACE);
	
		if (inputLine.contains(CONTAINING_ALL)) {
			taskNumbers = null;
		}
	
		else {
			String[] taskNumberLine = inputLine.split(CONTAINING_COMMA);
	
			for (int startIndex = STARTING_INDEX; startIndex < taskNumberLine.length; startIndex++) {
				checkUsingDashOrCommaAndExtract(taskNumbers, taskNumberLine, startIndex);
			}
		}
		
		detectDuplicates(taskNumbers);
		return taskNumbers;
	}

	/**
	 * Add the range of index
	 * 
	 * @param taskNumbers
	 * @param startDeleteIndex
	 * @param endDeleteIndex
	 */
	private void extractTaskNumbersContainingDash(ArrayList<Integer> taskNumbers, int startDeleteIndex, int endDeleteIndex) {
		while (startDeleteIndex <= endDeleteIndex) {
			taskNumbers.add(startDeleteIndex);
			startDeleteIndex++;
		}
	}

	/**
	 * find location with starting of "@"
	 * @param inputLine
	 * @return
	 */
	private String findLocation(String inputLine) {
		String location = null;
		Matcher retrieveLocation = Pattern.compile(MATCH_LOCATION).matcher(inputLine);
		location = addLocation(location, retrieveLocation);
	
		return location;
	}

	/**
	 * find list of hash tags from inputline
	 * @param inputLine
	 * @return ArrayList<String> hashTagsList
	 */
	
	private ArrayList<String> findHashTags(String inputLine) {
		ArrayList<String> tags = new ArrayList<String>();
		Matcher retrieveHashTags;
	
		if (inputLine.contains(SYMBOL_DASH)) {
			retrieveHashTags = Pattern.compile(MATCH_DELETE_HASHTAGS).matcher(inputLine);
			addRemoveHashTags(tags, retrieveHashTags);
	
		}
	
		else{
			retrieveHashTags = Pattern.compile(MATCH_HASHTAGS).matcher(inputLine);
			addHashTags(tags, retrieveHashTags);
		}
	
		return tags;
	}

	/**
	 * find the index of first date
	 * @param prepositionIndex
	 * @param group
	 * @return index of first date
	 */
	private int getFirstDateIndex(int prepositionIndex, DateGroup group) {
		int firstDateIndex;
		firstDateIndex = group.getPosition() - ONE + prepositionIndex;
		return firstDateIndex;
	}

	/**
	 * find last index of preposition in inputline
	 * 
	 * @param inputLine
	 * @param prepositionIndex
	 * @return int  index of preposition 
	 */
	private int getPrepositionIndex(String inputLine, int prepositionIndex) {
	
		prepositionIndex = checkPrepositionIndexAndInitialise(inputLine, prepositionIndex);
		prepositionIndex = checkFromAndTo(inputLine, prepositionIndex);
	
		return prepositionIndex;
	}

	/**
	 * check through all task numbers
	 * @param taskNumbers
	 */
	
	private void iterateAndDetectDuplicates(ArrayList<Integer> taskNumbers) {
		for (int keyword = STARTING_INDEX; keyword < taskNumbers.size(); keyword++){
			checkDuplicatesForAllElements(taskNumbers, keyword);
		}
	}

	/**
	 * Trim inputline if contain "-", "," and taskNumbers
	 * 
	 * @param inputLine
	 * @param taskNumbers
	 * @return inputLine
	 */
	private String removeAllDeletedNumberAndSymbols(String inputLine, ArrayList<Integer> taskNumbers) {
		for (int startIndex = STARTING_INDEX; startIndex < taskNumbers.size(); startIndex++) {
			if (inputLine.contains(String.valueOf(taskNumbers.get(startIndex)))) {
				inputLine = deleteKeyword(CONTAINING_COMMA, inputLine);
				inputLine = deleteKeyword(SYMBOL_DASH, inputLine);
				inputLine = deleteKeyword(String.valueOf(taskNumbers.get(startIndex)), inputLine);
			}
		}
		return inputLine;
	}

	/**
	 * trim input line without dates
	 * @param inputLine
	 * @param group
	 * @return
	 */
	private String removeDateFromInputLine(String inputLine, DateGroup group) {
		if (inputLine.contains(group.getText())) {
			inputLine = inputLine.replace(group.getText(), EMPTY_SPACE);
		}
		return inputLine;
	}

	/**
	 * remove preposition word and date of input line
	 * 
	 * @param input
	 * @param dateIndex
	 * @param group
	 * @return inputLine
	 */
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

	/**
	 * search for dates with natural language variation and set
	 * 
	 * @param inputLine
	 * @param dateGroups
	 */
	private void setAllDates(String inputLine, List<DateGroup> dateGroups) {
		for (DateGroup group : dateGroups) {
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

	/**
	 * set end time to 0
	 */
	private void setEndTimeInMilliZero() {
		endDate = Calendar.getInstance();
		endDate.setTimeInMillis(STARTING_INDEX);
	}

	/**
	 * set end time to default of 23 59
	 */
	private void setEndDateTimeDefault() {
		endDate.set(Calendar.HOUR_OF_DAY, DEFAULT_END_HOUR);
		endDate.set(Calendar.MINUTE, DEFAULT_END_MINUTE);
		endDate.set(Calendar.SECOND, DEFAULT_END_SECOND);
		endDate.set(Calendar.MILLISECOND, DEFAULT_END_MILLISECOND);
	}

	/**
	 * set date and time
	 * @param group
	 * @param dates
	 */
	private void setEndDateAndTime(DateGroup group, List<Date> dates) {
		endDateToCalendar(dates.get(STARTING_INDEX));
	
		/* set default end time if no time specified */
		if (group.isTimeInferred()) {
			setEndDateTimeDefault();
		}
	}

	/**
	 * if user deletes location is found, set to empty space
	 * 
	 * @param location
	 * @param retrieveLocation
	 * @return location of empty space
	 */
	private String setLocationToEmptySpace(String location, Matcher retrieveLocation) {
		if (retrieveLocation.find()) {
			location = EMPTY_SPACE;
		}
		return location;
	}

	/**
	 * set start date and trim inputline to without dates
	 * @param inputLine
	 * @param dateGroups
	 * @return
	 */
	private String setStartDateTimeAndTrimInputLine(String inputLine, List<DateGroup> dateGroups) {
		for (DateGroup group : dateGroups) {
			List<Date> dates = group.getDates();
			startDateToCalendar(dates.get(STARTING_INDEX));

			if (group.isTimeInferred()) {
				setStartDateTimeDefault();
			}

			inputLine = removeDateFromInputLine(inputLine, group);

		}
		return inputLine;
	}

	/**
	 * set start time to default of 00 00
	 */
	private void setStartDateTimeDefault() {
		startDate.set(Calendar.HOUR_OF_DAY, DEFAULT_START_HOUR);
		startDate.set(Calendar.MINUTE, DEFAULT_START_MINUTE);
		startDate.set(Calendar.SECOND, DEFAULT_START_SECOND);
		startDate.set(Calendar.MILLISECOND, DEFAULT_START_MILLISECOND);
	}

	/**
	 * set both start and end date time
	 * @param group
	 * @param dates
	 */
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
	
	/**
	 * set start time to 0
	 */
	private void setStartTimeInMilliZero() {
		startDate = Calendar.getInstance();
		startDate.setTimeInMillis(STARTING_INDEX);
	}

	/**
	 * Postpone time to x minutes later and trim inputline
	 * 
	 * @param inputLine
	 * @param parameters
	 * @param time
	 * @param keyword
	 * @return inputLine 
	 */
	private String setTimeMinute(String inputLine, ArrayList<String> parameters, Calendar time, int keyword) {
		Integer timeToPostpone;
		parameters.add(MINUTE);
		inputLine = deleteKeyword(CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword], inputLine);
		timeToPostpone = extractNumber(inputLine);
		inputLine = deleteKeyword(String.valueOf(timeToPostpone), inputLine);
		time.set(Calendar.MINUTE, timeToPostpone);
		return inputLine;
	}

	/**
	 * Postpone time to x hour later and trim inputline
	 * 
	 * @param inputLine
	 * @param parameters
	 * @param time
	 * @param keyword
	 * @return inputLine 
	 */
	private String setTimeHour(String inputLine, ArrayList<String> parameters, Calendar time, int keyword) {
		Integer timeToPostpone;
		parameters.add(HOUR);
		inputLine = deleteKeyword(CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword], inputLine);
		timeToPostpone = extractNumber(inputLine);
		inputLine = deleteKeyword(String.valueOf(timeToPostpone), inputLine);					
		time.set(Calendar.HOUR_OF_DAY, timeToPostpone);
		return inputLine;
	}

	/**
	 * Postpone time to x day later and trim inputline
	 * 
	 * @param inputLine
	 * @param parameters
	 * @param time
	 * @param keyword
	 * @return inputLine 
	 */
	private String setTimeDay(String inputLine, ArrayList<String> parameters, Calendar time, int keyword) {
		Integer timeToPostpone;
		parameters.add(DAY);
		inputLine = deleteKeyword(CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword], inputLine);
		timeToPostpone = extractNumber(inputLine);
		inputLine = deleteKeyword(String.valueOf(timeToPostpone), inputLine);
		time.set(Calendar.DATE, timeToPostpone);
		return inputLine;
	}

	/**
	 * Postpone time to x month later and trim inputline
	 * 
	 * @param inputLine
	 * @param parameters
	 * @param time
	 * @param keyword
	 * @return inputLine 
	 */
	private String setTimeMonth(String inputLine, ArrayList<String> parameters, Calendar time, int keyword) {
		Integer timeToPostpone;
		parameters.add(MONTH);
		inputLine = deleteKeyword(CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword], inputLine);
		timeToPostpone = extractNumber(inputLine);
		inputLine = deleteKeyword(String.valueOf(timeToPostpone), inputLine);
		time.set(Calendar.MONTH, timeToPostpone);
		return inputLine;
	}

	/**
	 * Postpone time to x years later and trim inputline
	 * 
	 * @param inputLine
	 * @param parameters
	 * @param time
	 * @param keyword
	 * @return inputLine 
	 */
	private String setTimeYears(String inputLine, ArrayList<String> parameters, Calendar time, int keyword) {
		Integer timeToPostpone;
		parameters.add(YEAR);
		inputLine = deleteKeyword(CONTAIN_YEAR_MONTH_DAY_HOUR_MIN[keyword], inputLine);
		timeToPostpone = extractNumber(inputLine);
		inputLine = deleteKeyword(String.valueOf(timeToPostpone), inputLine);
		time.set(Calendar.YEAR, timeToPostpone);
		return inputLine;
	}

	/**
	 * set start date to calendar
	 * @param date
	 */
	private void startDateToCalendar(Date date) {
		startDate = Calendar.getInstance();
		startDate.setTime(date);
	}

	/**
	 * checks if start date after end date, if after, swap the dates
	 */
	private void swapDates(){
		if (startDate.after(endDate)) {
			Calendar temp = endDate;
			endDate = startDate;
			startDate = temp;
		}
	}

	/**
	 * removes removehashtagLists from input Line
	 * @param inputLine
	 * @param removeTagLists
	 * @return inputLine after removing all -#
	 */
	private String trimInputLineWithoutRemoveHashTags(String inputLine, ArrayList<String> removeTagLists) {
		for (int keyword = STARTING_INDEX; keyword < removeTagLists.size(); keyword++) {
			inputLine = inputLine.replace(SYMBOL_REMOVE_HASHTAG + removeTagLists.get(keyword), EMPTY_SPACE).trim();
		}

		return inputLine;
	}

	/**
	 * left task description as input line
	 * @param inputLine
	 * @param location
	 * @param tagLists
	 * @return inputline
	 */
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

}

