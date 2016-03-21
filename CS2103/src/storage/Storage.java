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

import com.google.gson.Gson;

import bean.Display;
import bean.Task;
import bean.TaskDeadline;
import bean.TaskEvent;
import bean.TaskFloat;
import bean.TaskReserved;

public class Storage {

	private static String filePath;
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
	private static final String ATTRIBUTE_START_DATES = "Start Dates: ";
	private static final String ATTRIBUTE_END_DATES = "End Dates: ";

	private static final String MESSAGE_EMPTY = "";

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
	private static Logger logger = Logger.getLogger(Storage.class.getName());
	static Gson gson = new Gson();

	// public static void main(String[] args) throws IOException, ParseException
	// {
	// FileHandler handler = new FileHandler("storagelogfile.txt");
	// handler.setFormatter(new SimpleFormatter());
	// logger.addHandler(handler);
	//
	// createFile("C:\\Users\\Chloe\\Documents\\Spring2016\\test.txt");
	// System.out.println(filePath);
	//
	// ArrayList<String> tag = new ArrayList<String>();
	// tag.add("test");
	// tag.add("test2");
	//
	// ArrayList<Calendar> startDates = new ArrayList<Calendar>();
	// startDates.add(Calendar.getInstance());
	//
	// ArrayList<Calendar> endDates = new ArrayList<Calendar>();
	// endDates.add(Calendar.getInstance());
	//
	// ArrayList<TaskFloat> fTasks = new ArrayList<TaskFloat>();
	// TaskFloat fTask = new TaskFloat("Test Description Floating", "Here",
	// tag);
	// fTasks.add(fTask);
	// fTasks.add(fTask);
	//
	// ArrayList<TaskDeadline> dTasks = new ArrayList<TaskDeadline>();
	// TaskDeadline dTask = new TaskDeadline("Test Description Deadline",
	// "Here", Calendar.getInstance(), tag);
	// dTasks.add(dTask);
	// dTasks.add(dTask);
	//
	// ArrayList<TaskEvent> eTasks = new ArrayList<TaskEvent>();
	// TaskEvent eTask = new TaskEvent("Test Description Event", "Here",
	// Calendar.getInstance(),
	// Calendar.getInstance(), tag);
	// eTasks.add(eTask);
	// eTasks.add(eTask);
	//
	// ArrayList<TaskReserved> rTasks = new ArrayList<TaskReserved>();
	// TaskReserved rTask = new TaskReserved("Test Description Reserved",
	// "Here", startDates, endDates, tag);
	// rTasks.add(rTask);
	// rTasks.add(rTask);
	//
	// ArrayList<Task> cTasks = new ArrayList<Task>();
	// Task cTask = new Task("Test Description Completed", "Here", tag);
	// cTasks.add(cTask);
	// cTasks.add(cTask);
	//
	// Display display = new Display(MESSAGE_EMPTY, eTasks, dTasks, fTasks,
	// rTasks, cTasks);
	// saveFile(display);
	//
	// Display testDisplay = getDisplay(filePath);
	// String test = gson.toJson(testDisplay);
	// System.out.println(test);
	// handler.flush();
	// }

	public Storage getInstance() {
		if (storageInstance == null) {
			return new Storage();
		}
		return storageInstance;
	}

	private static void setFilePath(String filepath) {
		filePath = filepath;
	}

	public static void createFile(String filepath) throws IOException {
		File file = new File(filepath);
		if (!file.exists()) {
			file.createNewFile();
		}
		setFilePath(filepath);
	}

	public static Display getDisplay(String filepath) throws IOException {
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

	private static void readTasksFloating(BufferedReader br, ArrayList<TaskFloat> floatTasks) throws IOException {
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

	private static void readTasksDeadline(BufferedReader br, ArrayList<TaskDeadline> deadlineTasks) throws IOException {
		String line = null;
		String description = null;
		Calendar deadline = null;
		String location = null;
		ArrayList<String> tags = null;

		try {
			readHeader(br);

			while ((line = br.readLine()) != null) {
				line = br.readLine();
				if (isInvalidLine(line)) {
					break;
				} else {
					logger.log(Level.INFO, "Reading new deadline task.\r\n");
					description = readDescription(line);
					deadline = readDate(br, ATTRIBUTE_DEADLINE);
					location = readLocation(br);
					tags = readTags(br);

					TaskDeadline deadlineTask = new TaskDeadline(description, location, deadline, tags);
					deadlineTasks.add(deadlineTask);
					logger.log(Level.INFO, "Successfully read: " + deadlineTask.getDescription() + "\r\n");
				}
			}
		} catch (IOException ioe) {
			logger.log(Level.WARNING, "IOException: Could not read deadline task.\r\n");
			throw ioe;

		} catch (ParseException pe) {
			logger.log(Level.WARNING, "Could not read deadline task.\r\n");
			pe.printStackTrace();
		}
	}

	private static void readTasksEvents(BufferedReader br, ArrayList<TaskEvent> events) throws IOException {
		String line;
		String description = null;
		Calendar startDate = null;
		Calendar endDate = null;
		String location = null;
		ArrayList<String> tags = null;

		try {
			readHeader(br);
			while ((line = br.readLine()) != null) {
				line = br.readLine();
				if (isInvalidLine(line)) {
					break;
				} else {
					logger.log(Level.INFO, "Reading new event.\r\n");
					description = readDescription(line);
					startDate = readDate(br, ATTRIBUTE_START_DATE);
					endDate = readDate(br, ATTRIBUTE_END_DATE);
					location = readLocation(br);
					tags = readTags(br);

					TaskEvent eventTask = new TaskEvent(description, location, startDate, endDate, tags);
					events.add(eventTask);
					logger.log(Level.INFO, "Successfully read: " + eventTask.getDescription() + "\r\n");
				}
			}
		} catch (IOException ioe) {
			logger.log(Level.WARNING, "IOException: Could not read reserved task.\r\n");
			throw ioe;

		} catch (ParseException pe) {
			logger.log(Level.WARNING, "Could not read reserved task.\r\n");
			pe.printStackTrace();
		}
	}

	private static void readTasksReserved(BufferedReader br, ArrayList<TaskReserved> reservedTasks) throws IOException {
		String line = null;
		String description = null;
		ArrayList<Calendar> startDates = null;
		ArrayList<Calendar> endDates = null;
		String location = null;
		ArrayList<String> tags = null;

		try {
			readHeader(br);

			while ((line = br.readLine()) != null) {
				line = br.readLine();
				if (isInvalidLine(line)) {
					break;
				} else {
					logger.log(Level.INFO, "Reading new reserved task.\r\n");
					description = readDescription(line);
					startDates = readDates(br, ATTRIBUTE_START_DATES);
					endDates = readDates(br, ATTRIBUTE_END_DATES);
					location = readLocation(br);
					tags = readTags(br);

					TaskReserved reservedTask = new TaskReserved(description, location, startDates, endDates, tags);
					reservedTasks.add(reservedTask);
					logger.log(Level.INFO, "Successfully read: " + reservedTask.getDescription() + "\r\n");
				}

			}
		} catch (IOException ioe) {
			logger.log(Level.WARNING, "Could not read reserved task.\r\n");
			throw ioe;

		} catch (ParseException pe) {
			logger.log(Level.WARNING, "Could not read reserved task.\r\n");
			pe.printStackTrace();
		}
	}

	private static void readTasksCompleted(BufferedReader br, ArrayList<Task> completedTasks) throws IOException {
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

	private static FileHandler createLogHandler() throws IOException {
		FileHandler handler = new FileHandler("logs\\log.txt");
		handler.setFormatter(new SimpleFormatter());
		logger.addHandler(handler);
		return handler;
	}

	private static void readHeader(BufferedReader br) throws IOException {
		br.readLine();
		br.readLine();
	}

	private static String readDescription(String line) {
		String description = null;
		if (line.startsWith(ATTRIBUTE_DESCRIPTION)) {
			description = line.replaceFirst(ATTRIBUTE_DESCRIPTION, "").trim();
			if (description.isEmpty()) {
				description = null;
			}
		}
		return description;
	}

	private static String readLocation(BufferedReader br) throws IOException {
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

	private static ArrayList<String> readTags(BufferedReader br) throws IOException {
		ArrayList<String> tags = null;
		String line = br.readLine();
		if (line.startsWith(ATTRIBUTE_TAGS)) {
			tags = new ArrayList<String>(Arrays.asList(line.trim().split("\\s*#\\s*")));
			tags.remove(0);
		}
		return tags;
	}

	private static Calendar readDate(BufferedReader br, String dateType) throws IOException, ParseException {
		Calendar date = null;
		String line = br.readLine();
		if (line.startsWith(dateType)) {
			date = Calendar.getInstance();
			date.setTime(sdf.parse(line.replaceFirst(dateType, "").trim()));
		}
		return date;
	}

	private static ArrayList<Calendar> readDates(BufferedReader br, String dateType)
			throws IOException, ParseException {
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

	private static boolean isInvalidLine(String line) {
		return line == null || line.equals(HEADER_DIVIDER);
	}

	private static void closeReaderClasses(FileHandler handler, BufferedReader br) throws IOException {
		br.close();
		handler.close();
	}

	public static void saveFile(Display thisDisplay) throws IOException {
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

	private static void writeHeaderToFile(BufferedWriter bw, String header) throws IOException {
		bw.write(HEADER_DIVIDER);
		bw.newLine();
		bw.write(header);
		bw.newLine();
		bw.write(HEADER_DIVIDER);
		bw.newLine();
		bw.newLine();
	}
	private static void writeFloatingTasks(BufferedWriter bw, ArrayList<TaskFloat> floatTasks) throws IOException {
		writeHeaderToFile(bw, HEADER_FLOATING);
		for (TaskFloat task : floatTasks) {
			logger.log(Level.INFO, "Writing " + task.getDescription() + " to file.\r\n");
			bw.write(task.toString());
		}
	}
	private static void writeDeadlineTasks(BufferedWriter bw, ArrayList<TaskDeadline> deadlineTasks)
			throws IOException {
		writeHeaderToFile(bw, HEADER_DEADLINE);
		for (TaskDeadline task : deadlineTasks) {
			logger.log(Level.INFO, "Writing " + task.getDescription() + " to file.\r\n");
			bw.write(task.toString());
		}
	}
	private static void writeEventTasks(BufferedWriter bw, ArrayList<TaskEvent> events) throws IOException {
		writeHeaderToFile(bw, HEADER_EVENT);
		for (TaskEvent event : events) {
			logger.log(Level.INFO, "Writing " + event.getDescription() + " to file.\r\n");
			bw.write(event.toString());
		}
	}
	private static void writeReservedTasks(BufferedWriter bw, ArrayList<TaskReserved> reservedTasks)
			throws IOException {
		writeHeaderToFile(bw, HEADER_RESERVED);
		for (TaskReserved task : reservedTasks) {
			logger.log(Level.INFO, "Writing " + task.getDescription() + " to file.\r\n");
			bw.write(task.toString());
		}
	}

	private static void writeCompletedTasks(BufferedWriter bw, ArrayList<Task> completedTasks) throws IOException {
		writeHeaderToFile(bw, HEADER_COMPLETED);
		for (Task task : completedTasks) {
			logger.log(Level.INFO, "Writing " + task.getDescription() + " to file.\r\n");
			bw.write(task.toString());
		}
	}
	
	private static void closeWriterClasses(FileHandler handler, BufferedWriter bw) throws IOException {
		bw.flush();
		bw.close();
		handler.close();
	}

}
