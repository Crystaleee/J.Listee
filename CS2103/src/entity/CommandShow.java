/*
 * @@author A0139995E
 */
package entity;

/**
 * This command is for filtering the visible tasks
 * to match certain criteria
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandShow implements Command {
    private String _msgShow = "Displaying ";
    private boolean _updateFile = false;
    private boolean _saveHistory = true;
    private TaskEvent _searchedTask;
    private Display _display;
    private ArrayList<String> _taskTypes;
    private Logger _logger = GlobalLogger.getLogger();

    public CommandShow() {
        _searchedTask = null;
    }

    public CommandShow(String keyword) {
        _searchedTask = new TaskEvent(keyword.trim().toLowerCase(), GlobalConstants.EMPTY_STRING, null, null,
                new ArrayList<String>());
        _taskTypes = new ArrayList<String>();
    }

    public CommandShow(String keyword, String location, Calendar start, Calendar end,
            ArrayList<String> tags) {
        if (keyword == null) {
            keyword = GlobalConstants.EMPTY_STRING;
        }
        if (location == null) {
            location = GlobalConstants.EMPTY_STRING;
        }
        if (tags == null) {
            tags = new ArrayList<String>();
        }
        _searchedTask = new TaskEvent(keyword.trim().toLowerCase(), location.trim().toLowerCase(), start, end,
                tags);
        _taskTypes = new ArrayList<String>();
    }

    public CommandShow(String keyword, String location, Calendar start, Calendar end, ArrayList<String> tags,
            ArrayList<String> taskTypes) {
        if (keyword == null) {
            keyword = GlobalConstants.EMPTY_STRING;
        }
        if (location == null) {
            location = GlobalConstants.EMPTY_STRING;
        }
        if (tags == null) {
            tags = new ArrayList<String>();
        }
        if (taskTypes == null) {
            taskTypes = new ArrayList<String>();
        }
        _searchedTask = new TaskEvent(keyword.trim().toLowerCase(), location.trim().toLowerCase(), start, end,
                tags);
        this._taskTypes = taskTypes;
    }

    public Display execute(Display oldDisplay) {
        assert oldDisplay != null : "Show: null display";
        initialiseDisplay(oldDisplay);
        if (isShowAll()) {
            _logger.log(Level.INFO, "Show: Show all");
            setShowAll(oldDisplay);
            return oldDisplay;
        }
        if (isInvalidDateRange()) {
            _logger.log(Level.INFO, "Show: Invalid time range");
            setInvalidDisplay(oldDisplay);
            return oldDisplay;

        }
        this._display = oldDisplay.deepClone();

        showTasks();
        if (hasNoTasksFound()) {
            _logger.log(Level.INFO, "Show: No tasks");
            oldDisplay.setMessage(GlobalConstants.MESSAGE_NO_TASKS);
            return oldDisplay;
        } else {
            _logger.log(Level.INFO, "Show: No errors");
            _display.setMessage(getFeedback());
        }

        return _display;
    }

    /**
     * This method checks if there are no tasks matching the filter
     */
    private boolean hasNoTasksFound() {
        int numVisible = _display.getVisibleCompletedTasks().size()
                + _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().size()
                + _display.getVisibleFloatTasks().size() + _display.getVisibleReservedTasks().size();
        return numVisible == 0;
    }

    private void initialiseDisplay(Display oldDisplay) {
        oldDisplay.setTaskIndices(new ArrayList<Integer>());
        oldDisplay.setConflictingTasksIndices(new ArrayList<Integer>());
    }

    private void setInvalidDisplay(Display oldDisplay) {
        _updateFile = false;
        _saveHistory = false;
        oldDisplay.setMessage(GlobalConstants.MESSAGE_ERROR_DATE_RANGE);
    }

    // returns true if end date is before start date
    private boolean isInvalidDateRange() {
        if ((_searchedTask.getStartDate() != null) && (_searchedTask.getEndDate() != null)) {
            if (_searchedTask.getStartDate().after(_searchedTask.getEndDate())) {
                return true;
            }
        }
        return false;
    }

    private void setShowAll(Display oldDisplay) {
        oldDisplay.setMessage(GlobalConstants.MESSAGE_SHOW_ALL);
        oldDisplay.setVisibleDeadlineTasks(oldDisplay.getDeadlineTasks());
        oldDisplay.setVisibleEvents(oldDisplay.getEventTasks());
        oldDisplay.setVisibleFloatTasks(oldDisplay.getFloatTasks());
        oldDisplay.setVisibleReservedTasks(oldDisplay.getReservedTasks());
        oldDisplay.setVisibleCompletedTasks(new ArrayList<Task>());
        initialiseDisplay(oldDisplay);
    }

    private boolean isShowAll() {
        if (_searchedTask.getDescription().isEmpty()) {
            if (_searchedTask.getLocation().isEmpty()) {
                if ((_searchedTask.getStartDate() == null) && (_searchedTask.getEndDate() == null)) {
                    if (_searchedTask.getTags().isEmpty()) {
                        if (_taskTypes.isEmpty()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * This method sets the feedback to the user
     */
    private String getFeedback() {
        if (!_taskTypes.isEmpty()) {
            for (int i = 0; i < _taskTypes.size(); i++) {
                _msgShow += _taskTypes.get(i) + GlobalConstants.SPACE;
            }
            _msgShow += GlobalConstants.TASKS;
        } else {
            _msgShow += GlobalConstants.ALL_TASKS;
        }
        if (!_searchedTask.getDescription().isEmpty()) {
            _msgShow += GlobalConstants.CONTAINING + _searchedTask.getDescription();
        }
        if (!_searchedTask.getLocation().isEmpty()) {
            _msgShow += GlobalConstants.AT + _searchedTask.getLocation();
        }
        if ((_searchedTask.getStartDate() != null) && (_searchedTask.getEndDate() != null)) {
            SimpleDateFormat format1 = new SimpleDateFormat(GlobalConstants.DATE_FORMAT);
            String startDate = format1.format(_searchedTask.getStartDate().getTime());
            String endDate = format1.format(_searchedTask.getEndDate().getTime());

            _msgShow += GlobalConstants.FROM + startDate + GlobalConstants.TO + endDate;
        }
        if (!_searchedTask.getTags().isEmpty()) {
            _msgShow += GlobalConstants.TAGGED;
            for (int i = 0; i < _searchedTask.getTags().size(); i++) {
                if (i == 0) {
                    _msgShow += GlobalConstants.SPACE + _searchedTask.getTags().get(i);
                } else {
                    _msgShow += GlobalConstants.COMMA_SPACE + _searchedTask.getTags().get(i);
                }
            }
        }
        return _msgShow;
    }

    private void showTasks() {
        getFloatTasks();
        getEventTasks();
        getDeadLineTasks();
        getReservedTasks();
        getTaskType();
        return;
    }

    private void getTaskType() {
        if (!isTaskTypesEmpty()) {
            for (int i = 0; i < _taskTypes.size(); i++) {
                _taskTypes.set(i, _taskTypes.get(i).toLowerCase());
            }
            if (!_taskTypes.contains(GlobalConstants.TASK_TYPE_EVENT)) {
                _display.setVisibleEvents(new ArrayList<TaskEvent>());
            }
            if (!_taskTypes.contains(GlobalConstants.TASK_TYPE_DEADLINE)) {
                _display.setVisibleDeadlineTasks(new ArrayList<TaskDeadline>());
            }
            if (!_taskTypes.contains(GlobalConstants.TASK_TYPE_FLOAT)) {
                _display.setVisibleFloatTasks(new ArrayList<TaskFloat>());
            }
            if (!_taskTypes.contains(GlobalConstants.TASK_TYPE_RESERVED)) {
                _display.setVisibleReservedTasks(new ArrayList<TaskReserved>());
            }
            if (_taskTypes.contains(GlobalConstants.TASK_TYPE_COMPLETED)) {
                getCompletedTasks();
            }
        }
    }

    private boolean isTaskTypesEmpty() {
        if (_taskTypes == null) {
            return true;
        } else if (_taskTypes.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Gets completed tasks matching the filter
     */
    private void getCompletedTasks() {
        Task task;
        _display.setVisibleCompletedTasks(new ArrayList<Task>());
        for (int i = 0; i < _display.getCompletedTasks().size(); i++) {
            task = _display.getCompletedTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        if (withinTimeRange(task)) {
                            _display.getVisibleCompletedTasks().add(task);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets deadline tasks matching the filter
     */
    private void getDeadLineTasks() {
        TaskDeadline task;
        _display.setVisibleDeadlineTasks(new ArrayList<TaskDeadline>());
        for (int i = 0; i < _display.getDeadlineTasks().size(); i++) {
            task = _display.getDeadlineTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        if (withinTimeRange(task)) {
                            _display.getVisibleDeadlineTasks().add(task);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets event tasks matching the filter
     */
    private void getEventTasks() {
        TaskEvent task;
        _display.setVisibleEvents(new ArrayList<TaskEvent>());
        for (int i = 0; i < _display.getEventTasks().size(); i++) {
            task = _display.getEventTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        if (withinTimeRange(task)) {
                            _display.getVisibleEvents().add(task);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets float tasks matching the filter
     */
    private void getFloatTasks() {
        TaskFloat task;
        _display.setVisibleFloatTasks(new ArrayList<TaskFloat>());
        for (int i = 0; i < _display.getFloatTasks().size(); i++) {
            task = _display.getFloatTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        _display.getVisibleFloatTasks().add(task);

                    }
                }
            }
        }
    }

    /**
     * Gets reserved tasks matching the filter
     */
    private void getReservedTasks() {
        TaskReserved task;
        _display.setVisibleReservedTasks(new ArrayList<TaskReserved>());
        for (int i = 0; i < _display.getReservedTasks().size(); i++) {
            task = _display.getReservedTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        if (withinTimeRange(task)) {
                            _display.getVisibleReservedTasks().add(task);
                        }
                    }
                }
            }
        }
    }

    private boolean containsTag(Task task) {
        if (_searchedTask.getTags().isEmpty()) {
            return true;
        }
        boolean containsTags = false;
        for (int i = 0; i < _searchedTask.getTags().size(); i++) {
            for (int j = 0; j < task.getTags().size(); j++) {
                containsTags = false;
                if (_searchedTask.getTags().get(i).toLowerCase()
                        .equals(task.getTags().get(j).trim().toLowerCase())) {
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

    private boolean withinTimeRange(Task task) {
        if ((_searchedTask.getStartDate() == null) && (_searchedTask.getEndDate() == null)) {
            return true;
        }
        if (task instanceof TaskEvent) {
            TaskEvent myTask = (TaskEvent) task;
            if (isWithinTimeRange(myTask)) {
                return true;
            }
        } else if (task instanceof TaskDeadline) {
            TaskDeadline myTask = (TaskDeadline) task;
            if (isWithinTimeRange(myTask)) {
                return true;
            }
        }
        return false;
    }

    private boolean withinTimeRange(TaskReserved task) {
        if ((_searchedTask.getStartDate() == null) && (_searchedTask.getEndDate() == null)) {
            return true;
        }
        for (int i = 0; i < task.getStartDates().size(); i++) {
            if (!task.getStartDates().get(i).before(_searchedTask.getStartDate())) {
                if (!task.getStartDates().get(i).after(_searchedTask.getEndDate())) {
                    return true;
                }
            } else if (!task.getEndDates().get(i).before(_searchedTask.getStartDate())) {
                return true;
            }

        }
        return false;
    }

    private boolean isWithinTimeRange(TaskEvent task) {
        if ((_searchedTask.getStartDate() == null) && (_searchedTask.getEndDate() == null)) {
            return true;
        }
        if (!task.getStartDate().before(_searchedTask.getStartDate())) {
            if (!task.getStartDate().after(_searchedTask.getEndDate())) {
                return true;
            }
        } else if (!task.getEndDate().before(_searchedTask.getStartDate())) {
            return true;
        }
        return false;
    }

    private boolean isWithinTimeRange(TaskDeadline task) {
        if ((_searchedTask.getStartDate() == null) && (_searchedTask.getEndDate() == null)) {
            return true;
        }
        if (!task.getEndDate().before(_searchedTask.getStartDate())) {
            if (!task.getEndDate().after(_searchedTask.getEndDate())) {
                return true;
            }
        }
        return false;
    }

    private boolean atLocation(Task task) {
        if (_searchedTask.getLocation().isEmpty()) {
            return true;
        }
        if (task.getLocation() == null) {
            return false;
        }
        if (task.getLocation().equalsIgnoreCase(_searchedTask.getLocation())) {
            return true;
        }
        return false;
    }

    private boolean containsKeyword(Task task) {
        String[] keywords = _searchedTask.getDescription().split(GlobalConstants.SPACE);
        for (int i = 0; i < keywords.length; i++) {
            if (!task.getDescription().toLowerCase().contains(keywords[i])) {
                return false;
            }
        }
        return true;
    }

    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}
