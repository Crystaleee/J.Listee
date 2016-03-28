# Chloe Odquier Fortuna A0149063E
###### src\bean\Display.java
``` java
	@Override
	public String toString() {
		return "Display [message=" + message + ", events=" + events + ", deadlineTasks=" + deadlineTasks
				+ ", floatTasks=" + floatTasks + ", reservedTasks=" + reservedTasks + ", completedTasks="
				+ completedTasks + "]";
	}
}
```
###### src\bean\Task.java
``` java
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Description: " + this.getDescription() + "\r\n");
		String location = this.getLocation();
		if (location == null) {
			location = "";
		}
		sb.append("Location: " + location + "\r\n");

		ArrayList<String> tagsList = this.getTags();
		String tagsString = "";
		for (String tag : tagsList) {
			tagsString += " #" + tag;
		}

		sb.append("Tags:" + tagsString + "\r\n");
		sb.append("\r\n");
		return sb.toString();
	}
}
```
###### src\bean\TaskDeadline.java
``` java
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Description: " + this.getDescription() + "\r\n");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
		String dateString = sdf.format(this.endDate.getTime());
		sb.append("Deadline: " + dateString + "\r\n");

		String location = this.getLocation();
		if (location == null) {
			location = "";
		}
		sb.append("Location: " + location + "\r\n");

		ArrayList<String> tagsList = this.getTags();
		String tagsString = "";
		for (String tag : tagsList) {
			tagsString += " #" + tag;
		}

		sb.append("Tags:" + tagsString + "\r\n");
		sb.append("\r\n");
		return sb.toString();
	}

}
```
###### src\bean\TaskEvent.java
``` java
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Description: " + this.getDescription() + "\r\n");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
		String startDate = sdf.format(this.startDate.getTime());
		sb.append("Start Date: " + startDate + "\r\n");

		String endDate = sdf.format(this.getEndDate().getTime());
		sb.append("End Date: " + endDate + "\r\n");

		String location = this.getLocation();
		if (location == null) {
			location = "";
		}
		sb.append("Location: " + location + "\r\n");

		ArrayList<String> tagsList = this.getTags();
		String tagsString = "";
		for (String tag : tagsList) {
			tagsString += " #" + tag;
		}

		sb.append("Tags:" + tagsString + "\r\n");
		sb.append("\r\n");
		return sb.toString();
	}

}
```
###### src\bean\TaskFloat.java
``` java
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Description: " + this.getDescription() + "\r\n");
		String location = this.getLocation();
		if (location == null) {
			location = "";
		}
		sb.append("Location: " + location + "\r\n");

		ArrayList<String> tagsList = this.getTags();
		String tagsString = "";
		for (String tag : tagsList) {
			tagsString += " #" + tag;
		}

		sb.append("Tags:" + tagsString + "\r\n");
		sb.append("\r\n");
		return sb.toString();
	}

}
```
###### src\bean\TaskReserved.java
``` java
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Description: " + this.getDescription() + "\r\n");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");

		String startDates = "";
		for (int i = 0; i < this.startDates.size(); i++) {
			if (i == this.startDates.size() - 1) {
				startDates += sdf.format(this.startDates.get(i).getTime());
			} else {
				startDates += sdf.format(this.startDates.get(i).getTime()) + ", ";
			}
		}
		sb.append("Start Dates: " + startDates + "\r\n");

		String endDates = "";
		for (int i = 0; i < this.endDates.size(); i++) {
			if (i == this.endDates.size() - 1) {
				endDates += sdf.format(this.endDates.get(i).getTime());
			} else {
				endDates += sdf.format(this.endDates.get(i).getTime()) + ", ";
			}
		}
		sb.append("End Dates: " + endDates + "\r\n");

		String location = this.getLocation();
		if (location == null) {
			location = "";
		}
		sb.append("Location: " + location + "\r\n");

		ArrayList<String> tagsList = this.getTags();
		String tagsString = "";
		for (String tag : tagsList) {
			tagsString += " #" + tag;
		}

		sb.append("Tags:" + tagsString + "\r\n");
		sb.append("\r\n");
		return sb.toString();
	}

}
```
###### src\storage\Storage.java
``` java
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
```
###### src\storage\StorageTest.java
``` java

package storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bean.Display;
import bean.Task;
import bean.TaskDeadline;
import bean.TaskEvent;
import bean.TaskFloat;
import bean.TaskReserved;

public class StorageTest {

	Storage storage = Storage.getInstance();
	Display display;

	/* Creates a display to be used for the unit tests */
	@Before
	public void setUpDisplay() {
		ArrayList<String> tags = new ArrayList<>(Arrays.asList("tag1, tag2"));
		Calendar date = Calendar.getInstance();

		TaskFloat floatTask = new TaskFloat("Test Floating", "NUS", tags);
		TaskDeadline deadlineTask = new TaskDeadline("Test Deadline", "NUS", date, tags);
		TaskEvent eventTask = new TaskEvent("Test Event", "NUS", date, date, tags);

		ArrayList<TaskFloat> floatTasks = new ArrayList<TaskFloat>();
		ArrayList<TaskDeadline> deadlineTasks = new ArrayList<TaskDeadline>();
		ArrayList<TaskEvent> eventTasks = new ArrayList<TaskEvent>();
		ArrayList<TaskReserved> reservedTasks = new ArrayList<TaskReserved>();
		ArrayList<Task> completedTasks = new ArrayList<Task>();

		floatTasks.add(floatTask);
		floatTasks.add(floatTask);

		deadlineTasks.add(deadlineTask);
		deadlineTasks.add(deadlineTask);

		eventTasks.add(eventTask);
		eventTasks.add(eventTask);

		display = new Display("", eventTasks, deadlineTasks, floatTasks, reservedTasks, completedTasks);
	}

	@Before
	public void prepareTextFileForTests() throws IOException {
		storage.createFile("src\\storage\\tests\\test.txt");
	}

	/* This is a case for the successful createFile() partition */
	@Test
	public void testfileExists() {
		File file = new File("src\\storage\\tests\\test.txt");
		assertTrue(file.exists());
	}

	/*
	 * This is a case for the createFile() partition proving that it creates a
	 * file in the correct location
	 */
	@Test
	public void testfileLocation() {
		File file = new File("src\\storage\\test.txt");
		assertFalse(file.exists());
	}

	/* This is a case for the normal reading from file partition */
	@Test
	public void testSaveAndRead() throws IOException {
		storage.saveFile(display);
		assertEquals(display.toString(), storage.getDisplay(storage.filePath).toString());
	}

	/*
	 * This is a case for the reading from a file with empty descriptions
	 * partition
	 */
	@Test
	public void testReadEmptyDescription() throws IOException {
		Display emptyDescriptionDisplay = storage.getDisplay("src\\storage\\tests\\emptyDescriptionTest.txt");

		ArrayList<TaskFloat> emptyFloat = emptyDescriptionDisplay.getFloatTasks();
		ArrayList<TaskDeadline> emptyDeadline = emptyDescriptionDisplay.getDeadlineTasks();
		ArrayList<TaskEvent> emptyEvent = emptyDescriptionDisplay.getEventTasks();

		assertEquals("undefined", emptyFloat.get(0).getDescription());
		assertEquals("undefined", emptyDeadline.get(0).getDescription());
		assertEquals("undefined", emptyEvent.get(0).getDescription());
	}

	/*
	 * This is a case for the reading from a file with empty locations partition
	 */
	@Test
	public void testReadEmptyLocation() throws IOException {
		Display emptyDescriptionDisplay = storage.getDisplay("src\\storage\\tests\\emptyLocationTest.txt");

		ArrayList<TaskFloat> emptyFloat = emptyDescriptionDisplay.getFloatTasks();
		ArrayList<TaskDeadline> emptyDeadline = emptyDescriptionDisplay.getDeadlineTasks();
		ArrayList<TaskEvent> emptyEvent = emptyDescriptionDisplay.getEventTasks();

		assertEquals(null, emptyFloat.get(0).getLocation());
		assertEquals(null, emptyDeadline.get(0).getLocation());
		assertEquals(null, emptyEvent.get(0).getLocation());
	}

	/* This is a case for the reading from a file with empty tags partition */
	@Test
	public void testReadEmptyTags() throws IOException {
		Display emptyDescriptionDisplay = storage.getDisplay("src\\storage\\tests\\emptyTagsTest.txt");

		ArrayList<TaskFloat> emptyFloat = emptyDescriptionDisplay.getFloatTasks();
		ArrayList<TaskDeadline> emptyDeadline = emptyDescriptionDisplay.getDeadlineTasks();
		ArrayList<TaskEvent> emptyEvent = emptyDescriptionDisplay.getEventTasks();

		ArrayList<Task> emptyTagList = new ArrayList<Task>();

		assertEquals(emptyTagList, emptyFloat.get(0).getTags());
		assertEquals(emptyTagList, emptyDeadline.get(0).getTags());
		assertEquals(emptyTagList, emptyEvent.get(0).getTags());
	}

	/*
	 * This is a case for the reading from a file with empty deadlines partition
	 */
	@Test
	public void testReadEmptyDeadline() throws IOException {
		Display emptyDescriptionDisplay = storage.getDisplay("src\\storage\\tests\\emptyDeadlineTest.txt");
		ArrayList<TaskDeadline> emptyDeadline = emptyDescriptionDisplay.getDeadlineTasks();
		assertTrue(emptyDeadline.isEmpty());
	}

	/* This is a case for the reading from a file with empty dates partition */
	@Test
	public void testReadEmptyDates() throws IOException {
		Display emptyDescriptionDisplay = storage.getDisplay("src\\storage\\tests\\emptyDatesTest.txt");
		ArrayList<TaskEvent> emptyEvent = emptyDescriptionDisplay.getEventTasks();
		assertTrue(emptyEvent.isEmpty());
	}

	@After
	public void closeDownAfterTests() {
		File file = new File("src\\storage\\tests\\test.txt");
		file.delete();
	}

}
```
