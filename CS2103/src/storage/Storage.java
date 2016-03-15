//@@author Chloe Odquier Fortuna A0149063E
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

import bean.Display;
import bean.Task;
import bean.TaskDeadline;
import bean.TaskEvent;
import bean.TaskFloat;
import bean.TaskReserved;

public class Storage {

	private static String filePath;

	private static final String HEADER_FLOATING = "     TASKS";
	private static final String HEADER_DEADLINE = "   DEADLINES";
	private static final String HEADER_EVENT = "    EVENTS";
	private static final String HEADER_RESERVED = "   RESERVED";
	private static final String HEADER_COMPLETED = "   COMPLETED";
	private static final String HEADER_DIVIDER = "===============";

	private static final String ATTRIBUTE_DESCRIPTION = "Description: ";
	private static final String ATTRIBUTE_LOCATION = "Location: ";
	private static final String ATTRIBUTE_TAGS = "Tags:";
	private static final String ATTRIBUTE_DEADLINE = "Deadline: ";
	private static final String ATTRIBUTE_START_DATE = "Start Date: ";
	private static final String ATTRIBUTE_END_DATE = "End Date: ";
	private static final String ATTRIBUTE_START_DATES = "Start Dates: ";
	private static final String ATTRIBUTE_END_DATES = "End Dates: ";

	private static final String MESSAGE_EMPTY = "";

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");

	/*
	 * public static void main(String[] args) throws IOException, ParseException
	 * { createFile("C:\\Users\\Chloe\\Documents\\Spring2016\\test.txt");
	 * System.out.println(filePath); Gson gson = new Gson();
	 * 
	 * ArrayList<String> tag = new ArrayList<String>(); tag.add("test");
	 * tag.add("test2");
	 * 
	 * ArrayList<Calendar> startDates = new ArrayList<Calendar>();
	 * startDates.add(Calendar.getInstance());
	 * 
	 * ArrayList<Calendar> endDates = new ArrayList<Calendar>();
	 * endDates.add(Calendar.getInstance());
	 * 
	 * ArrayList<Task> fTasks = new ArrayList<Task>(); Task fTask = new Task(
	 * "Test Description Floating", "Here", tag); fTasks.add(fTask);
	 * fTasks.add(fTask);
	 * 
	 * ArrayList<TaskDeadline> dTasks = new ArrayList<TaskDeadline>();
	 * TaskDeadline dTask = new TaskDeadline("Test Description Deadline",
	 * "Here", Calendar.getInstance(), tag); dTasks.add(dTask);
	 * dTasks.add(dTask);
	 * 
	 * ArrayList<TaskEvent> eTasks = new ArrayList<TaskEvent>(); TaskEvent eTask
	 * = new TaskEvent("Test Description Event", "Here", Calendar.getInstance(),
	 * Calendar.getInstance(), tag); eTasks.add(eTask); eTasks.add(eTask);
	 * 
	 * ArrayList<TaskReserved> rTasks = new ArrayList<TaskReserved>();
	 * TaskReserved rTask = new TaskReserved("Test Description Reserved",
	 * "Here", startDates, endDates, tag); rTasks.add(rTask); rTasks.add(rTask);
	 * 
	 * ArrayList<Task> cTasks = new ArrayList<Task>(); Task cTask = new
	 * TaskEvent("Test Description Completed", "Here", Calendar.getInstance(),
	 * Calendar.getInstance(), tag); cTasks.add(cTask); cTasks.add(cTask);
	 * 
	 * Display display = new Display(MESSAGE_EMPTY, eTasks, dTasks, fTasks,
	 * rTasks, cTasks); saveFile(display);
	 * 
	 * Display testDisplay = getDisplay(filePath); String test =
	 * gson.toJson(testDisplay); System.out.println(test);
	 * 
	 * }
	 */

	public static String getFilePath() {
		return filePath;
	}

	public static void setFilePath(String filepath) {
		filePath = filepath;
	}

	// @@author Chloe Odquier Fortuna A0149063E
	public static void createFile(String filepath) throws IOException {
		File file = new File(filepath);
		if (!file.exists()) {
			file.createNewFile();
		}
		setFilePath(filepath);
	}

	// @@author Chloe Odquier Fortuna A0149063E
	public static Display getDisplay(String filepath) throws IOException {

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
		} catch (Exception e) {
			System.out.println("There was an error in reading your file.");
			e.printStackTrace();
		}

		br.close();
		setFilePath(filepath);

		Display display = new Display(MESSAGE_EMPTY, events, deadlineTasks, floatTasks, reservedTasks, completedTasks);
		return display;

	}

	private static void readTasksCompleted(BufferedReader br, ArrayList<Task> completedTasks) throws IOException {
		try {
			String line;
			readHeader(br);
			while ((line = br.readLine()) != null) {
				String description = null;
				String location = null;
				ArrayList<String> tags = null;

				line = br.readLine();

				if (line == null || isDivider(line)) {
					break;
				} else {
					if (line.startsWith(ATTRIBUTE_DESCRIPTION)) {

						description = line.replaceFirst(ATTRIBUTE_DESCRIPTION, "");
					}
					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_LOCATION)) {
						location = line.replaceFirst(ATTRIBUTE_LOCATION, "");
					}
					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_TAGS)) {
						tags = new ArrayList<String>(Arrays.asList(line.split(" #")));
						tags.remove(0);
					}
					Task completedTask = new Task(description, location, tags);
					completedTasks.add(completedTask);
				}
			}
		} catch (IOException ioe) {
			throw ioe;
		} catch (Exception e) {
			System.out.println("There was an error in reading the completed tasks.");
			e.printStackTrace();
		}
	}

	private static void readTasksReserved(BufferedReader br, ArrayList<TaskReserved> reservedTasks)
			throws IOException, ParseException {
		try {
			String line;
			readHeader(br);
			while ((line = br.readLine()) != null) {
				String description = null;
				ArrayList<Calendar> startDates = null;
				ArrayList<Calendar> endDates = null;
				String location = null;
				ArrayList<String> tags = null;

				line = br.readLine();

				if (line == null || isDivider(line)) {
					break;
				} else {
					if (line.startsWith(ATTRIBUTE_DESCRIPTION)) {
						description = line.replaceFirst(ATTRIBUTE_DESCRIPTION, "");
					}
					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_START_DATES)) {
						ArrayList<String> startDatesString = new ArrayList<String>(
								Arrays.asList(line.replaceFirst(ATTRIBUTE_START_DATES, "").split(", ")));
						startDates = new ArrayList<Calendar>();

						for (String date : startDatesString) {
							Calendar startDate = Calendar.getInstance();
							startDate.setTime(sdf.parse(date));
							startDates.add(startDate);
						}
					}

					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_END_DATES)) {
						ArrayList<String> endDatesString = new ArrayList<String>(
								Arrays.asList(line.replaceFirst(ATTRIBUTE_END_DATES, "").split(", ")));
						endDates = new ArrayList<Calendar>();

						for (String date : endDatesString) {
							Calendar endDate = Calendar.getInstance();
							endDate.setTime(sdf.parse(date));
							startDates.add(endDate);
						}
					}

					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_LOCATION)) {
						location = line.replaceFirst(ATTRIBUTE_LOCATION, "");
					}

					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_TAGS)) {
						tags = new ArrayList<String>(Arrays.asList(line.split(" #")));
						tags.remove(0);
					}
					TaskReserved reservedTask = new TaskReserved(description, location, startDates, endDates, tags);
					reservedTasks.add(reservedTask);

				}

			}
		} catch (IOException ioe) {
			throw ioe;
		} catch (Exception e) {
			System.out.println("There was an error in reading the reserved tasks.");
			e.printStackTrace();
		}
	}

	private static void readTasksEvents(BufferedReader br, ArrayList<TaskEvent> events) throws IOException {
		try {
			String line;
			readHeader(br);
			while ((line = br.readLine()) != null) {
				String description = null;
				Calendar startDate = null;
				Calendar endDate = null;
				String location = null;
				ArrayList<String> tags = null;

				line = br.readLine();

				if (line == null || isDivider(line)) {
					break;
				} else {
					if (line.startsWith(ATTRIBUTE_DESCRIPTION)) {
						description = line.replaceFirst(ATTRIBUTE_DESCRIPTION, "").trim();
					}
					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_START_DATE)) {
						startDate = Calendar.getInstance();
						startDate.setTime(sdf.parse(line.replaceFirst(ATTRIBUTE_START_DATE, "")));
					}
					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_END_DATE)) {
						endDate = Calendar.getInstance();
						endDate.setTime(sdf.parse(line.replaceFirst(ATTRIBUTE_END_DATE, "")));
					}

					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_LOCATION)) {
						location = line.replaceFirst(ATTRIBUTE_LOCATION, "").trim();
					}
					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_TAGS)) {
						tags = new ArrayList<String>(Arrays.asList(line.split(" #")));
						tags.remove(0);
					}

					TaskEvent eventTask = new TaskEvent(description, location, startDate, endDate, tags);

					if (location.isEmpty()) {
						eventTask.setLocation(null);
					}
					events.add(eventTask);
				}
			}
		} catch (IOException ioe) {
			throw ioe;
		} catch (Exception e) {
			System.out.println("There was an error in reading the events.");
			e.printStackTrace();
		}
	}

	private static void readTasksDeadline(BufferedReader br, ArrayList<TaskDeadline> deadlineTasks) throws IOException {
		try {
			String line;
			readHeader(br);

			while ((line = br.readLine()) != null) {
				String description = null;
				Calendar deadline = null;
				String location = null;
				ArrayList<String> tags = null;

				line = br.readLine();

				if (line == null || isDivider(line)) {
					break;
				} else {
					if (line.startsWith(ATTRIBUTE_DESCRIPTION)) {
						description = line.replaceFirst(ATTRIBUTE_DESCRIPTION, "").trim();
					}
					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_DEADLINE)) {
						deadline = Calendar.getInstance();
						deadline.setTime(sdf.parse(line.replaceFirst(ATTRIBUTE_DEADLINE, "")));
					}
					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_LOCATION)) {
						location = line.replaceFirst(ATTRIBUTE_LOCATION, "").trim();
					}
					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_TAGS)) {
						tags = new ArrayList<String>(Arrays.asList(line.split(" #")));
						tags.remove(0);
					}
					TaskDeadline deadlineTask = new TaskDeadline(description, location, deadline, tags);

					if (location.isEmpty()) {
						deadlineTask.setLocation(null);
					}
					deadlineTasks.add(deadlineTask);
				}
			}
		} catch (IOException ioe) {
			throw ioe;
		} catch (Exception e) {
			System.out.println("There was an error in reading the deadline tasks.");
			e.printStackTrace();
		}
	}

	private static void readTasksFloating(BufferedReader br, ArrayList<TaskFloat> floatTasks) throws IOException {
		try {
			String line;
			readHeader(br);

			while ((line = br.readLine()) != null) {

				String description = null;
				String location = null;
				ArrayList<String> tags = null;

				line = br.readLine();

				if (line == null || isDivider(line)) {
					break;
				} else {
					if (line.startsWith(ATTRIBUTE_DESCRIPTION)) {
						description = line.replaceFirst(ATTRIBUTE_DESCRIPTION, "").trim();
					}
					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_LOCATION)) {
						location = line.replaceFirst(ATTRIBUTE_LOCATION, "").trim();
					}
					line = br.readLine();
					if (line.startsWith(ATTRIBUTE_TAGS)) {
						tags = new ArrayList<String>(Arrays.asList(line.split(" #")));
						tags.remove(0);
					}
					TaskFloat floatTask = new TaskFloat(description, location, tags);
					if (location.isEmpty()) {
						floatTask.setLocation(null);
					}

					floatTasks.add(floatTask);

				}
			}
		} catch (IOException ioe) {
			throw ioe;
		} catch (Exception e) {
			System.out.println("There was an error in reading the floating tasks.");
			e.printStackTrace();
		}
	}

	private static boolean isDivider(String line) {
		return line.equals(HEADER_DIVIDER);
	}

	private static void readHeader(BufferedReader br) throws IOException {
		br.readLine();
		br.readLine();
	}

	/*
	 * // @@author Chloe Odquier Fortuna A0149063E private static boolean
	 * hasContent(String line) { return !line.trim().isEmpty(); }
	 */

	// @@author Chloe Odquier Fortuna A0149063E
	public static void saveFile(Display thisDisplay) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));

		ArrayList<TaskFloat> floatTasks = thisDisplay.getFloatTasks();
		ArrayList<TaskDeadline> deadlineTasks = thisDisplay.getDeadlineTasks();
		ArrayList<TaskEvent> events = thisDisplay.getEventTasks();
		ArrayList<TaskReserved> reservedTasks = thisDisplay.getReservedTasks();
		ArrayList<Task> completedTasks = thisDisplay.getCompletedTasks();

		writeHeaderToFile(bw, HEADER_FLOATING);
		for (TaskFloat task : floatTasks) {
			bw.write(task.toString());
			// writeTaskFloatToFile(bw, task);
		}

		writeHeaderToFile(bw, HEADER_DEADLINE);
		for (TaskDeadline task : deadlineTasks) {
			bw.write(task.toString());
			// writeTaskDeadlineToFile(bw, task);
		}

		writeHeaderToFile(bw, HEADER_EVENT);
		for (TaskEvent event : events) {
			bw.write(event.toString());
			// writeTaskEventToFile(bw, event);
		}

		writeHeaderToFile(bw, HEADER_RESERVED);
		for (TaskReserved task : reservedTasks) {
			bw.write(task.toString());
			// writeTaskReservedToFile(bw, task);
		}

		writeHeaderToFile(bw, HEADER_COMPLETED);
		for (Task task : completedTasks) {
			bw.write(task.toString());
			//writeTaskToFile(bw, task);
			/*
			 * if (task instanceof Task) { writeTaskFloatingToFile(bw, task); }
			 * else if (task instanceof TaskDeadline) {
			 * writeTaskDeadlineToFile(bw, task); } else if (task instanceof
			 * TaskEvent) { writeTaskEventToFile(bw, task); } else if (task
			 * instanceof TaskReserved) { writeTaskReservedToFile(bw, task); }
			 * else { System.out.println(
			 * "There was an error in saving the file."); }
			 */
		}

		bw.flush();
		bw.close();
	}

	/*
	 * private static void writeTaskReservedToFile(BufferedWriter bw,
	 * TaskReserved task) throws IOException { ArrayList<Calendar> startDates =
	 * task.getStartDates(); String startDate = ""; for (int i = 0; i <
	 * startDates.size(); i++) { startDate += getDateAsString(startDates.get(i))
	 * + ", "; if (i == startDates.size() - 1) { startDate +=
	 * getDateAsString(startDates.get(i)); } }
	 * 
	 * ArrayList<Calendar> endDates = task.getEndDates(); String endDate = "";
	 * for (int i = 0; i < endDates.size(); i++) { endDate +=
	 * getDateAsString(endDates.get(i)) + ", "; if (i == endDates.size() - 1) {
	 * endDate += getDateAsString(endDates.get(i)); } }
	 * 
	 * bw.write(ATTRIBUTE_DESCRIPTION + task.getDescription()); bw.newLine();
	 * bw.write(ATTRIBUTE_START_DATES + startDate); bw.newLine();
	 * bw.write(ATTRIBUTE_END_DATES + endDate); bw.newLine(); String location =
	 * task.getLocation(); if (location == null) { location = ""; }
	 * bw.write(ATTRIBUTE_LOCATION + location); bw.newLine(); ArrayList<String>
	 * tagsList = task.getTags(); String tagsString = ""; for (String tag :
	 * tagsList) { tagsString += " #" + tag; } bw.write(ATTRIBUTE_TAGS +
	 * tagsString); bw.newLine(); bw.newLine(); }
	 * 
	 * 
	 * private static void writeTaskEventToFile(BufferedWriter bw, TaskEvent
	 * event) throws IOException { bw.write(ATTRIBUTE_DESCRIPTION +
	 * event.getDescription()); bw.newLine(); bw.write(ATTRIBUTE_START_DATE +
	 * getDateAsString(event.getStartDate())); bw.newLine();
	 * bw.write(ATTRIBUTE_END_DATE + getDateAsString(event.getEndDate()));
	 * bw.newLine(); String location = event.getLocation(); if (location ==
	 * null) { location = ""; } bw.write(ATTRIBUTE_LOCATION + location);
	 * bw.newLine(); ArrayList<String> tagsList = event.getTags(); String
	 * tagsString = ""; for (String tag : tagsList) { tagsString += " #" + tag;
	 * } bw.write(ATTRIBUTE_TAGS + tagsString); bw.newLine(); bw.newLine(); }
	 * 
	 * private static void writeTaskDeadlineToFile(BufferedWriter bw,
	 * TaskDeadline task) throws IOException { bw.write(ATTRIBUTE_DESCRIPTION +
	 * task.getDescription()); bw.newLine(); bw.write(ATTRIBUTE_DEADLINE +
	 * getDateAsString(task.getEndDate())); bw.newLine(); String location =
	 * task.getLocation(); if (location == null) { location = ""; }
	 * bw.write(ATTRIBUTE_LOCATION + location); bw.newLine(); ArrayList<String>
	 * tagsList = task.getTags(); String tagsString = ""; for (String tag :
	 * tagsList) { tagsString += " #" + tag; } bw.write(ATTRIBUTE_TAGS +
	 * tagsString); bw.newLine(); bw.newLine(); }
	 * 
	 * private static void writeTaskFloatToFile(BufferedWriter bw, TaskFloat
	 * task) throws IOException { bw.write(ATTRIBUTE_DESCRIPTION +
	 * task.getDescription()); bw.newLine();
	 * 
	 * String location = task.getLocation(); if (location == null) { location =
	 * ""; } bw.write(ATTRIBUTE_LOCATION + location);
	 * 
	 * bw.newLine(); ArrayList<String> tagsList = task.getTags(); String
	 * tagsString = ""; for (String tag : tagsList) { tagsString += " #" + tag;
	 * } bw.write(ATTRIBUTE_TAGS + tagsString); bw.newLine(); bw.newLine(); }
	 */

	private static void writeTaskToFile(BufferedWriter bw, Task task) throws IOException {
		bw.write(ATTRIBUTE_DESCRIPTION + task.getDescription());
		bw.newLine();
		String location = task.getLocation();
		if (location == null) {
			location = "";
		}
		bw.write(ATTRIBUTE_LOCATION + location);
		bw.newLine();
		ArrayList<String> tagsList = task.getTags();
		String tagsString = "";
		for (String tag : tagsList) {
			tagsString += " #" + tag;
		}
		bw.write(ATTRIBUTE_TAGS + tagsString);
		bw.newLine();
		bw.newLine();
	}

	// @@author Chloe Odquier Fortuna A0149063E
	private static void writeHeaderToFile(BufferedWriter bw, String header) throws IOException {
		bw.write(HEADER_DIVIDER);
		bw.newLine();
		bw.write(header);
		bw.newLine();
		bw.write(HEADER_DIVIDER);
		bw.newLine();
		bw.newLine();
	}

	/*
	 * // @@author Chloe Odquier Fortuna A0149063E private static void
	 * writeTaskToFile(BufferedWriter bw, Gson gson, Task task) throws
	 * IOException { String taskAsString = gson.toJson(task);
	 * bw.write(taskAsString); bw.newLine(); }
	 * 
	 * 
	 * private static String getDateAsString(Calendar date) { //
	 * SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM/yy HH:mm"); String
	 * dateString = sdf.format(date.getTime()); return dateString; }
	 */
}
