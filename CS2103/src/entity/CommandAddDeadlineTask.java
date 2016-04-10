/*
 * @@author A0139995E
 */
package entity;

/**
 * This is the command to add deadline task.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandAddDeadlineTask implements Command {
    private TaskDeadline _task;
    private boolean _updateFile = true;
    private boolean _saveHistory = true;
    private Logger logger = GlobalLogger.getLogger();

    public CommandAddDeadlineTask() {
        _task = null;
    }

    public CommandAddDeadlineTask(TaskDeadline task) {
        this._task = task;
    }

    public CommandAddDeadlineTask(String description, String location, Calendar endDate,
            ArrayList<String> tags) {
        _task = new TaskDeadline(description, location, endDate, tags);
    }

    public Display execute(Display display) {
        assert display != null: "AddDeadline: null display";
        if (hasNoDescription()) {
            logger.log(Level.INFO, "AddDeadline: No desc");
            setInvalidDisplay(display);
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
        if (_task.getDescription() == null) {
            return true;
        } else {
            _task.setDescription(_task.getDescription().trim());
            if (_task.getDescription().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /*
     * sets variables when the command has invalid parametersS
     */
    private void setInvalidDisplay(Display display) {
        _updateFile = false;
        _saveHistory = false;
        display.setCommandType(GlobalConstants.GUI_ANIMATION_INVALID);
        display.setMessage(GlobalConstants.MESSAGE_ERROR_DESCRIPTION);
    }

    private void setDisplay(Display display) {
        ArrayList<Integer> taskIndices = new ArrayList<Integer>();
        display.setCommandType(GlobalConstants.GUI_ANIMATION_ADD);
        taskIndices.add(display.getVisibleDeadlineTasks().indexOf(_task) + 1);
        display.setTaskIndices(taskIndices);
        display.setMessage(String.format(GlobalConstants.MESSAGE_ADD_SUCCESS, _task.getDescription()));
        display.setConflictingTasksIndices(new ArrayList<Integer>());
        setIfOverdue();
    }

    private void setIfOverdue() {
        if (_task.getEndDate().before(Calendar.getInstance())) {
            _task.setIsOverdue(true);
        }
    }

    private void addDeadlineTask(ArrayList<TaskDeadline> taskList) {
        int index = getIndex(taskList);
        taskList.add(index, _task);
    }

    /*
     * This method searches for the index to slot the deadline task in since we
     * are sorting the list in order of earliest deadline first
     */
    private int getIndex(ArrayList<TaskDeadline> taskList) {
        int i = 0;
        for (i = 0; i < taskList.size(); i++) {
            if (_task.getEndDate().compareTo(taskList.get(i).getEndDate()) < 0) {
                break;
            }
        }
        return i;
    }

    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}
