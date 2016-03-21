/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 21 Mar, 11:00pm
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

    public CommandUpdate(Integer taskNumber, String description, String location, Calendar startDate,
            Calendar endDate, ArrayList<String> addTags, ArrayList<String> removeTags) {
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
            return (new Display(message_invalid_task_number));
        }
        editTask();
        return display;
    }

    public boolean hasInvalidTaskNumber(int numOfTasks) {
        return ((taskNumber > numOfTasks) || (taskNumber < 1));
    }

    public void editTask() {
        if (taskNumber <= display.getDeadlineTasks().size()) {
            editDeadline();
        } else {
            taskNumber -= display.getDeadlineTasks().size();
            if (taskNumber <= display.getEventTasks().size()) {
                editEvent();
            } else {
                taskNumber -= display.getEventTasks().size();
                editFloat();
            }
        }
    }

    public void editDeadline() {
        TaskDeadline task = display.getDeadlineTasks().remove(taskNumber - 1);
        task = (TaskDeadline) editDescription(task);
        task = (TaskDeadline) editLocation(task);
        task = (TaskDeadline) editTags(task);
        task = editEndDate(task);
        changeDeadlineTaskType(task);
    }

    public void changeDeadlineTaskType(TaskDeadline task) {
        if ((getEndDate().getTimeInMillis() == 0) && (getStartDate().getTimeInMillis() == 0)) {
            Command addCommand = new CommandAddFloatTask(task.getDescription(), task.getLocation(),
                    task.getTags());
            display = addCommand.execute(display);
        } else if (getStartDate() != null) {
            Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(),
                    getStartDate(), task.getEndDate(), task.getTags());
            display = addCommand.execute(display);
        }
    }

    public void editEvent() {
        TaskEvent task = display.getEventTasks().remove(taskNumber - 1);
        task = (TaskEvent) editDescription(task);
        task = (TaskEvent) editLocation(task);
        task = (TaskEvent) editTags(task);
        task = editStartDate(task);
        task = editEndDate(task);
        changeEventTaskType(task);
    }

    public void editFloat() {
        TaskFloat task = display.getFloatTasks().remove(taskNumber - 1);
        task = (TaskFloat) editDescription(task);
        task = (TaskFloat) editLocation(task);
        task = (TaskFloat) editTags(task);
        changeFloatTaskType(task);
    }

    public Task editTags(Task task) {
        if (getTags() != null) {
            for (int i = 0; i < getTags().size(); i++) {
                String tag = getTags().get(i);
                task.getTags().add(tag);
            }
        }
        if (removeTags != null) {
            for (int i = 0; i < getTags().size(); i++) {
                String tag = removeTags.get(i);
                task.getTags().remove(tag);
            }
        }
        return task;
    }

    public TaskEvent editStartDate(TaskEvent task) {
        if (getStartDate() != null) {
            task.setStartDate(getEndDate());
        }
        return task;
    }
    
    //for event tasks
    public TaskEvent editEndDate(TaskEvent task) {
        if (getEndDate() != null) {
            task.setEndDate(getEndDate());
        }
        return task;
    }

    //for deadline tasks
    public TaskDeadline editEndDate(TaskDeadline task) {
        if (getEndDate() != null) {
            task.setEndDate(getEndDate());
        }
        return task;
    }

    public Task editLocation(Task task) {
        if (getLocation() != null) {
            task.setLocation(getLocation());
        }
        return task;
    }

    public Task editDescription(Task task) {
        if (getDescription() != null) {
            task.setDescription(getDescription());
        }
        return task;
    }

    public void changeEventTaskType(TaskEvent task) {
        if ((getStartDate() != null) && (getEndDate() != null)) {
            //if user wants to remove both start and end dates
            if ((getStartDate().getTimeInMillis() == 0) && (getEndDate().getTimeInMillis() == 0)) {
                Command addCommand = new CommandAddFloatTask(task.getDescription(), task.getLocation(),
                        task.getTags());
                display = addCommand.execute(display);
            }
        } else if (getStartDate() != null) {
            //if user wants to remove both start date
            if (getStartDate().getTimeInMillis() == 0) {
                Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                        task.getEndDate(), task.getTags());
                display = addCommand.execute(display);
            }
        }
    }

    public void changeFloatTaskType(TaskFloat task) {
        if ((getStartDate() != null) && (getEndDate() != null)) {
            Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(),
                    getStartDate(), getEndDate(), task.getTags());
            display = addCommand.execute(display);
        } else if (getEndDate() != null) {
            Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                    getEndDate(), task.getTags());
            display = addCommand.execute(display);
        }
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}