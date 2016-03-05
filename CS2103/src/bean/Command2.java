/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 8:00pm
 * CS2103
 */
package bean;
public abstract class Command{
    
    
    public Display execute(Display display){
        return display;
    }
}

/*
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
}*/
