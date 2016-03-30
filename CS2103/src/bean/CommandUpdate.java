/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
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

    public CommandUpdate(Integer taskNumber, String description, String location, Calendar startDate,
            Calendar endDate, ArrayList<String> addTags, ArrayList<String> removeTags) {
        super(description, location, startDate, endDate, addTags);
        this.removeTags = removeTags;
        updateFile = true;
        this.taskNumber = taskNumber;
    }

    public Display execute(Display oldDisplay) {
        display = oldDisplay;
        if (hasInvalidTaskNumber()) {
            updateFile = false;
            saveHistory = false;
            display.setMessage(message_invalid_task_number);
            return display;
            // return (new Display(message_invalid_task_number));
        }
        if ((getStartDate() != null) && (getEndDate() != null)) {
            if (getStartDate().after(getEndDate())) {
                updateFile = false;
                saveHistory = false;
                oldDisplay.setMessage("Please specify a valid date range");
                return oldDisplay;

            }
        }
        editTask();
        display.setMessage(message);
        return display;
    }

    private boolean hasInvalidTaskNumber() {
        int numOfTasks = 0;
        numOfTasks += display.getVisibleDeadlineTasks().size() + display.getVisibleEvents().size()
                + display.getVisibleFloatTasks().size() + display.getVisibleReservedTasks().size();
        return ((taskNumber > numOfTasks) || (taskNumber < 1));
    }

    private void editTask() {
        if (taskNumber <= display.getVisibleDeadlineTasks().size()) {
            editDeadline();
        } else {
            taskNumber -= display.getVisibleDeadlineTasks().size();
            if (taskNumber <= display.getVisibleEvents().size()) {
                editEvent();
            } else {
                taskNumber -= display.getVisibleEvents().size();
                if (taskNumber <= display.getVisibleFloatTasks().size()) {
                    editFloat();
                } else {
                    taskNumber -= display.getVisibleFloatTasks().size();
                    editReserved();
                }
            }
        }
    }

    private void editDeadline() {
        TaskDeadline task = display.getVisibleDeadlineTasks().remove(taskNumber - 1);
        display.getDeadlineTasks().remove(task);
        message += task.getDescription() + "\"";
        task = (TaskDeadline) editDescription(task);
        task = (TaskDeadline) editLocation(task);
        task = (TaskDeadline) editTags(task);
        task = editEndDate(task);
        changeDeadlineTaskType(task);
    }

    private void editEvent() {
        TaskEvent task = display.getVisibleEvents().remove(taskNumber - 1);
        display.getEventTasks().remove(task);
        message += task.getDescription() + "\"";
        task = (TaskEvent) editDescription(task);
        task = (TaskEvent) editLocation(task);
        task = (TaskEvent) editTags(task);
        task = editStartDate(task);
        task = editEndDate(task);
        changeEventTaskType(task);

    }

    private void editFloat() {
        TaskFloat task = display.getVisibleFloatTasks().get(taskNumber - 1);
        message += task.getDescription() + "\"";
        task = (TaskFloat) editDescription(task);
        task = (TaskFloat) editLocation(task);
        task = (TaskFloat) editTags(task);
        if (hasChangeFloatTaskType(task)) {
            display.getVisibleFloatTasks().remove(taskNumber - 1);
            display.getFloatTasks().remove(task);
        }
    }

    private void editReserved() {
        TaskReserved task = display.getReservedTasks().get(taskNumber - 1);
        message += task.getDescription() + "\"";
        task = (TaskReserved) editDescription(task);
        task = (TaskReserved) editLocation(task);
        task = (TaskReserved) editTags(task);

    }

    private boolean changeDeadlineTaskType(TaskDeadline task) {
        // assertFalse endDate==0 AND startDate != 0&null
        boolean hasTaskChanged = false;
        if (getEndDate() != null) {
            if (getEndDate().getTimeInMillis() == 0) {
                Command addCommand = new CommandAddFloatTask(task.getDescription(), task.getLocation(),
                        task.getTags());
                display = addCommand.execute(display);
                hasTaskChanged = true;
            }
        }
        if (getStartDate() != null) {
            if ((getStartDate().getTimeInMillis() != 0) && (!hasTaskChanged)) {
                Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(),
                        getStartDate(), task.getEndDate(), task.getTags());
                display = addCommand.execute(display);
                hasTaskChanged = true;
            }
        }

        if (!hasTaskChanged) {
            Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                    task.getEndDate(), task.getTags());
            display = addCommand.execute(display);
        }
        return hasTaskChanged;
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
                Command addCommand = new CommandAddFloatTask(task.getDescription(), task.getLocation(),
                        task.getTags());
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
            Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(),
                    task.getStartDate(), task.getEndDate(), task.getTags());
            display = addCommand.execute(display);
        }
        return;
    }

    private boolean hasChangeFloatTaskType(TaskFloat task) {
        boolean hasTaskChanged = false;
        if ((getStartDate() != null) && (getEndDate() != null)) {
            if ((getStartDate().getTimeInMillis() != 0) && (getEndDate().getTimeInMillis() != 0)) {
                Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(),
                        getStartDate(), getEndDate(), task.getTags());
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
