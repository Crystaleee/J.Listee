/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

import logic.Logic;

public class CommandAddDeadlineTask implements Command {
    private static final String COMMAND_TYPE_ADD = "Add";
    private static final String COMMAND_TYPE_INVALID = "Invalid";
    private TaskDeadline task;
    private ArrayList<Integer> taskIndices = new ArrayList<Integer>();
    private boolean updateFile = true;
    private boolean saveHistory = true;

    public CommandAddDeadlineTask() {
        task = null;
    }

    public CommandAddDeadlineTask(TaskDeadline task) {
        this.task = task;
    }

    public CommandAddDeadlineTask(String description, String location, Calendar endDate,
            ArrayList<String> tags) {
        task = new TaskDeadline(description, location, endDate, tags);
    }

    public Display execute(Display display) {
        if (hasNoDescription()) {
            setInvalidDisplay(display);
            display.setMessage(Logic.MESSAGE_NO_DESCRIPTION);
            return display;
        }
        addDeadlineTask(display.getDeadlineTasks());
        if (!display.getVisibleDeadlineTasks().equals(display.getDeadlineTasks())) {
            addDeadlineTask(display.getVisibleDeadlineTasks());
        }
        setDisplay(display);

        return display;
    }

    private boolean hasNoDescription() {
        if (task.getDescription() == null) {
            return true;
        } else {
            task.setDescription(task.getDescription().trim());
            if (task.getDescription().isEmpty()) {
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
        taskIndices.add(display.getVisibleDeadlineTasks().indexOf(task) + 1);
        display.setTaskIndices(taskIndices);
        display.setMessage(String.format(Logic.MESSAGE_ADD_SUCCESS, task.getDescription()));
        display.setConflictingTasksIndices(new ArrayList<Integer>());
    }

    private void addDeadlineTask(ArrayList<TaskDeadline> taskList) {
        int index = getIndex(taskList);
        taskList.add(index, task);
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
