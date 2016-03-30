/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 21 Mar, 11:00pm
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

import logic.Logic;

public class CommandAddReserved implements Command {
	private TaskReserved task;
	private boolean updateFile = true;
	private boolean saveHistory = true;

	public CommandAddReserved() {
		task = null;
	}

	public CommandAddReserved(String description, String location, ArrayList<Calendar> startDates,
			ArrayList<Calendar> endDates, ArrayList<String> tags) {
		task = new TaskReserved(description, location, startDates, endDates, tags);
	}

	public Display execute(Display display) {
		if (task.getDescription() == null) {
			updateFile = false;
			saveHistory = false;
			display.setMessage(Logic.MESSAGE_NO_DESCRIPTION);
			return display;
		}
		task.setDescription(task.getDescription().trim());
		if (task.getDescription().isEmpty()) {
			updateFile = false;
			saveHistory = false;
			display.setMessage(Logic.MESSAGE_NO_DESCRIPTION);
			return display;
		}
		ArrayList<TaskReserved> reservedTasks = addReservedTask(display.getReservedTasks());
		display.setReservedTasks(reservedTasks);
        if(!display.getVisibleReservedTasks().equals(display.getReservedTasks())){
            display.getVisibleReservedTasks().add(task);
        }
		display.setMessage("Reserved: " + task.getDescription());
		return display;
	}

	private ArrayList<TaskReserved> addReservedTask(ArrayList<TaskReserved> taskList) {
		int index = getIndex(taskList);
		taskList.add(index, task);
		return taskList;
	}

	/*
	 * This method searches for the index to slot the deadline task in since we
	 * are sorting the list in order of earliest start date of the first time
	 * slot.
	 */
	private int getIndex(ArrayList<TaskReserved> taskList) {
		int i = 0;
		Calendar addedTaskStartDate = task.getStartDates().get(0);
		for (i = 0; i < taskList.size(); i++) {
			Calendar taskInListStartDate = taskList.get(i).getStartDates().get(0);
			if (addedTaskStartDate.compareTo(taskInListStartDate) <= 0) {
				break;
			}
		}
		return i;
	}

	public boolean getSaveHistory() {
		return saveHistory;
	}

	public boolean getUpdateFile() {
		return updateFile;
	}
}
