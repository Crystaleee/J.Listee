/**
 * @@author A0139995E
 */
package entity;
/**
 * This command is to add an event task.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandAddEvent implements Command {
    private TaskEvent _task;
    private boolean _updateFile = true;
    private boolean _saveHistory = true;
    private ArrayList<Integer> _conflictingTasksIndices = new ArrayList<Integer>();
    private Logger _logger = GlobalLogger.getLogger();

    public CommandAddEvent() {
        _task = null;
    }

    public CommandAddEvent(TaskEvent task) {
        this._task = task;
    }

    public CommandAddEvent(String description, String location, Calendar startDate, Calendar endDate,
            ArrayList<String> tags) {
        _task = new TaskEvent(description, location, startDate, endDate, tags);
    }

    public Display execute(Display display) {
        assert display != null: "AddEvent: null display";
        
        if (hasNoDescription()) {
            _logger.log(Level.INFO, "AddEvent: No desc");
            setInvalidDisplay(display, GlobalConstants.MESSAGE_ERROR_DESCRIPTION);
            return display;
        }
        if (isInvalidTimeRange()) {
            _logger.log(Level.INFO, "AddEvent: Invalid time");
            setInvalidDisplay(display, GlobalConstants.MESSAGE_ERROR_DATE_RANGE);
            return display;
        }
        addEvent(display.getEventTasks());
        if (!display.getVisibleEvents().equals(display.getEventTasks())) {
            addEvent(display.getVisibleEvents());
        }
        setDisplay(display);
        return display;
    }

    private boolean isInvalidTimeRange() {
        return _task.getStartDate().after(_task.getEndDate());
    }

    private boolean hasNoDescription() {
        if (_task.getDescription() == null) {
            return true;
        } else {
            _task.setDescription(_task.getDescription().trim());
            if (_task.getDescription().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * sets variables when the command has invalid parametersS
     */
    private void setInvalidDisplay(Display display, String msg) {
        _updateFile = false;
        _saveHistory = false;
        display.setCommandType(GlobalConstants.GUI_ANIMATION_INVALID);
        display.setMessage(msg);
    }

    private void setDisplay(Display display) {
        ArrayList<Integer> taskIndices = new ArrayList<Integer>();
        display.setCommandType(GlobalConstants.GUI_ANIMATION_ADD);
        int index = getAddedTaskIndex(display);
        taskIndices.add(index);
        display.setTaskIndices(taskIndices);
        display.setMessage(String.format(GlobalConstants.MESSAGE_ADD_SUCCESS, _task.getDescription()));
        getConflictingTasks(display);
        setIfOverdue();
    }

    private void setIfOverdue() {
        if(_task.getEndDate().before(Calendar.getInstance())){
            _task.setIsOverdue(true);
        }
    }

    private void getConflictingTasks(Display display) {
        getConflictingEvents(display);
        getConflictingReservedTasks(display);
        display.setConflictingTasksIndices(_conflictingTasksIndices);
    }


    /**
     * This method searches for conflicting reserved tasks
     */
    private void getConflictingReservedTasks(Display display) {
        ArrayList<TaskReserved> listReserved = display.getReservedTasks();
        for (TaskReserved myTask : listReserved) {
            for (int i = 0; i < myTask.getStartDates().size(); i++) {
                if (isWithinTimeRange(_task.getStartDate(), _task.getEndDate(), myTask.getStartDates().get(i),
                        myTask.getEndDates().get(i))) {
                    int index = display.getVisibleReservedTasks().indexOf(myTask);
                    if (isValidIndex(index)) {
                        index = getConflictingTaskReservedIndex(display, index);
                        _conflictingTasksIndices.add(index);
                    }
                    break;
                }
            }
        }
    }

    private int getConflictingTaskReservedIndex(Display display, int index) {
        return index + display.getVisibleDeadlineTasks().size() + display.getVisibleEvents().size()
                + display.getVisibleFloatTasks().size() + 1;
    }

    private boolean isValidIndex(int index) {
        return index >= 0;
    }
    
    /**
     * This method searches for conflicting events
     */
    private void getConflictingEvents(Display display) {
        ArrayList<TaskEvent> listEvents = display.getEventTasks();
        for (TaskEvent myTask : listEvents) {
            if (!myTask.equals(_task)) {
                if (isWithinTimeRange(_task.getStartDate(), _task.getEndDate(), myTask.getStartDate(),
                        myTask.getEndDate())) {
                    int index = display.getVisibleEvents().indexOf(myTask);
                    if (isValidIndex(index)) {
                        index = getConflictingTaskEventIndex(display, index);
                        _conflictingTasksIndices.add(index);
                    }
                }
            }
        }
    }

    private int getConflictingTaskEventIndex(Display display, int index) {
        return index + display.getVisibleDeadlineTasks().size() + 1;
    }

    private boolean isWithinTimeRange(Calendar start, Calendar end, Calendar rangeStart, Calendar rangeEnd) {
        if (!start.before(rangeStart)) {
            if (!start.after(rangeEnd)) {
                return true;
            }
        } else if (!end.before(rangeStart)) {
            return true;
        }
        return false;
    }

    private int getAddedTaskIndex(Display display) {
        return display.getVisibleEvents().indexOf(_task) + display.getVisibleDeadlineTasks().size() + 1;
    }

    private void addEvent(ArrayList<TaskEvent> taskList) {
        int index = getAddIndex(taskList);
        taskList.add(index, _task);
    }

    /**
     * This method searches for the index to slot the deadline task in since we
     * are sorting the list in order of earliest start time first
     */
    private int getAddIndex(ArrayList<TaskEvent> taskList) {
        int i = 0;
        for (i = 0; i < taskList.size(); i++) {
            if (_task.getStartDate().compareTo(taskList.get(i).getStartDate()) < 0) {
                break;
            }
        }
        return i;
    }

    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}
