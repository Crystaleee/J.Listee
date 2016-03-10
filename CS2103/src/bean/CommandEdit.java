package bean;

import java.util.ArrayList;
import java.util.Calendar;

public class CommandEdit extends Command{
    private Integer taskNumber;
    private String description;
    private Calendar addStartDate;
    private Calendar addEndDate;
    private boolean deleteStartDate;
    private boolean deleteEndDate;
    private Display display;
    private final String MESSAGE_ERROR_INVALID_TASK_NUMBER = "Pls specify a valid task number";
    
    public CommandEdit(){
        this.taskNumber = null;
        this.description = null;
        this.addStartDate = null;
        this.addEndDate = null;
        this.deleteStartDate = false;
        this.deleteEndDate = false;
    }
    public CommandEdit(Integer taskNumber, String description){
        this.taskNumber = taskNumber;
        this.description = description;
        this.addStartDate = null;
        this.addEndDate = null;
        this.deleteStartDate = false;
        this.deleteEndDate = false;
    }
    public CommandEdit(Integer taskNumber, String description, Calendar addStartDate, 
            Calendar addEndDate, boolean deleteStartDate, boolean deleteEndDate){
        this.taskNumber = taskNumber;
        this.description = description;
        this.addStartDate = addStartDate;
        this.addEndDate = addEndDate;
        this.deleteStartDate = deleteStartDate;
        this.deleteEndDate = deleteEndDate;
    }
    
    public Display execute(Display oldDisplay){
        if(hasInvalidTaskNumber(display.getNumberOfTasks())){
            display.setMessage(MESSAGE_ERROR_INVALID_TASK_NUMBER);
            return display;
        }
        display = oldDisplay;
        editDescription();
        //editStartDate(taskList);
        //editEndDate(taskList);
        //setDisplay(null, taskList);
        
        return display;
    }
    
    public boolean hasInvalidTaskNumber(int numOfTasks){
        return ((taskNumber > numOfTasks) || (taskNumber < 1));
    }
    
    public void editDescription() {
        if(description != null){
            if(taskNumber < display.getDeadlineTasks().size()){
                display.getDeadlineTasks().get(taskNumber-1).setDescription(description);;
            }
            else{
                taskNumber -= display.getDeadlineTasks().size();
                if(taskNumber < display.getEventTasks().size()){
                    display.getEventTasks().get(taskNumber-1).setDescription(description);;
                }
                else{
                    taskNumber -= display.getEventTasks().size();
                    display.getFloatTasks().get(taskNumber-1).setDescription(description);;
                }
            }
        }
    }
    /*
    public void editEndDate(ArrayList<Task> taskList) {
        if(addEndDate != null){
            taskList.get(taskNumber - 1).setEndDate(addEndDate);
        }
        if(deleteEndDate){
            taskList.get(taskNumber - 1).setEndDate(null);
        }
    }
    
    public void editStartDate(ArrayList<Task> taskList) {
        if(addStartDate != null){
            taskList.get(taskNumber - 1).setStartDate(addStartDate);
        }
        if(deleteStartDate){
            taskList.get(taskNumber - 1).setStartDate(null);
        }
    }*/
}