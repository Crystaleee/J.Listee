/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 2:40am
 * CS2103
 */
package bean;

public interface Command {

    public Display execute(Display display);

    public boolean getUpdateFile();
}
/*
 * class ShowCommand extends Command{ private final String
 * MESSAGE_NO_TASKS_FOUND = "No such tasks found"; private String keyword;
 * 
 * public ShowCommand(){ this.keyword = null; }
 * 
 * public ShowCommand(String keyword){ this.keyword = keyword; }
 * 
 * public Display execute(ArrayList<Task> taskList){ if(keyword == null){
 * setDisplay(null, History.getTaskList(0)); return getDisplay(); }
 * 
 * ArrayList<Task> searchedTasks = null; searchedTasks =
 * getTasksContainingKeyword(taskList); if(searchedTasks.isEmpty()){
 * setDisplay(MESSAGE_NO_TASKS_FOUND, null); } else{ setDisplay(null,
 * searchedTasks); }
 * 
 * return getDisplay(); }
 * 
 * public ArrayList<Task> getTasksContainingKeyword(ArrayList<Task> taskList) {
 * ArrayList<Task> tasksContainingKeyword = new ArrayList<Task>(); for(int i =
 * 0; i < taskList.size(); i++){
 * if(taskList.get(i).getDescription().contains(keyword)){
 * tasksContainingKeyword.add(taskList.get(i)); } } return
 * tasksContainingKeyword; } }
 */
