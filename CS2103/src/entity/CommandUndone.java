/*
 * @@author A0139995E
 */
package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandUndone implements Command {
    private ArrayList<Integer> _taskNumbers;
    private ArrayList<Integer> _taskIndices = new ArrayList<Integer>();
    private ArrayList<Task> _tasks = new ArrayList<Task>();
    private Display _display;
    private String _msgUndone = "Undone task: ";
    private String _msgInvalidNum = "You have specified invalid task numbers: ";
    private boolean _saveHistory = true;
    private boolean _updateFile = true;
    private Logger logger = GlobalLogger.getLogger();

    public CommandUndone() {
        this._taskNumbers = null;
        this._display = null;
    }

    public CommandUndone(ArrayList<Integer> taskNumbers) {
        this._taskNumbers = taskNumbers;
        this._display = null;
    }

    public Display execute(Display oldDisplay) {
        assert oldDisplay != null: "Undone: null display";
        this._display = oldDisplay;
        if (_taskNumbers != null) {
            if (hasInvalidTaskNumbers()) {
                logger.log(Level.INFO, "Undone: Invalid Indices");
                setInvalidDisplay();
                return oldDisplay;
            }
            Collections.sort(_taskNumbers);
        }

        undoneTasksFromList();
        setDisplay(GlobalConstants.GUI_ANIMATION_ADD, _taskNumbers);
        return _display;
    }

    private void setInvalidDisplay() {
        _updateFile = false;
        _saveHistory = false;
        _display.setCommandType(GlobalConstants.GUI_ANIMATION_INVALID);
        _display.setMessage(_msgInvalidNum);
    }

    private void setDisplay(String commandType, ArrayList<Integer> completedTasks) {
        _display.setCommandType(commandType);
        incrementTaskNumbers();
        _display.setTaskIndices(completedTasks);
        getTaskIndices();
    }

    private void getTaskIndices() {
        for (int i = 0; i < _tasks.size(); i++) {
            Task task = _tasks.get(i);
            int index = getIndex(task);
            _taskIndices.add(index);
        }
    }

    private int getIndex(Task task) {
        if (task instanceof TaskDeadline) {
            return _display.getVisibleDeadlineTasks().indexOf(task) + 1;
        } else if (task instanceof TaskEvent) {
            return _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().indexOf(task) + 1;
        } else {
            return _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().size()
                    + _display.getVisibleFloatTasks().indexOf(task) + 1;
        }
    }

    private void incrementTaskNumbers() {
        if (_taskNumbers != null) {
            for (int i = 0; i < _taskNumbers.size(); i++) {
                _taskNumbers.set(i, _taskNumbers.get(i) + 1);
            }
        }
    }

    private boolean hasInvalidTaskNumbers() {
        ArrayList<Integer> invalidTaskNumbers = new ArrayList<Integer>();
        int taskNum, maxNum = getNumOfVisibleTasks(), minNum = getMinimumTaskNum();
        for (int i = 0; i < _taskNumbers.size(); i++) {
            taskNum = _taskNumbers.get(i);
            if (isTaskNumberInvalid(taskNum, maxNum, minNum)) {
                feedbackInvalidNumbers(invalidTaskNumbers, taskNum);
                invalidTaskNumbers.add(taskNum);
            }
        }
        return (invalidTaskNumbers.size() > 0);
    }

    private int getMinimumTaskNum() {
        int minNum = _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().size()
                + _display.getVisibleFloatTasks().size() + _display.getVisibleReservedTasks().size() + 1;
        return minNum;
    }

    private int getNumOfVisibleTasks() {
        int numOfTasks = _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().size()
                + _display.getVisibleFloatTasks().size() + _display.getVisibleReservedTasks().size()
                + _display.getVisibleCompletedTasks().size();
        return numOfTasks;
    }

    private void feedbackInvalidNumbers(ArrayList<Integer> invalidTaskNumbers, int taskNum) {
        if (invalidTaskNumbers.size() == 0) {
            _msgInvalidNum += taskNum;
        } else {
            _msgInvalidNum += ", " + taskNum;
        }
    }

    private boolean isTaskNumberInvalid(int taskNum, int max, int min) {
        return (taskNum > max) || (taskNum < min);
    }

    private void undoneTasksFromList() {
        if (isUndoneAll()) {
            undoneAllVisibleDoneTasks();
        } else {
            undoneMultipleDoneTasks();
        }
        return;
    }

    private void undoneMultipleDoneTasks() {
        Collections.sort(_taskNumbers);
        for (int i = 0; i < _taskNumbers.size(); i++) {
            Task completedTask = getTask(i);
            _display.getVisibleCompletedTasks().remove(completedTask);
            markUndoneTask(completedTask);
            feedbackUndoneTasks(completedTask, i);
        }
        _display.setMessage(_msgUndone);
    }

    private Task getTask(int i) {
        int index = _taskNumbers.get(i) - _display.getVisibleDeadlineTasks().size()
                - _display.getVisibleEvents().size() - _display.getVisibleFloatTasks().size()
                - _display.getVisibleReservedTasks().size() - 1;
        return _display.getCompletedTasks().remove(index);
    }

    private void undoneAllVisibleDoneTasks() {
        int numOfVisibleCompletedTasks = _display.getVisibleCompletedTasks().size();
        for (int i = numOfVisibleCompletedTasks - 1; i >= 0; i--) {
            Task completedTask = _display.getCompletedTasks().remove(i);
            markUndoneTask(completedTask);
            
        }
        _display.setVisibleCompletedTasks(_display.getCompletedTasks());
        _display.setMessage(GlobalConstants.MESSAGE_ALL_UNDONE);
    }

    private boolean isUndoneAll() {
        System.out.println(_taskNumbers == null);
        return _taskNumbers == null;
    }

    private void feedbackUndoneTasks(Task undoneTask, int i) {
        if (i == 0) {
            _msgUndone += GlobalConstants.INVERTED_COMMAS + undoneTask.getDescription()
                    + GlobalConstants.INVERTED_COMMAS;
        } else {
            _msgUndone += ", \"" + undoneTask.getDescription() + GlobalConstants.INVERTED_COMMAS;
        }
    }

    private void markUndoneTask(Task completedTask) {
        _tasks.add(completedTask);
        if (completedTask instanceof TaskEvent) {
            undoneTaskEvent(completedTask);
        } else if (completedTask instanceof TaskDeadline) {
            undoneTaskDeadline(completedTask);
        } else if (completedTask instanceof TaskFloat) {
            undoneTaskFloat(completedTask);
        }
        return;
    }

    private void undoneTaskEvent(Task completedTask) {
        TaskEvent task = (TaskEvent) completedTask;
        Command addCommand = new CommandAddEvent(task);
        _display = addCommand.execute(_display);
    }

    private void undoneTaskDeadline(Task completedTask) {
        TaskDeadline task = (TaskDeadline) completedTask;
        Command addCommand = new CommandAddDeadlineTask(task);
        _display = addCommand.execute(_display);
    }

    private void undoneTaskFloat(Task completedTask) {
        TaskFloat task = (TaskFloat) completedTask;
        Command addCommand = new CommandAddFloatTask(task);
        _display = addCommand.execute(_display);
    }

    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}
