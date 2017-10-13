/*
 * @@author A0139995E
 */
package entity;
/**
 * This command is to delete tasks
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandDelete implements Command {
    private Display _display;
    private ArrayList<Integer> _taskNumbers;
    private boolean _saveHistory = true;
    private boolean _updateFile = true;
    private String _invalidNumbers = "You have specified invalid numbers: ";
    private String _msgDelete = "deleted: ";
    private Logger _logger = GlobalLogger.getLogger();

    public CommandDelete() {
        this._taskNumbers = null;
        this._display = null;
    }

    public CommandDelete(ArrayList<Integer> taskNumbers) {
        this._taskNumbers = taskNumbers;
        this._display = null;
    }

    public Display execute(Display display) {
        assert display != null : "Delete: null display";
        this._display = display;
        if (hasInvalidTaskNumbers()) {
            setInvalidDisplay();
            _logger.log(Level.INFO, "Delete: Index Invalid");
            return _display;
        } else {
            _logger.log(Level.INFO, "Delete: No errors");
            deleteTasksFromList();
        }
        return _display;
    }

    private boolean hasNoTaskNumber() {
        if (_taskNumbers != null) {
            if (_taskNumbers.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * sets variables when the command has invalid parametersS
     */
    private void setInvalidDisplay() {
        _updateFile = false;
        _saveHistory = false;
        _display.setCommandType(GlobalConstants.GUI_ANIMATION_INVALID);
        _display.setMessage(_invalidNumbers);
        _display.setTaskIndices(new ArrayList<Integer>());
        _display.setConflictingTasksIndices(new ArrayList<Integer>());
    }

    private void setDisplay(String msg, String commandType, ArrayList<Integer> deletedTasks,
            ArrayList<Integer> conflictingTasks) {
        _display.setMessage(msg);
        _display.setCommandType(commandType);
        _display.setTaskIndices(deletedTasks);
        _display.setConflictingTasksIndices(conflictingTasks);
    }

    private boolean hasInvalidTaskNumbers() {
        if(hasNoTaskNumber()){
            return true;
        }
        if (isDeleteAll()) {
            return false;
        } else {
            ArrayList<Integer> invalidTaskNumbers = getInvalidTaskNumbers();
            return (invalidTaskNumbers.size() > 0);
        }
    }

    private ArrayList<Integer> getInvalidTaskNumbers() {
        ArrayList<Integer> invalidTaskNumbers = new ArrayList<Integer>();
        int taskNum, numOfTasks = getNumOfTasks();
        for (int i = 0; i < _taskNumbers.size(); i++) {
            taskNum = _taskNumbers.get(i);
            if (isTaskNumberInvalid(taskNum, numOfTasks)) {
                feedbackInvalidNumbers(invalidTaskNumbers, taskNum);
                invalidTaskNumbers.add(taskNum);
            }
        }
        return invalidTaskNumbers;
    }

    private int getNumOfTasks() {
        int numOfTasks = _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().size()
                + _display.getVisibleFloatTasks().size() + _display.getVisibleReservedTasks().size()
                + _display.getVisibleCompletedTasks().size();
        return numOfTasks;
    }

    private void feedbackInvalidNumbers(ArrayList<Integer> invalidTaskNumbers, int taskNum) {
        if (invalidTaskNumbers.size() == 0) {
            _invalidNumbers += taskNum;
        } else {
            _invalidNumbers += GlobalConstants.COMMA_SPACE + taskNum;
        }
    }

    private boolean isTaskNumberInvalid(int taskNum, int numOfTasks) {
        return (taskNum > numOfTasks) || (taskNum < 1);
    }

    private void deleteTasksFromList() {
        if (isDeleteAll()) {
            deleteAllShownTasks();
            return;
        } else {
            deleteMultipleTasks();
            return;
        }
    }

    /**
     * deletes one/multiple visible tasks
     */
    private void deleteMultipleTasks() {
        Task deletedTask;
        System.out.println(_taskNumbers.get(0));
        Collections.sort(_taskNumbers);
        for (int i = 0; i < _taskNumbers.size(); i++) {
            deletedTask = removeTask(_taskNumbers.get(i) - 1 - i);
            feedbackDeletedTasks(deletedTask, i);
        }
        setDisplay(_msgDelete, GlobalConstants.GUI_ANIMATION_DELETE, _taskNumbers, new ArrayList<Integer>());
    }

    /**
     * deletes all visible tasks
     */
    private void deleteAllShownTasks() {
        int numTasks = getNumOfTasks();
        _taskNumbers = new ArrayList<Integer>();
        for (int i = 0; i < numTasks; i++) {
            removeTask(numTasks - i - 1);
            _taskNumbers.add(i + 1);
        }
        setDisplay(GlobalConstants.MESSAGE_ALL_DELETED, GlobalConstants.GUI_ANIMATION_DELETE, _taskNumbers,
                new ArrayList<Integer>());
    }

    private boolean isDeleteAll() {
        return _taskNumbers == null;
    }

    private void feedbackDeletedTasks(Task deletedTask, int i) {
        if (i == 0) {
            _msgDelete += GlobalConstants.INVERTED_COMMAS + deletedTask.getDescription()
                    + GlobalConstants.INVERTED_COMMAS;
        } else {
            _msgDelete += ", \"" + deletedTask.getDescription() + GlobalConstants.INVERTED_COMMAS;
        }
    }

    private Task removeTask(int taskNum) {
        Task deletedTask;
        if (isDeleteTaskDeadline(taskNum)) {
            deletedTask = deleteTaskDeadline(taskNum);
        } else {
            taskNum -= _display.getVisibleDeadlineTasks().size();
            if (isDeleteTaskEvent(taskNum)) {
                deletedTask = deleteEvent(taskNum);
            } else {
                taskNum -= _display.getVisibleEvents().size();
                if (isDeleteTaskFloat(taskNum)) {
                    deletedTask = deleteTaskFloat(taskNum);
                } else {
                    taskNum -= _display.getVisibleFloatTasks().size();
                    if (isDeleteTaskReserved(taskNum)) {
                        deletedTask = deleteTaskReserved(taskNum);
                    } else {
                        taskNum -= _display.getVisibleReservedTasks().size();
                        deletedTask = deleteTaskCompleted(taskNum);
                    }
                }
            }
        }
        return deletedTask;
    }

    private boolean isDeleteTaskReserved(int taskNum) {
        return taskNum < _display.getVisibleReservedTasks().size();
    }

    private boolean isDeleteTaskFloat(int taskNum) {
        return taskNum < _display.getVisibleFloatTasks().size();
    }

    private boolean isDeleteTaskEvent(int taskNum) {
        return taskNum < _display.getVisibleEvents().size();
    }

    private boolean isDeleteTaskDeadline(int taskNum) {
        return taskNum < _display.getVisibleDeadlineTasks().size();
    }

    private Task deleteTaskCompleted(int taskNum) {
        Task deletedTask = _display.getVisibleCompletedTasks().remove(taskNum);
        _display.getCompletedTasks().remove(deletedTask);
        return deletedTask;
    }

    private Task deleteTaskReserved(int taskNum) {
        Task deletedTask = _display.getVisibleReservedTasks().remove(taskNum);
        _display.getReservedTasks().remove(deletedTask);
        return deletedTask;
    }

    private Task deleteTaskFloat(int taskNum) {
        Task deletedTask = _display.getVisibleFloatTasks().remove(taskNum);
        _display.getFloatTasks().remove(deletedTask);
        return deletedTask;
    }

    private Task deleteEvent(int taskNum) {
        Task deletedTask = _display.getVisibleEvents().remove(taskNum);
        _display.getEventTasks().remove(deletedTask);
        return deletedTask;
    }

    private Task deleteTaskDeadline(int taskNum) {
        Task deletedTask = _display.getVisibleDeadlineTasks().remove(taskNum);
        _display.getDeadlineTasks().remove(deletedTask);
        return deletedTask;
    }

    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}
