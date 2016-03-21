/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 2:40am
 * CS2103
 */
package bean;

import java.util.ArrayList;

import logic.Logic;

public class CommandAddFloatTask implements Command {
    private TaskFloat task;
    private boolean updateFile = true;
    private boolean saveHistory = true;

    public CommandAddFloatTask() {
        task = null;
    }

    public CommandAddFloatTask(String description, String location, ArrayList<String> tags) {
        task = new TaskFloat(description, location, tags);
        updateFile = true;
    }

    public Display execute(Display display) {
        if (task.getDescription() == null) {
            updateFile = false;
            saveHistory = false;
            return (new Display(Logic.MESSAGE_NO_DESCRIPTION));
        }
        display.getFloatTasks().add(task);
        display.setMessage(String.format(Logic.MESSAGE_ADD_SUCCESS, task.getDescription()));
        return display;
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}