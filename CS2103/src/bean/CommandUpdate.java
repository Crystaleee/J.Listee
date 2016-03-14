/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 3:20am
 * CS2103
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

public class CommandUpdate extends TaskEvent implements Command {
    private Integer taskNumber;
    private Display display;
    private boolean updateFile;
    private final String message_invalid_task_number = "Pls specify a valid task number";

    public CommandUpdate() {
        super();
        updateFile = true;
        this.taskNumber = null;
    }

    public CommandUpdate(Integer taskNumber, String description) {
        super(description, null, null, null, null);
        updateFile = true;
        this.taskNumber = taskNumber;
    }

    public CommandUpdate(Integer taskNumber, String description, String location, Calendar startDate,
            Calendar endDate, ArrayList<String> tags) {
        super(description, location, startDate, endDate, tags);
        updateFile = true;
        this.taskNumber = taskNumber;
    }

    public Display execute(Display oldDisplay) {
        display = oldDisplay;
        if (hasInvalidTaskNumber(display.getNumberOfTasks())) {
            updateFile = false;
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
        if (getDescription() != null) {
            display.getEventTasks().get(taskNumber - 1).setDescription(getDescription());
        }
        if (getLocation() != null) {
            display.getEventTasks().get(taskNumber - 1).setLocation(getLocation());
        }/*
        TaskDeadline task;
        if ((getStartDate().getTimeInMillis() == 0) && (getEndDate().getTimeInMillis() != 0)) {
            task = display.getDeadlineTasks().remove(taskNumber - 1);
            Command addCommand = new CommandAddFloatTask(task.getDescription(), task.getLocation(),
                    task.getTags());
            display = addCommand.execute(display);
        } else if (getStartDate().getTimeInMillis() == 0) {
            task = display.getEventTasks().remove(taskNumber - 1);
            Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                    task.getEndDate(), task.getTags());
            display = addCommand.execute(display);
        }*/
    }

    public void editEvent() {
        if (getDescription() != null) {
            display.getEventTasks().get(taskNumber - 1).setDescription(getDescription());
        }
        if (getLocation() != null) {
            display.getEventTasks().get(taskNumber - 1).setLocation(getLocation());
        }
        //changeEventTaskType();
    }

    public void editFloat() {
        if (getDescription() != null) {
            //display.getFloatTasks().get(taskNumber - 1).setDescription(getDescription());
        }
        if (getLocation() != null) {
            display.getFloatTasks().get(taskNumber - 1).setLocation(getLocation());
        }
        //changeFloatTaskType();
    }

    public void changeEventTaskType() {
        TaskEvent task;
        if ((getStartDate().getTimeInMillis() == 0) && (getEndDate().getTimeInMillis() != 0)) {
            task = display.getEventTasks().remove(taskNumber - 1);
            Command addCommand = new CommandAddFloatTask(task.getDescription(), task.getLocation(),
                    task.getTags());
            display = addCommand.execute(display);
        } else if (getStartDate().getTimeInMillis() == 0) {
            task = display.getEventTasks().remove(taskNumber - 1);
            Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                    task.getEndDate(), task.getTags());
            display = addCommand.execute(display);
        }
    }

    public void changeFloatTaskType() {
        TaskFloat task;
        if ((getStartDate() != null) && (getEndDate() != null)) {
            task = display.getFloatTasks().remove(taskNumber - 1);
            Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(),
                    getStartDate(), getEndDate(), task.getTags());
            display = addCommand.execute(display);
        } else if (getEndDate() != null) {
            task = display.getFloatTasks().remove(taskNumber - 1);
            Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                    getEndDate(), task.getTags());
            display = addCommand.execute(display);
        }
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}