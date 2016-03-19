/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 2:40am
 * CS2103
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

import logic.Logic;

public class CommandAddDeadlineTask extends TaskDeadline implements Command {
    private boolean updateFile = true;
    private boolean saveHistory = true;

    public CommandAddDeadlineTask() {
        super();
        updateFile = true;
    }

    public CommandAddDeadlineTask(String description, String location, Calendar endDate,
            ArrayList<String> tags) {
        super(description, location, endDate, tags);
        updateFile = true;
    }

    public Display execute(Display display) {
        if (getDescription() == null) {
            updateFile = false;
            saveHistory = false;
            return (new Display(Logic.MESSAGE_NO_DESCRIPTION));
        }
        ArrayList<TaskDeadline> deadlineTasks = addDeadlineTask(display.getDeadlineTasks());
        display.setDeadlineTasks(deadlineTasks);
        display.setMessage(String.format(Logic.MESSAGE_ADD_SUCCESS, getDescription()));

        return display;
    }

    public ArrayList<TaskDeadline> addDeadlineTask(ArrayList<TaskDeadline> taskList) {
        int index = getIndex(taskList);
        taskList.add(index, new TaskDeadline(getDescription(), getLocation(), getEndDate(), getTags()));
        return taskList;
    }

    /*
     * This method searches for the index to slot the deadline task in since we
     * are sorting the list in order of earliest deadline first
     */
    public int getIndex(ArrayList<TaskDeadline> taskList) {
        int i = 0;
        for (i = 0; i < taskList.size(); i++) {
            if (getEndDate().compareTo(taskList.get(i).getEndDate()) < 0) {
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
