/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 2:50am
 * CS2103
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

import logic.Logic;

public class CommandAddReserved extends CommandAddFloatTask {
    private ArrayList<Calendar> startDates;
    private ArrayList<Calendar> endDates;
    private boolean updateFile;

    public CommandAddReserved() {
        super();
        startDates = null;
        endDates = null;
        updateFile = true;
    }

    public CommandAddReserved(String description, String location, ArrayList<Calendar> startDates,
            ArrayList<Calendar> endDates, ArrayList<String> tags) {
        super(description, location, tags);
        this.startDates = startDates;
        this.endDates = endDates;
        updateFile = true;
    }

    public void setEndDates(ArrayList<Calendar> endDates) {
        this.endDates = endDates;
    }

    public ArrayList<Calendar> GetEndDates() {
        return endDates;
    }

    public void setStartDates(ArrayList<Calendar> startDates) {
        this.startDates = startDates;
    }

    public ArrayList<Calendar> GetStartDates() {
        return startDates;
    }

    public Display execute(Display display) {
        if (getDescription() == null) {
            updateFile = false;
            return (new Display(Logic.MESSAGE_NO_DESCRIPTION));
        }
        ArrayList<TaskReserved> reservedTasks = addReservedTask(display.getReservedTasks());
        display.setReservedTasks(reservedTasks);
        return display;
    }

    public ArrayList<TaskReserved> addReservedTask(ArrayList<TaskReserved> taskList) {
        int index = getIndex(taskList);
        taskList.add(index,
                new TaskReserved(getDescription(), getLocation(), startDates, endDates, getTags()));
        return taskList;
    }

    /*
     * This method searches for the index to slot the deadline task in since we
     * are sorting the list in order of earliest start date of the first time
     * slot.
     */
    public int getIndex(ArrayList<TaskReserved> taskList) {
        int i = 0;
        Calendar addedTaskStartDate = startDates.get(0);
        for (i = 0; i < taskList.size(); i++) {
            Calendar taskInListStartDate = taskList.get(i).getStartDates().get(0);
            if (addedTaskStartDate.compareTo(taskInListStartDate) < 0) {
                break;
            }
        }
        return i;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
