/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 11:16pm
 * CS2103
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

import History.History;

public class CommandAddReserved extends CommandAddFloatTask{
    private ArrayList<Calendar> startDates;
    private ArrayList<Calendar> endDates;
        
        public CommandAddReserved(){
            this.setDescription(null);
            this.setLocation(null);
            startDates = null;
            endDates = null;
            this.setTags(null);
        }
        
        public CommandAddReserved(String description, String location, ArrayList<Calendar> startDates, 
                ArrayList<Calendar> endDates, ArrayList<String> tags){
            this.setDescription(description);
            this.setLocation(location);
            this.startDates = startDates;
            this.endDates = endDates;
            this.setTags(tags);
        }
        
        public void setEndDates(ArrayList<Calendar> endDates){
            this.endDates = endDates;
        }
        
        public ArrayList<Calendar> GetEndDates(){
            return endDates;
        }
        
        public void setStartDates(ArrayList<Calendar> startDates){
            this.startDates = startDates;
        }
        
        public ArrayList<Calendar> GetStartDates(){
            return startDates;
        }
        
        public Display execute(Display display){
            if(getDescription() == null){
                display = new Display(getMessageNoDescription());
                return display;
            }
            ArrayList<TaskReserved> reservedTasks = addReservedTask(display.getReservedTasks());
            display.setReservedTasks(reservedTasks);
            if(hasUpdateFile(display)){
                display.setMessage(getSuccessMessage());
                History.saveDisplay(display);
            }
            else{
                display = new Display(getMessageErrorUpdateFile());
            }
            return display;
        }
        
        public ArrayList<TaskReserved> addReservedTask(ArrayList<TaskReserved> taskList) {
            int index = getIndex(taskList);
            taskList.add(index, new TaskReserved(getDescription(), getLocation(), startDates, endDates, getTags()));
            return taskList;
        }
        
        /*
         * This method searches for the index to slot the deadline task in since we
         * are sorting the list in order of earliest start date of the first
         * time slot.
         */
        public int getIndex(ArrayList<TaskReserved> taskList) {
            int i = 0;
            Calendar addedTaskStartDate = startDates.get(0);
            for(i = 0; i < taskList.size(); i++){
                Calendar taskInListStartDate = taskList.get(i).getStartDates().get(0);
                if(addedTaskStartDate.compareTo(taskInListStartDate) < 0){
                    break;
                }
            }
            return i;
        }
    
}
