/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.util.ArrayList;
import java.util.Collections;

public class CommandDelete implements Command {
    private static final String COMMAND_TYPE_DELETE = "Delete";
    private static final String COMMAND_TYPE_INVALID = "Invalid";
    private static final String MESSAGE_NO_NUMBER_SPECIFIED = "please specify a number(s)";
    private boolean updateFile;
    private ArrayList<Integer> taskNumbers;
    private Display display;
    private String deletedMessage = "deleted: ";
    private String message_invalid_task_numbers = "You have specified invalid task numbers: ";
    private String message_all_tasks_deleted = "All tasks deleted";
    private boolean saveHistory = true;

    public CommandDelete() {
        updateFile = true;
        this.taskNumbers = null;
        this.display = null;
    }

    public CommandDelete(ArrayList<Integer> taskNumbers) {
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
            deleteTasksFromList();
        }
        return display;
    }

    private void setInvalidDisplay() {
        updateFile = false;
        saveHistory = false;
        display.setCommandType(COMMAND_TYPE_INVALID);
        display.setMessage(message_invalid_task_numbers);
    }

    private void setDisplay(String msg, String commandType, ArrayList<Integer> deletedTasks,
            ArrayList<Integer> conflictingTasks) {
        display.setMessage(msg);
        display.setCommandType(commandType);
        incrementTaskNumbers();
        display.setTaskIndices(deletedTasks);
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
                + display.getVisibleFloatTasks().size() + display.getVisibleReservedTasks().size()
                + display.getVisibleCompletedTasks().size();
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

    private void deleteTasksFromList() {
        if (isDeleteAll()) {
            deleteAllShownTasks();
            return;
        } else {
            deleteMultipleTasks();
            return;
        }
    }

    private void deleteMultipleTasks() {
        Task deletedTask;
        Collections.sort(taskNumbers);
        for (int i = 0; i < taskNumbers.size(); i++) {
            deletedTask = removeTask(taskNumbers.get(i) - 1 - i);
            feedbackDeletedTasks(deletedTask, i);
        }
        setDisplay(deletedMessage, COMMAND_TYPE_DELETE, taskNumbers, new ArrayList<Integer>());
    }

    private void deleteAllShownTasks() {
        int numTasks = getNumOfTasks();
        for (int i = 0; i < numTasks; i++) {
            removeTask(numTasks - i - 1);
        }
        setDisplay(message_all_tasks_deleted, COMMAND_TYPE_DELETE, new ArrayList<Integer>(), new ArrayList<Integer>());
    }

    private boolean isDeleteAll() {
        return taskNumbers == null;
    }

    private void feedbackDeletedTasks(Task deletedTask, int i) {
        if (i == 0) {
            deletedMessage += "\"" + deletedTask.getDescription() + "\"";
        } else {
            deletedMessage += ", \"" + deletedTask.getDescription() + "\"";
        }
    }

    private Task removeTask(int taskNum) {
        Task deletedTask;
        if (isDeleteTaskDeadline(taskNum)) {
            deletedTask = deleteTaskDeadline(taskNum);
        } else {
            taskNum -= display.getVisibleDeadlineTasks().size();
            if (isDeleteTaskEvent(taskNum)) {
                deletedTask = deleteEvent(taskNum);
            } else {
                taskNum -= display.getVisibleEvents().size();
                if (isDeleteTaskFloat(taskNum)) {
                    deletedTask = deleteTaskFloat(taskNum);
                } else {
                    taskNum -= display.getVisibleFloatTasks().size();
                    if (isDeleteTaskReserved(taskNum)) {
                        deletedTask = deleteTaskReserved(taskNum);
                    } else {
                        taskNum -= display.getVisibleReservedTasks().size();
                        deletedTask = deleteTaskCompleted(taskNum);
                    }
                }
            }
        }
        return deletedTask;
    }

    private boolean isDeleteTaskReserved(int taskNum) {
        return taskNum < display.getVisibleReservedTasks().size();
    }

    private boolean isDeleteTaskFloat(int taskNum) {
        return taskNum < display.getVisibleFloatTasks().size();
    }

    private boolean isDeleteTaskEvent(int taskNum) {
        return taskNum < display.getVisibleEvents().size();
    }

    private boolean isDeleteTaskDeadline(int taskNum) {
        return taskNum < display.getVisibleDeadlineTasks().size();
    }

    private Task deleteTaskCompleted(int taskNum) {
        Task deletedTask = display.getVisibleCompletedTasks().remove(taskNum);
        display.getCompletedTasks().remove(deletedTask);
        return deletedTask;
    }

    private Task deleteTaskReserved(int taskNum) {
        Task deletedTask = display.getVisibleReservedTasks().remove(taskNum);
        display.getReservedTasks().remove(deletedTask);
        return deletedTask;
    }

    private Task deleteTaskFloat(int taskNum) {
        Task deletedTask = display.getVisibleFloatTasks().remove(taskNum);
        display.getFloatTasks().remove(deletedTask);
        return deletedTask;
    }

    private Task deleteEvent(int taskNum) {
        Task deletedTask = display.getVisibleEvents().remove(taskNum);
        display.getEventTasks().remove(deletedTask);
        return deletedTask;
    }

    private Task deleteTaskDeadline(int taskNum) {
        Task deletedTask = display.getVisibleDeadlineTasks().remove(taskNum);
        display.getDeadlineTasks().remove(deletedTask);
        return deletedTask;
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
