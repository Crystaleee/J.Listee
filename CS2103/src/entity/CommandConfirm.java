/*
 * @@author A0139995E
 */
package entity;
/**
 * This command is to confirm a timeslot of a reserved task
 * and convert it to an event
 */
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandConfirm implements Command {
    private Integer _timeSlotIndex;
    private Integer _taskNumber;
    private TaskReserved task;
    private String msg;
    private int index;
    private boolean _saveHistory = true;
    private boolean _updateFile = true;
    private Logger logger = GlobalLogger.getLogger();

    public CommandConfirm() {
        this._taskNumber = null;
    }

    public CommandConfirm(Integer taskNumbers, Integer timeSlotIndex) {
        this._taskNumber = taskNumbers;
        this._timeSlotIndex = timeSlotIndex;
    }

    public Display execute(Display display) {
        assert display != null: "Confirm: null display";
        if (hasInvalidParameters(display)) {
            logger.log(Level.INFO, "Confirm: Invalid Parameters");
            setInvalidDisplay(display);
            return display;
        } else {
            confirmTask(display);
        }
        return display;
    }

    private void confirmTask(Display display) {
        Calendar start = task.getStartDates().get(index);
        Calendar end = task.getEndDates().get(index);
        TaskEvent newTask = new TaskEvent(task.getDescription(), task.getLocation(), start, end, task.getTags());
        Command addEvent = new CommandAddEvent(newTask);
        addEvent.execute(display);
        display.getVisibleReservedTasks().remove(task);
        display.getReservedTasks().remove(task);
    }

    /*
     * sets variables when the command has invalid parametersS
     */
    private void setInvalidDisplay(Display display) {
        _saveHistory = false;
        _updateFile = false;
        display.setTaskIndices(null);
        display.setConflictingTasksIndices(null);
        display.setMessage(msg);
    }

    /*
     * checks for invalid parameters
     */
    private boolean hasInvalidParameters(Display display) {
        if (display.getReservedTasks().size() == 0) {
            msg = GlobalConstants.MESSAGE_ERROR_NO_RESERVED_TASKS;
            return true;
        } else if (display.getVisibleReservedTasks().size() == 0) {
            msg = GlobalConstants.MESSAGE_ERROR_NO_VISIBLE_RESERVED_TASKS;
            return true;
        }
        if (hasNoTaskNumber()) {
            msg = GlobalConstants.MESSAGE_ERROR_NO_NUMBER;
            return true;
        } else if (isInvalidTaskNumber(display)) {
            msg = GlobalConstants.MESSAGE_ERROR_INVALID_INDEX;
            return true;
        }
        if (hasNoTimeSlot()) {
            msg = GlobalConstants.MESSAGE_ERROR_NO_TIMESLOT;
            return true;
        } else if (isInvalidTimeSlot(display)) {
            msg = GlobalConstants.MESSAGE_ERROR_INVALID_TIMESLOT;
            return true;
        }
        return false;
    }

    private boolean hasNoTimeSlot() {
        return (_timeSlotIndex == null);
    }

    private boolean isInvalidTimeSlot(Display display) {
        index = _taskNumber - display.getVisibleDeadlineTasks().size() - display.getVisibleEvents().size()
                - display.getVisibleFloatTasks().size() - 1;
        task = display.getVisibleReservedTasks().get(index);
        _timeSlotIndex--;
        if((_timeSlotIndex < 0) || (_timeSlotIndex >= task.getStartDates().size())){
            return true;
        }
        return false;
    }

    /*
     * This method will check if the task selected is 
     * a reserved task or if the task number is invalid
     */
    private boolean isInvalidTaskNumber(Display display) {
        int minIndex = display.getVisibleDeadlineTasks().size() + display.getVisibleEvents().size()
                + display.getVisibleFloatTasks().size() + 1;
        int maxIndex = display.getVisibleDeadlineTasks().size() + display.getVisibleEvents().size()
                + display.getVisibleFloatTasks().size() + display.getVisibleReservedTasks().size();
        if ((_taskNumber < minIndex) || (_taskNumber > maxIndex)) {
            return true;
        }
        return false;
    }

    private boolean hasNoTaskNumber() {
        return (_taskNumber == null);
    }

    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}
