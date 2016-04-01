/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

import logic.Logic;

public class CommandAddReserved implements Command {
    private static final String COMMAND_TYPE_INVALID = "Invalid";
    private static final String COMMAND_TYPE_ADD = "Add";
    private static final String MESSAGE_RESERVED = "Reserved: ";
    private static final String MESSAGE_INVALID_TIME_RANGE = "you have entered invalid time range(s)";
    private TaskReserved task;
    private boolean updateFile = true;
    private boolean saveHistory = true;
    private ArrayList<Integer> taskIndices = new ArrayList<Integer>();
    private ArrayList<Integer> conflictingTasksIndices = new ArrayList<Integer>();

    public CommandAddReserved() {
        task = null;
    }

    public CommandAddReserved(TaskReserved task) {
        this.task = task;
    }
    
    public CommandAddReserved(String description, String location, ArrayList<Calendar> startDates,
            ArrayList<Calendar> endDates, ArrayList<String> tags) {
        task = new TaskReserved(description, location, startDates, endDates, tags);
    }

    public Display execute(Display display) {
        if (hasNoDescription()) {
            setInvalidDisplay(display);
            display.setMessage(Logic.MESSAGE_NO_DESCRIPTION);
            return display;
        }
        if (containsInvalidTimeSlots()) {
            setInvalidDisplay(display);
            display.setMessage(MESSAGE_INVALID_TIME_RANGE);
            return display;
        }
        display.getReservedTasks().add(task);
        if (!display.getVisibleReservedTasks().equals(display.getReservedTasks())) {
            display.getVisibleReservedTasks().add(task);
        }
        getConflictingTasks(display);
        setDisplay(display);
        return display;
    }
    
    private boolean hasNoDescription() {
        if(task.getDescription() == null){
            return true;
        }else{
            task.setDescription(task.getDescription().trim());
            if(task.getDescription().isEmpty()){
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
        int index = display.getVisibleReservedTasks().indexOf(task) + display.getVisibleDeadlineTasks().size()
                + display.getVisibleEvents().size() + display.getVisibleFloatTasks().size() + 1;
        taskIndices.add(index);
        display.setTaskIndices(taskIndices);
        display.setMessage(MESSAGE_RESERVED + task.getDescription());
    }

    private void getConflictingTasks(Display display) {
        getConflictingEvents(display);
        getConflictingReservedTasks(display);
        display.setConflictingTasksIndices(conflictingTasksIndices);
    }

    private void getConflictingReservedTasks(Display display) {
        ArrayList<TaskReserved> listReserved = display.getReservedTasks();
        for (TaskReserved myTask : listReserved) {
            checkReservedTask: for (int i = 0; i < myTask.getStartDates().size(); i++) {
                for (int j = 0; j < task.getStartDates().size(); j++) {
                    if (!myTask.equals(task)) {
                        if (isWithinTimeRange(task.getStartDates().get(j), task.getEndDates().get(j),
                                myTask.getStartDates().get(i), myTask.getEndDates().get(i))) {
                            int index = display.getVisibleReservedTasks().indexOf(myTask);
                            if (isValidIndex(index)) {
                                index = getConflictingTaskReservedIndex(display, index);
                                conflictingTasksIndices.add(index);
                                // System.out.println(index);
                            }
                            break checkReservedTask;
                        }
                    }
                }
            }
        }
    }

    private int getConflictingTaskReservedIndex(Display display, int index) {
        return index + display.getVisibleDeadlineTasks().size()
                + display.getVisibleEvents().size()
                + display.getVisibleFloatTasks().size() + 1;
    }

    private boolean isValidIndex(int index) {
        return index >= 0;
    }

    private void getConflictingEvents(Display display) {
        ArrayList<TaskEvent> listEvents = display.getEventTasks();
        for (TaskEvent myTask : listEvents) {
            for (int i = 0; i < task.getStartDates().size(); i++) {
                if (isWithinTimeRange(task.getStartDates().get(i), task.getEndDates().get(i),
                        myTask.getStartDate(), myTask.getEndDate())) {
                    int index = display.getVisibleEvents().indexOf(myTask);
                    if (isValidIndex(index)) {
                        index = getConflictingTaskEventIndex(display, index);
                        conflictingTasksIndices.add(index);
                        // System.out.println(index);
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
        for (int i = 0; i < task.getStartDates().size(); i++) {
            if (task.getStartDates().get(i).after(task.getEndDates().get(i))) {
                return true;
            }
        }
        return false;
    }

    /*
     * private ArrayList<TaskReserved> addReservedTask(ArrayList<TaskReserved>
     * taskList) { int index = getIndex(taskList); taskList.add(index, task);
     * return taskList; }
     */

    /*
     * This method searches for the index to slot the deadline task in since we
     * are sorting the list in order of earliest start date of the first time
     * slot.
     *//*
       * private int getIndex(ArrayList<TaskReserved> taskList) { int i = 0;
       * Calendar addedTaskStartDate = task.getStartDates().get(0); for (i = 0;
       * i < taskList.size(); i++) { Calendar taskInListStartDate =
       * taskList.get(i).getStartDates().get(0); if
       * (addedTaskStartDate.compareTo(taskInListStartDate) <= 0) { break; } }
       * return i; }
       */

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
