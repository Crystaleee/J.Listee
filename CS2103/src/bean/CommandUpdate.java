package bean;

import java.io.IOException;
import java.util.Calendar;

import History.History;
import storage.Storage;

public class CommandUpdate extends Command{
    private Integer taskNumber;
    private String description;
    private Calendar addStartDate;
    private Calendar addEndDate;
    private boolean deleteStartDate;
    private boolean deleteEndDate;
    private Display display;
    private final String MESSAGE_ERROR_INVALID_TASK_NUMBER = "Pls specify a valid task number";
    
    public CommandUpdate(){
        this.taskNumber = null;
        this.description = null;
        this.addStartDate = null;
        this.addEndDate = null;
        this.deleteStartDate = false;
        this.deleteEndDate = false;
    }
    public CommandUpdate(Integer taskNumber, String description){
        this.taskNumber = taskNumber;
        this.description = description;
        this.addStartDate = null;
        this.addEndDate = null;
        this.deleteStartDate = false;
        this.deleteEndDate = false;
    }
    public CommandUpdate(Integer taskNumber, String description, Calendar addStartDate, 
            Calendar addEndDate, boolean deleteStartDate, boolean deleteEndDate){
        this.taskNumber = taskNumber;
        this.description = description;
        this.addStartDate = addStartDate;
        this.addEndDate = addEndDate;
        this.deleteStartDate = deleteStartDate;
        this.deleteEndDate = deleteEndDate;
    }
    
    public Display execute(Display oldDisplay){
        display = oldDisplay;
        if(hasInvalidTaskNumber(display.getNumberOfTasks())){
            display.setMessage(MESSAGE_ERROR_INVALID_TASK_NUMBER);
            return display;
        }
        editTask();
        //editStartDate(taskList);
        //editEndDate(taskList);
        //setDisplay(null, taskList);
        if(updateFile()){
            History.saveDisplay(display);
        }
        else{
            display = new Display("IO Error");
        }
        return display;
    }
    
    public boolean hasInvalidTaskNumber(int numOfTasks){
        System.out.println("invalid");
        return ((taskNumber > numOfTasks) || (taskNumber < 1));
    }
    
    public void editTask() {
        if(description != null){
            if(taskNumber < display.getDeadlineTasks().size() ){
                display.getDeadlineTasks().get(taskNumber-1).setDescription(description);
            }
            else{
                taskNumber -= display.getDeadlineTasks().size();
                if(taskNumber < display.getEventTasks().size()){
                    display.getEventTasks().get(taskNumber-1).setDescription(description);
                }
                else{
                    taskNumber -= display.getEventTasks().size();
                    display.getFloatTasks().get(taskNumber-1).setDescription(description);
                }
            }
        }
    }
    public boolean updateFile() {
        try{
            Storage.saveFile(display);
            return true;
        }catch(IOException error){
            return false;
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