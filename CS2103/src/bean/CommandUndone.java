/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 31 Mar
 */
package bean;

import java.util.ArrayList;
import java.util.Collections;

public class CommandUndone implements Command {
    private boolean updateFile;
    private ArrayList<Integer> taskNumbers;
    private Display display;
    private String undoneMessage = "Undone task: ";
    private String message_invalid_task_numbers = "You have specified invalid task numbers: ";
    private String message_all_tasks_undone = "All tasks undone";
    private boolean saveHistory = true;

    public CommandUndone() {
        updateFile = true;
        this.taskNumbers = null;
        this.display = null;
    }

    public CommandUndone(ArrayList<Integer> taskNumbers) {
        updateFile = true;
        this.taskNumbers = taskNumbers;
        this.display = null;
    }

    public Display execute(Display oldDisplay) {
        this.display = oldDisplay;
        if (taskNumbers != null) {
            if (hasInvalidTaskNumbers()) {
                updateFile = false;
                saveHistory = false;
                display.setMessage(message_invalid_task_numbers);
                return oldDisplay;
            }
            Collections.sort(taskNumbers);
        }

        undoneTasksFromList();

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

    private int getNumOfTasks() {
        int numOfTasks = display.getCompletedTasks().size();
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

    private void undoneTasksFromList() {
        Task undoneTask;
        if (taskNumbers == null) {
            for (int i = display.getCompletedTasks().size() - 1; i >= 0; i--) {
                undoneTask = markUndoneTask(i);
            }
            display.setMessage(message_all_tasks_undone);
        }else{
            for (int i = 0; i < taskNumbers.size(); i++) {
                undoneTask = markUndoneTask(taskNumbers.get(i) - 1 - i);
                feedbackUndoneTasks(undoneTask, i);
            }
            display.setMessage(undoneMessage);
        }
        return;
    }

    private void feedbackUndoneTasks(Task undoneTask, int i) {
        if (i == 0) {
            undoneMessage += "\"" + undoneTask.getDescription() + "\"";
        } else {
            undoneMessage += ", \"" + undoneTask.getDescription() + "\"";
        }
    }

    private Task markUndoneTask(int taskNum) {
        Task completedTask = display.getCompletedTasks().remove(taskNum);
        System.out.println(completedTask.getDescription());
        if(completedTask instanceof TaskEvent){
            TaskEvent task = (TaskEvent)completedTask;
            Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(),
                    task.getStartDate(), task.getEndDate(), task.getTags());
            display = addCommand.execute(display);
            //System.out.println(task.getDescription());
        }
        else if(completedTask instanceof TaskDeadline){
            TaskDeadline task = (TaskDeadline)completedTask;
            Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                    task.getEndDate(), task.getTags());
            display = addCommand.execute(display);
            //System.out.println(task.getDescription());
        }
        else if(completedTask instanceof TaskFloat){
            TaskFloat task = (TaskFloat)completedTask;
            Command addCommand = new CommandAddFloatTask(task.getDescription(), task.getLocation(), task.getTags());
            display = addCommand.execute(display);
            //System.out.println(task.getDescription());
        }else{
            System.out.println("Err");
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
