/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 21 Mar, 11:00pm
 */
package bean;

import java.util.ArrayList;
import java.util.Collections;

public class CommandDone implements Command {
    private boolean updateFile;
    private ArrayList<Integer> taskNumbers;
    private Display display;
    private String completedMessage = "Completed: ";
    private String message_invalid_task_numbers = "You have specified invalid task numbers: ";
    private String message_all_tasks_completed = "All tasks completed";
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
        if (taskNumbers == null) {
            oldDisplay = completeAllTasks(oldDisplay);
            return oldDisplay;
        }
        this.display = oldDisplay;
        if (hasInvalidTaskNumbers()) {
            updateFile = false;
            saveHistory = false;
            display.setMessage(message_invalid_task_numbers);
            return oldDisplay;
        } else {
            Collections.sort(taskNumbers);
            completeTasksFromList();
            display.setMessage(completedMessage);
        }
        return display;
    }

    private Display completeAllTasks(Display oldDisplay) {
       /* for(int i =0; i< oldDisplay.getFloatTasks().size();i++){
            oldDisplay.getCompletedTasks().add(oldDisplay.getFloatTasks().get(i));
        }*/
        oldDisplay.getCompletedTasks().addAll(oldDisplay.getDeadlineTasks());
        oldDisplay.getCompletedTasks().addAll(oldDisplay.getEventTasks());
        oldDisplay.getCompletedTasks().addAll(oldDisplay.getFloatTasks());
        oldDisplay.setEvents(new ArrayList<TaskEvent>());
        oldDisplay.setFloatTasks(new ArrayList<TaskFloat>());
        oldDisplay.setDeadlineTasks(new ArrayList<TaskDeadline>());
        
        oldDisplay.setVisibleEvents(oldDisplay.getEventTasks());
        oldDisplay.setVisibleFloatTasks(oldDisplay.getFloatTasks());
        oldDisplay.setVisibleDeadlineTasks(oldDisplay.getDeadlineTasks());
        oldDisplay.setMessage(message_all_tasks_completed);
        return oldDisplay;
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
	            + display.getVisibleFloatTasks().size();
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

    private void completeTasksFromList() {
        Task completedTask;
        for (int i = 0; i < taskNumbers.size(); i++) {
            completedTask = markDoneTask(taskNumbers.get(i) - 1 - i);
            feedbackDeletedTasks(completedTask, i);
        }
        return;
    }

    private void feedbackDeletedTasks(Task deletedTask, int i) {
        if (i == 0) {
            completedMessage += "\"" + deletedTask.getDescription() + "\"";
        } else {
            completedMessage += ", \"" + deletedTask.getDescription() + "\"";
        }
    }

    private Task markDoneTask(int taskNum) {
        Task completedTask = null;
        if (taskNum < display.getVisibleDeadlineTasks().size()) {
            completedTask = display.getVisibleDeadlineTasks().remove(taskNum);
            display.getDeadlineTasks().remove(completedTask);
            display.getCompletedTasks().add(completedTask);
        } else {
            taskNum -= display.getVisibleDeadlineTasks().size();
            if (taskNum < display.getVisibleEvents().size()) {
                completedTask = display.getVisibleEvents().remove(taskNum);
                display.getEventTasks().remove(completedTask);
                display.getCompletedTasks().add(completedTask);
            } else {
                taskNum -= display.getVisibleEvents().size();
                if (taskNum < display.getVisibleFloatTasks().size()) {
                    completedTask = display.getVisibleFloatTasks().remove(taskNum);
                    display.getFloatTasks().remove(completedTask);
                    display.getCompletedTasks().add(completedTask);
                }
            }
        }
        return completedTask;
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
