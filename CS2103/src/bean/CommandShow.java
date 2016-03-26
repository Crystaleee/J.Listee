/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 21 Mar, 11:00pm
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

public class CommandShow implements Command {
    private final String message_no_tasks = "No such tasks found";
    private final String message_show = "Displaying tasks containing ";

    private boolean updateFile = false;
    private boolean saveHistory = true;
    private TaskEvent searchedTask;
    private int count;
    private Display oldDisplay;
    private Display newDisplay;

    public CommandShow() {
        searchedTask = null;
        newDisplay = null;
        count = 0;
    }

    public CommandShow(String keyword) {
        searchedTask = new TaskEvent();
        searchedTask.setDescription(keyword.toLowerCase().trim());
        newDisplay = new Display();
        count = 0;
    }

    public CommandShow(String keyword, String location, Calendar start, Calendar end,
            ArrayList<String> tags) {
        searchedTask = new TaskEvent(keyword.trim().toLowerCase(), location.trim().toLowerCase(), start, end,
                tags);
        newDisplay = new Display();
        count = 0;
    }

    public Display execute(Display oldDisplay) {
        this.oldDisplay = oldDisplay;

        newDisplay = getTasksContainingKeyword();

        if (count == 0) {
            newDisplay = new Display(message_no_tasks);
        } else {
            newDisplay.setMessage(message_show + searchedTask.getDescription());
            ;
        }

        return newDisplay;
    }

    private Display getTasksContainingKeyword() {
        getFloatTasks();
        getEventTasks();
        getDeadLineTasks();
        getReservedTasks();
        return newDisplay;
    }

    private void getReservedTasks() {
        TaskReserved task;
        for (int i = 0; i < oldDisplay.getReservedTasks().size(); i++) {
            task = oldDisplay.getReservedTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        if (withinTimeRange(task)) {
                            newDisplay.getReservedTasks().add(task);
                            count++;
                        }
                    }
                }
            }
        }
    }

    private boolean containsTag(Task task) {
        if (searchedTask.getTags().isEmpty()) {
            return true;
        }
        for (int i = 0; i < task.getTags().size(); i++) {
            for (int j = 0; j < searchedTask.getTags().size(); j++) {
                if (task.getTags().get(i).toLowerCase()
                        .equals(searchedTask.getTags().get(j).trim().toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean withinTimeRange(TaskDeadline task) {
        if ((searchedTask.getStartDate() != null) && (searchedTask.getEndDate() != null)) {
            if (task.getEndDate().after(searchedTask.getStartDate())) {
                if (task.getEndDate().before(searchedTask.getEndDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean withinTimeRange(TaskReserved task) {
        if ((searchedTask.getStartDate() != null) && (searchedTask.getEndDate() != null)) {
            for (int i = 0; i < task.getStartDates().size(); i++) {
                if (task.getStartDates().get(i).after(searchedTask.getStartDate())) {
                    if (task.getStartDates().get(i).before(searchedTask.getEndDate())) {
                        return true;
                    }
                } else if (task.getEndDates().get(i).after(searchedTask.getStartDate())) {
                    if (task.getEndDates().get(i).before(searchedTask.getEndDate())) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private boolean withinTimeRange(TaskEvent task) {
        if ((searchedTask.getStartDate() != null) && (searchedTask.getEndDate() != null)) {
            if (task.getStartDate().after(searchedTask.getStartDate())) {
                if (task.getStartDate().before(searchedTask.getEndDate())) {
                    return true;
                }
            } else if (task.getEndDate().after(searchedTask.getStartDate())) {
                if (task.getEndDate().before(searchedTask.getEndDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean atLocation(Task task) {
        if (task.getLocation().toLowerCase().equals(searchedTask.getLocation())) {
            return true;
        }
        return false;
    }

    private boolean containsKeyword(Task task) {
        if (task.getDescription().toLowerCase().contains(searchedTask.getDescription())) {
            return true;
        }
        return false;
    }

    private void getDeadLineTasks() {
        TaskDeadline task;
        for (int i = 0; i < oldDisplay.getDeadlineTasks().size(); i++) {
            task = oldDisplay.getDeadlineTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        if (withinTimeRange(task)) {
                            newDisplay.getDeadlineTasks().add(task);
                            count++;
                        }
                    }
                }
            }
        }
    }

    private void getEventTasks() {
        TaskEvent task;
        for (int i = 0; i < oldDisplay.getEventTasks().size(); i++) {
            task = oldDisplay.getEventTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        if (withinTimeRange(task)) {
                            newDisplay.getEventTasks().add(task);
                            count++;
                        }
                    }
                }
            }
        }
    }

    private void getFloatTasks() {
        TaskFloat task;
        for (int i = 0; i < oldDisplay.getFloatTasks().size(); i++) {
            task = oldDisplay.getFloatTasks().get(i);
            if (containsKeyword(task)) {
                if (atLocation(task)) {
                    if (containsTag(task)) {
                        newDisplay.getFloatTasks().add(task);
                        count++;
                    }
                }
            }
        }
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
