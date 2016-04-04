/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.util.ArrayList;
import java.util.Collections;

public class CommandDone implements Command {
    private String _msgInvalidNumbers = "You have specified invalid task numbers: ";
    private String _msgComplete = "Completed: ";
    private ArrayList<Integer> _taskNumbers;
    private Display _display;
    private boolean _saveHistory = true;
    private boolean _updateFile = true;

    public CommandDone() {
        this._taskNumbers = null;
        this._display = null;
    }

    public CommandDone(ArrayList<Integer> taskNumbers) {
        this._taskNumbers = taskNumbers;
        this._display = null;
    }

    public Display execute(Display oldDisplay) {
        this._display = oldDisplay;
        if (_taskNumbers != null) {
            if (_taskNumbers.isEmpty()) {
                setDisplay(GlobalConstants.MESSAGE_ERROR_NO_NUMBER, GlobalConstants.GUI_ANIMATION_INVALID,
                        new ArrayList<Integer>(), new ArrayList<Integer>());
                return _display;
            }
        }
        if (hasInvalidTaskNumbers()) {
            setInvalidDisplay();
            return _display;
        } else {
            doneTasksFromList();
        }
        return _display;
    }

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
        incrementTaskNumbers();
        _display.setTaskIndices(completedTasks);
        _display.setConflictingTasksIndices(conflictingTasks);
    }

    private void incrementTaskNumbers() {
        if (_taskNumbers != null) {
            for (int i = 0; i < _taskNumbers.size(); i++) {
                _taskNumbers.set(i, _taskNumbers.get(i) + 1);
            }
        }
    }

    private boolean hasInvalidTaskNumbers() {
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

    private void feedbackInvalidNumbers(ArrayList<Integer> invalidTaskNumbers, int taskNum) {
        if (invalidTaskNumbers.size() == 0) {
            _msgInvalidNumbers += taskNum;
        } else {
            _msgInvalidNumbers += ", " + taskNum;
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

    private void doneMultipleTasks() {
        Task doneTask;
        Collections.sort(_taskNumbers);
        for (int i = 0; i < _taskNumbers.size(); i++) {
            doneTask = markTaskAsDone(_taskNumbers.get(i) - 1 - i);
            feedbackCompletedTasks(doneTask, i);
        }
        setDisplay(_msgComplete, GlobalConstants.GUI_ANIMATION_DELETE, _taskNumbers, new ArrayList<Integer>());
    }

    private void doneAllVisibleTasks() {
        int numTasks = getNumOfTasks();
        for (int i = 0; i < numTasks; i++) {
            markTaskAsDone(numTasks - i - 1);
        }
        setDisplay(GlobalConstants.MESSAGE_ALL_COMPLETED, GlobalConstants.GUI_ANIMATION_DELETE, new ArrayList<Integer>(),
                new ArrayList<Integer>());
    }

    private boolean isDoneAll() {
        return _taskNumbers == null;
    }

    private void feedbackCompletedTasks(Task doneTask, int i) {
        if (i == 0) {
            _msgComplete += "\"" + doneTask.getDescription() + "\"";
        } else {
            _msgComplete += ", \"" + doneTask.getDescription() + "\"";
        }
    }

    private Task markTaskAsDone(int taskNum) {
        Task doneTask;
        if (isDoneTaskDeadline(taskNum)) {
            doneTask = doneTaskDeadline(taskNum);
        } else {
            taskNum -= _display.getVisibleDeadlineTasks().size();
            if (isDoneTaskEvent(taskNum)) {
                doneTask = doneEvent(taskNum);
            } else {
                taskNum -= _display.getVisibleEvents().size();
                doneTask = doneTaskFloat(taskNum);
            }
        }
        return doneTask;
    }

    private boolean isDoneTaskEvent(int taskNum) {
        return taskNum < _display.getVisibleEvents().size();
    }

    private boolean isDoneTaskDeadline(int taskNum) {
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
