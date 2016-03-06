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
import java.util.ArrayList;

import com.google.gson.Gson;

import bean.Display;
import bean.Task;
import bean.TaskDeadline;
import bean.TaskEvent;
import bean.TaskReserved;

public class Storage {

	private static String filePath;

	private static final String HEADER_FLOATING = "=== FLOATING ===";
	private static final String HEADER_DEADLINE = "=== DEADLINE ===";
	private static final String HEADER_EVENT = "=== EVENT ===";
	private static final String HEADER_RESERVED = "=== RESERVED ===";
	private static final String HEADER_COMPLETED = "=== COMPLETED ===";

	private static final String MESSAGE_EMPTY = "";

	// @@author Chloe Odquier Fortuna A0149063E
	public static void createFile(String filepath) throws IOException {
		File file = new File(filepath);
		if (!file.exists()) {
			file.createNewFile();
		}
		filePath = filepath;
	}

	// @@author Chloe Odquier Fortuna A0149063E
	public static Display getDisplay(String filepath) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
		Gson gson = new Gson();

		ArrayList<Task> floatTasks = new ArrayList<Task>();
		ArrayList<TaskDeadline> deadlineTasks = new ArrayList<TaskDeadline>();
		ArrayList<TaskEvent> events = new ArrayList<TaskEvent>();
		ArrayList<TaskReserved> reservedTasks = new ArrayList<TaskReserved>();
		ArrayList<Task> completedTasks = new ArrayList<Task>();

		String line = null;

		findHeaderInFile(br, line, HEADER_FLOATING);
		while ((line = br.readLine()) != null && hasContent(line)) {
			Task floatTask = gson.fromJson(line, Task.class);
			floatTasks.add(floatTask);
		}

		findHeaderInFile(br, line, HEADER_DEADLINE);
		while ((line = br.readLine()) != null && hasContent(line)) {
			TaskDeadline deadlineTask = gson.fromJson(line, TaskDeadline.class);
			deadlineTasks.add(deadlineTask);
		}

		findHeaderInFile(br, line, HEADER_EVENT);
		while ((line = br.readLine()) != null && hasContent(line)) {
			TaskEvent event = gson.fromJson(line, TaskEvent.class);
			events.add(event);
		}

		findHeaderInFile(br, line, HEADER_RESERVED);
		while ((line = br.readLine()) != null && hasContent(line)) {
			TaskReserved reservedTask = gson.fromJson(line, TaskReserved.class);
			reservedTasks.add(reservedTask);
		}

		findHeaderInFile(br, line, HEADER_COMPLETED);
		while ((line = br.readLine()) != null && hasContent(line)) {
			Task completedTask = gson.fromJson(line, Task.class);
			completedTasks.add(completedTask);
		}

		br.close();

		Display display = new Display(MESSAGE_EMPTY, events, deadlineTasks, floatTasks, reservedTasks, completedTasks);
		return display;

	}

	// @@author Chloe Odquier Fortuna A0149063E
	private static void findHeaderInFile(BufferedReader br, String line, String header) throws IOException {
		line = br.readLine();
		while (line != null && !line.equals(header)) {
			line = br.readLine();
		}
	}

	// @@author Chloe Odquier Fortuna A0149063E
	private static boolean hasContent(String line) {
		return !line.trim().isEmpty();
	}

	// @@author Chloe Odquier Fortuna A0149063E
	public static void saveFile(Display thisDisplay) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
		Gson gson = new Gson();

		ArrayList<Task> floatTasks = thisDisplay.getFloatTasks();
		ArrayList<TaskDeadline> deadlineTasks = thisDisplay.getDeadlineTasks();
		ArrayList<TaskEvent> events = thisDisplay.getEventTasks();
		ArrayList<TaskReserved> reservedTasks = thisDisplay.getReservedTasks();
		ArrayList<Task> completedTasks = thisDisplay.getCompletedTasks();

		writeHeaderToFile(bw, HEADER_FLOATING);
		for (Task task : floatTasks) {
			writeTaskToFile(bw, gson, task);
		}

		writeHeaderToFile(bw, HEADER_DEADLINE);
		for (TaskDeadline task : deadlineTasks) {
			writeTaskToFile(bw, gson, task);
		}

		writeHeaderToFile(bw, HEADER_EVENT);
		for (TaskEvent event : events) {
			writeTaskToFile(bw, gson, event);
		}

		writeHeaderToFile(bw, HEADER_RESERVED);
		for (TaskReserved task : reservedTasks) {
			writeTaskToFile(bw, gson, task);
		}

		writeHeaderToFile(bw, HEADER_COMPLETED);
		for (Task task : completedTasks) {
			writeTaskToFile(bw, gson, task);
		}

		bw.flush();
		bw.close();
	}

	// @@author Chloe Odquier Fortuna A0149063E
	private static void writeHeaderToFile(BufferedWriter bw, String header) throws IOException {
		bw.newLine();
		bw.write(header);
		bw.newLine();
	}

	// @@author Chloe Odquier Fortuna A0149063E
	private static void writeTaskToFile(BufferedWriter bw, Gson gson, Task task) throws IOException {
		String taskAsString = gson.toJson(task);
		bw.write(taskAsString);
		bw.newLine();
	}

}
