/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 27 Mar, 1:20am
 */
package bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CommandShow implements Command {
	private final String message_no_tasks = "No such tasks found";
	private final String message_show_all = "Displaying all tasks";
	private String message_show = "Displaying tasks";
	private boolean updateFile = false;
	private boolean saveHistory = true;
	private TaskEvent searchedTask;
	private int count;
	private Display oldDisplay;
	private Display newDisplay;

	public CommandShow() {
		searchedTask = null;
		newDisplay = null;
		count = 0;
	}

	public CommandShow(String keyword) {
		searchedTask = new TaskEvent(keyword.trim().toLowerCase(), "", null, null, new ArrayList<String>());
		newDisplay = new Display();
		count = 0;
	}

	public CommandShow(String keyword, String location, Calendar start, Calendar end, ArrayList<String> tags) {
		searchedTask = new TaskEvent(keyword.trim().toLowerCase(), location.trim().toLowerCase(), start, end, tags);
		newDisplay = new Display();
		count = 0;
	}

	public Display execute(Display oldDisplay) {
		if (showAll()) {
			oldDisplay.setMessage(message_show_all);
			return oldDisplay;
		}
		this.oldDisplay = oldDisplay;

		newDisplay = getTasksContainingKeyword();

		if (count == 0) {
			newDisplay = new Display(message_no_tasks);
		} else {
			newDisplay.setMessage(getFeedback());
		}

		return newDisplay;
	}

	private boolean showAll() {
		if (searchedTask.getDescription().isEmpty()) {
			if (searchedTask.getLocation().isEmpty()) {
				if ((searchedTask.getStartDate() == null) && (searchedTask.getEndDate() == null)) {
					if (searchedTask.getTags().isEmpty()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private String getFeedback() {
		if (!searchedTask.getDescription().isEmpty()) {
			message_show += " containing " + searchedTask.getDescription();
		}
		if (!searchedTask.getLocation().isEmpty()) {
			message_show += " at " + searchedTask.getLocation();
		}
		if ((searchedTask.getStartDate() != null) && (searchedTask.getEndDate() != null)) {
			SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yy HH:mm");
			String startDate = format1.format(searchedTask.getStartDate().getTime());
			String endDate = format1.format(searchedTask.getEndDate().getTime());

			message_show += " from " + startDate + " to " + endDate;
		}
		if (!searchedTask.getTags().isEmpty()) {
			message_show += " tagged";
			for (int i = 0; i < searchedTask.getTags().size(); i++) {
				if (i == 0) {
					message_show += " " + searchedTask.getTags().get(i);
				} else {
					message_show += ", " + searchedTask.getTags().get(i);
				}
			}
		}
		return message_show;
	}

	private Display getTasksContainingKeyword() {
		getFloatTasks();
		getEventTasks();
		getDeadLineTasks();
		getReservedTasks();
		return newDisplay;
	}

	private void getDeadLineTasks() {
		TaskDeadline task;
		for (int i = 0; i < oldDisplay.getDeadlineTasks().size(); i++) {
			task = oldDisplay.getDeadlineTasks().get(i);
			if (containsKeyword(task)) {
				if (atLocation(task)) {
					if (containsTag(task)) {
						if (withinTimeRange(task)) {
							newDisplay.getDeadlineTasks().add(task);
							count++;
						}
					}
				}
			}
		}
	}

	private void getEventTasks() {
		TaskEvent task;
		for (int i = 0; i < oldDisplay.getEventTasks().size(); i++) {
			task = oldDisplay.getEventTasks().get(i);
			if (containsKeyword(task)) {
				if (atLocation(task)) {
					if (containsTag(task)) {
						if (withinTimeRange(task)) {
							newDisplay.getEventTasks().add(task);
							count++;
						}
					}
				}
			}
		}
	}

	private void getFloatTasks() {
		TaskFloat task;
		for (int i = 0; i < oldDisplay.getFloatTasks().size(); i++) {
			task = oldDisplay.getFloatTasks().get(i);
			if (containsKeyword(task)) {
				if (atLocation(task)) {
					if (containsTag(task)) {
						newDisplay.getFloatTasks().add(task);
						count++;
					}
				}
			}
		}
	}

	private void getReservedTasks() {
		TaskReserved task;
		for (int i = 0; i < oldDisplay.getReservedTasks().size(); i++) {
			task = oldDisplay.getReservedTasks().get(i);
			if (containsKeyword(task)) {
				if (atLocation(task)) {
					if (containsTag(task)) {
						if (withinTimeRange(task)) {
							newDisplay.getReservedTasks().add(task);
							count++;
						}
					}
				}
			}
		}
	}

	private boolean containsTag(Task task) {
		if (searchedTask.getTags().isEmpty()) {
			return true;
		}
		boolean containsTags = false;
		for (int i = 0; i < searchedTask.getTags().size(); i++) {
			for (int j = 0; j < task.getTags().size(); j++) {
				containsTags = false;
				if (searchedTask.getTags().get(i).toLowerCase().equals(task.getTags().get(j).trim().toLowerCase())) {
					containsTags = true;
					break;
				}
			}
			if (containsTags == false) {
				return false;
			}
		}
		return true;
	}

	private boolean withinTimeRange(TaskDeadline task) {
		if ((searchedTask.getStartDate() == null) && (searchedTask.getEndDate() == null)) {
			return true;
		}
		if (task.getEndDate().after(searchedTask.getStartDate())) {
			if (task.getEndDate().before(searchedTask.getEndDate())) {
				return true;
			}
		}
		return false;
	}

	private boolean withinTimeRange(TaskReserved task) {
		if ((searchedTask.getStartDate() == null) && (searchedTask.getEndDate() == null)) {
			return true;
		}
		for (int i = 0; i < task.getStartDates().size(); i++) {
			if (!task.getStartDates().get(i).before(searchedTask.getStartDate())) {
				if (!task.getStartDates().get(i).after(searchedTask.getEndDate())) {
					return true;
				}
			} else if (!task.getEndDates().get(i).before(searchedTask.getStartDate())) {
				return true;
			}

		}
		return false;
	}

	private boolean withinTimeRange(TaskEvent task) {
		if ((searchedTask.getStartDate() == null) && (searchedTask.getEndDate() == null)) {
			return true;
		}
		if (!task.getStartDate().before(searchedTask.getStartDate())) {
			if (!task.getStartDate().after(searchedTask.getEndDate())) {
				return true;
			}
		} else if (!task.getEndDate().before(searchedTask.getStartDate())) {
			return true;
		}
		return false;
	}

	private boolean atLocation(Task task) {
		if (searchedTask.getLocation().isEmpty()) {
			return true;
		}
		if (task.getLocation() == null) {
			return false;
		}
		if (task.getLocation().equalsIgnoreCase(searchedTask.getLocation())) {
			return true;
		}
		return false;
	}

	private boolean containsKeyword(Task task) {
		if (task.getDescription().toLowerCase().contains(searchedTask.getDescription())) {
			return true;
		}
		return false;
	}

	public boolean getSaveHistory() {
		return saveHistory;
	}

	public boolean getUpdateFile() {
		return updateFile;
	}
}
