# Boh Tuang Hwee Jehiel A0139995E
###### src\bean\Command.java
``` java
 * Last updated: 3/15/2016, 2:40am
 * CS2103
 */
package bean;

public interface Command {

	public Display execute(Display display);

	public boolean getUpdateFile();

	public boolean getSaveHistory();
}
```
###### src\bean\CommandAddDeadlineTask.java
``` java
 * Last updated: 21 Mar, 11:00pm
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

import logic.Logic;

public class CommandAddDeadlineTask implements Command {
	private TaskDeadline task;
	private boolean updateFile = true;
	private boolean saveHistory = true;

	public CommandAddDeadlineTask() {
		task = null;
	}

	public CommandAddDeadlineTask(String description, String location, Calendar endDate, ArrayList<String> tags) {
		task = new TaskDeadline(description, location, endDate, tags);
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
		ArrayList<TaskDeadline> deadlineTasks = addDeadlineTask(display.getDeadlineTasks());
		display.setDeadlineTasks(deadlineTasks);
		display.setMessage(String.format(Logic.MESSAGE_ADD_SUCCESS, task.getDescription()));

		return display;
	}

	private ArrayList<TaskDeadline> addDeadlineTask(ArrayList<TaskDeadline> taskList) {
		int index = getIndex(taskList);
		taskList.add(index, task);
		return taskList;
	}

	/*
	 * This method searches for the index to slot the deadline task in since we
	 * are sorting the list in order of earliest deadline first
	 */
	private int getIndex(ArrayList<TaskDeadline> taskList) {
		int i = 0;
		for (i = 0; i < taskList.size(); i++) {
			if (task.getEndDate().compareTo(taskList.get(i).getEndDate()) <= 0) {
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
```
###### src\bean\CommandAddEvent.java
``` java
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
```
###### src\bean\CommandAddFloatTask.java
``` java
 * Last updated: 21 Mar, 11:00pm
 */
package bean;

import java.util.ArrayList;

import logic.Logic;

public class CommandAddFloatTask implements Command {
	private TaskFloat task;
	private boolean updateFile = true;
	private boolean saveHistory = true;

	public CommandAddFloatTask() {
		task = null;
	}

	public CommandAddFloatTask(String description, String location, ArrayList<String> tags) {
		task = new TaskFloat(description, location, tags);
		updateFile = true;
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
		display.getFloatTasks().add(task);
		display.setMessage(String.format(Logic.MESSAGE_ADD_SUCCESS, task.getDescription()));
		return display;
	}

	public boolean getSaveHistory() {
		return saveHistory;
	}

	public boolean getUpdateFile() {
		return updateFile;
	}
}
```
###### src\bean\CommandAddReserved.java
``` java
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
```
###### src\bean\CommandDelete.java
``` java
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
			if (isTaskNumberInvalid(numOfTasks, taskNum)) {
				feedbackInvalidNumbers(invalidTaskNumbers, taskNum);
				invalidTaskNumbers.add(taskNum);
			}
		}
		return (invalidTaskNumbers.size() > 0);
	}

	private void feedbackInvalidNumbers(ArrayList<Integer> invalidTaskNumbers, int taskNum) {
		if (invalidTaskNumbers.size() == 0) {
			message_invalid_task_numbers += taskNum;
		} else {
			message_invalid_task_numbers += ", " + taskNum;
		}
	}

	private boolean isTaskNumberInvalid(int numOfTasks, int taskNum) {
		return (taskNum > numOfTasks) || (taskNum < 1);
	}

	private void deleteTasksFromList() {
		Task deletedTask;
		for (int i = 0; i < taskNumbers.size(); i++) {
			deletedTask = removeTask(taskNumbers.get(i) - 1 - i);
			feedbackDeletedTasks(deletedTask, i);
		}
		return;
	}

	private void feedbackDeletedTasks(Task deletedTask, int i) {
		if (i == 0) {
			deletedMessage += "\"" + deletedTask.getDescription() + "\"";
		} else {
			deletedMessage += ", \"" + deletedTask.getDescription() + "\"";
		}
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
```
###### src\bean\CommandDone.java
``` java
 * Last updated: 21 Mar, 11:00pm
 */
package bean;

import java.util.ArrayList;
import java.util.Collections;

public class CommandDone implements Command {
	private boolean updateFile;
	private ArrayList<Integer> taskNumbers;
	private Display display;
	private String doneMessage = "You have completed: ";
	private String message_invalid_task_numbers = "You have specified invalid task numbers: ";
	private boolean saveHistory = true;

	public CommandDone() {
		updateFile = true;
		this.taskNumbers = null;
		this.display = null;
	}

	public CommandDone(ArrayList<Integer> taskNumbers) {
		updateFile = true;
		this.taskNumbers = taskNumbers;
		this.display = null;
	}

	public Display execute(Display oldDisplay) {
		this.display = oldDisplay;
		if (hasInvalidTaskNumbers(display.getNumberOfTasks())) {
			updateFile = false;
			saveHistory = false;
			display.setMessage(message_invalid_task_numbers);
			return oldDisplay;
			// return (new Display(message_invalid_task_numbers));
		} else {
			Collections.sort(taskNumbers);
			markDoneTasksFromList();
			display.setMessage(doneMessage);
		}
		return display;
	}

	private boolean hasInvalidTaskNumbers(int numOfTasks) {
		ArrayList<Integer> invalidTaskNumbers = new ArrayList<Integer>();
		int taskNum;
		for (int i = 0; i < taskNumbers.size(); i++) {
			taskNum = taskNumbers.get(i);
			if (isTaskNumberInvalid(numOfTasks, taskNum)) {
				feedbackInvalidNumbers(invalidTaskNumbers, taskNum);
				invalidTaskNumbers.add(taskNum);
			}
		}
		return (invalidTaskNumbers.size() > 0);
	}

	private void feedbackInvalidNumbers(ArrayList<Integer> invalidTaskNumbers, int taskNum) {
		if (invalidTaskNumbers.size() == 0) {
			message_invalid_task_numbers += taskNum;
		} else {
			message_invalid_task_numbers += ", " + taskNum;
		}
	}

	private boolean isTaskNumberInvalid(int numOfTasks, int taskNum) {
		return (taskNum > numOfTasks) || (taskNum < 1);
	}

	private void markDoneTasksFromList() {
		Task deletedTask;
		for (int i = 0; i < taskNumbers.size(); i++) {
			deletedTask = markTaskDone(taskNumbers.get(i) - 1 - i);

		}
		return;
	}

	private void feedbackCompletedTasks(Task deletedTask, int i) {
		if (i == 0) {
			doneMessage += "\"" + deletedTask.getDescription() + "\"";
		} else {
			doneMessage += ", \"" + deletedTask.getDescription() + "\"";
		}
	}

	private Task markTaskDone(int taskNum) {
		Task deletedTask;
		if (taskNum < display.getDeadlineTasks().size()) {
			deletedTask = display.getDeadlineTasks().remove(taskNum);
			display.getCompletedTasks().add(deletedTask);
		} else {
			taskNum -= display.getDeadlineTasks().size();
			if (taskNum < display.getEventTasks().size()) {
				deletedTask = display.getEventTasks().remove(taskNum);
				display.getCompletedTasks().add(deletedTask);
			} else {
				taskNum -= display.getEventTasks().size();
				if (taskNum < display.getFloatTasks().size()) {
					deletedTask = display.getFloatTasks().remove(taskNum);
					display.getCompletedTasks().add(deletedTask);
				} else {
					taskNum -= display.getFloatTasks().size();
					deletedTask = display.getReservedTasks().remove(taskNum);
					display.getCompletedTasks().add(deletedTask);
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
```
###### src\bean\CommandInvalid.java
``` java
 * Last updated: 21 Mar, 11:00pm
 */

package bean;

public class CommandInvalid implements Command {
	private final String MESSAGE_INVALID_COMMAND = "You have specified an invalid command";
	private boolean updateFile = false;
	private boolean saveHistory = false;

	public CommandInvalid() {
	}

	public Display execute(Display display) {
		display.setMessage(MESSAGE_INVALID_COMMAND);
		return display;
		// return (new Display(MESSAGE_INVALID_COMMAND));
	}

	public boolean getSaveHistory() {
		return saveHistory;
	}

	public boolean getUpdateFile() {
		return updateFile;
	}
}
```
###### src\bean\CommandRedo.java
``` java
 * Last updated: 3/15/2016, 3:00am
 * CS2103
 */
package bean;

import History.History;

public class CommandRedo implements Command {
	private final String MESSAGE_REDO = "Redid last command";
	private final String MESSAGE_ERROR_REDO = "You have reached the latest point possible";
	private boolean updateFile = true;
	private boolean saveHistory = false;

	public CommandRedo() {
	}

	public Display execute(Display display) {
		if (History.atLastState()) {
			updateFile = false;
			display.setMessage(MESSAGE_ERROR_REDO);
			return display;
			// return (new Display(MESSAGE_ERROR_REDO));
		}

		display = History.getDisplay(1);
		display.setMessage(MESSAGE_REDO);
		return display;
	}

	public boolean getSaveHistory() {
		return saveHistory;
	}

	public boolean getUpdateFile() {
		return updateFile;
	}
}
```
###### src\bean\CommandShow.java
``` java
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
```
###### src\bean\CommandUndo.java
``` java
 * Last updated: 3/15/2016, 3:00am
 * CS2103
 */

package bean;

import History.History;

public class CommandUndo implements Command {
	private final String MESSAGE_UNDO = "Undid last command";
	private final String MESSAGE_ERROR_UNDO = "You have reached the earliest point possible";
	private boolean updateFile = true;
	private boolean saveHistory = false;

	public CommandUndo() {
	}

	public Display execute(Display display) {
		if (History.atFirstState()) {
			updateFile = false;
			display.setMessage(MESSAGE_ERROR_UNDO);
			return display;
			// return (new Display(MESSAGE_ERROR_UNDO));
		}

		display = History.getDisplay(-1);
		display.setMessage(MESSAGE_UNDO);
		return display;
	}

	public boolean getSaveHistory() {
		return saveHistory;
	}

	public boolean getUpdateFile() {
		return updateFile;
	}
}
```
###### src\bean\CommandUpdate.java
``` java
 * Last updated: 27 Mar, 2:22am
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

public class CommandUpdate extends TaskEvent implements Command {
	private Integer taskNumber;
	private ArrayList<String> removeTags;
	private Display display;
	private boolean updateFile = true;;
	private boolean saveHistory = true;
	private String message = "Edited : \"";
	private final String message_invalid_task_number = "Pls specify a valid task number";

	public CommandUpdate() {
		super();
		removeTags = null;
		this.taskNumber = null;
	}

	public CommandUpdate(Integer taskNumber, String description) {
		super(description, null, null, null, null);
		removeTags = null;
		this.taskNumber = taskNumber;
	}

	public CommandUpdate(Integer taskNumber, String description, String location, Calendar startDate, Calendar endDate,
			ArrayList<String> addTags, ArrayList<String> removeTags) {
		super(description, location, startDate, endDate, addTags);
		this.removeTags = removeTags;
		updateFile = true;
		this.taskNumber = taskNumber;
	}

	public Display execute(Display oldDisplay) {
		display = oldDisplay;
		if (hasInvalidTaskNumber(display.getNumberOfTasks())) {
			updateFile = false;
			saveHistory = false;
			display.setMessage(message_invalid_task_number);
			return display;
			// return (new Display(message_invalid_task_number));
		}
		editTask();
		display.setMessage(message);
		return display;
	}

	private boolean hasInvalidTaskNumber(int numOfTasks) {
		return ((taskNumber > numOfTasks) || (taskNumber < 1));
	}

	private void editTask() {
		if (taskNumber <= display.getDeadlineTasks().size()) {
			editDeadline();
		} else {
			taskNumber -= display.getDeadlineTasks().size();
			if (taskNumber <= display.getEventTasks().size()) {
				editEvent();
			} else {
				taskNumber -= display.getEventTasks().size();
				if (taskNumber <= display.getFloatTasks().size()) {
					editFloat();
				} else {
					taskNumber -= display.getFloatTasks().size();
					editReserved();
				}
			}
		}
	}

	private void editDeadline() {
		TaskDeadline task = display.getDeadlineTasks().remove(taskNumber - 1);
		message += task.getDescription() + "\"";
		task = (TaskDeadline) editDescription(task);
		task = (TaskDeadline) editLocation(task);
		task = (TaskDeadline) editTags(task);
		task = editEndDate(task);
		changeDeadlineTaskType(task);
	}

	private void editEvent() {
		TaskEvent task = display.getEventTasks().remove(taskNumber - 1);
		message += task.getDescription() + "\"";
		task = (TaskEvent) editDescription(task);
		task = (TaskEvent) editLocation(task);
		task = (TaskEvent) editTags(task);
		task = editStartDate(task);
		task = editEndDate(task);
		changeEventTaskType(task);
	}

	private void editFloat() {
		TaskFloat task = display.getFloatTasks().get(taskNumber - 1);
		message += task.getDescription() + "\"";
		task = (TaskFloat) editDescription(task);
		task = (TaskFloat) editLocation(task);
		task = (TaskFloat) editTags(task);
		if (hasChangeFloatTaskType(task)) {
			display.getFloatTasks().remove(taskNumber - 1);
		}
	}

	private void editReserved() {
		TaskReserved task = display.getReservedTasks().get(taskNumber - 1);
		message += task.getDescription() + "\"";
		task = (TaskReserved) editDescription(task);
		task = (TaskReserved) editLocation(task);
		task = (TaskReserved) editTags(task);

	}

	private void changeDeadlineTaskType(TaskDeadline task) {
		// assertFalse endDate==0 AND startDate != 0&null
		boolean hasTaskChanged = false;
		if (getEndDate() != null) {
			if (getEndDate().getTimeInMillis() == 0) {
				Command addCommand = new CommandAddFloatTask(task.getDescription(), task.getLocation(), task.getTags());
				display = addCommand.execute(display);
				hasTaskChanged = true;
			}
		}
		if (getStartDate() != null) {
			if ((getStartDate().getTimeInMillis() != 0) && (!hasTaskChanged)) {
				Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(), getStartDate(),
						task.getEndDate(), task.getTags());
				display = addCommand.execute(display);
				hasTaskChanged = true;
			}
		}

		if (!hasTaskChanged) {
			Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
					task.getEndDate(), task.getTags());
			display = addCommand.execute(display);
		}
	}

	private Task editTags(Task task) {
		if (removeTags != null) {
			for (int i = 0; i < removeTags.size(); i++) {
				String tag = removeTags.get(i);
				task.getTags().remove(tag);
			}
		}

		if (getTags() != null) {
			for (int i = 0; i < getTags().size(); i++) {
				String tag = getTags().get(i);
				task.getTags().add(tag);
			}
		}
		return task;
	}

	private TaskEvent editStartDate(TaskEvent task) {
		if (getStartDate() != null) {
			task.setStartDate(getStartDate());
		}
		return task;
	}

	// for event tasks
	private TaskEvent editEndDate(TaskEvent task) {
		if (getEndDate() != null) {
			task.setEndDate(getEndDate());
		}
		return task;
	}

	// for deadline tasks
	private TaskDeadline editEndDate(TaskDeadline task) {
		if (getEndDate() != null) {
			task.setEndDate(getEndDate());
		}
		return task;
	}

	private Task editLocation(Task task) {
		if (getLocation() != null) {
			task.setLocation(getLocation().trim());
		}
		return task;
	}

	private Task editDescription(Task task) {
		if (getDescription() != null) {
			if (!getDescription().trim().isEmpty()) {
				task.setDescription(getDescription().trim());
			}
		}
		return task;
	}

	private void changeEventTaskType(TaskEvent task) {
		boolean hasTaskChanged = false;
		if ((getStartDate() != null) && (getEndDate() != null)) {
			// if user wants to change to floating task
			if ((getStartDate().getTimeInMillis() == 0) && (getEndDate().getTimeInMillis() == 0)) {
				Command addCommand = new CommandAddFloatTask(task.getDescription(), task.getLocation(), task.getTags());
				display = addCommand.execute(display);
				hasTaskChanged = true;
			}
		}
		if (getStartDate() != null) {
			if ((getStartDate().getTimeInMillis() == 0) && (!hasTaskChanged)) {
				Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
						task.getEndDate(), task.getTags());
				display = addCommand.execute(display);
				hasTaskChanged = true;
			}
		}

		if (!hasTaskChanged) {
			Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(), task.getStartDate(),
					task.getEndDate(), task.getTags());
			display = addCommand.execute(display);
		}
	}

	private boolean hasChangeFloatTaskType(TaskFloat task) {
		boolean hasTaskChanged = false;
		if ((getStartDate() != null) && (getEndDate() != null)) {
			if ((getStartDate().getTimeInMillis() != 0) && (getEndDate().getTimeInMillis() != 0)) {
				Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(), getStartDate(),
						getEndDate(), task.getTags());
				display = addCommand.execute(display);
				hasTaskChanged = true;
			}
		} else {
			if (getEndDate() != null) {
				if (getEndDate().getTimeInMillis() != 0) {
					Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
							getEndDate(), task.getTags());
					display = addCommand.execute(display);
					hasTaskChanged = true;
				}
			}
		}
		return hasTaskChanged;
	}

	public boolean getSaveHistory() {
		return saveHistory;
	}

	public boolean getUpdateFile() {
		return updateFile;
	}
}
```
###### src\bean\Display.java
``` java
 * Last updated: 3/5/2016, 10:57pm
 * CS2103
 */
package bean;

import java.util.ArrayList;

public class Display {

	private String message;
	private ArrayList<TaskEvent> events;
	private ArrayList<TaskDeadline> deadlineTasks;
	private ArrayList<TaskFloat> floatTasks;
	private ArrayList<TaskReserved> reservedTasks;
	private ArrayList<Task> completedTasks;

	public Display() {
		message = "";
		events = new ArrayList<TaskEvent>();
		deadlineTasks = new ArrayList<TaskDeadline>();
		floatTasks = new ArrayList<TaskFloat>();
		reservedTasks = new ArrayList<TaskReserved>();
		completedTasks = new ArrayList<Task>();
	}

	public Display(String message) {
		this.message = message;
		events = new ArrayList<TaskEvent>();
		deadlineTasks = new ArrayList<TaskDeadline>();
		floatTasks = new ArrayList<TaskFloat>();
		reservedTasks = new ArrayList<TaskReserved>();
		completedTasks = new ArrayList<Task>();
	}

	public Display(String message, ArrayList<TaskEvent> events, ArrayList<TaskDeadline> deadlineTasks,
			ArrayList<TaskFloat> floatTasks, ArrayList<TaskReserved> reservedTasks, ArrayList<Task> completedTasks) {
		this.message = "";
		this.events = events;
		this.deadlineTasks = deadlineTasks;
		this.floatTasks = floatTasks;
		this.reservedTasks = reservedTasks;
		this.completedTasks = completedTasks;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getNumberOfTasks() {
		return (events.size() + deadlineTasks.size() + floatTasks.size() + reservedTasks.size()
				+ completedTasks.size());
	}

	public String getMessage() {
		return message;
	}

	public void setEvents(ArrayList<TaskEvent> events) {
		this.events = events;
	}

	public ArrayList<TaskEvent> getEventTasks() {
		return events;
	}

	public void setDeadlineTasks(ArrayList<TaskDeadline> deadlineTasks) {
		this.deadlineTasks = deadlineTasks;
	}

	public ArrayList<TaskDeadline> getDeadlineTasks() {
		return deadlineTasks;
	}

	public void setFloatTasks(ArrayList<TaskFloat> floatTasks) {
		this.floatTasks = floatTasks;
	}

	public ArrayList<TaskFloat> getFloatTasks() {
		return floatTasks;
	}

	public void setReservedTasks(ArrayList<TaskReserved> reservedTasks) {
		this.reservedTasks = reservedTasks;
	}

	public ArrayList<TaskReserved> getReservedTasks() {
		return reservedTasks;
	}

	public ArrayList<Task> getCompletedTasks() {
		return completedTasks;
	}

	public void setCompletedTasks(ArrayList<Task> completedTasks) {
		this.completedTasks = completedTasks;
	}

```
###### src\bean\Task.java
``` java
 * Last updated: 3/5/2016, 8:55pm
 * CS2103
 */
package bean;

import java.util.ArrayList;

public class Task {
	private String description;
	private String location;
	private ArrayList<String> tags;

	public Task() {
		description = null;
		location = null;
		tags = null;
	}

	public Task(String description, String location, ArrayList<String> tags) {
		this.description = description;
		this.location = location;
		this.tags = tags;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

```
###### src\bean\TaskDeadline.java
``` java
 * Last updated: 3/5/2016, 8:57pm
 * CS2103
 */
package bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskDeadline extends Task {
	private Calendar endDate;

	public TaskDeadline() {
		this.setDescription(null);
		this.setLocation(null);
		this.setTags(null);
		this.endDate = null;
	}

	public TaskDeadline(String description, String location, Calendar endDate, ArrayList<String> tags) {
		this.setDescription(description);
		this.setLocation(location);
		this.setTags(tags);
		this.endDate = endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

```
###### src\bean\TaskEvent.java
``` java
 * Last updated: 3/5/2016, 9:12pm
 * CS2103
 */
package bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskEvent extends TaskDeadline {
	private Calendar startDate;

	public TaskEvent() {
		this.setDescription(null);
		this.setLocation(null);
		this.setTags(null);
		this.setEndDate(null);
		this.startDate = null;
	}

	public TaskEvent(String description, String location, Calendar startDate, Calendar endDate,
			ArrayList<String> tags) {
		this.setDescription(description);
		this.setLocation(location);
		this.setTags(tags);
		this.setEndDate(endDate);
		this.startDate = startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getStartDate() {
		return startDate;
	}

```
###### src\bean\TaskFloat.java
``` java
 * Last updated: 3/15/2016, 1:45am
 * CS2103
 */
package bean;

import java.util.ArrayList;

public class TaskFloat extends Task {
	public TaskFloat() {
		super();
	}

	public TaskFloat(String description, String location, ArrayList<String> tags) {
		super(description, location, tags);
	}

```
###### src\bean\TaskReserved.java
``` java
 * Last updated: 3/5/2016, 8:55pm
 * CS2103
 */

package bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskReserved extends Task {
	private ArrayList<Calendar> startDates;
	private ArrayList<Calendar> endDates;

	public TaskReserved() {
		this.setDescription(null);
		this.setLocation(null);
		this.setTags(null);
		startDates = null;
		endDates = null;
	}

	public TaskReserved(String description, String location, ArrayList<Calendar> startDates,
			ArrayList<Calendar> endDates, ArrayList<String> tags) {
		this.setDescription(description);
		this.setLocation(location);
		this.setTags(tags);
		this.startDates = startDates;
		this.endDates = endDates;
	}

	public void setStartDates(ArrayList<Calendar> startDates) {
		this.startDates = startDates;
	}

	public ArrayList<Calendar> getStartDates() {
		return startDates;
	}

	public void setEndDates(ArrayList<Calendar> endDates) {
		this.endDates = endDates;
	}

	public ArrayList<Calendar> getEndDates() {
		return endDates;
	}

```
###### src\History\History.java
``` java
 * Last updated: 3/5/2016, 8:00pm
 * CS2103
 */
package History;

import java.util.ArrayList;

import bean.Display;

public class History {

	private static ArrayList<String> userInputs = new ArrayList<String>();
	private static ArrayList<Display> oldDisplays = new ArrayList<Display>();
	private static int oldDisplaysIndex = -1;

	public static void saveDisplay(Display display) {
		if (oldDisplaysIndex < (oldDisplays.size() - 1)) {
			for (int i = (oldDisplays.size() - 1); i > oldDisplaysIndex; i--) {
				oldDisplays.remove(i);
			}
		}
		oldDisplays.add(display);
		oldDisplaysIndex++;
	}

	public static void saveUserInput(String userInput) {
		userInputs.add(userInput);
	}

	public static boolean atLastState() {
		return (oldDisplaysIndex == (oldDisplays.size() - 1));
	}

	public static boolean atFirstState() {
		return (oldDisplaysIndex == 0);
	}

	public static Display getDisplay(int offset) {
		oldDisplaysIndex += offset;
		return oldDisplays.get(oldDisplaysIndex);
	}

}
```
###### src\logic\Logic.java
``` java
 * Last updated: 22 Mar, 12:10AM
 * CS2103
 */
package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import History.History;
import bean.Command;
import bean.CommandAddDeadlineTask;
import bean.CommandUndo;
import bean.Display;
import parser.JListeeParser;
import storage.Storage;

public class Logic {

	public static String MESSAGE_ADD_SUCCESS = "added: \"%1$s\"";

	public static final String MESSAGE_FILE_CREATED = "File created and ready for use";
	public static final String MESSAGE_ERROR_FILE_EXISTS = "File already exists";
	public static final String MESSAGE_ERROR_READING_FILE = "Error occured while reading file";
	public static final String MESSAGE_NO_DESCRIPTION = "Pls enter a description";
	public static final String MESSAGE_ERROR_UPDATE_FILE = "Error occured while updating to file";

	private static Storage storage = Storage.getInstance();
	private static Display display;
	private static String file;

	public static boolean createFile(String filePath) {
		file = filePath;
		try {
			storage.createFile(filePath);
			return true;
		} catch (IOException error) {
			return false;
		}
	}

	public static Display initializeProgram(String filePath) {
		file = filePath;
		getDisplayFromStorage();
		display.setMessage(null);
		History.saveDisplay(display);
		return display;
	}

	public static Display executeUserCommand(String userInput) {
		History.saveUserInput(userInput);
		Command userCommand = parseUserInput(userInput);
		display = executeCommand(userCommand);

		return display;
	}

	private static void saveToHistory(Command userCommand) {
		if (userCommand.getSaveHistory()) {
			History.saveDisplay(display);
		}
	}

	public static Display executeCommand(Command userCommand) {
		getDisplayFromStorage();
		display = userCommand.execute(display);

		if (userCommand.getUpdateFile()) {
			if (successfullyUpdatesFile()) {
				saveToHistory(userCommand);
			} else {
				display = new Display(MESSAGE_ERROR_UPDATE_FILE);
			}
		}
		return display;
	}

	private static Command parseUserInput(String userInput) {
		JListeeParser myParser = new JListeeParser();
		Command userCommand = myParser.ParseCommand(userInput);
		return userCommand;
	}

	private static void getDisplayFromStorage() {
		try {
			display = storage.getDisplay(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static boolean successfullyUpdatesFile() {
		try {
			storage.saveFile(display);
			return true;
		} catch (IOException error) {
			return false;
		}
	}
}
```
###### src\logic\LogicTest1.java
``` java

package logic;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import bean.CommandAddDeadlineTask;
import bean.CommandAddEvent;
import bean.CommandAddFloatTask;
import bean.CommandDelete;
import bean.CommandInvalid;
import bean.CommandRedo;
import bean.CommandUndo;

public class LogicTest1 {
	/*
	 * Commands Tested: add Float with location and tag add Event according to
	 * time with location and/or single/multiple tags add Deadline according to
	 * time delete all delete 1 task delete multiple tasks undo at state>
	 * earliest state undo but alr at 1st state redo at state < last state redo
	 * but at last state
	 * 
	 */
	@Test
	public void test() {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		String args[] = null;
		main.App.main(args);
		assertEquals(
				"Display [message=You have reached the earliest point possible, events=[], deadlineTasks=[], "
						+ "floatTasks=[], reservedTasks=[], completedTasks=[]]",
				Logic.executeCommand(new CommandUndo()).toString());
		assertEquals(
				"Display [message=All tasks deleted, events=[], deadlineTasks=[], "
						+ "floatTasks=[], reservedTasks=[], completedTasks=[]]",
				Logic.executeCommand(new CommandDelete(null)).toString());

		ArrayList<String> tags = new ArrayList<String>();
		tags.add("movie");
		assertEquals(
				"Display [message=added: \"Float1\", events=[], deadlineTasks=[], "
						+ "floatTasks=[Description: Float1\r\nLocation: jcube\r\nTags: #movie\r\n\r\n], reservedTasks=[], "
						+ "completedTasks=[]]",
				Logic.executeCommand(new CommandAddFloatTask("Float1", "jcube", tags)).toString());

		tags = new ArrayList<String>();
		tags.add("movie");
		end.set(2016, 2 - 1, 19, 19, 00);
		assertEquals(
				"Display [message=added: \"Deadline1\", events=[], " + "deadlineTasks=[" + "Description: Deadline1\r\n"
						+ "Deadline: 19/02/16 19:00\r\n" + "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "

						+ "floatTasks=[" + "Description: Float1\r\n" + "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
						+ "reservedTasks=[], " + "completedTasks=[]]",
				Logic.executeCommand(new CommandAddDeadlineTask("Deadline1", "jcube", end, tags)).toString());

		tags = new ArrayList<String>();
		tags.add("movie");
		start.set(2016, 2 - 1, 19, 19, 00);
		end.set(2016, 2 - 1, 19, 21, 00);
		assertEquals("Display [message=added: \"Event1\", " + "events=[" + "Description: Event1\r\n"
				+ "Start Date: 19/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n" + "Location: jcube\r\n"
				+ "Tags: #movie\r\n\r\n],"
				/**************************************************************/

				+ " deadlineTasks=[" + "Description: Deadline1\r\n" + "Deadline: 19/02/16 19:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "floatTasks=[" + "Description: Float1\r\n" + "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				+ "reservedTasks=[], completedTasks=[]]",
				Logic.executeCommand(new CommandAddEvent("Event1", "jcube", start, end, tags)).toString());

		tags = new ArrayList<String>();
		start.set(2016, 2 - 1, 18, 19, 00);
		end.set(2016, 2 - 1, 19, 21, 00);
		assertEquals("Display [message=added: \"Event2\", " + "events=[" + "Description: Event2\r\n"
				+ "Start Date: 18/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n" + "Location: \r\n" + "Tags:\r\n\r\n"

				+ ", Description: Event1\r\n" + "Start Date: 19/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "deadlineTasks=[" + "Description: Deadline1\r\nDeadline: " + "19/02/16 19:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "floatTasks=[" + "Description: Float1\r\n" + "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				+ "reservedTasks=[], completedTasks=[]]",
				Logic.executeCommand(new CommandAddEvent("Event2", null, start, end, tags)).toString());
		assertEquals("Display [message=Pls enter a valid command, " + "events=[" + "Description: Event2\r\n"
				+ "Start Date: 18/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n" + "Location: \r\n" + "Tags:\r\n\r\n"

				+ ", Description: Event1\r\n" + "Start Date: 19/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "deadlineTasks=[" + "Description: Deadline1\r\nDeadline: " + "19/02/16 19:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "floatTasks=[" + "Description: Float1\r\n" + "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				+ "reservedTasks=[], completedTasks=[]]", Logic.executeCommand(new CommandInvalid()).toString());

		ArrayList<Integer> tasks = new ArrayList<Integer>();
		tasks.add(0);
		tasks.add(3);
		tasks.add(9);
		assertEquals("Display [message=You have specified invalid task numbers: 0, 9, " + "events=["
				+ "Description: Event2\r\n" + "Start Date: 18/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n"
				+ "Location: \r\n" + "Tags:\r\n\r\n"

				+ ", Description: Event1\r\n" + "Start Date: 19/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "deadlineTasks=[" + "Description: Deadline1\r\nDeadline: " + "19/02/16 19:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "floatTasks=[" + "Description: Float1\r\n" + "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				+ "reservedTasks=[], completedTasks=[]]", Logic.executeCommand(new CommandDelete(tasks)).toString());

		tags = new ArrayList<String>();
		tags.add("movie");
		start.set(2016, 2 - 1, 21, 19, 00);
		end.set(2016, 2 - 1, 21, 21, 00);
		assertEquals("Display [message=added: \"Event3\", " + "events=[" + "Description: Event2\r\n"
				+ "Start Date: 18/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n" + "Location: \r\n"
				+ "Tags:\r\n\r\n, "

				+ "Description: Event1\r\n" + "Start Date: 19/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n, "

				+ "Description: Event3\r\n" + "Start Date: 21/02/16 19:00\r\n" + "End Date: 21/02/16 21:00\r\n"
				+ "Location: \r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "deadlineTasks=[" + "Description: Deadline1\r\n" + "Deadline: 19/02/16 19:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "floatTasks=[" + "Description: Float1\r\n" + "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				+ "reservedTasks=[], completedTasks=[]]",
				Logic.executeCommand(new CommandAddEvent("Event3", null, start, end, tags)).toString());

		tasks = new ArrayList<Integer>();
		tasks.add(3);
		tasks.add(4);
		assertEquals("Display [message=deleted: \"Event1\", \"Event3\", " + "events=[" + "Description: Event2\r\n"
				+ "Start Date: 18/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n" + "Location: \r\n"
				+ "Tags:\r\n\r\n], "
				/**************************************************************/

				+ "deadlineTasks=[" + "Description: Deadline1\r\n" + "Deadline: 19/02/16 19:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "floatTasks=[" + "Description: Float1\r\n" + "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				+ "reservedTasks=[], completedTasks=[]]", Logic.executeCommand(new CommandDelete(tasks)).toString());
		assertEquals("Display [message=Undid last command, " + "events=[" + "Description: Event2\r\n"
				+ "Start Date: 18/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n" + "Location: \r\n"
				+ "Tags:\r\n\r\n, "

				+ "Description: Event1\r\n" + "Start Date: 19/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n, "

				+ "Description: Event3\r\n" + "Start Date: 21/02/16 19:00\r\n" + "End Date: 21/02/16 21:00\r\n"
				+ "Location: \r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "deadlineTasks=[" + "Description: Deadline1\r\n" + "Deadline: 19/02/16 19:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "floatTasks=[" + "Description: Float1\r\n" + "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				+ "reservedTasks=[], completedTasks=[]]", Logic.executeCommand(new CommandUndo()).toString());
		assertEquals("Display [message=Redid last command, " + "events=[" + "Description: Event2\r\n"
				+ "Start Date: 18/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n" + "Location: \r\n"
				+ "Tags:\r\n\r\n], "
				/**************************************************************/

				+ "deadlineTasks=[" + "Description: Deadline1\r\n" + "Deadline: 19/02/16 19:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "floatTasks=[" + "Description: Float1\r\n" + "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				+ "reservedTasks=[], completedTasks=[]]", Logic.executeCommand(new CommandRedo()).toString());
		assertEquals("Display [message=You have reached the latest point possible, " + "events=["
				+ "Description: Event2\r\n" + "Start Date: 18/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n"
				+ "Location: \r\n" + "Tags:\r\n\r\n], "
				/**************************************************************/

				+ "deadlineTasks=[" + "Description: Deadline1\r\n" + "Deadline: 19/02/16 19:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "floatTasks=[" + "Description: Float1\r\n" + "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				+ "reservedTasks=[], completedTasks=[]]", Logic.executeCommand(new CommandRedo()).toString());

		end.set(2016, 2 - 1, 18, 19, 00);
		assertEquals("Display [message=added: \"Deadline2\", " + "events=[" + "Description: Event2\r\n"
				+ "Start Date: 18/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n" + "Location: \r\n"
				+ "Tags:\r\n\r\n], "
				/**************************************************************/

				+ "deadlineTasks=[" + "Description: Deadline2\r\n" + "Deadline: 18/02/16 19:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n, "

				+ "Description: Deadline1\r\n" + "Deadline: 19/02/16 19:00\r\n" + "Location: jcube\r\n"
				+ "Tags: #movie\r\n\r\n], "
				/**************************************************************/

				+ "floatTasks=[" + "Description: Float1\r\n" + "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				+ "reservedTasks=[], completedTasks=[]]",
				Logic.executeCommand(new CommandAddDeadlineTask("Deadline2", "jcube", end, tags)).toString());
		tags = new ArrayList<String>();
		tags.add("tag1");
		tags.add("tag2");
		tags.add("tag3");
		tags.add("tag4");
		end.set(2016, 2 - 1, 22, 19, 00);
		assertEquals("Display [message=added: \"Deadline3\", " + "events=[" + "Description: Event2\r\n"
				+ "Start Date: 18/02/16 19:00\r\n" + "End Date: 19/02/16 21:00\r\n" + "Location: \r\n"
				+ "Tags:\r\n\r\n], "
				/**************************************************************/

				+ "deadlineTasks=[" + "Description: Deadline2\r\n" + "Deadline: 18/02/16 19:00\r\n"
				+ "Location: jcube\r\n" + "Tags: #movie\r\n\r\n, "

				+ "Description: Deadline1\r\n" + "Deadline: 19/02/16 19:00\r\n" + "Location: jcube\r\n"
				+ "Tags: #movie\r\n\r\n, "

				+ "Description: Deadline3\r\n" + "Deadline: 22/02/16 19:00\r\n" + "Location: \r\n"
				+ "Tags: #tag1 #tag2 #tag3 #tag4\r\n\r\n], "
				/**************************************************************/

				+ "floatTasks=[" + "Description: Float1\r\n" + "Location: jcube\r\n" + "Tags: #movie\r\n\r\n], "
				+ "reservedTasks=[], completedTasks=[]]",
				Logic.executeCommand(new CommandAddDeadlineTask("Deadline3", new String(), end, tags)).toString());
	}

}
```
