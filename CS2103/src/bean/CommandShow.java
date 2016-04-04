/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CommandShow implements Command {
    private String msgShow = "Displaying ";
    private boolean _updateFile = false;
    private boolean _saveHistory = true;
    private TaskEvent _searchedTask;
    private Display _display;
    private ArrayList<String> _taskTypes;

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
        // System.out.println(searchedTask.getDescription());
        initialiseDisplay(oldDisplay);
        if (isShowAll()) {
            setShowAll(oldDisplay);
            return oldDisplay;
        }
        if (isInvalidDateRange()) {
            setInvalidDisplay(oldDisplay);
            return oldDisplay;

        }
        this._display = oldDisplay.deepClone();

        showTasks();
        if (noTasksFound()) {
            oldDisplay.setMessage(GlobalConstants.MESSAGE_NO_TASKS);
            return oldDisplay;
        } else {
            _display.setMessage(getFeedback());
        }

        return _display;
    }

    private boolean noTasksFound() {
        int numVisible = _display.getVisibleCompletedTasks().size() + _display.getVisibleDeadlineTasks().size()
                + _display.getVisibleEvents().size() + _display.getVisibleFloatTasks().size()
                + _display.getVisibleReservedTasks().size();
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

    private String getFeedback() {
        if (!_taskTypes.isEmpty()) {
            for (int i = 0; i < _taskTypes.size(); i++) {
                msgShow += _taskTypes.get(i) + " ";
            }
            msgShow += "tasks";
        } else {
            msgShow += "all tasks";
        }
        if (!_searchedTask.getDescription().isEmpty()) {
            msgShow += " containing " + _searchedTask.getDescription();
        }
        if (!_searchedTask.getLocation().isEmpty()) {
            msgShow += " at " + _searchedTask.getLocation();
        }
        if ((_searchedTask.getStartDate() != null) && (_searchedTask.getEndDate() != null)) {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yy HH:mm");
            String startDate = format1.format(_searchedTask.getStartDate().getTime());
            String endDate = format1.format(_searchedTask.getEndDate().getTime());

            msgShow += " from " + startDate + " to " + endDate;
        }
        if (!_searchedTask.getTags().isEmpty()) {
            msgShow += " tagged";
            for (int i = 0; i < _searchedTask.getTags().size(); i++) {
                if (i == 0) {
                    msgShow += " " + _searchedTask.getTags().get(i);
                } else {
                    msgShow += ", " + _searchedTask.getTags().get(i);
                }
            }
        }
        return msgShow;
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
        // System.out.println("D" +
        // oldDisplay.getVisibleDeadlineTasks().size());
    }

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
        // System.out.println("E" + oldDisplay.getVisibleEvents().size());
    }

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
        // System.out.println("F" + oldDisplay.getVisibleFloatTasks().size());
    }

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
        // System.out.println("R" +
        // oldDisplay.getVisibleReservedTasks().size());
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
        String[] keywords = _searchedTask.getDescription().split(" ");
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
