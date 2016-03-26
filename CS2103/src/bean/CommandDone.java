/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 21 Mar, 11:00pm
 */
package bean;

import java.util.ArrayList;
import java.util.Collections;

public class CommandDone implements Command {
    private boolean updateFile;
    private ArrayList<Integer> taskNumbers;
    private Display display;
    private String doneMessage = "You have completed: ";
    private String message_invalid_task_numbers = "You have specified invalid task numbers: ";
    private boolean saveHistory = true;

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
        if (hasInvalidTaskNumbers(display.getNumberOfTasks())) {
            updateFile = false;
            saveHistory = false;
            display.setMessage(message_invalid_task_numbers);
            return oldDisplay;
            // return (new Display(message_invalid_task_numbers));
        } else {
            Collections.sort(taskNumbers);
            markDoneTasksFromList();
            display.setMessage(doneMessage);
        }
        return display;
    }

    private boolean hasInvalidTaskNumbers(int numOfTasks) {
        ArrayList<Integer> invalidTaskNumbers = new ArrayList<Integer>();
        int taskNum;
        for (int i = 0; i < taskNumbers.size(); i++) {
            taskNum = taskNumbers.get(i);
            if (isTaskNumberInvalid(numOfTasks, taskNum)) {
                feedbackInvalidNumbers(invalidTaskNumbers, taskNum);
                invalidTaskNumbers.add(taskNum);
            }
        }
        return (invalidTaskNumbers.size() > 0);
    }

    private void feedbackInvalidNumbers(ArrayList<Integer> invalidTaskNumbers, int taskNum) {
        if (invalidTaskNumbers.size() == 0) {
            message_invalid_task_numbers += taskNum;
        } else {
            message_invalid_task_numbers += ", " + taskNum;
        }
    }

    private boolean isTaskNumberInvalid(int numOfTasks, int taskNum) {
        return (taskNum > numOfTasks) || (taskNum < 1);
    }

    private void markDoneTasksFromList() {
        Task deletedTask;
        for (int i = 0; i < taskNumbers.size(); i++) {
            deletedTask = markTaskDone(taskNumbers.get(i) - 1 - i);

        }
        return;
    }

    private void feedbackCompletedTasks(Task deletedTask, int i) {
        if (i == 0) {
            doneMessage += "\"" + deletedTask.getDescription() + "\"";
        } else {
            doneMessage += ", \"" + deletedTask.getDescription() + "\"";
        }
    }

    private Task markTaskDone(int taskNum) {
        Task deletedTask;
        if (taskNum < display.getDeadlineTasks().size()) {
            deletedTask = display.getDeadlineTasks().remove(taskNum);
            display.getCompletedTasks().add(deletedTask);
        } else {
            taskNum -= display.getDeadlineTasks().size();
            if (taskNum < display.getEventTasks().size()) {
                deletedTask = display.getEventTasks().remove(taskNum);
                display.getCompletedTasks().add(deletedTask);
            } else {
                taskNum -= display.getEventTasks().size();
                if (taskNum < display.getFloatTasks().size()) {
                    deletedTask = display.getFloatTasks().remove(taskNum);
                    display.getCompletedTasks().add(deletedTask);
                } else {
                    taskNum -= display.getFloatTasks().size();
                    deletedTask = display.getReservedTasks().remove(taskNum);
                    display.getCompletedTasks().add(deletedTask);
                }
            }
        }
        return deletedTask;
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
