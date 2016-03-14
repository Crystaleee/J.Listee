/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 2:40am
 * CS2103
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

import logic.Logic;

public class CommandAddEvent extends TaskEvent implements Command {
    private boolean updateFile;

    public CommandAddEvent() {
        super();
        updateFile = true;
    }

    public CommandAddEvent(String description, String location, Calendar startDate, Calendar endDate,
            ArrayList<String> tags) {
        super(description, location, startDate, endDate, tags);
        updateFile = true;
    }

    public Display execute(Display display) {
        if (getDescription() == null) {
            updateFile = false;
            return (new Display(Logic.MESSAGE_NO_DESCRIPTION));
        }
        ArrayList<TaskEvent> events = addEvent(display.getEventTasks());
        display.setEvents(events);
        display.setMessage(String.format(Logic.MESSAGE_ADD_SUCCESS, getDescription()));
        return display;
    }

    public ArrayList<TaskEvent> addEvent(ArrayList<TaskEvent> taskList) {
        int index = getAddIndex(taskList);
        taskList.add(index,
                new TaskEvent(getDescription(), getLocation(), getStartDate(), getEndDate(), getTags()));
        return taskList;
    }

    /*
     * This method searches for the index to slot the deadline task in since we
     * are sorting the list in order of earliest start time first
     */
    public int getAddIndex(ArrayList<TaskEvent> taskList) {
        int i = 0;
        for (i = 0; i < taskList.size(); i++) {
            if (getStartDate().compareTo(taskList.get(i).getStartDate()) < 0) {
                break;
            }
        }
        return i;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
