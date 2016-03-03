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
import java.util.Calendar;

import bean.Task;

public class Storage {

	private static String filePath;

	/*
	 * public static void main(String[] args) throws IOException {
	 * createFile("C:\\Users\\Chloe\\Documents\\Spring2016\\test.txt");
	 * System.out.println(filePath);
	 * 
	 * Task testTask = new Task("Test Description", Calendar.getInstance(),
	 * Calendar.getInstance()); addTask(testTask); Task testTask2 = new Task(
	 * "Test Description", Calendar.getInstance(), Calendar.getInstance());
	 * addTask(testTask2); Task testTask3 = new Task("Test Description",
	 * Calendar.getInstance(), Calendar.getInstance()); addTask(testTask3);
	 * 
	 * ArrayList<Task> list = getList(filePath);
	 * 
	 * for (Task p : list) { System.out.println(p.getDescription());
	 * System.out.println(p.getStartDate()); System.out.println(p.getEndDate());
	 * }
	 * 
	 * PrintWriter writer = new PrintWriter(filePath); writer.print("");
	 * writer.close();
	 * 
	 * editFile(list); }
	 */

	public static void createFile(String filepath) throws IOException {
		File file = new File(filepath);
		if (!file.exists()) {
			file.createNewFile();
		}
		filePath = filepath;
	}

	public static ArrayList<Task> getList(String filepath) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		ArrayList<Task> tasks = new ArrayList<Task>();

		try {
			String description;

			while ((description = br.readLine()) != null) {

				String startDateString = br.readLine();
				Calendar startDate = Calendar.getInstance();
				startDate.setTime(sdf.parse(startDateString));

				String endDateString = br.readLine();
				Calendar endDate = Calendar.getInstance();
				endDate.setTime(sdf.parse(endDateString));

				Task task = new Task(description, startDate, endDate);
				tasks.add(task);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		filePath = filepath;
		br.close();
		return tasks;
	}

	public static void editFile(ArrayList<Task> tasks) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));

		for (Task task : tasks) {
			String description = task.getDescription();

			Calendar startDateCalendar = task.getStartDate();
			String startDate = startDateCalendar.getTime().toString();

			Calendar endDateCalendar = task.getEndDate();
			String endDate = endDateCalendar.getTime().toString();

			writeTaskToFile(bw, description, startDate, endDate);
		}
		bw.flush();
		bw.close();
	}

	public static void addTask(Task taskToAdd) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true)));

		String description = taskToAdd.getDescription();

		Calendar startDateCalendar = taskToAdd.getStartDate();
		String startDate = startDateCalendar.getTime().toString();

		Calendar endDateCalendar = taskToAdd.getEndDate();
		String endDate = endDateCalendar.getTime().toString();

		writeTaskToFile(bw, description, startDate, endDate);

		bw.flush();
		bw.close();
	}

	private static void writeTaskToFile(BufferedWriter bw, String description, String startDate, String endDate)
			throws IOException {
		bw.write(description);
		bw.newLine();
		bw.write(startDate);
		bw.newLine();
		bw.write(endDate);
		bw.newLine();
	}

}
