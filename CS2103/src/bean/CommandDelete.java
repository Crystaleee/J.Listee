/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 21 Mar, 11:00pm
 */
package bean;

import java.util.ArrayList;
import java.util.Collections;

public class CommandDelete implements Command {
	private boolean updateFile;
	private ArrayList<Integer> taskNumbers;
	private Display display;
	private String deletedMessage = "deleted: ";
	private String message_invalid_task_numbers = "You have specified invalid task numbers: ";
	private String message_all_tasks_deleted = "All tasks deleted";
	private boolean saveHistory = true;

	public CommandDelete() {
		updateFile = true;
		this.taskNumbers = null;
		this.display = null;
	}

	public CommandDelete(ArrayList<Integer> taskNumbers) {
		updateFile = true;
		this.taskNumbers = taskNumbers;
		this.display = null;
	}

	public Display execute(Display oldDisplay) {
		this.display = oldDisplay;
		if (taskNumbers == null) {
			display = new Display(message_all_tasks_deleted);
		} else if (hasInvalidTaskNumbers(display.getNumberOfTasks())) {
			updateFile = false;
			saveHistory = false;
			display.setMessage(message_invalid_task_numbers);
			return oldDisplay;
			// return (new Display(message_invalid_task_numbers));
		} else {
			Collections.sort(taskNumbers);
			deleteTasksFromList();
			display.setMessage(deletedMessage);
		}
		return display;
	}

	private boolean hasInvalidTaskNumbers(int numOfTasks) {
		ArrayList<Integer> invalidTaskNumbers = new ArrayList<Integer>();
		int taskNum;
		for (int i = 0; i < taskNumbers.size(); i++) {
			taskNum = taskNumbers.get(i);
			if ((taskNum > numOfTasks) || (taskNum < 1)) {
				if (invalidTaskNumbers.size() == 0) {
					message_invalid_task_numbers += taskNum;
				} else {
					message_invalid_task_numbers += ", " + taskNum;
				}
				invalidTaskNumbers.add(taskNum);
			}
		}
		return (invalidTaskNumbers.size() > 0);
	}

	private void deleteTasksFromList() {
		Task deletedTask;
		for (int i = 0; i < taskNumbers.size(); i++) {
			deletedTask = removeTask(taskNumbers.get(i) - 1 - i);
			if (i == 0) {
				deletedMessage += "\"" + deletedTask.getDescription() + "\"";
			} else {
				deletedMessage += ", \"" + deletedTask.getDescription() + "\"";
			}
		}
		return;
	}

	private Task removeTask(int taskNum) {
		Task deletedTask;
		if (taskNum < display.getDeadlineTasks().size()) {
			deletedTask = display.getDeadlineTasks().remove(taskNum);
		} else {
			taskNum -= display.getDeadlineTasks().size();
			if (taskNum < display.getEventTasks().size()) {
				deletedTask = display.getEventTasks().remove(taskNum);
			} else {
				taskNum -= display.getEventTasks().size();
				if (taskNum < display.getFloatTasks().size()) {
					deletedTask = display.getFloatTasks().remove(taskNum);
				} else {
	                taskNum -= display.getFloatTasks().size();
					deletedTask = display.getReservedTasks().remove(taskNum);
				}
			}
		}
		return deletedTask;
	}

	public boolean getSaveHistory() {
		return saveHistory;
	}

	public boolean getUpdateFile() {
		return updateFile;
	}
}
