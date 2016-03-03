/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/4/2016, 4:30AM
 * CS2103
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

class Command{
    private Display display;
    
    public Command(){
        display = new Display();
    }
    
    public Display getDisplay() {
        return display;
    }
    
    public void setDisplay(String message, ArrayList<Task> list) {
        display.setMessage(message);
        display.setList(list);
    }
}

class InvalidCommand extends Command{
    private final String MESSAGE_INVALID_COMMAND = "Pls enter a valid command";
    
    public InvalidCommand(){
    }
    
    public Display execute(ArrayList<Task> taskList){
        setDisplay(MESSAGE_INVALID_COMMAND, null);
        return getDisplay();
    }
}

class AddFloatCommand extends Command{
    private String description;
    private ArrayList<String> tags;
    private final String MESSAGE_SUCCESS = "added: \"%2$s\"";
    private final String MESSAGE_ERROR = "Error occured while updating to file";
    
    public AddFloatCommand(){
        this.description = null;
        this.tags = null;
    }
    
    public AddFloatCommand(String description, ArrayList<String> tags){
        this.description = description;
        this.tags = tags;
    }
    
    public String getDescription(){
        return description;
    }
    
    public String getSuccessMessage(){
        return MESSAGE_SUCCESS;
    }
    
    public String getErrorMessage(){
        return MESSAGE_ERROR;
    }
    
    public void setDescription(String description){
        this.description = description;
    } 
    
    public void setTags(ArrayList<String> tags){
        this.tags = tags;
    }
    
    public ArrayList<String> getTags(){
        return tags;
    }
    
    public Display execute(ArrayList<Task> taskList){
        taskList.add(new Task(description, tags));
        if(updateFile(taskList)){
            History.saveList(taskList);
            setDisplay(String.format(MESSAGE_SUCCESS, description), taskList);
        }
        else{
            setDisplay(MESSAGE_ERROR, null);
        }
        return getDisplay();
    }
    
    public boolean updateFile(ArrayList<Task> taskList) {
        try{
            Storage.editFile(taskList);
            return true;
        }catch(IOException error){
            return false;
        }
    }
}

class AddTimedCommand extends AddFloatCommand{
    private Calendar startDate;
    private Calendar endDate;
    
    public AddTimedCommand(){
        this.setDescription(null);
        this.startDate = null;
        this.endDate = null;
        this.setTags(null);
    }
    
    public AddTimedCommand(String description, Calendar startDate, Calendar endDate, ArrayList<String> tags){
        this.setDescription(description);
        this.startDate = startDate;
        this.endDate = endDate;
        this.setTags(tags);
    }
    
    public Display execute(ArrayList<Task> taskList){
        /*if(userCommand.getDescription()== null){
        return MESSAGE_NO_DESCRIPTION;
        }*/
        
        taskList = addTaskToList(taskList);
        if(updateFile(taskList)){
            History.saveList(taskList);
            setDisplay(String.format(getSuccessMessage(), getDescription()), taskList);
        }
        else{
            setDisplay(getErrorMessage(), null);
        }
        return getDisplay();
    }
    
    public ArrayList<Task> addTaskToList(ArrayList<Task> taskList) {
        int i = 0;
        for(i = 0; taskList.get(i).getStartDate()!= null; i++){
            if(startDate.compareTo(taskList.get(i).getStartDate()) < 0){
                break;
            }
        }
        taskList.add(i, new Task(getDescription(), startDate, endDate, getTags()));
        return taskList;
    }
}

class DeleteCommand extends Command{
    private ArrayList<Integer> taskNumbers;
    private String invalidTaskNumbersMessage = "You have specified invalid task numbers: ";
    private String deletedMessage = "deleted: ";
    private final String MESSAGE_ERROR = "Error occured while updating to file";
    
    public DeleteCommand(){
        this.taskNumbers = null;
    }
    
    public DeleteCommand(ArrayList<Integer> taskNumbers){
        this.taskNumbers = taskNumbers;
    }
    
    public Display execute(ArrayList<Task> taskList){
        if(hasInvalidTaskNumbers(taskList)){
            setDisplay(invalidTaskNumbersMessage, null);
            return getDisplay();
        }
        
        taskList = deleteTaskFromList(taskList);

        if(updateFile(taskList)){
            History.saveList(taskList);
            setDisplay(deletedMessage, taskList);
        }
        else{
            setDisplay(MESSAGE_ERROR, null);
        }
        return getDisplay();
    }
    
    public boolean hasInvalidTaskNumbers(ArrayList<Task> taskList) {
        ArrayList<Integer> invalidTaskNumbers = new ArrayList<Integer>();
        int taskNum;
        for(int i = 0; i < taskNumbers.size(); i++){
            taskNum = taskNumbers.get(i);
            if((taskNum > taskList.size()) || (taskNum < 1)){
                if(invalidTaskNumbers.size() == 0){
                    invalidTaskNumbersMessage += taskNum;
                }
                else{
                    invalidTaskNumbersMessage += ", " + taskNum;
                }
                invalidTaskNumbers.add(taskNum);
            }
        }
        return (invalidTaskNumbers.size() > 0);
    }
    
    public ArrayList<Task> deleteTaskFromList(ArrayList<Task> taskList) {
        for(int i = 0; i < taskNumbers.size(); i++){
            if(i==0){
                deletedMessage += taskNumbers.get(i);
            }
            else{
                deletedMessage += ", " + taskNumbers.get(i);
            }
            taskList.remove(taskNumbers.get(i) - 1);
        }
        return taskList;
    }
    
    public boolean updateFile(ArrayList<Task> taskList) {
        try{
            Storage.editFile(taskList);
            return true;
        }catch(IOException error){
            return false;
        }
    }
}

class UndoCommand extends Command{
    private final String MESSAGE_UNDO = "Undid last command";
    private final String MESSAGE_ERROR_UNDO = "You have reached the earliest point possible";
    private final String MESSAGE_ERROR = "Error occured while updating to file";
    
    public UndoCommand(){
    }
    
    public Display execute(ArrayList<Task> taskList){
        if(History.atFirstState()){
            setDisplay(MESSAGE_ERROR_UNDO, null);
            return getDisplay();
        }
        
        taskList = History.getTaskList(-1);
        if(updateFile(taskList)){
            setDisplay(MESSAGE_UNDO,taskList);
            History.decrementIndex();
        }
        else{
            setDisplay(MESSAGE_ERROR, null);
        }
        return getDisplay();
    }
    
    public boolean updateFile(ArrayList<Task> taskList) {
        try{
            Storage.editFile(taskList);
            return true;
        }catch(IOException error){
            return false;
        }
    }
}

class RedoCommand extends Command{
    private final String MESSAGE_REDO = "Redid last command";
    private final String MESSAGE_ERROR_REDO = "You have reached the latest point possible";
    private final String MESSAGE_ERROR = "Error occured while updating to file";
    
    public RedoCommand(){
    }
    
    public Display execute(ArrayList<Task> taskList){
        if(History.atLastState()){
            setDisplay(MESSAGE_ERROR_REDO, null);
            return getDisplay();
        }
        
        taskList = History.getTaskList(1);
        if(updateFile(taskList)){
            setDisplay(MESSAGE_REDO, taskList);
            History.incrementIndex();
        }
        else{
            setDisplay(MESSAGE_ERROR,null);
        }
        return getDisplay();
    }
    
    public boolean updateFile(ArrayList<Task> taskList) {
        try{
            Storage.editFile(taskList);
            return true;
        }catch(IOException error){
            return false;
        }
    }
}

class ShowCommand extends Command{
    private final String MESSAGE_NO_TASKS_FOUND = "No such tasks found";
    private String keyword;
    
    public ShowCommand(){
        this.keyword = null;
    }
    
    public ShowCommand(String keyword){
        this.keyword = keyword;
    }
    
    public Display execute(ArrayList<Task> taskList){
        if(keyword == null){
            setDisplay(null, History.getTaskList(0));
            return getDisplay();
        }
        
        ArrayList<Task> searchedTasks = null;
        searchedTasks = getTasksContainingKeyword(taskList);
        if(searchedTasks.isEmpty()){
            setDisplay(MESSAGE_NO_TASKS_FOUND, null);
        }
        else{
            setDisplay(null, searchedTasks);
        }
        
        return getDisplay();
    }
    
    public ArrayList<Task> getTasksContainingKeyword(ArrayList<Task> taskList) {
        ArrayList<Task> tasksContainingKeyword = new ArrayList<Task>();
        for(int i = 0; i < taskList.size(); i++){
            if(taskList.get(i).getDescription().contains(keyword)){
                tasksContainingKeyword.add(taskList.get(i));
            }
        }
        return tasksContainingKeyword;
    }
}
class EditCommand extends Command{
    private Integer taskNumber;
    private String description;
    private Calendar addStartDate;
    private Calendar addEndDate;
    private boolean deleteStartDate;
    private boolean deleteEndDate;
    private final String MESSAGE_ERROR_INVALID_TASK_NUMBER = "Pls specify a valid task number";
    
    public EditCommand(){
        this.taskNumber = null;
        this.description = null;
        this.addStartDate = null;
        this.addEndDate = null;
        this.deleteStartDate = false;
        this.deleteEndDate = false;
    }
    
    public EditCommand(Integer taskNumber, String description, Calendar addStartDate, 
            Calendar addEndDate, boolean deleteStartDate, boolean deleteEndDate){
        this.taskNumber = taskNumber;
        this.description = description;
        this.addStartDate = addStartDate;
        this.addEndDate = addEndDate;
        this.deleteStartDate = deleteStartDate;
        this.deleteEndDate = deleteEndDate;
    }
    
    public Display execute(ArrayList<Task> taskList){
        if(hasInvalidTaskNumber(taskList)){
            setDisplay(MESSAGE_ERROR_INVALID_TASK_NUMBER, null);
            return getDisplay();
        }
        editDescription(taskList);
        editStartDate(taskList);
        editEndDate(taskList);
        setDisplay(null, taskList);
        
        return getDisplay();
    }
    
    public boolean hasInvalidTaskNumber(ArrayList<Task> taskList){
        return ((taskNumber > taskList.size()) || (taskNumber < 1));
    }
    
    public void editDescription(ArrayList<Task> taskList) {
        if(description != null){
            taskList.get(taskNumber - 1).setDescription(description);
        }
    }
    
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
    }
}
