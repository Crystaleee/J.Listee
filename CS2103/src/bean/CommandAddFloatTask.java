/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 21 Mar, 11:00pm
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
            display.setMessage(Logic.MESSAGE_NO_DESCRIPTION);
            return display;
            //return (new Display(Logic.MESSAGE_NO_DESCRIPTION));
        }
        
        task.setDescription(task.getDescription().trim());
        if(task.getDescription().isEmpty()){
            updateFile = false;
            saveHistory = false;
            display.setMessage(Logic.MESSAGE_NO_DESCRIPTION);
            return display;
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