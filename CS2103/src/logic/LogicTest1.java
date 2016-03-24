package logic;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import bean.CommandAddDeadlineTask;
import bean.CommandAddEvent;
import bean.CommandAddFloatTask;
import bean.CommandDelete;
import bean.CommandInvalid;
import bean.CommandRedo;
import bean.CommandUndo;

public class LogicTest1 {
    /*Commands Tested:
     * add Float with location and tag
     * add Event according to time with location and/or single/multiple tags
     * add Deadline according to time
     * delete all
     * delete 1 task
     * delete multiple tasks
     * undo at state> earliest state
     * undo but alr at 1st state
     * redo at state < last state
     * redo but at last state
     * 
     */
    @Test
    public void test() {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        String args[] = null;
        main.App.main(args);
        assertEquals("Display [message=You have reached the earliest point possible, events=[], deadlineTasks=[], "
                + "floatTasks=[], reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandUndo()).toString());
        assertEquals("Display [message=All tasks deleted, events=[], deadlineTasks=[], "
                + "floatTasks=[], reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandDelete(null)).toString());

        ArrayList<String> tags = new ArrayList<String>();
        tags.add("movie");
        assertEquals("Display [message=added: \"Float1\", events=[], deadlineTasks=[], "
                + "floatTasks=[Description: Float1\r\nLocation: jcube\r\nTags: #movie\r\n\r\n], reservedTasks=[], "
                + "completedTasks=[]]", Logic.executeCommand(new CommandAddFloatTask("Float1", "jcube", tags)).toString());
        
        tags = new ArrayList<String>();
        tags.add("movie");
        end.set(2016,2-1,19,19,00);
        assertEquals("Display [message=added: \"Deadline1\", events=[], "
                + "deadlineTasks=["
                + "Description: Deadline1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                + "reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddDeadlineTask("Deadline1", "jcube", end, tags)).toString());
        
        tags = new ArrayList<String>();
        tags.add("movie");
        start.set(2016,2-1,19,19,00);
        end.set(2016,2-1,19,21,00);
        assertEquals("Display [message=added: \"Event1\", "
                + "events=["
                + "Description: Event1\r\n"
                + "Start Date: 19/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n],"
                /**************************************************************/
                
                + " deadlineTasks=["
                + "Description: Deadline1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                + "reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandAddEvent("Event1", "jcube", start, end, tags)).toString());
        
        

        tags = new ArrayList<String>();
        start.set(2016,2-1,18,19,00);
        end.set(2016,2-1,19,21,00);
        assertEquals("Display [message=added: \"Event2\", "
                + "events=["
                + "Description: Event2\r\n"
                + "Start Date: 18/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "deadlineTasks=["
                + "Description: Deadline1\r\nDeadline: "
                + "19/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                + "reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandAddEvent("Event2", null, start, end, tags)).toString());
        assertEquals("Display [message=Pls enter a valid command, "
                + "events=["
                + "Description: Event2\r\n"
                + "Start Date: 18/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "deadlineTasks=["
                + "Description: Deadline1\r\nDeadline: "
                + "19/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                + "reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandInvalid()).toString());
        
        ArrayList<Integer> tasks = new ArrayList<Integer>();
        tasks.add(0);
        tasks.add(3);
        tasks.add(9);
        assertEquals("Display [message=You have specified invalid task numbers: 0, 9, "
                + "events=["
                + "Description: Event2\r\n"
                + "Start Date: 18/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "deadlineTasks=["
                + "Description: Deadline1\r\nDeadline: "
                + "19/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                + "reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandDelete(tasks)).toString());

        tags = new ArrayList<String>();
        tags.add("movie");
        start.set(2016,2-1,21,19,00);
        end.set(2016,2-1,21,21,00);
        assertEquals("Display [message=added: \"Event3\", "
                + "events=["
                + "Description: Event2\r\n"
                + "Start Date: 18/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: Event1\r\n"
                + "Start Date: 19/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n, "
                
                + "Description: Event3\r\n"
                + "Start Date: 21/02/16 19:00\r\n"
                + "End Date: 21/02/16 21:00\r\n"
                + "Location: \r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "deadlineTasks=["
                + "Description: Deadline1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                + "reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandAddEvent("Event3", null, start, end, tags)).toString());
        

        tasks = new ArrayList<Integer>();
        tasks.add(3);
        tasks.add(4);
        assertEquals("Display [message=deleted: \"Event1\", \"Event3\", "
                + "events=["
                + "Description: Event2\r\n"
                + "Start Date: 18/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                /**************************************************************/
                
                + "deadlineTasks=["
                + "Description: Deadline1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                + "reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandDelete(tasks)).toString());
        assertEquals("Display [message=Undid last command, "
                + "events=["
                + "Description: Event2\r\n"
                + "Start Date: 18/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: Event1\r\n"
                + "Start Date: 19/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n, "
                
                + "Description: Event3\r\n"
                + "Start Date: 21/02/16 19:00\r\n"
                + "End Date: 21/02/16 21:00\r\n"
                + "Location: \r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "deadlineTasks=["
                + "Description: Deadline1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                + "reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandUndo()).toString());
        assertEquals("Display [message=Redid last command, "
                + "events=["
                + "Description: Event2\r\n"
                + "Start Date: 18/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                /**************************************************************/
                
                + "deadlineTasks=["
                + "Description: Deadline1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                + "reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandRedo()).toString());
        assertEquals("Display [message=You have reached the latest point possible, "
                + "events=["
                + "Description: Event2\r\n"
                + "Start Date: 18/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                /**************************************************************/
                
                + "deadlineTasks=["
                + "Description: Deadline1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                + "reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandRedo()).toString());
        
        
        end.set(2016,2-1,18,19,00);
        assertEquals("Display [message=added: \"Deadline2\", "
                + "events=["
                + "Description: Event2\r\n"
                + "Start Date: 18/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                /**************************************************************/
                
                + "deadlineTasks=["
                + "Description: Deadline2\r\n"
                + "Deadline: 18/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n, "
                
                + "Description: Deadline1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                /**************************************************************/
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                + "reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandAddDeadlineTask("Deadline2", "jcube", end, tags)).toString());
        tags = new ArrayList<String>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");
        tags.add("tag4");
        end.set(2016,2-1,22,19,00);
        assertEquals("Display [message=added: \"Deadline3\", "
                + "events=["
                + "Description: Event2\r\n"
                + "Start Date: 18/02/16 19:00\r\n"
                + "End Date: 19/02/16 21:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                /**************************************************************/
                
                + "deadlineTasks=["
                + "Description: Deadline2\r\n"
                + "Deadline: 18/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n, "
                
                + "Description: Deadline1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n, "
                
                + "Description: Deadline3\r\n"
                + "Deadline: 22/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4\r\n\r\n], "
                /**************************************************************/
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: jcube\r\n"
                + "Tags: #movie\r\n\r\n], "
                + "reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandAddDeadlineTask("Deadline3", new String(), end, tags)).toString());
    }

}
