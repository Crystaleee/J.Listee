/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 21 Mar, 11:00pm
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

import logic.Logic;

public class CommandAddEvent implements Command {
	private TaskEvent task;
	private boolean updateFile = true;
	private boolean saveHistory = true;

	public CommandAddEvent() {
		task = null;
	}

	public CommandAddEvent(String description, String location, Calendar startDate, Calendar endDate,
			ArrayList<String> tags) {
		task = new TaskEvent(description, location, startDate, endDate, tags);
	}

	public Display execute(Display display) {
		if (task.getDescription() == null) {
			updateFile = false;
			saveHistory = false;
			display.setMessage(Logic.MESSAGE_NO_DESCRIPTION);
			return display;
			// return (new Display(Logic.MESSAGE_NO_DESCRIPTION));
		}

		task.setDescription(task.getDescription().trim());
		if (task.getDescription().isEmpty()) {
			updateFile = false;
			saveHistory = false;
			display.setMessage(Logic.MESSAGE_NO_DESCRIPTION);
			return display;
		}
		ArrayList<TaskEvent> events = addEvent(display.getEventTasks());
		display.setEvents(events);
		display.setMessage(String.format(Logic.MESSAGE_ADD_SUCCESS, task.getDescription()));
		return display;
	}

	private ArrayList<TaskEvent> addEvent(ArrayList<TaskEvent> taskList) {
		int index = getAddIndex(taskList);
		taskList.add(index, task);
		return taskList;
	}

	/*
	 * This method searches for the index to slot the deadline task in since we
	 * are sorting the list in order of earliest start time first
	 */
	private int getAddIndex(ArrayList<TaskEvent> taskList) {
		int i = 0;
		for (i = 0; i < taskList.size(); i++) {
			if (task.getStartDate().compareTo(taskList.get(i).getStartDate()) <= 0) {
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
