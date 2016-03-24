/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 21 Mar, 11:00pm
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

import logic.Logic;

public class CommandAddDeadlineTask implements Command {
    private TaskDeadline task;
    private boolean updateFile = true;
    private boolean saveHistory = true;

    public CommandAddDeadlineTask() {
        task = null;
    }

    public CommandAddDeadlineTask(String description, String location, Calendar endDate,
            ArrayList<String> tags) {
        task = new TaskDeadline(description, location, endDate, tags);
    }

    public Display execute(Display display) {
        if (task.getDescription() == null) {
            updateFile = false;
            saveHistory = false;
            display.setMessage(Logic.MESSAGE_NO_DESCRIPTION);
            return display;
            //return (new Display(Logic.MESSAGE_NO_DESCRIPTION));
        }
        if(task.getDescription().isEmpty()){
            updateFile = false;
            saveHistory = false;
            display.setMessage(Logic.MESSAGE_NO_DESCRIPTION);
            return display;
        }
        task.setDescription(task.getDescription().trim());
        ArrayList<TaskDeadline> deadlineTasks = addDeadlineTask(display.getDeadlineTasks());
        display.setDeadlineTasks(deadlineTasks);
        display.setMessage(String.format(Logic.MESSAGE_ADD_SUCCESS, task.getDescription()));

        return display;
    }

    private ArrayList<TaskDeadline> addDeadlineTask(ArrayList<TaskDeadline> taskList) {
        int index = getIndex(taskList);
        taskList.add(index, task);
        return taskList;
    }

    /*
     * This method searches for the index to slot the deadline task in since we
     * are sorting the list in order of earliest deadline first
     */
    private int getIndex(ArrayList<TaskDeadline> taskList) {
        int i = 0;
        for (i = 0; i < taskList.size(); i++) {
            if (task.getEndDate().compareTo(taskList.get(i).getEndDate()) < 0) {
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
