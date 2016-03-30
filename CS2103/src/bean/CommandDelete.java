/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 21 Mar, 11:00pm
 */
package bean;

import java.util.ArrayList;
import java.util.Collections;

public class CommandDelete implements Command {
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
        if (taskNumbers == null) {
            display = new Display(message_all_tasks_deleted);
            return display;
        }
        this.display = oldDisplay;
        if (hasInvalidTaskNumbers()) {
            updateFile = false;
            saveHistory = false;
            display.setMessage(message_invalid_task_numbers);
            return oldDisplay;
        } else {
            Collections.sort(taskNumbers);
            deleteTasksFromList();
            display.setMessage(deletedMessage);
        }
        return display;
    }

    private boolean hasInvalidTaskNumbers() {
        ArrayList<Integer> invalidTaskNumbers = new ArrayList<Integer>();
        int taskNum, numOfTasks = getNumOfTasks();
        for (int i = 0; i < taskNumbers.size(); i++) {
            taskNum = taskNumbers.get(i);
            if (isTaskNumberInvalid(taskNum, numOfTasks)) {
                feedbackInvalidNumbers(invalidTaskNumbers, taskNum);
                invalidTaskNumbers.add(taskNum);
            }
        }
        return (invalidTaskNumbers.size() > 0);
    }

    private int getNumOfTasks(){
	    int numOfTasks = 0;
	    numOfTasks += display.getVisibleDeadlineTasks().size() + display.getVisibleEvents().size()
	            + display.getVisibleFloatTasks().size() + display.getVisibleReservedTasks().size();
	    //System.out.println("num " + numOfTasks);
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
        Task deletedTask;
        for (int i = 0; i < taskNumbers.size(); i++) {
            deletedTask = removeTask(taskNumbers.get(i) - 1 - i);
            feedbackDeletedTasks(deletedTask, i);
        }
        return;
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
        if (taskNum < display.getVisibleDeadlineTasks().size()) {
            deletedTask = display.getVisibleDeadlineTasks().remove(taskNum);
            //System.out.println(deletedTask.getDescription());
            display.getDeadlineTasks().remove(deletedTask);
        } else {
            taskNum -= display.getVisibleDeadlineTasks().size();
            if (taskNum < display.getVisibleEvents().size()) {
                deletedTask = display.getVisibleEvents().remove(taskNum);
                //System.out.println(deletedTask.getDescription());
                display.getEventTasks().remove(deletedTask);
            } else {
                taskNum -= display.getVisibleEvents().size();
                if (taskNum < display.getVisibleFloatTasks().size()) {
                    deletedTask = display.getVisibleFloatTasks().remove(taskNum);
                    //System.out.println(deletedTask.getDescription());
                    display.getFloatTasks().remove(deletedTask);
                } else {
                    taskNum -= display.getVisibleFloatTasks().size();
                    deletedTask = display.getVisibleReservedTasks().remove(taskNum);
                    //System.out.println(deletedTask.getDescription());
                    display.getReservedTasks().remove(deletedTask);
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
