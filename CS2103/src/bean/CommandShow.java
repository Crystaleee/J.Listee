/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 21 Mar, 11:00pm
 */
package bean;

import History.History;

public class CommandShow implements Command {
    private final String message_no_tasks = "No such tasks found";
    private final String message_show = "Displaying tasks containing ";
    private boolean updateFile = false;
    private boolean saveHistory = true;
    private String keyword;
    private int count;
    private Display oldDisplay;
    private Display newDisplay;

    public CommandShow() {
        this.keyword = null;
        newDisplay = null;
        count = 0;
    }

    public CommandShow(String keyword) {
        this.keyword = keyword.toLowerCase();
        newDisplay = new Display();
        count = 0;
    }

    public Display execute(Display oldDisplay) {
        if (keyword == null) {
            newDisplay = History.getDisplay(0);
            newDisplay.setMessage("Displaying all tasks");
            return newDisplay;
        }

        this.oldDisplay = oldDisplay;
        
        newDisplay = getTasksContainingKeyword();
        
        if (count == 0) {
            newDisplay = new Display(message_no_tasks);
        } else {
            newDisplay.setMessage(message_show+keyword);;
        }

        return newDisplay;
    }

    private Display getTasksContainingKeyword() {
        getFloatTasks();
        getEventTasks();
        getDeadLineTasks();
        getReservedTasks();
        return newDisplay;
    }
    
    private void getReservedTasks() {
        TaskReserved task;
        for (int i = 0; i < oldDisplay.getReservedTasks().size(); i++) {
            task = oldDisplay.getReservedTasks().get(i);
            if(task.getDescription().toLowerCase().contains(keyword)){
                newDisplay.getReservedTasks().add(task);
                count++;
            }
        }
    }
    
    private void getDeadLineTasks() {
        TaskDeadline task;
        for (int i = 0; i < oldDisplay.getDeadlineTasks().size(); i++) {
            task = oldDisplay.getDeadlineTasks().get(i);
            if(task.getDescription().toLowerCase().contains(keyword)){
                newDisplay.getDeadlineTasks().add(task);
                count++;
            }
        }
    }
    
    private void getEventTasks() {
        TaskEvent task;
        for (int i = 0; i < oldDisplay.getEventTasks().size(); i++) {
            task = oldDisplay.getEventTasks().get(i);
            if(task.getDescription().toLowerCase().contains(keyword)){
                newDisplay.getEventTasks().add(task);
                count++;
            }
        }
    }

    private void getFloatTasks() {
        TaskFloat task;
        for (int i = 0; i < oldDisplay.getFloatTasks().size(); i++) {
            task = oldDisplay.getFloatTasks().get(i);
            if(task.getDescription().toLowerCase().contains(keyword)){
                newDisplay.getFloatTasks().add(task);
                count++;
            }
        }
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
