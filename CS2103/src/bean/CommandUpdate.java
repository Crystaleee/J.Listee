/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 3:20am
 * CS2103
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

public class CommandUpdate extends TaskEvent implements Command {
    private Integer taskNumber;
    private Display display;
    private boolean updateFile;
    private final String message_invalid_task_number = "Pls specify a valid task number";

    public CommandUpdate() {
        super();
        updateFile = true;
        this.taskNumber = null;
    }

    public CommandUpdate(Integer taskNumber, String description) {
        super(description, null, null, null, null);
        updateFile = true;
        this.taskNumber = taskNumber;
    }

    public CommandUpdate(Integer taskNumber, String description, String location, Calendar startDate,
            Calendar endDate, ArrayList<String> tags) {
        super(description, location, startDate, endDate, tags);
        updateFile = true;
        this.taskNumber = taskNumber;
    }

    public Display execute(Display oldDisplay) {
        display = oldDisplay;
        if (hasInvalidTaskNumber(display.getNumberOfTasks())) {
            updateFile = false;
            return (new Display(message_invalid_task_number));
        }
        editTask();
        // editStartDate(taskList);
        // editEndDate(taskList);
        // setDisplay(null, taskList);
        return display;
    }

    public boolean hasInvalidTaskNumber(int numOfTasks) {
        return ((taskNumber > numOfTasks) || (taskNumber < 1));
    }

    public void editTask() {
        if (getDescription() != null) {
            if (taskNumber <= display.getDeadlineTasks().size()) {
                display.getDeadlineTasks().get(taskNumber - 1).setDescription(getDescription());
            } else {
                taskNumber -= display.getDeadlineTasks().size();
                if (taskNumber <= display.getEventTasks().size()) {
                    display.getEventTasks().get(taskNumber - 1).setDescription(getDescription());
                } else {
                    taskNumber -= display.getEventTasks().size();
                    display.getFloatTasks().get(taskNumber - 1).setDescription(getDescription());
                }
            }
        }
    }
    
    public boolean getUpdateFile() {
        return updateFile;
    }
    /*
     * public void editEndDate(ArrayList<Task> taskList) { if(addEndDate !=
     * null){ taskList.get(taskNumber - 1).setEndDate(addEndDate); }
     * if(deleteEndDate){ taskList.get(taskNumber - 1).setEndDate(null); } }
     * 
     * public void editStartDate(ArrayList<Task> taskList) { if(addStartDate !=
     * null){ taskList.get(taskNumber - 1).setStartDate(addStartDate); }
     * if(deleteStartDate){ taskList.get(taskNumber - 1).setStartDate(null); } }
     */
}