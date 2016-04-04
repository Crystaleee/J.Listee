/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

public class CommandUpdate extends TaskEvent implements Command {
    private Integer _taskNumber;
    private ArrayList<String> _removeTags;
    private Display _display;
    private boolean _timeChanged = false;
    private boolean _updateFile = true;;
    private boolean _saveHistory = true;
    private String msgEdit = "Edited : \"";

    public CommandUpdate() {
        super();
        _removeTags = null;
        this._taskNumber = null;
    }

    public CommandUpdate(Integer taskNumber, String description) {
        super(description, null, null, null, null);
        _removeTags = null;
        this._taskNumber = taskNumber;
    }

    public CommandUpdate(Integer taskNumber, String description, String location, Calendar startDate,
            Calendar endDate, ArrayList<String> addTags, ArrayList<String> removeTags) {
        super(description, location, startDate, endDate, addTags);
        this._removeTags = removeTags;
        _updateFile = true;
        this._taskNumber = taskNumber;
    }

    public Display execute(Display oldDisplay) {
        _display = oldDisplay;
        if (hasInvalidTaskNumber()) {
            setInvalidDisplay(GlobalConstants.MESSAGE_ERROR_TASK_NUMBER);
            return _display;
        }
        if (isInvalidDateRange()) {
            setInvalidDisplay(GlobalConstants.MESSAGE_ERROR_DATE_RANGE);
            return oldDisplay;
        }
        editTask();
        /*
         * for (int i = 0; i < display.getConflictingTasksIndices().size(); i++)
         * { System.out.println("conflict " +
         * display.getConflictingTasksIndices().get(i)); }
         */
        // System.out.println("Index " + display.getTaskIndices().get(0));
        _display.setMessage(msgEdit);
        return _display;
    }

    // returns true if end date is before start date
    private boolean isInvalidDateRange() {
        if ((getStartDate() != null) && (getEndDate() != null)) {
            if (getStartDate().after(getEndDate())) {
                return true;
            }
        }
        return false;
    }

    private void setInvalidDisplay(String msg) {
        _updateFile = false;
        _saveHistory = false;
        _display.setMessage(msg);
        _display.setTaskIndices(new ArrayList<Integer>());
        _display.setConflictingTasksIndices(new ArrayList<Integer>());
    }

    private boolean hasInvalidTaskNumber() {
        int numOfTasks = 0;
        numOfTasks += _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().size()
                + _display.getVisibleFloatTasks().size() + _display.getVisibleReservedTasks().size();
        return ((_taskNumber > numOfTasks) || (_taskNumber < 1));
    }

    private void editTask() {
        if (_taskNumber <= _display.getVisibleDeadlineTasks().size()) {
            editDeadline();
        } else {
            _taskNumber -= _display.getVisibleDeadlineTasks().size();
            if (_taskNumber <= _display.getVisibleEvents().size()) {
                editEvent();
            } else {
                _taskNumber -= _display.getVisibleEvents().size();
                if (_taskNumber <= _display.getVisibleFloatTasks().size()) {
                    editFloat();
                } else {
                    _taskNumber -= _display.getVisibleFloatTasks().size();
                    editReserved();
                }
            }
        }
    }

    private void editDeadline() {
        TaskDeadline task = _display.getVisibleDeadlineTasks().remove(_taskNumber - 1);
        _display.getDeadlineTasks().remove(task);
        msgEdit += task.getDescription() + "\"";
        task = (TaskDeadline) editDescription(task);
        task = (TaskDeadline) editLocation(task);
        task = (TaskDeadline) editTags(task);
        task = editEndDate(task);
        changeDeadlineTaskType(task);
    }

    private void editEvent() {
        TaskEvent task = _display.getVisibleEvents().remove(_taskNumber - 1);
        _display.getEventTasks().remove(task);
        msgEdit += task.getDescription() + "\"";
        task = (TaskEvent) editDescription(task);
        task = (TaskEvent) editLocation(task);
        task = (TaskEvent) editTags(task);
        task = editStartDate(task);
        task = editEndDate(task);
        changeEventTaskType(task);

    }

    private void editFloat() {
        TaskFloat task = _display.getVisibleFloatTasks().get(_taskNumber - 1);
        msgEdit += task.getDescription() + "\"";
        task = (TaskFloat) editDescription(task);
        task = (TaskFloat) editLocation(task);
        task = (TaskFloat) editTags(task);
        int index = _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().size()
                + _display.getVisibleFloatTasks().indexOf(task) + 1;
        // System.out.println(index);
        _display.getTaskIndices().add(index);
        if (hasChangeFloatTaskType(task)) {
            _display.getVisibleFloatTasks().remove(_taskNumber - 1);
            _display.getFloatTasks().remove(task);
        }
    }

    private void editReserved() {
        TaskReserved task = _display.getReservedTasks().get(_taskNumber - 1);
        msgEdit += task.getDescription() + "\"";
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
                _display = addCommand.execute(_display);
                hasTaskChanged = true;
            }
        }
        if (getStartDate() != null) {
            if ((getStartDate().getTimeInMillis() != 0) && (!hasTaskChanged)) {
                Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(),
                        getStartDate(), task.getEndDate(), task.getTags());
                _display = addCommand.execute(_display);
                hasTaskChanged = true;
            }
        }

        if (!hasTaskChanged) {
            Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                    task.getEndDate(), task.getTags());
            _display = addCommand.execute(_display);
        }
        return hasTaskChanged;
    }

    private Task editTags(Task task) {
        if (_removeTags != null) {
            for (int i = 0; i < _removeTags.size(); i++) {
                String tag = _removeTags.get(i);
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
            _timeChanged = true;
        }
        return task;
    }

    private TaskEvent editEndDate(TaskEvent task) {
        if (getEndDate() != null) {
            task.setEndDate(getEndDate());
            _timeChanged = true;
        }
        return task;
    }

    private TaskDeadline editEndDate(TaskDeadline task) {
        if (getEndDate() != null) {
            task.setEndDate(getEndDate());
        }
        return task;
    }

    private Task editLocation(Task task) {
        if (getLocation() != null) {
            if (getLocation().trim().isEmpty()) {
                task.setLocation(null);
            } else {
                task.setLocation(getLocation().trim());
            }
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
                _display = addCommand.execute(_display);
                hasTaskChanged = true;
            }
        }
        if (getStartDate() != null) {
            if ((getStartDate().getTimeInMillis() == 0) && (!hasTaskChanged)) {
                Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                        task.getEndDate(), task.getTags());
                _display = addCommand.execute(_display);
                hasTaskChanged = true;
            }
        }

        if (!hasTaskChanged) {
            Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(),
                    task.getStartDate(), task.getEndDate(), task.getTags());
            _display = addCommand.execute(_display);
            if (!_timeChanged) {
                _display.setConflictingTasksIndices(new ArrayList<Integer>());
            }
        }
        return;
    }

    private boolean hasChangeFloatTaskType(TaskFloat task) {
        boolean hasTaskChanged = false;
        if ((getStartDate() != null) && (getEndDate() != null)) {
            if ((getStartDate().getTimeInMillis() != 0) && (getEndDate().getTimeInMillis() != 0)) {
                Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(),
                        getStartDate(), getEndDate(), task.getTags());
                _display = addCommand.execute(_display);
                hasTaskChanged = true;
            }
        } else {
            if (getEndDate() != null) {
                if (getEndDate().getTimeInMillis() != 0) {
                    Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                            getEndDate(), task.getTags());
                    _display = addCommand.execute(_display);
                    hasTaskChanged = true;
                }
            }
        }
        return hasTaskChanged;
    }

    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}
