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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import bean.Display;
import bean.GlobalLogger;
import bean.Task;
import bean.TaskDeadline;
import bean.TaskEvent;
import bean.TaskFloat;
import bean.TaskReserved;

public class Storage {

	public static String filePath;
	private static Storage storageInstance;

	private static final String HEADER_FLOATING = "     TASKS";
	private static final String HEADER_DEADLINE = "   DEADLINES";
	private static final String HEADER_EVENT = "    EVENTS";
	private static final String HEADER_RESERVED = "   RESERVED";
	private static final String HEADER_COMPLETED = "   COMPLETED";
	private static final String HEADER_DIVIDER = "===============";

	private static final String ATTRIBUTE_DESCRIPTION = "Description:";
	private static final String ATTRIBUTE_LOCATION = "Location:";
	private static final String ATTRIBUTE_TAGS = "Tags:";
	private static final String ATTRIBUTE_DEADLINE = "Deadline:";
	private static final String ATTRIBUTE_START_DATE = "Start Date:";
	private static final String ATTRIBUTE_END_DATE = "End Date:";
	private static final String ATTRIBUTE_START_DATES = "Start Dates:";
	private static final String ATTRIBUTE_END_DATES = "End Dates:";

	private static final String MESSAGE_EMPTY = "";

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
	private Logger logger = GlobalLogger.getLogger();

	public static Storage getInstance() {
		if (storageInstance == null) {
			return new Storage();
		}
		return storageInstance;
	}

	// private void initializeFilePath() throws IOException {
	// filePath = LogStorage.readLogFile();
	// }

	private void setFilePath(String filepath) {
		filePath = filepath;
	}

	public void createFile(String filepath) throws IOException {
		File file = new File(filepath);
		if (!file.exists()) {
			file.createNewFile();
		}
		setFilePath(filepath);
	}

	public Display getDisplay(String filepath) throws IOException {
		//FileHandler handler = createLogHandler();

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));

		br.readLine();
		ArrayList<TaskFloat> floatTasks = readTasksFloating(br);
		ArrayList<TaskDeadline> deadlineTasks = readTasksDeadline(br);
		ArrayList<TaskEvent> events = readTasksEvents(br);
		ArrayList<TaskReserved> reservedTasks = readTasksReserved(br);
		ArrayList<Task> completedTasks = readTasksCompleted(br);

		br.close();
		//GlobalLogger.closeHandler();
		//closeReaderClasses(handler, br);
		setFilePath(filepath);

		Display display = new Display(MESSAGE_EMPTY, events, deadlineTasks, floatTasks, reservedTasks, completedTasks);
		return display;
	}

	private ArrayList<TaskFloat> readTasksFloating(BufferedReader br) throws IOException {
		ArrayList<TaskFloat> floatTasks = new ArrayList<TaskFloat>();
		String line = null;
		readHeader(br);

		while ((line = br.readLine()) != null) {
			line = br.readLine();
			if (isInvalidLine(line)) {
				break;
			} else {
				String location = br.readLine();
				String tags = br.readLine();

				TaskFloat floatTask = processFloatingTask(line, location, tags);
				floatTasks.add(floatTask);
				logger.log(Level.INFO, "Successfully read floating task: " + floatTask.getDescription());
			}
		}
		return floatTasks;
	}

	private ArrayList<TaskDeadline> readTasksDeadline(BufferedReader br) throws IOException {
		ArrayList<TaskDeadline> deadlineTasks = new ArrayList<TaskDeadline>();
		String line = null;

		readHeader(br);

		while ((line = br.readLine()) != null) {
			line = br.readLine();
			if (isInvalidLine(line)) {
				break;
			} else {
				String deadline = br.readLine();
				String location = br.readLine();
				String tags = br.readLine();

				TaskDeadline deadlineTask = processDeadlineTask(line, deadline, location, tags);

				if (deadlineTask != null) {
					deadlineTasks.add(deadlineTask);
					logger.log(Level.INFO,
							"Successfully read deadline task: " + deadlineTask.getDescription());
				}
			}
		}
		return deadlineTasks;
	}

	private ArrayList<TaskEvent> readTasksEvents(BufferedReader br) throws IOException {
		ArrayList<TaskEvent> events = new ArrayList<TaskEvent>();
		String line = null;

		readHeader(br);

		while ((line = br.readLine()) != null) {
			line = br.readLine();
			if (isInvalidLine(line)) {
				break;
			} else {
				String startDate = br.readLine();
				String endDate = br.readLine();
				String location = br.readLine();
				String tags = br.readLine();

				TaskEvent eventTask = processEventTask(line, startDate, endDate, location, tags);

				if (eventTask != null) {
					events.add(eventTask);
					logger.log(Level.INFO, "Successfully read event task: " + eventTask.getDescription());
				}
			}
		}
		return events;
	}

	private ArrayList<TaskReserved> readTasksReserved(BufferedReader br) throws IOException {
		ArrayList<TaskReserved> reservedTasks = new ArrayList<TaskReserved>();
		String line = null;

		readHeader(br);

		while ((line = br.readLine()) != null) {
			line = br.readLine();
			if (isInvalidLine(line)) {
				break;
			} else {
				String startDates = br.readLine();
				String endDates = br.readLine();
				String location = br.readLine();
				String tags = br.readLine();

				TaskReserved reservedTask = processReservedTask(line, startDates, endDates, location, tags);

				if (reservedTask != null) {
					reservedTasks.add(reservedTask);
					logger.log(Level.INFO,
							"Successfully read reserved task: " + reservedTask.getDescription());
				}
			}
		}
		return reservedTasks;
	}

	private ArrayList<Task> readTasksCompleted(BufferedReader br) throws IOException {
		ArrayList<Task> completedTasks = new ArrayList<Task>();
		String line = null;

		readHeader(br);

		while ((line = br.readLine()) != null) {
			line = br.readLine();
			if (isInvalidLine(line)) {
				break;
			} else {
				Task task = null;
				String description = line;

				line = br.readLine();
				if (line.startsWith(ATTRIBUTE_LOCATION)) {
					String tags = br.readLine();
					task = processFloatingTask(description, line, tags);
				} else if (line.startsWith(ATTRIBUTE_DEADLINE)) {
					String location = br.readLine();
					String tags = br.readLine();
					task = processDeadlineTask(description, line, location, tags);
				} else if (line.startsWith(ATTRIBUTE_START_DATE)) {
					String endDate = br.readLine();
					String location = br.readLine();
					String tags = br.readLine();
					task = processEventTask(description, line, endDate, location, tags);
				} else if (line.startsWith(ATTRIBUTE_START_DATES)) {
					String endDates = br.readLine();
					String location = br.readLine();
					String tags = br.readLine();
					task = processReservedTask(description, line, endDates, location, tags);
				} else {
					logger.log(Level.WARNING, "Could not read completed task: " + description);
				}

				if (task != null) {
					completedTasks.add(task);
				}
			}
		}
		return completedTasks;
	}

	private TaskFloat processFloatingTask(String description, String location, String tagsString) throws IOException {
		description = processDescription(description);
		location = processLocation(location);
		ArrayList<String> tags = processTags(tagsString);

		TaskFloat floatingTask = new TaskFloat(description, location, tags);
		return floatingTask;
	}

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
			logger.log(Level.WARNING, "Deadline task has invalid deadline and can't be read.");
		}
		return deadlineTask;
	}

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
			logger.log(Level.WARNING, "Event task has invalid dates and can't be read.");
		}
		return eventTask;
	}

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
			logger.log(Level.WARNING, "Reserved task has invalid dates and can't be read.");
		}
		return reservedTask;
	}

	private String processDescription(String description) {
		if (description.startsWith(ATTRIBUTE_DESCRIPTION)) {
			description = description.replaceFirst(ATTRIBUTE_DESCRIPTION, "").trim();
			if (description.isEmpty()) {
				description = "undefined";
			}
		}
		return description;
	}

	private String processLocation(String location) throws IOException {
		if (location.startsWith(ATTRIBUTE_LOCATION)) {
			location = location.replaceFirst(ATTRIBUTE_LOCATION, "").trim();
			if (location.isEmpty()) {
				location = null;
			}
		}
		return location;
	}

	private ArrayList<String> processTags(String tagsString) throws IOException {
		ArrayList<String> tags = null;
		if (tagsString.startsWith(ATTRIBUTE_TAGS)) {
			tags = new ArrayList<String>(Arrays.asList(tagsString.trim().split("\\s*#\\s*")));
			tags.remove(0);
		}
		return tags;
	}

	private Calendar processDate(String dateString, String dateType) throws IOException, ParseException {
		Calendar date = null;
		if (dateString.startsWith(dateType)) {
			date = Calendar.getInstance();
			date.setTime(sdf.parse(dateString.replaceFirst(dateType, "").trim()));
		}
		return date;
	}

	private ArrayList<Calendar> processDates(String datesString, String dateType) throws IOException, ParseException {
		ArrayList<Calendar> dates = new ArrayList<Calendar>();

		if (datesString.startsWith(dateType)) {
			ArrayList<String> datesList = new ArrayList<String>(
					Arrays.asList(datesString.replaceFirst(dateType, "").trim().split("\\s*,\\s*")));

			for (String dateString : datesList) {
				Calendar date = Calendar.getInstance();
				date.setTime(sdf.parse(dateString));
				dates.add(date);
			}
		}
		return dates;
	}

	private FileHandler createLogHandler() throws IOException {
		FileHandler handler = new FileHandler("log.txt", true);
		handler.setFormatter(new SimpleFormatter());
		logger.addHandler(handler);
		return handler;
	}

	private void readHeader(BufferedReader br) throws IOException {
		br.readLine();
		br.readLine();
	}

	private boolean isInvalidLine(String line) {
		return line == null || line.equals(HEADER_DIVIDER);
	}

	private void closeReaderClasses(FileHandler handler, BufferedReader br) throws IOException {
		br.close();
		handler.close();
	}

	public void saveFile(Display thisDisplay) throws IOException {
		//FileHandler handler = createLogHandler();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));

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

		bw.flush();
		bw.close();
	//	GlobalLogger.closeHandler();
		//closeWriterClasses(handler, bw);
	}

	private void writeHeaderToFile(BufferedWriter bw, String header) throws IOException {
		bw.write(HEADER_DIVIDER);
		bw.newLine();
		bw.write(header);
		bw.newLine();
		bw.write(HEADER_DIVIDER);
		bw.newLine();
		bw.newLine();
	}

	private void writeFloatingTasks(BufferedWriter bw, ArrayList<TaskFloat> floatTasks) throws IOException {
		writeHeaderToFile(bw, HEADER_FLOATING);
		for (TaskFloat task : floatTasks) {
			logger.log(Level.INFO, "Writing " + task.getDescription() + " to file.");
			bw.write(task.toString());
		}
	}

	private void writeDeadlineTasks(BufferedWriter bw, ArrayList<TaskDeadline> deadlineTasks) throws IOException {
		writeHeaderToFile(bw, HEADER_DEADLINE);
		for (TaskDeadline task : deadlineTasks) {
			logger.log(Level.INFO, "Writing " + task.getDescription() + " to file.");
			bw.write(task.toString());
		}
	}

	private void writeEventTasks(BufferedWriter bw, ArrayList<TaskEvent> events) throws IOException {
		writeHeaderToFile(bw, HEADER_EVENT);
		for (TaskEvent event : events) {
			logger.log(Level.INFO, "Writing " + event.getDescription() + " to file.");
			bw.write(event.toString());
		}
	}

	private void writeReservedTasks(BufferedWriter bw, ArrayList<TaskReserved> reservedTasks) throws IOException {
		writeHeaderToFile(bw, HEADER_RESERVED);
		for (TaskReserved task : reservedTasks) {
			logger.log(Level.INFO, "Writing " + task.getDescription() + " to file.");
			bw.write(task.toString());
		}
	}

	private void writeCompletedTasks(BufferedWriter bw, ArrayList<Task> completedTasks) throws IOException {
		writeHeaderToFile(bw, HEADER_COMPLETED);
		for (Task task : completedTasks) {
			logger.log(Level.INFO, "Writing " + task.getDescription() + " to file.");
			bw.write(task.toString());
		}
	}

	private void closeWriterClasses(FileHandler handler, BufferedWriter bw) throws IOException {
		bw.flush();
		bw.close();
		handler.close();
	}

}