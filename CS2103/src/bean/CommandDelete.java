/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/6/2016, 1:25am
 * CS2103
 */
package bean;
import java.io.IOException;
import java.util.ArrayList;

import History.History;
import storage.Storage;

public class CommandDelete extends Command{
    private ArrayList<Integer> taskNumbers;
    private Display display;
    private String deletedMessage = "deleted: ";
    private String message_invalid_task_numbers = "You have specified invalid task numbers: ";
    private final String MESSAGE_ERROR = "Error occured while updating to file";
    
    public CommandDelete(){
        this.taskNumbers = null;
        this.display = null;
    }
    
    public CommandDelete(ArrayList<Integer> taskNumbers){
        this.taskNumbers = taskNumbers;
        this.display = null;
    }
    
    public Display execute(Display oldDisplay){
        this.display = oldDisplay;
        if(hasInvalidTaskNumbers(display.getNumberOfTasks())){
            display.setMessage(message_invalid_task_numbers);;
            return display;
        }
        
        deleteTasksFromList();

        if(updateFile()){
            History.saveDisplay(display);
        }
        else{
            display = new Display(MESSAGE_ERROR);
        }
        return display;
    }
    
    public boolean hasInvalidTaskNumbers(int numOfTasks) {
        ArrayList<Integer> invalidTaskNumbers = new ArrayList<Integer>();
        int taskNum;
        for(int i = 0; i < taskNumbers.size(); i++){
            taskNum = taskNumbers.get(i);
            if((taskNum > numOfTasks) || (taskNum < 1)){
                if(invalidTaskNumbers.size() == 0){
                    message_invalid_task_numbers += taskNum;
                }
                else{
                    message_invalid_task_numbers += ", " + taskNum;
                }
                invalidTaskNumbers.add(taskNum);
            }
        }
        return (invalidTaskNumbers.size() > 0);
    }
    
    public void deleteTasksFromList() {
        Task deletedTask;
        for(int i = 0; i < taskNumbers.size(); i++){
            deletedTask = removeTask(taskNumbers.get(i) - 1);
            if(i==0){
                deletedMessage += "\"" + deletedTask.getDescription() + "\"" ;
            }
            else{
                deletedMessage += ", \"" + deletedTask.getDescription() + "\"";
            }
        }
        display.setMessage(deletedMessage);
        return;
    }
    
    public Task removeTask(int taskNum){
        Task deletedTask;
        if(taskNum < display.getDeadlineTasks().size()){
            deletedTask = display.getDeadlineTasks().remove(taskNum - 1);
        }
        else{
            taskNum -= display.getDeadlineTasks().size();
            if(taskNum < display.getEventTasks().size()){
                deletedTask = display.getEventTasks().remove(taskNum - 1);
            }
            else{
                taskNum -= display.getEventTasks().size();
                deletedTask = display.getFloatTasks().remove(taskNum- 1);
            }
        }
        return deletedTask;
    }
    
    public boolean updateFile() {
        try{
            Storage.saveFile(display);
            return true;
        }catch(IOException error){
            return false;
        }
    }
}
