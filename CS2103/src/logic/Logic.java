import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.text.html.parser.Parser;


class Task{

    private String description;
    private Calendar startDate;
    private Calendar endDate;
    private String thatString;

    public Task(){
        description = "Default";
        endDate = null;
        startDate = null;
    }
    
    public Task(String description){
        this.description = description;
        this.endDate = null;
        this.startDate = null;
    }
    
    public Task(String description, Calendar startDate, Calendar endDate){
        this.description = description;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setStartDate(Calendar startDate){
        this.startDate= startDate;
    }
    
    public Calendar getStartDate(){
        return startDate;
    }
    
    public void setEndDate(Calendar endDate){
        this.endDate= endDate;
    }
    
    public Calendar getEndDate(){
        return endDate;
    }
}

class Display{

    private String message;
    private ArrayList<Task> taskList;

    public Display(){
        message = "";
        taskList = null;
    }
    
    public Display(String message, ArrayList<Task> taskList){
        this.message = message;
        this.taskList = taskList;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
    public String getMessage(){
        return message;
    }
    
    public void setList(ArrayList<Task> taskList){
        this.taskList = taskList;
    }
    
    public ArrayList<Task> getList(){
        return taskList;
    }
}

class Command{

    private String type;
    private String description;
    private ArrayList<Integer> taskNum;
    private Calendar addStartDate;
    private Calendar addEndDate;
    
    public Command(){
        type = null;
        description = null;
        taskNum = null;
        addStartDate = null;
        addEndDate = null;
    }
    
    public Command(String type, String description, ArrayList<Integer> taskNum, Calendar addStartDate, Calendar addEndDate){
        this.type = type;
        this.description = description;
        this.taskNum = taskNum;
        this.addStartDate = addStartDate;
        this.addEndDate = addEndDate;
    }
    
    public String getType(){
        return type;
    }
    public String getDescription(){
        return description;
    }
    public ArrayList<Integer> getTaskNum(){
        return taskNum;
    }
    public Calendar getAddStartDate(){
        return addStartDate;
    }
    public Calendar getAddEndDate(){
        return addEndDate;
    }
}
/*class TimedTask extends Task{
    private Calendar startDate;
    private Calendar endDate;
    public TimedTask(){
        this.setDescription("Default");
        endDate = null;
        startDate = null;
    }
    public TimedTask(String description, Calendar startDate, Calendar endDate){
        this.setDescription(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public void setStartDate(Calendar startDate){
        this.startDate= startDate;
    }
    
    public Calendar getStartDate(){
        return startDate;
    }
    
    public void setEndDate(Calendar endDate){
        this.endDate= endDate;
    }
    
    public Calendar getEndDate(){
        return endDate;
    }
}*/

public class Logic {
    
    private static final String MESSAGE_COMMAND_UNRECOGNISED = "Unrecognised Command";
    private static final String MESSAGE_REDO = "Redid last command";
    private static final String MESSAGE_ERROR_REDO = "Already at latest point";
    private static final String MESSAGE_UNDO = "Undid last command";
    private static final String COMMAND_INVALID = "invalid";
    private static final String MESSAGE_NO_DESCRIPTION = "Please specify a description";
    private static final String MESSAGE_ERROR_UNDO = "You have reached the earliest point possible";
    private static final String COMMAND_REDO = "redo";
    private static final String COMMAND_UNDO = "undo";
    private static final String COMMA = ", ";
    private static final String QUOTATION_MARKS = "\"";
    private static final String MESSAGE_INVALID_TASK_NUMBER = "Pls enter a valid task number";
    private static final String MESSAGE_ERROR_UPDATE_FILE = "Error occured while updating to file";
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_EDIT = "edit";
    private static final String COMMAND_SEARCH = "search";
    private static final String COMMAND_EXIT = "exit";
    private static final String MESSAGE_FILE_CREATED = "File created and ready for use";
    private static final String MESSAGE_FILE_EXISTS = "File already exists";
    private static final String MESSAGE_UNRECOGNISED_COMMAND = "Pls enter a valid command";
    private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
    private static final String MESSAGE_DELETED = "deleted from %1$s: ";
    
    private static ArrayList<ArrayList<Task>> oldTasks;
    private static int oldTasksIndex;
    private static ArrayList<Task> tasks;
    private static Task addTask;
    private static ArrayList<String> rawUserInputs;
    private static String textFile;
    private static Command userCommand;
    private static Display display;
    
    public static void main(String args[]) {
        

        System.out.println(myTask.getDescription());
        
    }
    public static String createFile(String filePath) throws IOException {
        try{
            Storage.createFile(filePath);
            return MESSAGE_FILE_CREATED;
        }catch(IOException error){
            return MESSAGE_FILE_EXISTS;
        }
    }
    
    public static Display initialiseProgram() throws IOException {
        tasks = Storage.getList();
        oldTasks.add(tasks);
        oldTasksIndex = 0;
        display.setList(tasks);
        return display;
    }
    
    public static Display handleCommand(String userInput) {
        rawUserInputs.add(userInput);
        userCommand = Parser.parseCommand(userInput);
        return display;
    }
    
    public static void executeUserCommand() {
        switch(userCommand.getType()){
        case COMMAND_ADD:
            addTask();
            
        case COMMAND_DELETE:
            deleteTask();
            break;
            
        case COMMAND_UNDO:
            undoCommand();
            break;
            
        case COMMAND_REDO:
            redoCommand();
            break;
            
        case COMMAND_SEARCH:
            searchCommand();
            break;
            
        case COMMAND_EDIT:
            editTask();
            break;
            
        case COMMAND_INVALID:
            setDisplay(MESSAGE_COMMAND_UNRECOGNISED, null);
            break;
            
            
        default:
            setDisplay(MESSAGE_COMMAND_UNRECOGNISED, null);
        }
    }

    public static void addTask(){
        /*if(userCommand.getDescription()== null){
            return MESSAGE_NO_DESCRIPTION;
        }*/
        
        addTaskToList(userCommand.getAddStartDate(), userCommand.getAddEndDate());
        if(addToFile()){
            setDisplay(MESSAGE_ADDED, tasks);
            saveList();
        }
        else{
             setDisplay(MESSAGE_ERROR_UPDATE_FILE, null);
             tasks = oldTasks.get(oldTasksIndex);
        }
    }
    
    public static void deleteTask(){
        if(hasInvalidTaskNumbers()){
            setDisplay(MESSAGE_INVALID_TASK_NUMBER, null);
            return;
        }
        
        ArrayList<String> deletedTasks = deleteTaskFromList();

        if(updateFile()){
            setDisplay(tasksDeletedMessage(deletedTasks),tasks);
            saveList();
        }
        else{
            setDisplay(MESSAGE_ERROR_UPDATE_FILE, null);
            tasks = oldTasks.get(oldTasksIndex);
        }
    }
    
    public static void undoCommand(){
        if(atFirstState()){
            setDisplay(MESSAGE_ERROR_UNDO, null);
            return;
        }
        
        tasks = oldTasks.get(oldTasksIndex - 1);
        if(updateFile()){
            setDisplay(MESSAGE_UNDO,tasks);
            oldTasksIndex--;
        }
        else{
            setDisplay(MESSAGE_ERROR_UPDATE_FILE, null);
            tasks = oldTasks.get(oldTasksIndex);
        }
    }
    
    public static void redoCommand(){
        if(atLastState()){
            setDisplay(MESSAGE_ERROR_REDO, null);
            return;
        }
        
        tasks = oldTasks.get(oldTasksIndex + 1);
        if(updateFile()){
            setDisplay(MESSAGE_REDO,tasks);
            oldTasksIndex++;
        }
        else{
            setDisplay(MESSAGE_ERROR_UPDATE_FILE,null);
            tasks = oldTasks.get(oldTasksIndex);
        }
    }
    
    public static void searchCommand(){
        ArrayList<Task> tasksContainingKeyword = getTasksContainingKeyword();
        setDisplay(null, tasksContainingKeyword);
    }
    
    public static void editTask(){
        if(hasInvalidTaskNumbers()){
            setDisplay(MESSAGE_INVALID_TASK_NUMBER, null);
            return;
        }
        editDescription();
        editStartDate();
        editEndDate();
        setDisplay(null, tasks);
    }
    
    public static void editDescription() {
        if(userCommand.getDescription() != null){
            tasks.get(userCommand.getTaskNum().get(0) - 1).setDescription(userCommand.getDescription());
        }
    }
    
    public static void editEndDate() {
        if(userCommand.getAddEndDate() != null){
            tasks.get(userCommand.getTaskNum().get(0) - 1).setEndDate(userCommand.getAddEndDate());
        }
        if(userCommand.getDeleteEndDate() != null){
            tasks.get(userCommand.getTaskNum().get(0) - 1).setEndDate(null);
        }
    }
    
    public static void editStartDate() {
        if(userCommand.getAddStartDate() != null){
            tasks.get(userCommand.getTaskNum().get(0) - 1).setStartDate(userCommand.getAddStartDate());
        }
        if(userCommand.getDeleteStartDate() != null){
            tasks.get(userCommand.getTaskNum().get(0) - 1).setStartDate(null);
        }
    }
    
    public static ArrayList<Task> getTasksContainingKeyword() {
        String keyword = userCommand.getDescription();
        ArrayList<Task> tasksContainingKeyword = new ArrayList<Task>();
        for(int i = 0; i < tasks.size(); i++){
            if(tasks.get(i).getDescription().contains(keyword)){
                tasksContainingKeyword.add(tasks.get(i));
            }
        }
        return tasksContainingKeyword;
    }
    
    public static boolean atLastState() {
        return oldTasksIndex == (oldTasks.size() -1);
    }
    
    public static boolean atFirstState() {
        return oldTasksIndex == 0;
    }
    
    public static String tasksDeletedMessage(ArrayList<String> deletedTasks) {
        String displayMessage = MESSAGE_DELETED;
        for(int i = 0; i < deletedTasks.size(); i++){
            displayMessage += QUOTATION_MARKS + deletedTasks.get(i) + QUOTATION_MARKS;
            if(i != deletedTasks.size() - 2){
                displayMessage += COMMA;
            }
        }
        return displayMessage;
    }
    
    public static ArrayList<String> deleteTaskFromList() {
        ArrayList<String> deletedTasks = new ArrayList<String>();
        Task deletedTask;
        for(int i = userCommand.getTaskNum().size(); i>=0; i--){
            deletedTask = tasks.remove(userCommand.getTaskNum().get(i-1) - 1);
            deletedTasks.add(deletedTask.getDescription());
        }
        return deletedTasks;
    }
    
    public static boolean updateFile() {
        try{
            Storage.editFile(tasks);
            return true;
        }catch(IOException error){
            return false;
        }
    }
        
    public static boolean hasInvalidTaskNumbers() {
        ArrayList<Integer> invalidTaskNumbers = new ArrayList<Integer>();
        int tasknum;
        for(int i = 0; i < userCommand.getTaskNum().size(); i++){
            tasknum = userCommand.getTaskNum().get(i);
            if((tasknum > tasks.size()) || (tasknum < 1)){
                invalidTaskNumbers.add(tasknum);
            }
        }
        return (invalidTaskNumbers.size() > 0);
    }

    public static void addTaskToList(Calendar startDate, Calendar endDate) {
        
        if((startDate != null) && (endDate != null)){
            addTimedTask(startDate, endDate);
        }
        else{
            addTask = new Task(userCommand.getDescription());
            tasks.add(addTask);
        }
    }

    public static void addTimedTask(Calendar startDate, Calendar endDate) {
        int i = 0;
        for(i = 0; tasks.get(i).getStartDate()!= null; i++){
            if(startDate.compareTo(tasks.get(i).getStartDate()) <= 0){
                break;
            }
        }
        addTask = new Task(userCommand.getDescription(), startDate, endDate);
        tasks.add(i, addTask);
    }

    public static boolean addToFile() {
        try{
            Storage.addTask(addTask);
            return true;
        }catch(IOException error){
            return false;
        }
    }

    public static void saveList() {
        if(oldTasksIndex < (oldTasksIndex -1)){
            for(int i = (oldTasks.size() - 1); i > oldTasksIndex; i--){
                oldTasks.remove(i);
            }
        }
        oldTasks.add(tasks);
        oldTasksIndex++;
    }

    public static void setDisplay(String message, ArrayList<Task> list) {
        display.setMessage(message);
        display.setList(list);
    }
}
