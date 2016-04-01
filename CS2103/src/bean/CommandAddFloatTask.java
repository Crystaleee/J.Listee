/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.util.ArrayList;

import logic.Logic;

public class CommandAddFloatTask implements Command {
    private static final String COMMAND_TYPE_ADD = "Add";
    private static final String COMMAND_TYPE_INVALID = "Invalid";
    private TaskFloat task;
    private boolean updateFile = true;
    private boolean saveHistory = true;
    private ArrayList<Integer> taskIndices = new ArrayList<Integer>();

    public CommandAddFloatTask() {
        task = null;
    }

    public CommandAddFloatTask(TaskFloat task) {
        this.task = task;
    }

    public CommandAddFloatTask(String description, String location, ArrayList<String> tags) {
        task = new TaskFloat(description, location, tags);
        updateFile = true;
    }

    public Display execute(Display display) {
        if (hasNoDescription()) {
            setInvalidDisplay(display);
            display.setMessage(Logic.MESSAGE_NO_DESCRIPTION);
            return display;
        }
        display.getFloatTasks().add(task);
        if (!display.getVisibleFloatTasks().equals(display.getFloatTasks())) {
            display.getVisibleFloatTasks().add(task);
        }
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
        int index = display.getVisibleFloatTasks().indexOf(task) + display.getVisibleEvents().size()
                + display.getVisibleDeadlineTasks().size() + 1;
        taskIndices.add(index);
        display.setTaskIndices(taskIndices);
        display.setMessage(String.format(Logic.MESSAGE_ADD_SUCCESS, task.getDescription()));
        display.setConflictingTasksIndices(new ArrayList<Integer>());
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}