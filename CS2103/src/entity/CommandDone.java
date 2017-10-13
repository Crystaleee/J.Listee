/*
 * @@author A0139995E
 */
package entity;

/**
 * This command is to mark a task as completed.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandDone implements Command {
    private String _msgInvalidNumbers = "You have specified invalid task numbers: ";
    private String _msgComplete = "Completed: ";
    private ArrayList<Integer> _taskNumbers;
    private Display _display;
    private boolean _saveHistory = true;
    private boolean _updateFile = true;
    private Logger logger = GlobalLogger.getLogger();

    public CommandDone() {
        this._taskNumbers = null;
        this._display = null;
    }

    public CommandDone(ArrayList<Integer> taskNumbers) {
        this._taskNumbers = taskNumbers;
        this._display = null;
    }

    public Display execute(Display oldDisplay) {
        assert oldDisplay != null : "Done: null display";
        this._display = oldDisplay;
        if (hasInvalidTaskNumbers()) {
            logger.log(Level.INFO, "Done: Index Invalid");
            setInvalidDisplay();
            return _display;
        } else {
            logger.log(Level.INFO, "Done: No errors");
            doneTasksFromList();
        }
        return _display;
    }

    /**
     * sets variables when the command has invalid parametersS
     */
    private void setInvalidDisplay() {
        _updateFile = false;
        _saveHistory = false;
        _display.setCommandType(GlobalConstants.GUI_ANIMATION_INVALID);
        _display.setMessage(_msgInvalidNumbers);
    }

    private void setDisplay(String msg, String commandType, ArrayList<Integer> completedTasks,
            ArrayList<Integer> conflictingTasks) {
        _display.setMessage(msg);
        _display.setCommandType(commandType);
        _display.setTaskIndices(completedTasks);
        _display.setConflictingTasksIndices(conflictingTasks);
    }

    private boolean hasNoTaskNumber() {
        if (_taskNumbers != null) {
            if (_taskNumbers.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasInvalidTaskNumbers() {
        if (hasNoTaskNumber()) {
            return true;
        }
        if (isDoneAll()) {
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
                + _display.getVisibleFloatTasks().size();
        return numOfTasks;
    }

    /**
     * sets the feedback for invalid task indices
     */
    private void feedbackInvalidNumbers(ArrayList<Integer> invalidTaskNumbers, int taskNum) {
        if (invalidTaskNumbers.size() == 0) {
            _msgInvalidNumbers += taskNum;
        } else {
            _msgInvalidNumbers += GlobalConstants.COMMA_SPACE + taskNum;
        }
    }

    private boolean isTaskNumberInvalid(int taskNum, int numOfTasks) {
        return (taskNum > numOfTasks) || (taskNum < 1);
    }

    private void doneTasksFromList() {
        if (isDoneAll()) {
            doneAllVisibleTasks();
            return;
        } else {
            doneMultipleTasks();
            return;
        }
    }

    /**
     * Mark one/multiple tasks as done
     */
    private void doneMultipleTasks() {
        Task doneTask;
        Collections.sort(_taskNumbers);
        for (int i = 0; i < _taskNumbers.size(); i++) {
            doneTask = markTaskAsDone(_taskNumbers.get(i) - 1 - i);
            feedbackCompletedTasks(doneTask, i);
        }
        setDisplay(_msgComplete, GlobalConstants.GUI_ANIMATION_DELETE, _taskNumbers,
                new ArrayList<Integer>());
    }

    /**
     * Mark all visible tasks as done
     */
    private void doneAllVisibleTasks() {
        int numTasks = getNumOfTasks();
        _taskNumbers = new ArrayList<Integer>();
        for (int i = 0; i < numTasks; i++) {
            markTaskAsDone(numTasks - i - 1);
            _taskNumbers.add(i + 1);
        }
        setDisplay(GlobalConstants.MESSAGE_ALL_COMPLETED, GlobalConstants.GUI_ANIMATION_DELETE, _taskNumbers,
                new ArrayList<Integer>());
    }

    private boolean isDoneAll() {
        return _taskNumbers == null;
    }

    private void feedbackCompletedTasks(Task doneTask, int i) {
        if (i == 0) {
            _msgComplete += GlobalConstants.INVERTED_COMMAS + doneTask.getDescription()
                    + GlobalConstants.INVERTED_COMMAS;
        } else {
            _msgComplete += GlobalConstants.COMMA_SPACE_INVERTED_COMMAS + doneTask.getDescription()
                    + GlobalConstants.INVERTED_COMMAS;
        }
    }

    private Task markTaskAsDone(int taskNum) {
        Task doneTask;
        if (isTaskDeadline(taskNum)) {
            doneTask = doneTaskDeadline(taskNum);
        } else {
            taskNum -= _display.getVisibleDeadlineTasks().size();
            if (isTaskEvent(taskNum)) {
                doneTask = doneEvent(taskNum);
            } else {
                taskNum -= _display.getVisibleEvents().size();
                doneTask = doneTaskFloat(taskNum);
            }
        }
        return doneTask;
    }

    private boolean isTaskEvent(int taskNum) {
        return taskNum < _display.getVisibleEvents().size();
    }

    private boolean isTaskDeadline(int taskNum) {
        return taskNum < _display.getVisibleDeadlineTasks().size();
    }

    private Task doneTaskFloat(int taskNum) {
        Task completedTask = _display.getVisibleFloatTasks().remove(taskNum);
        _display.getFloatTasks().remove(completedTask);
        _display.getCompletedTasks().add(completedTask);
        return completedTask;
    }

    private Task doneEvent(int taskNum) {
        Task completedTask = _display.getVisibleEvents().remove(taskNum);
        _display.getEventTasks().remove(completedTask);
        _display.getCompletedTasks().add(completedTask);
        return completedTask;
    }

    private Task doneTaskDeadline(int taskNum) {
        Task completedTask = _display.getVisibleDeadlineTasks().remove(taskNum);
        _display.getDeadlineTasks().remove(completedTask);
        _display.getCompletedTasks().add(completedTask);
        return completedTask;
    }

    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}
