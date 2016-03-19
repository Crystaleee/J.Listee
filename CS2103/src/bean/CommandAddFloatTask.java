/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 2:40am
 * CS2103
 */
package bean;

import java.util.ArrayList;

import logic.Logic;

public class CommandAddFloatTask extends TaskFloat implements Command {
    private boolean updateFile;
    private boolean saveHistory = true;

    public CommandAddFloatTask() {
        super();
        updateFile = true;
    }

    public CommandAddFloatTask(String description, String location, ArrayList<String> tags) {
        super(description, location, tags);
        updateFile = true;
    }

    public Display execute(Display display) {
        if (getDescription() == null) {
            updateFile = false;
            saveHistory = false;
            return (new Display(Logic.MESSAGE_NO_DESCRIPTION));
        }
        display.getFloatTasks().add(new TaskFloat(getDescription(), getLocation(), getTags()));
        display.setMessage(String.format(Logic.MESSAGE_ADD_SUCCESS, getDescription()));
        return display;
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}