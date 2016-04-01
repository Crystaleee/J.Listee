/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

import logic.Logic;

public class CommandAddEvent implements Command {
    private static final String COMMAND_TYPE_ADD = "Add";
    private static final String COMMAND_TYPE_INVALID = "Invalid";
    private static final String MESSAGE_INVALID_TIME_RANGE = "Please specify a valid date range";
    private TaskEvent task;
    private boolean updateFile = true;
    private boolean saveHistory = true;
    private ArrayList<Integer> taskIndices = new ArrayList<Integer>();
    private ArrayList<Integer> conflictingTasksIndices = new ArrayList<Integer>();

    public CommandAddEvent() {
        task = null;
    }

    public CommandAddEvent(TaskEvent task) {
        this.task = task;
    }

    public CommandAddEvent(String description, String location, Calendar startDate, Calendar endDate,
            ArrayList<String> tags) {
        task = new TaskEvent(description, location, startDate, endDate, tags);
    }

    public Display execute(Display display) {
        if (hasNoDescription()) {
            setInvalidDisplay(display);
            display.setMessage(Logic.MESSAGE_NO_DESCRIPTION);
            return display;
        }
        if (isInvalidTimeRange()) {
            setInvalidDisplay(display);
            display.setMessage(MESSAGE_INVALID_TIME_RANGE);
            return display;
        }
        addEvent(display.getEventTasks());
        if (!display.getVisibleEvents().equals(display.getEventTasks())) {
            addEvent(display.getVisibleEvents());
        }
        getConflictingTasks(display);
        setDisplay(display);
        return display;
    }

    private boolean isInvalidTimeRange() {
        return task.getStartDate().after(task.getEndDate());
    }

    private boolean hasNoDescription() {
        if (task.getDescription() == null) {
            return true;
        } else {
            task.setDescription(task.getDescription().trim());
            if (task.getDescription().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void setInvalidDisplay(Display display) {
        updateFile = false;
        saveHistory = false;
        display.setCommandType(COMMAND_TYPE_INVALID);
    }

    private void setDisplay(Display display) {
        display.setCommandType(COMMAND_TYPE_ADD);
        int index = getAddedTaskIndex(display);
        taskIndices.add(index);
        display.setTaskIndices(taskIndices);
        display.setMessage(String.format(Logic.MESSAGE_ADD_SUCCESS, task.getDescription()));
    }

    private void getConflictingTasks(Display display) {
        getConflictingEvents(display);
        getConflictingReservedTasks(display);
        display.setConflictingTasksIndices(conflictingTasksIndices);
    }

    private void getConflictingReservedTasks(Display display) {
        ArrayList<TaskReserved> listReserved = display.getReservedTasks();
        for (TaskReserved myTask : listReserved) {
            for (int i = 0; i < myTask.getStartDates().size(); i++) {
                if (isWithinTimeRange(task.getStartDate(), task.getEndDate(), myTask.getStartDates().get(i),
                        myTask.getEndDates().get(i))) {
                    int index = display.getVisibleReservedTasks().indexOf(myTask);
                    if (isValidIndex(index)) {
                        index = getConflictingTaskReservedIndex(display, index);
                        conflictingTasksIndices.add(index);
                        //System.out.println(index);
                    }
                    break;
                }
            }
        }
    }

    private int getConflictingTaskReservedIndex(Display display, int index) {
        return index + display.getVisibleDeadlineTasks().size()
                + display.getVisibleEvents().size() + display.getVisibleFloatTasks().size()
                + 1;
    }

    private boolean isValidIndex(int index) {
        return index >= 0;
    }

    private void getConflictingEvents(Display display) {
        ArrayList<TaskEvent> listEvents = display.getEventTasks();
        for (TaskEvent myTask : listEvents) {
            if (!myTask.equals(task)) {
                if (isWithinTimeRange(task.getStartDate(), task.getEndDate(), myTask.getStartDate(),
                        myTask.getEndDate())) {
                    int index = display.getVisibleEvents().indexOf(myTask);
                    if (isValidIndex(index)) {
                        index = getConflictingTaskEventIndex(display, index);
                        conflictingTasksIndices.add(index);
                        //System.out.println(index);
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
        return display.getVisibleEvents().indexOf(task) + display.getVisibleDeadlineTasks().size() + 1;
    }

    private void addEvent(ArrayList<TaskEvent> taskList) {
        int index = getAddIndex(taskList);
        taskList.add(index, task);
    }

    /*
     * This method searches for the index to slot the deadline task in since we
     * are sorting the list in order of earliest start time first
     */
    private int getAddIndex(ArrayList<TaskEvent> taskList) {
        int i = 0;
        for (i = 0; i < taskList.size(); i++) {
            if (task.getStartDate().compareTo(taskList.get(i).getStartDate()) < 0) {
                break;
            }
        }
        return i;
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
