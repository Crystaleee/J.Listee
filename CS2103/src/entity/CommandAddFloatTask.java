/*
 * @@author A0139995E
 */
package entity;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandAddFloatTask implements Command {
    private TaskFloat _task;
    private boolean _updateFile = true;
    private boolean _saveHistory = true;
    private Logger logger = GlobalLogger.getLogger();

    public CommandAddFloatTask() {
        _task = null;
    }

    public CommandAddFloatTask(TaskFloat task) {
        this._task = task;
    }

    public CommandAddFloatTask(String description, String location, ArrayList<String> tags) {
        _task = new TaskFloat(description, location, tags);
    }

    public Display execute(Display display) {
        assert display != null: "AddFloat: null display";
        if (hasNoDescription()) {
            logger.log(Level.INFO, "AddFloat: No desc");
            setInvalidDisplay(display);
            return display;
        }
        display.getFloatTasks().add(_task);
        if (!display.getVisibleFloatTasks().equals(display.getFloatTasks())) {
            display.getVisibleFloatTasks().add(_task);
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

    private void setInvalidDisplay(Display display) {
        _updateFile = false;
        _saveHistory = false;
        display.setCommandType(GlobalConstants.GUI_ANIMATION_INVALID);
        display.setMessage(GlobalConstants.MESSAGE_ERROR_DESCRIPTION);
    }

    private void setDisplay(Display display) {
        ArrayList<Integer> taskIndices = new ArrayList<Integer>();
        display.setCommandType(GlobalConstants.GUI_ANIMATION_ADD);
        int index = display.getVisibleFloatTasks().indexOf(_task) + display.getVisibleEvents().size()
                + display.getVisibleDeadlineTasks().size() + 1;
        taskIndices.add(index);
        display.setTaskIndices(taskIndices);
        display.setMessage(String.format(GlobalConstants.MESSAGE_ADD_SUCCESS, _task.getDescription()));
        display.setConflictingTasksIndices(new ArrayList<Integer>());
    }

    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}