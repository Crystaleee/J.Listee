/*
 * @@author A0139995E
 */
package entity;
/**
 * This command is to add a reserved task.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandAddReserved implements Command {
    private TaskReserved _task;
    private boolean _updateFile = true;
    private boolean _saveHistory = true;
    private ArrayList<Integer> _conflictingTasksIndices = new ArrayList<Integer>();
    private Logger _logger = GlobalLogger.getLogger();

    public CommandAddReserved() {
        _task = null;
    }

    public CommandAddReserved(TaskReserved task) {
        this._task = task;
    }

    public CommandAddReserved(String description, String location, ArrayList<Calendar> startDates,
            ArrayList<Calendar> endDates, ArrayList<String> tags) {
        _task = new TaskReserved(description, location, startDates, endDates, tags);
    }

    public Display execute(Display display) {
        assert display != null: "AddReserved: null display";
        if (hasNoDescription()) {
            _logger.log(Level.INFO, "AddReserved: No desc");
            setInvalidDisplay(display, GlobalConstants.MESSAGE_ERROR_DESCRIPTION);
            return display;
        }
        if (containsInvalidTimeSlots()) {
            _logger.log(Level.INFO, "AddReserved: Invalid time");
            setInvalidDisplay(display, GlobalConstants.MESSAGE_ERROR_TIME_RANGE);
            return display;
        }
        display.getReservedTasks().add(_task);
        if (!display.getVisibleReservedTasks().equals(display.getReservedTasks())) {
            display.getVisibleReservedTasks().add(_task);
        }
        setDisplay(display);
        return display;
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
        int index = display.getVisibleReservedTasks().indexOf(_task)
                + display.getVisibleDeadlineTasks().size() + display.getVisibleEvents().size()
                + display.getVisibleFloatTasks().size() + 1;
        taskIndices.add(index);
        display.setTaskIndices(taskIndices);
        display.setMessage(String.format(GlobalConstants.MESSAGE_RESERVED, _task.getDescription()));
        getConflictingTasks(display);
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
            checkReservedTask: for (int i = 0; i < myTask.getStartDates().size(); i++) {
                for (int j = 0; j < _task.getStartDates().size(); j++) {
                    if (!myTask.equals(_task)) {
                        if (isWithinTimeRange(_task.getStartDates().get(j), _task.getEndDates().get(j),
                                myTask.getStartDates().get(i), myTask.getEndDates().get(i))) {
                            int index = display.getVisibleReservedTasks().indexOf(myTask);
                            if (isValidIndex(index)) {
                                index = getConflictingTaskReservedIndex(display, index);
                                _conflictingTasksIndices.add(index);
                            }
                            break checkReservedTask;
                        }
                    }
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
            for (int i = 0; i < _task.getStartDates().size(); i++) {
                if (isWithinTimeRange(_task.getStartDates().get(i), _task.getEndDates().get(i),
                        myTask.getStartDate(), myTask.getEndDate())) {
                    int index = display.getVisibleEvents().indexOf(myTask);
                    if (isValidIndex(index)) {
                        index = getConflictingTaskEventIndex(display, index);
                        _conflictingTasksIndices.add(index);
                    }
                    break;
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
    
    private boolean containsInvalidTimeSlots() {
        if (_task.getStartDates() == null) {
            return true;
        } else if (_task.getEndDates() == null) {
            return true;
        }
        if(_task.getStartDates().size() != _task.getEndDates().size()){
            return true;
        }
        if((_task.getStartDates().isEmpty()) || (_task.getEndDates().isEmpty())){
            return true;
        }
        for (int i = 0; i < _task.getStartDates().size(); i++) {
            if (_task.getStartDates().get(i).after(_task.getEndDates().get(i))) {
                return true;
            }
        }
        return false;
    }
    
    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}
