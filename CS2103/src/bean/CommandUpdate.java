/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 3:20am
 * CS2103
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

public class CommandUpdate extends TaskEvent implements Command {
    private Integer taskNumber;
    private ArrayList<String> removeTags;
    private Display display;
    private boolean updateFile;
    private final String message_invalid_task_number = "Pls specify a valid task number";
    private boolean saveHistory = true;

    public CommandUpdate() {
        super();
        removeTags = null;
        updateFile = true;
        this.taskNumber = null;
    }

    public CommandUpdate(Integer taskNumber, String description) {
        super(description, null, null, null, null);
        removeTags = null;
        updateFile = true;
        this.taskNumber = taskNumber;
    }

    public CommandUpdate(Integer taskNumber, String description, String location, Calendar startDate,
            Calendar endDate, ArrayList<String> addTags, ArrayList<String> removeTags) {
        super(description, location, startDate, endDate, addTags);
        this.removeTags = removeTags;
        updateFile = true;
        this.taskNumber = taskNumber;
    }

    public Display execute(Display oldDisplay) {
        display = oldDisplay;
        if (hasInvalidTaskNumber(display.getNumberOfTasks())) {
            updateFile = false;
            saveHistory = false;
            return (new Display(message_invalid_task_number));
        }
        editTask();
        return display;
    }

    public boolean hasInvalidTaskNumber(int numOfTasks) {
        return ((taskNumber > numOfTasks) || (taskNumber < 1));
    }

    public void editTask() {
        if (taskNumber <= display.getDeadlineTasks().size()) {
            editDeadline();
        } else {
            taskNumber -= display.getDeadlineTasks().size();
            if (taskNumber <= display.getEventTasks().size()) {
                editEvent();
            } else {
                taskNumber -= display.getEventTasks().size();
                editFloat();
            }
        }
    }

    public void editDeadline() {
        if (getDescription() != null) {
            display.getDeadlineTasks().get(taskNumber - 1).setDescription(getDescription());
        }
        if (getLocation() != null) {
            display.getDeadlineTasks().get(taskNumber - 1).setLocation(getLocation());
        }
        if(getEndDate() != null){
            display.getDeadlineTasks().get(taskNumber - 1).setEndDate(getEndDate());
        }
        if(getTags() != null){
            for(int i = 0; i < getTags().size(); i++){
                String tag = getTags().get(i);
                display.getDeadlineTasks().get(taskNumber - 1).getTags().add(tag);
            }
        }
        if(removeTags != null){
            for(int i = 0; i < getTags().size(); i++){
                String tag = removeTags.get(i);
                display.getDeadlineTasks().get(taskNumber - 1).getTags().remove(tag);
            }
        }
        TaskDeadline task;
        if (getEndDate().getTimeInMillis() == 0) {
            task = display.getDeadlineTasks().remove(taskNumber - 1);
            Command addCommand = new CommandAddFloatTask(task.getDescription(), task.getLocation(),
                    task.getTags());
            display = addCommand.execute(display);
        } else if (getStartDate() != null) {
            task = display.getDeadlineTasks().remove(taskNumber - 1);
            Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(),
                    getStartDate(), task.getEndDate(), task.getTags());
            display = addCommand.execute(display);
        }
    }

    public void editEvent() {
        if (getDescription() != null) {
            display.getEventTasks().get(taskNumber - 1).setDescription(getDescription());
        }
        if (getLocation() != null) {
            display.getEventTasks().get(taskNumber - 1).setLocation(getLocation());
        }
        if(getStartDate() != null){
            display.getEventTasks().get(taskNumber - 1).setStartDate(getStartDate());
        }
        if(getEndDate() != null){
            display.getEventTasks().get(taskNumber - 1).setEndDate(getEndDate());
        }
        if(getTags() != null){
            for(int i = 0; i < getTags().size(); i++){
                String tag = getTags().get(i);
                display.getEventTasks().get(taskNumber - 1).getTags().add(tag);
            }
        }
        if(removeTags != null){
            for(int i = 0; i < getTags().size(); i++){
                String tag = removeTags.get(i);
                display.getEventTasks().get(taskNumber - 1).getTags().remove(tag);
            }
        }
        changeEventTaskType();
    }

    public void editFloat() {
        if (getDescription() != null) {
            display.getFloatTasks().get(taskNumber - 1).setDescription(getDescription());
        }
        if (getLocation() != null) {
            display.getFloatTasks().get(taskNumber - 1).setLocation(getLocation());
        }
        if(getTags() != null){
            for(int i = 0; i < getTags().size(); i++){
                String tag = getTags().get(i);
                display.getFloatTasks().get(taskNumber - 1).getTags().add(tag);
            }
        }
        if(removeTags != null){
            for(int i = 0; i < getTags().size(); i++){
                String tag = removeTags.get(i);
                display.getFloatTasks().get(taskNumber - 1).getTags().remove(tag);
            }
        }
        changeFloatTaskType();
    }

    public void changeEventTaskType() {
        TaskEvent task;
        if((getStartDate() != null) && (getEndDate() !=null)){
            if ((getStartDate().getTimeInMillis() == 0) && (getEndDate().getTimeInMillis() != 0)) {
                task = display.getEventTasks().remove(taskNumber - 1);
                Command addCommand = new CommandAddFloatTask(task.getDescription(), task.getLocation(),
                        task.getTags());
                display = addCommand.execute(display);
            }
        } else if (getStartDate() != null) {
            if (getStartDate().getTimeInMillis() == 0){
                task = display.getEventTasks().remove(taskNumber - 1);
                Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                        task.getEndDate(), task.getTags());
                display = addCommand.execute(display);
            }
        }
    }

    public void changeFloatTaskType() {
        TaskFloat task;
        if ((getStartDate() != null) && (getEndDate() != null)) {
            task = display.getFloatTasks().remove(taskNumber - 1);
            Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(),
                    getStartDate(), getEndDate(), task.getTags());
            display = addCommand.execute(display);
        } else if (getEndDate() != null) {
            task = display.getFloatTasks().remove(taskNumber - 1);
            Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                    getEndDate(), task.getTags());
            display = addCommand.execute(display);
        }
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}