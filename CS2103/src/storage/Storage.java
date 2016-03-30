//@@author Chloe Odquier Fortuna (A0149063E)
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
	private Logger logger = Logger.getLogger(Storage.class.getName());

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
		FileHandler handler = createLogHandler();
		logger.log(Level.INFO, "Reading all tasks from file.\r\n");

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));

		ArrayList<TaskFloat> floatTasks = new ArrayList<TaskFloat>();
		ArrayList<TaskDeadline> deadlineTasks = new ArrayList<TaskDeadline>();
		ArrayList<TaskEvent> events = new ArrayList<TaskEvent>();
		ArrayList<TaskReserved> reservedTasks = new ArrayList<TaskReserved>();
		ArrayList<Task> completedTasks = new ArrayList<Task>();

		try {
			br.readLine();
			readTasksFloating(br, floatTasks);
			readTasksDeadline(br, deadlineTasks);
			readTasksEvents(br, events);
			readTasksReserved(br, reservedTasks);
			readTasksCompleted(br, completedTasks);

		} catch (IOException ioe) {
			throw ioe;

		} catch (Exception e) {
			System.out.println("There was an error in reading your file.");
			e.printStackTrace();
		}

		closeReaderClasses(handler, br);
		setFilePath(filepath);

		Display display = new Display(MESSAGE_EMPTY, events, deadlineTasks, floatTasks, reservedTasks, completedTasks);
		return display;
	}

	private void readTasksFloating(BufferedReader br, ArrayList<TaskFloat> floatTasks) throws IOException {
		String line = null;
		String description = null;
		String location = null;
		ArrayList<String> tags = null;

		try {
			readHeader(br);

			while ((line = br.readLine()) != null) {
				line = br.readLine();
				if (isInvalidLine(line)) {
					break;
				} else {
					logger.log(Level.INFO, "Reading new floating task.\r\n");
					description = readDescription(line);
					location = readLocation(br);
					tags = readTags(br);

					TaskFloat floatTask = new TaskFloat(description, location, tags);
					floatTasks.add(floatTask);
					logger.log(Level.INFO, "Successfully read: " + floatTask.getDescription() + "\r\n");
				}
			}
		} catch (IOException ioe) {
			logger.log(Level.WARNING, "IOException: Could not read floating task.\r\n");
			throw ioe;

		} catch (Exception e) {
			logger.log(Level.WARNING, "Could not read floating task.\r\n");
			e.printStackTrace();
		}
	}

	private void readTasksDeadline(BufferedReader br, ArrayList<TaskDeadline> deadlineTasks) throws IOException {
		String line = null;
		String description = null;
		Calendar deadline = null;
		String location = null;
		ArrayList<String> tags = null;

		readHeader(br);

		while ((line = br.readLine()) != null) {
			line = br.readLine();
			if (isInvalidLine(line)) {
				break;
			} else {
				logger.log(Level.INFO, "Reading new deadline task.\r\n");
				description = readDescription(line);
				try {
					deadline = readDate(br, ATTRIBUTE_DEADLINE);
				} catch (ParseException e) {
					logger.log(Level.WARNING, description + " doesn't have a deadline and can't be read.\r\n");
					// br.readLine();
					// br.readLine();
					handleInvalidDate(ATTRIBUTE_DEADLINE, br);
					continue;
				}
				location = readLocation(br);
				tags = readTags(br);

				TaskDeadline deadlineTask = new TaskDeadline(description, location, deadline, tags);
				deadlineTasks.add(deadlineTask);
				logger.log(Level.INFO, "Successfully read: " + deadlineTask.getDescription() + "\r\n");
			}
		}
	}

	private void readTasksEvents(BufferedReader br, ArrayList<TaskEvent> events) throws IOException {
		String line;
		String description = null;
		Calendar startDate = null;
		Calendar endDate = null;
		String location = null;
		ArrayList<String> tags = null;

		readHeader(br);
		while ((line = br.readLine()) != null) {
			line = br.readLine();
			if (isInvalidLine(line)) {
				break;
			} else {
				logger.log(Level.INFO, "Reading new event.\r\n");
				description = readDescription(line);
				try {
					startDate = readDate(br, ATTRIBUTE_START_DATE);
				} catch (ParseException e) {
					logger.log(Level.WARNING, description + " doesn't have a start date and can't be read.\r\n");
					handleInvalidDate(ATTRIBUTE_START_DATE, br);
					continue;
				}
				try {
					endDate = readDate(br, ATTRIBUTE_END_DATE);
				} catch (ParseException e) {
					logger.log(Level.WARNING, description + " doesn't have an end date and can't be read.\r\n");
					handleInvalidDate(ATTRIBUTE_END_DATE, br);
					continue;
				}
				location = readLocation(br);
				tags = readTags(br);

				TaskEvent eventTask = new TaskEvent(description, location, startDate, endDate, tags);
				events.add(eventTask);
				logger.log(Level.INFO, "Successfully read: " + eventTask.getDescription() + "\r\n");
			}
		}
	}

	private void readTasksReserved(BufferedReader br, ArrayList<TaskReserved> reservedTasks) throws IOException {
		String line = null;
		String description = null;
		ArrayList<Calendar> startDates = null;
		ArrayList<Calendar> endDates = null;
		String location = null;
		ArrayList<String> tags = null;

		readHeader(br);

		while ((line = br.readLine()) != null) {
			line = br.readLine();
			if (isInvalidLine(line)) {
				break;
			} else {
				logger.log(Level.INFO, "Reading new reserved task.\r\n");
				description = readDescription(line);

				try {
					startDates = readDates(br, ATTRIBUTE_START_DATES);
				} catch (ParseException e) {
					handleInvalidDate(ATTRIBUTE_START_DATE, br);
					continue;
				}

				try {
					endDates = readDates(br, ATTRIBUTE_END_DATES);
				} catch (ParseException e) {
					handleInvalidDate(ATTRIBUTE_END_DATE, br);
					continue;
				}
				location = readLocation(br);
				tags = readTags(br);

				TaskReserved reservedTask = new TaskReserved(description, location, startDates, endDates, tags);
				reservedTasks.add(reservedTask);
				logger.log(Level.INFO, "Successfully read: " + reservedTask.getDescription() + "\r\n");
			}
		}
	}

	private void readTasksCompleted(BufferedReader br, ArrayList<Task> completedTasks) throws IOException {
		String line;
		String description = null;
		String location = null;
		ArrayList<String> tags = null;

		try {
			readHeader(br);
			while ((line = br.readLine()) != null) {
				line = br.readLine();
				if (isInvalidLine(line)) {
					break;
				} else {
					logger.log(Level.INFO, "Reading new completed task.\r\n");
					description = readDescription(line);
					location = readLocation(br);
					tags = readTags(br);

					Task completedTask = new Task(description, location, tags);
					completedTasks.add(completedTask);
					logger.log(Level.INFO, "Successfully read: " + completedTask.getDescription() + "\r\n");
				}
			}
		} catch (IOException ioe) {
			logger.log(Level.WARNING, "Could not read completed task.\r\n");
			throw ioe;

		} catch (Exception e) {
			System.out.println("There was an error in reading the completed tasks.");
			logger.log(Level.WARNING, "Could not read completed task.\r\n");
			e.printStackTrace();
		}
	}

	private FileHandler createLogHandler() throws IOException {
		FileHandler handler = new FileHandler("logs\\log.txt");
		handler.setFormatter(new SimpleFormatter());
		logger.addHandler(handler);
		return handler;
	}

	private void readHeader(BufferedReader br) throws IOException {
		br.readLine();
		br.readLine();
	}

	private String readDescription(String line) {
		String description = null;
		if (line.startsWith(ATTRIBUTE_DESCRIPTION)) {
			description = line.replaceFirst(ATTRIBUTE_DESCRIPTION, "").trim();
			if (description.isEmpty()) {
				description = "undefined";
			}
		}
		return description;
	}

	private String readLocation(BufferedReader br) throws IOException {
		String location = null;
		String line = br.readLine();
		if (line.startsWith(ATTRIBUTE_LOCATION)) {
			location = line.replaceFirst(ATTRIBUTE_LOCATION, "").trim();
			if (location.isEmpty()) {
				location = null;
			}
		}
		return location;
	}

	private ArrayList<String> readTags(BufferedReader br) throws IOException {
		ArrayList<String> tags = null;
		String line = br.readLine();
		if (line.startsWith(ATTRIBUTE_TAGS)) {
			tags = new ArrayList<String>(Arrays.asList(line.trim().split("\\s*#\\s*")));
			tags.remove(0);
		}
		return tags;
	}

	private Calendar readDate(BufferedReader br, String dateType) throws IOException, ParseException {
		Calendar date = null;
		String line = br.readLine();
		if (line.startsWith(dateType)) {
			date = Calendar.getInstance();
			date.setTime(sdf.parse(line.replaceFirst(dateType, "").trim()));
		}
		return date;
	}

	private ArrayList<Calendar> readDates(BufferedReader br, String dateType) throws IOException, ParseException {
		ArrayList<Calendar> dates = new ArrayList<Calendar>();
		String line = br.readLine();

		if (line.startsWith(dateType)) {
			ArrayList<String> datesString = new ArrayList<String>(
					Arrays.asList(line.replaceFirst(dateType, "").trim().split("\\s*,\\s*")));

			for (String dateString : datesString) {
				Calendar date = Calendar.getInstance();
				date.setTime(sdf.parse(dateString));
				dates.add(date);
			}
		}
		return dates;
	}

	private boolean isInvalidLine(String line) {
		return line == null || line.equals(HEADER_DIVIDER);
	}

	private void handleInvalidDate(String dateType, BufferedReader br) throws IOException {
		if (dateType.equals(ATTRIBUTE_START_DATE)) {
			br.readLine();
			br.readLine();
			br.readLine();
		} else if (dateType.equals(ATTRIBUTE_END_DATE) || dateType.equals(ATTRIBUTE_DEADLINE)) {
			br.readLine();
			br.readLine();
		}
	}

	private void closeReaderClasses(FileHandler handler, BufferedReader br) throws IOException {
		br.close();
		handler.close();
	}

	public void saveFile(Display thisDisplay) throws IOException {
		FileHandler handler = createLogHandler();

		logger.log(Level.INFO, "Writing all tasks to file.\r\n");
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

		closeWriterClasses(handler, bw);
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
			logger.log(Level.INFO, "Writing " + task.getDescription() + " to file.\r\n");
			bw.write(task.toString());
		}
	}

	private void writeDeadlineTasks(BufferedWriter bw, ArrayList<TaskDeadline> deadlineTasks) throws IOException {
		writeHeaderToFile(bw, HEADER_DEADLINE);
		for (TaskDeadline task : deadlineTasks) {
			logger.log(Level.INFO, "Writing " + task.getDescription() + " to file.\r\n");
			bw.write(task.toString());
		}
	}

	private void writeEventTasks(BufferedWriter bw, ArrayList<TaskEvent> events) throws IOException {
		writeHeaderToFile(bw, HEADER_EVENT);
		for (TaskEvent event : events) {
			logger.log(Level.INFO, "Writing " + event.getDescription() + " to file.\r\n");
			bw.write(event.toString());
		}
	}

	private void writeReservedTasks(BufferedWriter bw, ArrayList<TaskReserved> reservedTasks) throws IOException {
		writeHeaderToFile(bw, HEADER_RESERVED);
		for (TaskReserved task : reservedTasks) {
			logger.log(Level.INFO, "Writing " + task.getDescription() + " to file.\r\n");
			bw.write(task.toString());
		}
	}

	private void writeCompletedTasks(BufferedWriter bw, ArrayList<Task> completedTasks) throws IOException {
		writeHeaderToFile(bw, HEADER_COMPLETED);
		for (Task task : completedTasks) {
			logger.log(Level.INFO, "Writing " + task.getDescription() + " to file.\r\n");
			bw.write(task.toString());
		}
	}

	private void closeWriterClasses(FileHandler handler, BufferedWriter bw) throws IOException {
		bw.flush();
		bw.close();
		handler.close();
	}

}
