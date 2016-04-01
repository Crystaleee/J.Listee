/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.util.ArrayList;
import java.util.Collections;

public class CommandDone implements Command {
    private static final String COMMAND_TYPE_DONE = "Done";
    private static final String COMMAND_TYPE_INVALID = "Invalid";
    private static final String MESSAGE_NO_NUMBER_SPECIFIED = "please specify a number(s)";
    private static final String MESSAGE_ALL_TASKS_COMPLETED = "All shown tasks completed";
    private String message_invalid_task_numbers = "You have specified invalid task numbers: ";
    private ArrayList<Integer> taskNumbers;
    private Display display;
    private String message_completed = "Completed: ";
    private boolean saveHistory = true;
    private boolean updateFile;

    public CommandDone() {
        updateFile = true;
        this.taskNumbers = null;
        this.display = null;
    }

    public CommandDone(ArrayList<Integer> taskNumbers) {
        updateFile = true;
        this.taskNumbers = taskNumbers;
        this.display = null;
    }

    public Display execute(Display oldDisplay) {
        this.display = oldDisplay;
        if (taskNumbers != null) {
            if (taskNumbers.isEmpty()) {
                setDisplay(MESSAGE_NO_NUMBER_SPECIFIED, COMMAND_TYPE_INVALID, new ArrayList<Integer>(),
                        new ArrayList<Integer>());
                return display;
            }
        }
        if (hasInvalidTaskNumbers()) {
            setInvalidDisplay();
            return display;
        } else {
            doneTasksFromList();
        }
        return display;
    }

    private void setInvalidDisplay() {
        updateFile = false;
        saveHistory = false;
        display.setCommandType(COMMAND_TYPE_INVALID);
        display.setMessage(message_invalid_task_numbers);
    }

    private void setDisplay(String msg, String commandType, ArrayList<Integer> completedTasks,
            ArrayList<Integer> conflictingTasks) {
        display.setMessage(msg);
        display.setCommandType(commandType);
        incrementTaskNumbers();
        display.setTaskIndices(completedTasks);
        display.setConflictingTasksIndices(conflictingTasks);
    }

    private void incrementTaskNumbers() {
        if (taskNumbers != null) {
            for (int i = 0; i < taskNumbers.size(); i++) {
                taskNumbers.set(i, taskNumbers.get(i) + 1);
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
        for (int i = 0; i < taskNumbers.size(); i++) {
            taskNum = taskNumbers.get(i);
            if (isTaskNumberInvalid(taskNum, numOfTasks)) {
                feedbackInvalidNumbers(invalidTaskNumbers, taskNum);
                invalidTaskNumbers.add(taskNum);
            }
        }
        return invalidTaskNumbers;
    }

    private int getNumOfTasks() {
        int numOfTasks = display.getVisibleDeadlineTasks().size() + display.getVisibleEvents().size()
                + display.getVisibleFloatTasks().size();
        return numOfTasks;
    }

    private void feedbackInvalidNumbers(ArrayList<Integer> invalidTaskNumbers, int taskNum) {
        if (invalidTaskNumbers.size() == 0) {
            message_invalid_task_numbers += taskNum;
        } else {
            message_invalid_task_numbers += ", " + taskNum;
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
        Collections.sort(taskNumbers);
        for (int i = 0; i < taskNumbers.size(); i++) {
            doneTask = markTaskAsDone(taskNumbers.get(i) - 1 - i);
            feedbackCompletedTasks(doneTask, i);
        }
        setDisplay(message_completed, COMMAND_TYPE_DONE, taskNumbers, new ArrayList<Integer>());
    }

    private void doneAllVisibleTasks() {
        int numTasks = getNumOfTasks();
        for (int i = 0; i < numTasks; i++) {
            markTaskAsDone(numTasks - i - 1);
        }
        setDisplay(MESSAGE_ALL_TASKS_COMPLETED, COMMAND_TYPE_DONE, new ArrayList<Integer>(),
                new ArrayList<Integer>());
    }

    private boolean isDoneAll() {
        return taskNumbers == null;
    }

    private void feedbackCompletedTasks(Task doneTask, int i) {
        if (i == 0) {
            message_completed += "\"" + doneTask.getDescription() + "\"";
        } else {
            message_completed += ", \"" + doneTask.getDescription() + "\"";
        }
    }

    private Task markTaskAsDone(int taskNum) {
        Task doneTask;
        if (isDoneTaskDeadline(taskNum)) {
            doneTask = doneTaskDeadline(taskNum);
        } else {
            taskNum -= display.getVisibleDeadlineTasks().size();
            if (isDoneTaskEvent(taskNum)) {
                doneTask = doneEvent(taskNum);
            } else {
                taskNum -= display.getVisibleEvents().size();
                doneTask = doneTaskFloat(taskNum);
            }
        }
        return doneTask;
    }

    private boolean isDoneTaskEvent(int taskNum) {
        return taskNum < display.getVisibleEvents().size();
    }

    private boolean isDoneTaskDeadline(int taskNum) {
        return taskNum < display.getVisibleDeadlineTasks().size();
    }

    private Task doneTaskFloat(int taskNum) {
        Task completedTask = display.getVisibleFloatTasks().remove(taskNum);
        display.getFloatTasks().remove(completedTask);
        display.getCompletedTasks().add(completedTask);
        return completedTask;
    }

    private Task doneEvent(int taskNum) {
        Task completedTask = display.getVisibleEvents().remove(taskNum);
        display.getEventTasks().remove(completedTask);
        display.getCompletedTasks().add(completedTask);
        return completedTask;
    }

    private Task doneTaskDeadline(int taskNum) {
        Task completedTask = display.getVisibleDeadlineTasks().remove(taskNum);
        display.getDeadlineTasks().remove(completedTask);
        display.getCompletedTasks().add(completedTask);
        return completedTask;
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
