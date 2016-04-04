/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CommandShow implements Command {
    private static final String TASK_TYPE_COMPLETED = "done";
    private static final String TASK_TYPE_RESERVED = "reserved";
    private static final String TASK_TYPE_FLOAT = "untimed";
    private static final String TASK_TYPE_DEADLINE = "deadline";
    private static final String TASK_TYPE_EVENT = "event";
    private static final String EMPTY_STRING = "";
    private static final String MESSAGE_INVALID_DATE_RANGE = "Please specify a valid date range";
    private final String message_no_tasks = "No such tasks found";
    private final String message_show_all = "Displaying all tasks";
    private String message_show = "Displaying ";
    private boolean updateFile = false;
    private boolean saveHistory = true;
    private TaskEvent searchedTask;
    private Display display;
    private ArrayList<String> taskTypes;

    public CommandShow() {
        searchedTask = null;
    }

    public CommandShow(String keyword) {
        searchedTask = new TaskEvent(keyword.trim().toLowerCase(), EMPTY_STRING, null, null,
                new ArrayList<String>());
        taskTypes = new ArrayList<String>();
    }

    public CommandShow(String keyword, String location, Calendar start, Calendar end,
            ArrayList<String> tags) {
        if (keyword == null) {
            keyword = EMPTY_STRING;
        }
        if (location == null) {
            location = EMPTY_STRING;
        }
        if (tags == null) {
            tags = new ArrayList<String>();
        }
        searchedTask = new TaskEvent(keyword.trim().toLowerCase(), location.trim().toLowerCase(), start, end,
                tags);
        taskTypes = new ArrayList<String>();
    }

    public CommandShow(String keyword, String location, Calendar start, Calendar end, ArrayList<String> tags,
            ArrayList<String> taskTypes) {
        if (keyword == null) {
            keyword = EMPTY_STRING;
        }
        if (location == null) {
            location = EMPTY_STRING;
        }
        if (tags == null) {
            tags = new ArrayList<String>();
        }
        if (taskTypes == null) {
            taskTypes = new ArrayList<String>();
        }
        searchedTask = new TaskEvent(keyword.trim().toLowerCase(), location.trim().toLowerCase(), start, end,
                tags);
        this.taskTypes = taskTypes;
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
        this.display = oldDisplay.deepClone();

        showTasks();
        if (noTasksFound()) {
            oldDisplay.setMessage(message_no_tasks);
            return oldDisplay;
        } else {
            display.setMessage(getFeedback());
        }

        return display;
    }

    private boolean noTasksFound() {
        int numVisible = display.getVisibleCompletedTasks().size() + display.getVisibleDeadlineTasks().size()
                + display.getVisibleEvents().size() + display.getVisibleFloatTasks().size()
                + display.getVisibleReservedTasks().size();
        return numVisible == 0;
    }

    private void initialiseDisplay(Display oldDisplay) {
        oldDisplay.setTaskIndices(new ArrayList<Integer>());
        oldDisplay.setConflictingTasksIndices(new ArrayList<Integer>());
    }

    private void setInvalidDisplay(Display oldDisplay) {
        updateFile = false;
        saveHistory = false;
        oldDisplay.setMessage(MESSAGE_INVALID_DATE_RANGE);
    }

    // returns true if end date is before start date
    private boolean isInvalidDateRange() {
        if ((searchedTask.getStartDate() != null) && (searchedTask.getEndDate() != null)) {
            if (searchedTask.getStartDate().after(searchedTask.getEndDate())) {
                return true;
            }
        }
        return false;
    }

    private void setShowAll(Display oldDisplay) {
        oldDisplay.setMessage(message_show_all);
        oldDisplay.setVisibleDeadlineTasks(oldDisplay.getDeadlineTasks());
        oldDisplay.setVisibleEvents(oldDisplay.getEventTasks());
        oldDisplay.setVisibleFloatTasks(oldDisplay.getFloatTasks());
        oldDisplay.setVisibleReservedTasks(oldDisplay.getReservedTasks());
        oldDisplay.setVisibleCompletedTasks(new ArrayList<Task>());
        initialiseDisplay(oldDisplay);
    }

    private boolean isShowAll() {
        if (searchedTask.getDescription().isEmpty()) {
            if (searchedTask.getLocation().isEmpty()) {
                if ((searchedTask.getStartDate() == null) && (searchedTask.getEndDate() == null)) {
                    if (searchedTask.getTags().isEmpty()) {
                        if (taskTypes.isEmpty()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private String getFeedback() {
        if (!taskTypes.isEmpty()) {
            for (int i = 0; i < taskTypes.size(); i++) {
                message_show += taskTypes.get(i) + " ";
            }
            message_show += "tasks";
        } else {
            message_show += "all tasks";
        }
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
            for (int i = 0; i < taskTypes.size(); i++) {
                taskTypes.set(i, taskTypes.get(i).toLowerCase());
            }
            if (!taskTypes.contains(TASK_TYPE_EVENT)) {
                display.setVisibleEvents(new ArrayList<TaskEvent>());
            }
            if (!taskTypes.contains(TASK_TYPE_DEADLINE)) {
                display.setVisibleDeadlineTasks(new ArrayList<TaskDeadline>());
            }
            if (!taskTypes.contains(TASK_TYPE_FLOAT)) {
                display.setVisibleFloatTasks(new ArrayList<TaskFloat>());
            }
            if (!taskTypes.contains(TASK_TYPE_RESERVED)) {
                display.setVisibleReservedTasks(new ArrayList<TaskReserved>());
            }
            if (taskTypes.contains(TASK_TYPE_COMPLETED)) {
                getCompletedTasks();
            }
        }
    }

    private boolean isTaskTypesEmpty() {
        if (taskTypes == null) {
            return true;
        } else if (taskTypes.isEmpty()) {
            return true;
        }
        return false;
    }

    private void getCompletedTasks() {
        Task task;
        display.setVisibleCompletedTasks(new ArrayList<Task>());
        for (int i = 0; i < display.getCompletedTasks().size(); i++) {
            task = display.getCompletedTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        if (withinTimeRange(task)) {
                            display.getVisibleCompletedTasks().add(task);
                        }
                    }
                }
            }
        }
    }

    private void getDeadLineTasks() {
        TaskDeadline task;
        display.setVisibleDeadlineTasks(new ArrayList<TaskDeadline>());
        for (int i = 0; i < display.getDeadlineTasks().size(); i++) {
            task = display.getDeadlineTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        if (withinTimeRange(task)) {
                            display.getVisibleDeadlineTasks().add(task);
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
        display.setVisibleEvents(new ArrayList<TaskEvent>());
        for (int i = 0; i < display.getEventTasks().size(); i++) {
            task = display.getEventTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        if (withinTimeRange(task)) {
                            display.getVisibleEvents().add(task);
                        }
                    }
                }
            }
        }
        // System.out.println("E" + oldDisplay.getVisibleEvents().size());
    }

    private void getFloatTasks() {
        TaskFloat task;
        display.setVisibleFloatTasks(new ArrayList<TaskFloat>());
        for (int i = 0; i < display.getFloatTasks().size(); i++) {
            task = display.getFloatTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        display.getVisibleFloatTasks().add(task);
                    }
                }
            }
        }
        // System.out.println("F" + oldDisplay.getVisibleFloatTasks().size());
    }

    private void getReservedTasks() {
        TaskReserved task;
        display.setVisibleReservedTasks(new ArrayList<TaskReserved>());
        for (int i = 0; i < display.getReservedTasks().size(); i++) {
            task = display.getReservedTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        if (withinTimeRange(task)) {
                            display.getVisibleReservedTasks().add(task);
                        }
                    }
                }
            }
        }
        // System.out.println("R" +
        // oldDisplay.getVisibleReservedTasks().size());
    }

    private boolean containsTag(Task task) {
        if (searchedTask.getTags().isEmpty()) {
            return true;
        }
        boolean containsTags = false;
        for (int i = 0; i < searchedTask.getTags().size(); i++) {
            for (int j = 0; j < task.getTags().size(); j++) {
                containsTags = false;
                if (searchedTask.getTags().get(i).toLowerCase()
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
        if ((searchedTask.getStartDate() == null) && (searchedTask.getEndDate() == null)) {
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

    private boolean isWithinTimeRange(TaskEvent task) {
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

    private boolean isWithinTimeRange(TaskDeadline task) {
        if ((searchedTask.getStartDate() == null) && (searchedTask.getEndDate() == null)) {
            return true;
        }
        if (!task.getEndDate().before(searchedTask.getStartDate())) {
            if (!task.getEndDate().after(searchedTask.getEndDate())) {
                return true;
            }
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
        String[] keywords = searchedTask.getDescription().split(" ");
        for (int i = 0; i < keywords.length; i++) {
            if (!task.getDescription().toLowerCase().contains(keywords[i])) {
                return false;
            }
        }
        return true;
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
