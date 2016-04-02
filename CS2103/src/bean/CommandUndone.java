/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.util.ArrayList;
import java.util.Collections;

public class CommandUndone implements Command {
    private static final String COMMAND_TYPE_INVALID = "Invalid";
    private static final String COMMAND_TYPE_UNDONE = "Undone";
    private static final String MESSAGE_ALL_UNDONE = "All tasks undone";
    private ArrayList<Integer> taskNumbers;
    private ArrayList<Integer> taskIndices;
    private Display display;
    private String undoneMessage = "Undone task: ";
    private String message_invalid_task_numbers = "You have specified invalid task numbers: ";
    private boolean saveHistory = true;
    private boolean updateFile = true;

    public CommandUndone() {
        this.taskNumbers = null;
        this.display = null;
    }

    public CommandUndone(ArrayList<Integer> taskNumbers) {
        this.taskNumbers = taskNumbers;
        this.display = null;
    }

    public Display execute(Display oldDisplay) {
        this.display = oldDisplay;
        if (taskNumbers != null) {
            if (hasInvalidTaskNumbers()) {
                setInvalidDisplay();
                return oldDisplay;
            }
            Collections.sort(taskNumbers);
        }

        undoneTasksFromList();
        setDisplay(COMMAND_TYPE_UNDONE, taskNumbers);
        return display;
    }

    private void setInvalidDisplay() {
        updateFile = false;
        saveHistory = false;
        display.setCommandType(COMMAND_TYPE_INVALID);
        display.setMessage(message_invalid_task_numbers);
    }

    private void setDisplay(String commandType, ArrayList<Integer> completedTasks) {
        display.setCommandType(commandType);
        incrementTaskNumbers();
        display.setTaskIndices(completedTasks);
    }

    private void incrementTaskNumbers() {
        for (int i = 0; i < taskNumbers.size(); i++) {
            taskNumbers.set(i, taskNumbers.get(i) + 1);
        }
    }

    private boolean hasInvalidTaskNumbers() {
        ArrayList<Integer> invalidTaskNumbers = new ArrayList<Integer>();
        int taskNum, maxNum = getNumOfVisibleTasks(), minNum = getMinimumTaskNum();
        for (int i = 0; i < taskNumbers.size(); i++) {
            taskNum = taskNumbers.get(i);
            if (isTaskNumberInvalid(taskNum, maxNum, minNum)) {
                feedbackInvalidNumbers(invalidTaskNumbers, taskNum);
                invalidTaskNumbers.add(taskNum);
            }
        }
        return (invalidTaskNumbers.size() > 0);
    }

    private int getMinimumTaskNum() {
        int minNum = display.getVisibleDeadlineTasks().size() + display.getVisibleEvents().size()
                + display.getVisibleFloatTasks().size() + display.getVisibleReservedTasks().size() + 1;
        return minNum;
    }

    private int getNumOfVisibleTasks() {
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
        for (int i = 0; i < taskNumbers.size(); i++) {
            Task completedTask = display.getCompletedTasks().remove(taskNumbers.get(i) - 1 - i);
            markUndoneTask(completedTask);
            feedbackUndoneTasks(completedTask, i);
        }
        display.setMessage(undoneMessage);
    }

    private void undoneAllVisibleDoneTasks() {
        int numOfVisibleCompletedTasks = display.getVisibleCompletedTasks().size();
        for (int i = numOfVisibleCompletedTasks - 1; i >= 0; i--) {
            Task completedTask = display.getCompletedTasks().remove(i);
            markUndoneTask(completedTask);
        }
        display.setMessage(MESSAGE_ALL_UNDONE);
    }

    private boolean isUndoneAll() {
        return taskNumbers == null;
    }

    private void feedbackUndoneTasks(Task undoneTask, int i) {
        if (i == 0) {
            undoneMessage += "\"" + undoneTask.getDescription() + "\"";
        } else {
            undoneMessage += ", \"" + undoneTask.getDescription() + "\"";
        }
    }

    private void markUndoneTask(Task completedTask) {
        //System.out.println(completedTask.getDescription());
        if (completedTask instanceof TaskEvent) {
            undoneTaskEvent(completedTask);
            // System.out.println(task.getDescription());
        } else if (completedTask instanceof TaskDeadline) {
            undoneTaskDeadline(completedTask);
            // System.out.println(task.getDescription());
        } else if (completedTask instanceof TaskFloat) {
            undoneTaskFloat(completedTask);
            // System.out.println(task.getDescription());
        } else {
            System.out.println("Err");
        }
        return ;
    }

    private void undoneTaskEvent(Task completedTask) {
        TaskEvent task = (TaskEvent) completedTask;
        Command addCommand = new CommandAddEvent(task);
        display = addCommand.execute(display);
    }

    private void undoneTaskDeadline(Task completedTask) {
        TaskDeadline task = (TaskDeadline) completedTask;
        Command addCommand = new CommandAddDeadlineTask(task);
        display = addCommand.execute(display);
    }

    private void undoneTaskFloat(Task completedTask) {
        TaskFloat task = (TaskFloat) completedTask;
        Command addCommand = new CommandAddFloatTask(task);
        display = addCommand.execute(display);
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
