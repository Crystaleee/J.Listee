package bean;

import java.util.ArrayList;

import History.History;

public class CommandShow implements Command {
    private boolean updateFile;
    private final String message_no_tasks = "No such tasks found";
    private final String message_show = "Showing tasks containing ";
    private String keyword;
    private int count;
    private Display oldDisplay;
    private Display newDisplay;

    public CommandShow() {
        this.keyword = null;
        updateFile = false;
        newDisplay = null;
        count = 0;
    }

    public CommandShow(String keyword) {
        this.keyword = keyword;
        updateFile = false;
        newDisplay = new Display();
        count = 0;
    }

    public Display execute(Display oldDisplay) {
        if (keyword == null) {
            newDisplay = History.getDisplay(0);
            newDisplay.setMessage("Pls enter keyword");
            return newDisplay;
        }

        this.oldDisplay = oldDisplay;
        
        newDisplay = getTasksContainingKeyword();
        
        if (count == 0) {
            newDisplay = new Display(message_no_tasks);
        } else {
            newDisplay.setMessage(message_show);;
        }

        return newDisplay;
    }

    public Display getTasksContainingKeyword() {
        getFloatTasks();
        getEventTasks();
        getDeadLineTasks();
        return newDisplay;
    }
    
    public void getDeadLineTasks() {
        TaskDeadline task;
        for (int i = 0; i < oldDisplay.getDeadlineTasks().size(); i++) {
            task = oldDisplay.getDeadlineTasks().get(i);
            if(task.getDescription().contains(keyword)){
                newDisplay.getDeadlineTasks().add(task);
                count++;
            }
        }
    }
    
    public void getEventTasks() {
        TaskEvent task;
        for (int i = 0; i < oldDisplay.getEventTasks().size(); i++) {
            task = oldDisplay.getEventTasks().get(i);
            if(task.getDescription().contains(keyword)){
                newDisplay.getEventTasks().add(task);
                count++;
            }
        }
    }

    public void getFloatTasks() {
        TaskFloat task;
        for (int i = 0; i < oldDisplay.getFloatTasks().size(); i++) {
            task = oldDisplay.getFloatTasks().get(i);
            if(task.getDescription().contains(keyword)){
                newDisplay.getFloatTasks().add(task);
                count++;
            }
        }
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
