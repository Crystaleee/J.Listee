//@@author A0149063E
package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import entity.Display;
import entity.GlobalLogger;
import entity.Task;
import entity.TaskDeadline;
import entity.TaskEvent;
import entity.TaskFloat;
import entity.TaskReserved;

public class Storage {

	private static String filePath_;
	private static Storage storageInstance_;

	private static SimpleDateFormat sdf_ = new SimpleDateFormat("dd/MM/yy HH:mm");
	private static Logger logger_ = GlobalLogger.getLogger();
	
	/* Headers that divide the text file into the different task categories. */
	private static final String HEADER_FLOATING = "     TASKS";
	private static final String HEADER_DEADLINE = "   DEADLINES";
	private static final String HEADER_EVENT = "    EVENTS";
	private static final String HEADER_RESERVED = "   RESERVED";
	private static final String HEADER_COMPLETED = "   COMPLETED";
	private static final String HEADER_DIVIDER = "===============";

	/* The attributes of each task. */
	private static final String ATTRIBUTE_DESCRIPTION = "Description:";
	private static final String ATTRIBUTE_LOCATION = "Location:";
	private static final String ATTRIBUTE_TAGS = "Tags:";
	private static final String ATTRIBUTE_DEADLINE = "Deadline:";
	private static final String ATTRIBUTE_START_DATE = "Start Date:";
	private static final String ATTRIBUTE_END_DATE = "End Date:";
	private static final String ATTRIBUTE_START_DATES = "Start Dates:";
	private static final String ATTRIBUTE_END_DATES = "End Dates:";

	/* Inputs into the display for an empty attribute. */
	private static final String EMPTY_MESSAGE = "";
	private static final String EMPTY_DESCRIPTION = "undefined";

	/* Messages to be logged for successfully reading a task. */
	private static final String LOGGER_READ_FLOATING = "Successfully read floating task: %1$s";
	private static final String LOGGER_READ_DEADLINE = "Successfully read deadline task: %1$s";
	private static final String LOGGER_READ_EVENT = "Successfully read event: %1$s";
	private static final String LOGGER_READ_RESERVED = "Successfully read reserved task: %1$s";
	private static final String LOGGER_READ_COMPLETED = "Successfully read completed task: %1$s";

	/* Messages to be logged if there is an error in reading a task. */
	private static final String LOGGER_READ_ERROR_DEADLINE = "Deadline task has invalid deadline and can't be read.";
	private static final String LOGGER_READ_ERROR_EVENT = "Event task has invalid dates and can't be read.";
	private static final String LOGGER_READ_ERROR_RESERVED = "Reserved task has invalid dates and can't be read.";
	private static final String LOGGER_READ_ERROR_COMPLETED = "Could not read completed task: %1$s";

	/* Messages to be logged for successfully writing a task. */
	private static final String LOGGER_WRITE_FLOATING = "Successfully wrote floating task: %1$s";
	private static final String LOGGER_WRITE_DEADLINE = "Successfully wrote deadline task: %1$s";
	private static final String LOGGER_WRITE_EVENT = "Successfully wrote event: %1$s";
	private static final String LOGGER_WRITE_RESERVED = "Successfully wrote reserved task: %1$s";
	private static final String LOGGER_WRITE_COMPLETED = "Successfully wrote completed task: %1$s";

	/**
	 * Gets an instance of Storage for other classes to use.
	 * 
	 * @return A Storage object.
	 */
	public static Storage getInstance() {
		if (storageInstance_ == null) {
			return new Storage();
		}
		return storageInstance_;
	}

	/**
	 * Creates the text file that will be used to store the tasks of J.Listee.
	 * 
	 * @param filepath      Location of where the file will be created.
	 * @throws IOException  If I/O operations fail.
	 */
	public void createFile(String filepath) throws IOException {
		File file = new File(filepath);
		if (!file.exists()) {
			file.createNewFile();
		}
		setFilePath(filepath);
	}

	/**
	 * Changes the location of the text file to a new filepath. If a valid
	 * storage file exists in the new filepath, read storage from there.
	 * 
	 * @param newFilePathString  The new location of the storage text file.
	 * @throws IOException       If I/O operations fail.
	 */
	public void changeFilePath(String newFilePathString) throws IOException {
		String oldFilePathString = readOldFilePath();

		File newFile = new File(newFilePathString);
		if (!newFile.exists()) {
			Path oldFilePath = Paths.get(oldFilePathString);
			Path newFilePath = Paths.get(newFilePathString);
			Files.move(oldFilePath, newFilePath, StandardCopyOption.ATOMIC_MOVE);
		}
		LogStorage.writeLogFile(newFilePathString);
		setFilePath(newFilePathString);
	}

	/**
	 * Reads the location of the old storage text file.
	 * 
	 * @return              The location of the text file.
	 * @throws IOException  If I/O operations fail.
	 */
	private String readOldFilePath() throws IOException {
		return LogStorage.readLog();
	}
	
	/**
	 * Reads storage's text file to create and return a Display object.
	 * 
	 * @param filepath     The filepath of the text file.
	 * @return             A display with the user's lists of tasks.
	 * @throws IOException If I/O operations fail.
	 */
	public Display getDisplay(String filepath) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));

		ArrayList<TaskFloat> floatTasks = getFloatingTaskList(br);
		ArrayList<TaskDeadline> deadlineTasks = getDeadlineTaskList(br);
		ArrayList<TaskEvent> events = getEventTaskList(br);
		ArrayList<TaskReserved> reservedTasks = getReservedTaskList(br);
		ArrayList<Task> completedTasks = getCompletedTaskList(br);

		closeReaderClasses(br);
		setFilePath(filepath);

		Display display = new Display(EMPTY_MESSAGE, events, deadlineTasks, floatTasks, reservedTasks, completedTasks);
		return display;
	}

	/**
	 * Reads storage's text file to create and return an ArrayList of TaskFloats.
	 * 
	 * @param br           A BufferedReader that reads the text file.
	 * @return             The ArrayList of TaskFloats.
	 * @throws IOException If I/O operations fail.
	 */
	private ArrayList<TaskFloat> getFloatingTaskList(BufferedReader br) throws IOException {
		ArrayList<TaskFloat> floatTasks = new ArrayList<TaskFloat>();
		String line = null;

		br.readLine();
		readHeader(br);

		while ((line = br.readLine()) != null) {
			line = br.readLine();
			if (isInvalidLine(line)) {
				break;
			} else {
				assert line != null : "There is no floating task to read.";
				readFloatingTask(br, floatTasks, line);
			}
		}
		return floatTasks;
	}

	/**
	 * Reads storage's text file to create and return an ArrayList of TaskDeadlines.
	 * 
	 * @param br           A BufferedReader that reads the text file.
	 * @return             The ArrayList of TaskDeadlines.
	 * @throws IOException If I/O operations fail.
	 */
	private ArrayList<TaskDeadline> getDeadlineTaskList(BufferedReader br) throws IOException {
		ArrayList<TaskDeadline> deadlineTasks = new ArrayList<TaskDeadline>();
		String line = null;

		readHeader(br);

		while ((line = br.readLine()) != null) {
			line = br.readLine();
			if (isInvalidLine(line)) {
				break;
			} else {
				assert line != null : "There is no deadline task to read.";
				readDeadlineTask(br, deadlineTasks, line);
			}
		}
		return deadlineTasks;
	}

	/**
	 * Reads storage's text file to create and return an ArrayList of TaskEvents.
	 * 
	 * @param br           A BufferedReader that reads the text file.
	 * @return             The ArrayList of TaskEvents.
	 * @throws IOException If I/O operations fail.
	 */
	private ArrayList<TaskEvent> getEventTaskList(BufferedReader br) throws IOException {
		ArrayList<TaskEvent> events = new ArrayList<TaskEvent>();
		String line = null;

		readHeader(br);

		while ((line = br.readLine()) != null) {
			line = br.readLine();
			if (isInvalidLine(line)) {
				break;
			} else {
				assert line != null : "There is no event to read.";
				readEventTask(br, events, line);
			}
		}
		return events;
	}

	/**
	 * Reads storage's text file to create and return an ArrayList of TaskReserveds.
	 * 
	 * @param br           A BufferedReader that reads the text file.
	 * @return             The ArrayList of TaskReserveds.
	 * @throws IOException If I/O operations fail.
	 */
	private ArrayList<TaskReserved> getReservedTaskList(BufferedReader br) throws IOException {
		ArrayList<TaskReserved> reservedTasks = new ArrayList<TaskReserved>();
		String line = null;

		readHeader(br);

		while ((line = br.readLine()) != null) {
			line = br.readLine();
			if (isInvalidLine(line)) {
				break;
			} else {
				assert line != null : "There is no reserved task to read.";
				readReservedTask(br, reservedTasks, line);
			}
		}
		return reservedTasks;
	}

	/**
	 * Reads storage's text file to create and return an ArrayList of completed Tasks.
	 * 
	 * @param br           A BufferedReader that reads the text file.
	 * @return             The ArrayList of completed Tasks.
	 * @throws IOException If I/O operations fail.
	 */
	private ArrayList<Task> getCompletedTaskList(BufferedReader br) throws IOException {
		ArrayList<Task> completedTasks = new ArrayList<Task>();
		String line = null;

		readHeader(br);

		while ((line = br.readLine()) != null) {
			line = br.readLine();
			if (isInvalidLine(line)) {
				break;
			} else {
				assert line != null : "There is no completed task to read.";
				readCompletedTask(br, completedTasks, line);
			}
		}
		return completedTasks;
	}

	/**
	 * Reads a floating task from the text file and adds it to the ArrayList of TaskFloats.
	 * 
	 * @param br           A BufferedReader that reads the text file.
	 * @param floatTasks   The ArrayList of TaskFloats.
	 * @param line         The line of text the BufferedReader is just read.
	 * @throws IOException If I/O operations fail.
	 */
	private void readFloatingTask(BufferedReader br, ArrayList<TaskFloat> floatTasks, String line) throws IOException {
		String location = br.readLine();
		String tags = br.readLine();

		TaskFloat floatTask = processFloatingTask(line, location, tags);
		addFloatingTaskToList(floatTasks, floatTask);
	}

	
	/**
	 * Reads a deadline task from the text file and adds it to the ArrayList of TaskDeadlines.
	 * 
	 * @param br            A BufferedReader that reads the text file.
	 * @param deadlineTasks The ArrayList of TaskDeadlines.
	 * @param line          The line of text the BufferedReader is just read.
	 * @throws IOException  If I/O operations fail.
	 */
	private void readDeadlineTask(BufferedReader br, ArrayList<TaskDeadline> deadlineTasks, String line)
			throws IOException {
		String deadline = br.readLine();
		String location = br.readLine();
		String tags = br.readLine();

		TaskDeadline deadlineTask = processDeadlineTask(line, deadline, location, tags);
		addDeadlineTaskToList(deadlineTasks, deadlineTask);
	}

	/**
	 * Reads a event from the text file and adds it to the ArrayList of TaskEvents.
	 * 
	 * @param br           A BufferedReader that reads the text file.
	 * @param events       The ArrayList of TaskEvents.
	 * @param line         The line of text the BufferedReader is just read.
	 * @throws IOException If I/O operations fail.
	 */
	private void readEventTask(BufferedReader br, ArrayList<TaskEvent> events, String line) throws IOException {
		String startDate = br.readLine();
		String endDate = br.readLine();
		String location = br.readLine();
		String tags = br.readLine();

		TaskEvent eventTask = processEventTask(line, startDate, endDate, location, tags);
		addEventTaskToList(events, eventTask);
	}

	/**
	 * Reads a reserved task from the text file and adds it to the ArrayList of TaskReserveds.
	 * 
	 * @param br            A BufferedReader that reads the text file.
	 * @param reservedTasks The ArrayList of TaskReserveds.
	 * @param line          The line of text the BufferedReader is just read.
	 * @throws IOException  If I/O operations fail.
	 */
	private void readReservedTask(BufferedReader br, ArrayList<TaskReserved> reservedTasks, String line)
			throws IOException {
		String startDates = br.readLine();
		String endDates = br.readLine();
		String location = br.readLine();
		String tags = br.readLine();

		TaskReserved reservedTask = processReservedTask(line, startDates, endDates, location, tags);
		addReservedTaskToList(reservedTasks, reservedTask);
	}

	/**
	 * Reads a completed task from the text file and adds it to the ArrayList of Tasks.
	 * 
	 * @param br           A BufferedReader that reads the text file.
	 * @param Tasks   The ArrayList of Tasks.
	 * @param line         The line of text the BufferedReader is just read.
	 * @throws IOException If I/O operations fail.
	 */
	private void readCompletedTask(BufferedReader br, ArrayList<Task> completedTasks, String line) throws IOException {
		Task task = null;
		String description = line;
		line = br.readLine();
		
		if (isCompletedFloatingTask(line)) {
			task = readCompletedFloatingTask(br, line, description);
		} else if (isCompletedDeadlineTask(line)) {
			task = readCompletedDeadlineTask(br, line, description);
		} else if (isCompletedEventTask(line)) {
			task = readCompletedEventTask(br, line, description);
		} else if (isCompletedReservedTask(line)) {
			task = readCompletedReservedTask(br, line, description);
		} else {
			logger_.log(Level.WARNING, String.format(LOGGER_READ_ERROR_COMPLETED, description));
		}
		
		addCompletedTaskToList(completedTasks, task);
	}

	/**
	 * Determines if the completed task is a TaskEvent.
	 * 
	 * @param line The second line of the task in the text file.
	 * @return     True if the task is a TaskFloat. False, otherwise.
	 */
	private boolean isCompletedFloatingTask(String line) {
		return line.startsWith(ATTRIBUTE_LOCATION);
	}

	/**
	 * Determines if the completed task is a TaskDeadline.
	 * 
	 * @param line The second line of the task in the text file.
	 * @return     True if the task is a TaskDeadline. False, otherwise.
	 */
	private boolean isCompletedDeadlineTask(String line) {
		return line.startsWith(ATTRIBUTE_DEADLINE);
	}

	/**
	 * Determines if the completed task is a TaskEvent.
	 * 
	 * @param line The second line of the task in the text file.
	 * @return     True if the task is a TaskEvent. False, otherwise.
	 */
	private boolean isCompletedEventTask(String line) {
		return line.startsWith(ATTRIBUTE_START_DATE);
	}

	/**
	 * Determines if the completed task is a TaskReserved.
	 * 
	 * @param line The second line of the task in the text file.
	 * @return     True if the task is a TaskReserved. False, otherwise.
	 */
	private boolean isCompletedReservedTask(String line) {
		return line.startsWith(ATTRIBUTE_START_DATES);
	}

	/**
	 * Reads and returns a completed TaskFloat from the text file.
	 * 
	 * @param br           A BufferedReader that reads the text file.
	 * @param line         The line of text the BufferedReader is just read.
	 * @param description  The description of the completed task.
	 * @return             A completed TaskFloat.
	 * @throws IOException If I/O operations fail.
	 */
	private Task readCompletedFloatingTask(BufferedReader br, String line, String description) throws IOException {
		String tags = br.readLine();
		Task task = processFloatingTask(description, line, tags);
		return task;
	}

	/**
	 * Reads and returns a completed TaskDeadline from the text file.
	 * 
	 * @param br           A BufferedReader that reads the text file.
	 * @param line         The line of text the BufferedReader is just read.
	 * @param description  The description of the completed task.
	 * @return             A completed TaskDeadline.
	 * @throws IOException If I/O operations fail.
	 */
	private Task readCompletedDeadlineTask(BufferedReader br, String line, String description) throws IOException {
		String location = br.readLine();
		String tags = br.readLine();
		Task task = processDeadlineTask(description, line, location, tags);
		return task;
	}

	/**
	 * Reads and returns a completed TaskEvent from the text file.
	 * 
	 * @param br           A BufferedReader that reads the text file.
	 * @param line         The line of text the BufferedReader is just read.
	 * @param description  The description of the completed task.
	 * @return             A completed TaskEvent.
	 * @throws IOException If I/O operations fail.
	 */
	private Task readCompletedEventTask(BufferedReader br, String line, String description) throws IOException {
		String endDate = br.readLine();
		String location = br.readLine();
		String tags = br.readLine();
		Task task = processEventTask(description, line, endDate, location, tags);
		return task;
	}

	/**
	 * Reads and returns a completed TaskReserved from the text file.
	 * 
	 * @param br           A BufferedReader that reads the text file.
	 * @param line         The line of text the BufferedReader is just read.
	 * @param description  The description of the completed task.
	 * @return             A completed TaskReserved.
	 * @throws IOException If I/O operations fail.
	 */
	private Task readCompletedReservedTask(BufferedReader br, String line, String description) throws IOException {
		String endDates = br.readLine();
		String location = br.readLine();
		String tags = br.readLine();
		Task task = processReservedTask(description, line, endDates, location, tags);
		return task;
	}

	/**
	 * Processes the information read to create and return a TaskFloat.
	 * 
	 * @param description  The description of the TaskFloat.
	 * @param location     The location of the TaskFloat.
	 * @param tagsString   The tags of the TaskFloat as a String.
	 * @return             A TaskFloat.
	 * @throws IOException If I/O operations fail.
	 */
	private TaskFloat processFloatingTask(String description, String location, String tagsString) throws IOException {
		description = processDescription(description);
		location = processLocation(location);
		ArrayList<String> tags = processTags(tagsString);

		TaskFloat floatingTask = new TaskFloat(description, location, tags);
		return floatingTask;
	}

	/**
	 * Processes the information read to create and return a TaskDeadline.
	 * 
	 * @param description    The description of the TaskDeadline.
	 * @param deadlineString The deadline of the TaskDeadline as a String.
	 * @param location       The location of the TaskDeadline.
	 * @param tagsString     The tags of the TaskDeadline as a String.
	 * @return               A TaskDeadline.
	 * @throws IOException   If I/O operations fail.
	 */
	private TaskDeadline processDeadlineTask(String description, String deadlineString, String location,
			String tagsString) throws IOException {
		TaskDeadline deadlineTask = null;
		try {
			description = processDescription(description);
			Calendar deadline = processDate(deadlineString, ATTRIBUTE_DEADLINE);
			location = processLocation(location);
			ArrayList<String> tags = processTags(tagsString);

			deadlineTask = new TaskDeadline(description, location, deadline, tags);

		} catch (ParseException e) {
			logger_.log(Level.WARNING, LOGGER_READ_ERROR_DEADLINE);
		}
		return deadlineTask;
	}

	/**
	 * Processes the information read to create and return a TaskEvent.
	 * 
	 * @param description     The description of the TaskEvent.
	 * @param startDateString The start date of the TaskEvent as a String.
	 * @param endDateString   The end date of the TaskEvent as a String.
	 * @param location        The location of the TaskEvent.
	 * @param tagsString      The tags of the TaskEvent as a String.
	 * @return                A TaskEvent.
	 * @throws IOException    If I/O operations fail.
	 */
	private TaskEvent processEventTask(String description, String startDateString, String endDateString,
			String location, String tagsString) throws IOException {
		TaskEvent eventTask = null;
		try {
			description = processDescription(description);
			Calendar startDate = processDate(startDateString, ATTRIBUTE_START_DATE);
			Calendar endDate = processDate(endDateString, ATTRIBUTE_END_DATE);
			location = processLocation(location);
			ArrayList<String> tags = processTags(tagsString);

			eventTask = new TaskEvent(description, location, startDate, endDate, tags);

		} catch (ParseException e) {
			logger_.log(Level.WARNING, LOGGER_READ_ERROR_EVENT);
		}
		return eventTask;
	}

	/**
	 * Processes the information read to create and return a TaskReserved.
	 * 
	 * @param description      The description of the TaskReserved.
	 * @param startDatesString The start dates of the TaskReserved as a String.
	 * @param endDatesString   The end dates of the TaskReserved as a String.
	 * @param location         The location of the TaskReserved.
	 * @param tagsString       The tags of the TaskReserved as a String.
	 * @return                 A TsakReserved.
	 * @throws IOException     If I/O operations fail.
	 */
	private TaskReserved processReservedTask(String description, String startDatesString, String endDatesString,
			String location, String tagsString) throws IOException {
		TaskReserved reservedTask = null;
		try {
			description = processDescription(description);
			ArrayList<Calendar> startDates = processDates(startDatesString, ATTRIBUTE_START_DATES);
			ArrayList<Calendar> endDates = processDates(endDatesString, ATTRIBUTE_END_DATES);
			location = processLocation(location);
			ArrayList<String> tags = processTags(tagsString);

			reservedTask = new TaskReserved(description, location, startDates, endDates, tags);

		} catch (ParseException e) {
			logger_.log(Level.WARNING, LOGGER_READ_ERROR_RESERVED);
		}
		return reservedTask;
	}

	/**
	 * Processes and removes the header from a line containing a tasks's description.
	 * 
	 * @param description The line containing a task's description.
	 * @return            The description of a task.
	 */
	private String processDescription(String description) {
		if (description.startsWith(ATTRIBUTE_DESCRIPTION)) {
			description = description.replaceFirst(ATTRIBUTE_DESCRIPTION, "").trim();
			if (description.isEmpty()) {
				description = EMPTY_DESCRIPTION;
			}
		}
		return description;
	}

	/**
	 * Processes and removes the header from a line containing a tasks's location.
	 * 
	 * @param location The line containing a task's location.
	 * @return         The location of a task.
	 */
	private String processLocation(String location) {
		if (isCompletedFloatingTask(location)) {
			location = location.replaceFirst(ATTRIBUTE_LOCATION, "").trim();
			if (location.isEmpty()) {
				location = null;
			}
		}
		return location;
	}

	/**
	 * Processes and removes the header from a line containing a task's tags.
	 * 
	 * @param tagsString A task's tags as a String.
	 * @return           A task's tags as an ArrayList.
	 */
	private ArrayList<String> processTags(String tagsString) {
		ArrayList<String> tags = null;
		if (tagsString.startsWith(ATTRIBUTE_TAGS)) {
			tags = new ArrayList<String>(Arrays.asList(tagsString.trim().split("\\s*#\\s*")));
			tags.remove(0);
		}
		return tags;
	}

	/**
	 * Processes and removes the header from a line containing a task's date.
	 * @param dateString      A task's date as a String.
	 * @param dateType        The type of date being processed (deadline, start date, or end date).
	 * @return                The date used in the Task as a Calendar.
	 * @throws ParseException If the Calendar is unable to parse the date in the text file.
	 */
	private Calendar processDate(String dateString, String dateType) throws ParseException {
		Calendar date = null;
		if (dateString.startsWith(dateType)) {
			date = Calendar.getInstance();
			date.setTime(sdf_.parse(dateString.replaceFirst(dateType, "").trim()));
		}
		return date;
	}

	/**
	 * Processes and removes the header from a line containing a reserved task's dates.
	 * 
	 * @param datesString     The dates of a reserved task as a String.
	 * @param dateType        The type of dates being processed (start dates or end dates).
	 * @return                The dates of the reserved task as a Calendar.
	 * @throws ParseException If the Calendar is unable to parse the date in the text file.
	 */
	private ArrayList<Calendar> processDates(String datesString, String dateType) throws ParseException {
		ArrayList<Calendar> dates = new ArrayList<Calendar>();

		if (datesString.startsWith(dateType)) {
			ArrayList<String> datesList = new ArrayList<String>(
					Arrays.asList(datesString.replaceFirst(dateType, "").trim().split("\\s*,\\s*")));

			for (String dateString : datesList) {
				Calendar date = Calendar.getInstance();
				date.setTime(sdf_.parse(dateString));
				dates.add(date);
			}
		}
		return dates;
	}

	/**
	 * Adds the TaskFloat to the ArrayList of TaskFloats if it is not null.
	 * 
	 * @param floatTasks The ArrayList of TaskFloats.
	 * @param floatTask  A TaskFloat.
	 */
	private void addFloatingTaskToList(ArrayList<TaskFloat> floatTasks, TaskFloat floatTask) {
		if (floatTask != null) {
			floatTasks.add(floatTask);
			logger_.log(Level.INFO, String.format(LOGGER_READ_FLOATING, floatTask.getDescription()));
		}
	}

	/**
	 * Adds a TaskDeadline to the ArrayList of TaskDeadlines if it is not null.
	 * 
	 * @param deadlineTasks The ArrayList of TaskDeadlines.
	 * @param deadlineTask  A TaskDeadline.
	 */
	private void addDeadlineTaskToList(ArrayList<TaskDeadline> deadlineTasks, TaskDeadline deadlineTask) {
		if (deadlineTask != null) {
			deadlineTasks.add(deadlineTask);
			logger_.log(Level.INFO, String.format(LOGGER_READ_DEADLINE, deadlineTask.getDescription()));
		}
	}

	/**
	 * Adds a TaskEvent to the ArrayList of TaskEvents if it is not null.
	 * 
	 * @param events    The ArrayList of TaskEvents.
	 * @param eventTask A TaskEvent.
	 */
	private void addEventTaskToList(ArrayList<TaskEvent> events, TaskEvent eventTask) {
		if (eventTask != null) {
			events.add(eventTask);
			logger_.log(Level.INFO, String.format(LOGGER_READ_EVENT, eventTask.getDescription()));
		}
	}

	/**
	 * Adds a TaskReserved to the ArrayList of TaskReserveds if it is not null.
	 * 
	 * @param reservedTasks The ArrayList of TaskReserveds.
	 * @param reservedTask  A TaskReserved.
	 */
	private void addReservedTaskToList(ArrayList<TaskReserved> reservedTasks, TaskReserved reservedTask) {
		if (reservedTask != null) {
			reservedTasks.add(reservedTask);
			logger_.log(Level.INFO, String.format(LOGGER_READ_RESERVED, reservedTask.getDescription()));
		}
	}

	/**
	 * Adds a completed Task if to the ArrayList of completed Tasks if it is not null.
	 * @param completedTasks The ArrayList of completed Tasks.
	 * @param task           A completed Task.
	 */
	private void addCompletedTaskToList(ArrayList<Task> completedTasks, Task task) {
		if (task != null) {
			completedTasks.add(task);
			logger_.log(Level.INFO, String.format(LOGGER_READ_COMPLETED, task.getDescription()));
		}
	}

	/**
	 * Reads the header of storage's text file.
	 * @param br           A BufferedReader that reads the text file. 
	 * @throws IOException If I/O operations fail.
	 */
	private void readHeader(BufferedReader br) throws IOException {
		br.readLine();
		br.readLine();
	}

	/**
	 * Checks to see if a line is invalid denoted by a null line or a divider.
	 * 
	 * @param line The line just read from the text file.
	 * @return     True if the line is invalid. False, otherwise.
	 */
	private boolean isInvalidLine(String line) {
		return line == null || line.equals(HEADER_DIVIDER);
	}

	/**
	 * Closes the BufferedReader.
	 * 
	 * @param br           The BufferedReader that read the text file.
	 * @throws IOException If I/O operations.
	 */
	private void closeReaderClasses(BufferedReader br) throws IOException {
		br.close();
	}

	/**
	 * Sets the global filePath variable to remember where the storage file is.
	 * @param filepath The file path of storage's text file.
	 */
	private void setFilePath(String filepath) {
		filePath_ = filepath;
	}

	/**
	 * Saves the given display into storage's text file.
	 * 
	 * @param thisDisplay  A Display containing all of a user's tasks.
	 * @throws IOException If I/O operations fail.
	 */
	public void saveFile(Display thisDisplay) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath_)));

		ArrayList<TaskFloat> floatTasks = thisDisplay.getFloatTasks();
		ArrayList<TaskDeadline> deadlineTasks = thisDisplay.getDeadlineTasks();
		ArrayList<TaskEvent> events = thisDisplay.getEventTasks();
		ArrayList<TaskReserved> reservedTasks = thisDisplay.getReservedTasks();
		ArrayList<Task> completedTasks = thisDisplay.getCompletedTasks();

		writeFloatingTasks(bw, floatTasks);
		writeDeadlineTasks(bw, deadlineTasks);
		writeEventTasks(bw, events);
		writeReservedTasks(bw, reservedTasks);
		writeCompletedTasks(bw, completedTasks);

		closeWriterClasses(bw);
	}

	/**
	 * Writes a header denoting the types of tasks into the text file.
	 * 
	 * @param bw           The BufferedWriter that writes to the text file.
	 * @param header       The header containing the type of tasks.
	 * @throws IOException If I/O operations fail.
	 */
	private void writeHeaderToFile(BufferedWriter bw, String header) throws IOException {
		bw.write(HEADER_DIVIDER);
		bw.newLine();
		bw.write(header);
		bw.newLine();
		bw.write(HEADER_DIVIDER);
		bw.newLine();
		bw.newLine();
	}

	/**
	 * Write the user's TaskFloats to storage's text file.
	 * 
	 * @param bw           The BufferedWriter that writes to the text file.
	 * @param floatTasks   An ArrayList of user's TaskFloats.
	 * @throws IOException If I/O operations fail.
	 */
	private void writeFloatingTasks(BufferedWriter bw, ArrayList<TaskFloat> floatTasks) throws IOException {
		writeHeaderToFile(bw, HEADER_FLOATING);
		for (TaskFloat task : floatTasks) {
			bw.write(task.toString());
			logger_.log(Level.INFO, String.format(LOGGER_WRITE_FLOATING, task.getDescription()));
		}
	}

	/**
	 * Write the user's TaskDeadlines to storage's text file.
	 * 
	 * @param bw            The BufferedWriter that writes to the text file.
	 * @param deadlineTasks An ArrayList of user's TaskDeadlines.
	 * @throws IOException  If I/O operations fail.
	 */
	private void writeDeadlineTasks(BufferedWriter bw, ArrayList<TaskDeadline> deadlineTasks) throws IOException {
		writeHeaderToFile(bw, HEADER_DEADLINE);
		for (TaskDeadline task : deadlineTasks) {
			bw.write(task.toString());
			logger_.log(Level.INFO, String.format(LOGGER_WRITE_DEADLINE, task.getDescription()));
		}
	}

	/**
	 * Write the user's TaskEvents to storage's text file.
	 * 
	 * @param bw           The BufferedWriter that writes to the text file.
	 * @param events       An ArrayList of user's TaskEvents.
	 * @throws IOException If I/O operations fail.
	 */
	private void writeEventTasks(BufferedWriter bw, ArrayList<TaskEvent> events) throws IOException {
		writeHeaderToFile(bw, HEADER_EVENT);
		for (TaskEvent event : events) {
			bw.write(event.toString());
			logger_.log(Level.INFO, String.format(LOGGER_WRITE_EVENT, event.getDescription()));
		}
	}

	/**
	 * Write the user's TaskReserveds to storage's text file.
	 * 
	 * @param bw            The BufferedWriter that writes to the text file.
	 * @param reservedTasks An ArrayList of user's TaskReserveds.
	 * @throws IOException  If I/O operations fail.
	 */
	private void writeReservedTasks(BufferedWriter bw, ArrayList<TaskReserved> reservedTasks) throws IOException {
		writeHeaderToFile(bw, HEADER_RESERVED);
		for (TaskReserved task : reservedTasks) {
			bw.write(task.toString());
			logger_.log(Level.INFO, String.format(LOGGER_WRITE_RESERVED, task.getDescription()));
		}
	}

	/**
	 * Write the user's completed Tasks to storage's text file.
	 * 
	 * @param bw            The BufferedWriter that writes to the text file.
	 * @param deadlineTasks An ArrayList of user's completed Tasks.
	 * @throws IOException  If I/O operations fail.
	 */
	private void writeCompletedTasks(BufferedWriter bw, ArrayList<Task> completedTasks) throws IOException {
		writeHeaderToFile(bw, HEADER_COMPLETED);
		for (Task task : completedTasks) {
			bw.write(task.toString());
			logger_.log(Level.INFO, String.format(LOGGER_WRITE_COMPLETED, task.getDescription()));
		}
	}

	/**
	 * Flushes and closes the BufferedWriter.
	 *  
	 * @param bw           The BufferedWriter that wrote to the text file.
	 * @throws IOException If I/O operations fail.
	 */
	private void closeWriterClasses(BufferedWriter bw) throws IOException {
		bw.flush();
		bw.close();
	}

}
