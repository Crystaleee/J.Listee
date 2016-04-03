/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import bean.CommandAddDeadlineTask;
import bean.CommandAddEvent;
import bean.CommandAddFloatTask;
import bean.CommandAddReserved;
import bean.CommandDelete;
import bean.CommandDone;
import bean.CommandInvalid;
import bean.CommandRedo;
import bean.CommandShow;
import bean.CommandUndo;
import bean.CommandUndone;
import bean.CommandUpdate;
import logic.Logic;

public class LogicTest2 {
    @Before
    public void startApp() {
        String args[] = null;
        main.App.main(args);
    }
    @Test
    public void test() {
        //undo when no commands entered yet
        //assertEquals("Display [message=You have reached the earliest point possible, events=[], deadlineTasks=[], "
        //        + "floatTasks=[], reservedTasks=[], completedTasks=[]]", 
         //       Logic.executeCommand(new CommandUndo()).toString());
        
        //clear display
        assertEquals("Display [message=All tasks deleted, events=[], deadlineTasks=[], "
                + "floatTasks=[], reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandDelete(null)).toString());
        Logic.executeCommand(new CommandShow("done"));
        Logic.executeCommand(new CommandDelete(null));
        Logic.executeCommand(new CommandShow(null, null, null, null, null));
        
        ArrayList<String> tags = new ArrayList<String>();

        //add float task without location and tags
        assertEquals("Display [message=added: \"Float1\", events=[], deadlineTasks=[], "
                + "floatTasks=[Description: Float1\r\nLocation: \r\nTags:\r\n\r\n], reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddFloatTask("Float1", "", tags)).toString());
        
        //undo after entering command
        assertEquals("Display [message=Undid previous commands, events=[], deadlineTasks=[], "
                + "floatTasks=[], reservedTasks=[], completedTasks=[]]", 
                Logic.executeCommand(new CommandUndo()).toString());

        //redo after undo
        assertEquals("Display [message=Redid last command, events=[], deadlineTasks=[], "
                + "floatTasks=[Description: Float1\r\nLocation: \r\nTags:\r\n\r\n], reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandRedo()).toString());

        //redo at latest state
        assertEquals("Display [message=You have reached the latest point possible, events=[], deadlineTasks=[], "
                + "floatTasks=[Description: Float1\r\nLocation: \r\nTags:\r\n\r\n], reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandRedo()).toString());
        
        //add float task without description
        assertEquals("Display [message=Please enter a description, events=[], deadlineTasks=[], "
                + "floatTasks=[Description: Float1\r\nLocation: \r\nTags:\r\n\r\n], reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddFloatTask("", "", tags)).toString());
        
        //add float task with location
        assertEquals("Display [message=added: \"Float2\", events=[], deadlineTasks=[], "
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float2\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddFloatTask("Float2", "jcube", tags)).toString());

        tags = new ArrayList<String>();
        //add float task with tag
        tags.add("tag1");
        assertEquals("Display [message=added: \"Float3\", events=[], deadlineTasks=[], "
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float2\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddFloatTask("Float3", "", tags)).toString());

        //add float task with multiple tags
        tags = new ArrayList<String>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");
        tags.add("tag4");
        tags.add("tag5");
        assertEquals("Display [message=added: \"Float4\", events=[], deadlineTasks=[], "
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float2\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddFloatTask("Float4", "", tags)).toString());

        tags = new ArrayList<String>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");
        tags.add("tag4");
        tags.add("tag5");
        //add float task with location and multiple tags
        assertEquals("Display [message=added: \"Float5\", events=[], deadlineTasks=[], "
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float2\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddFloatTask("Float5", "location", tags)).toString());
        
        //add deadline task without location and tag
        tags = new ArrayList<String>();
        Calendar end = Calendar.getInstance();
        end.set(2016,2-1,19,19,00);
        assertEquals("Display [message=added: \"DL1\", events=[], "
                + "deadlineTasks=["
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float2\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddDeadlineTask("DL1", "", end, tags)).toString());
        
        //add deadline task with location and has due date before previously added deadline task
        end = Calendar.getInstance();
        end.set(2016,2-1,19,14,00);
        assertEquals("Display [message=added: \"DL2\", events=[], "
                + "deadlineTasks=["
                + "Description: DL2\r\n"
                + "Deadline: 19/02/16 14:00\r\n"
                + "Location: JEM\r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float2\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddDeadlineTask("DL2", "JEM", end, tags)).toString());
        
        //add deadline task with tag with due dates between 2 previous added deadline tasks
        end = Calendar.getInstance();
        tags = new ArrayList<String>();
        end.set(2016,2-1,19,15,00);
        tags.add("tag1");
        assertEquals("Display [message=added: \"DL3\", events=[], "
                + "deadlineTasks=["
                + "Description: DL2\r\n"
                + "Deadline: 19/02/16 14:00\r\n"
                + "Location: JEM\r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float2\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddDeadlineTask("DL3", "", end, tags)).toString());
        
        //add deadline task with multiple tags and location
        end = Calendar.getInstance();
        tags = new ArrayList<String>();
        end.set(2016,2-1,19,15,00);
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");
        assertEquals("Display [message=added: \"DL4\", events=[], "
                + "deadlineTasks=["
                + "Description: DL2\r\n"
                + "Deadline: 19/02/16 14:00\r\n"
                + "Location: JEM\r\n"
                + "Tags:\r\n\r\n, "

                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float2\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddDeadlineTask("DL4", "JEM", end, tags)).toString());
        
        //add event without tag and location
        Calendar start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.set(2016,2-1,19,12,00);
        end.set(2016,2-1,19,15,00);
        tags = new ArrayList<String>();
        
        assertEquals("Display [message=added: \"Event1\", "
                + "events=["
                + "Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL2\r\n"
                + "Deadline: 19/02/16 14:00\r\n"
                + "Location: JEM\r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "

                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float2\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddEvent("Event1", "", start, end, tags)).toString());
        
        //add event with location before previously added event
        end = Calendar.getInstance();
        start = Calendar.getInstance();
        start.set(2016,2-1,19,11,00);
        end.set(2016,2-1,19,16,00);
        tags = new ArrayList<String>();
        
        assertEquals("Display [message=added: \"Event2\", "
                + "events=["

                + "Description: Event2\r\n"
                + "Start Date: 19/02/16 11:00\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL2\r\n"
                + "Deadline: 19/02/16 14:00\r\n"
                + "Location: JEM\r\n"
                + "Tags:\r\n\r\n, "

                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float2\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddEvent("Event2", "jcube", start, end, tags)).toString());
        
        //add event with tags before between 2 previously added events
        tags = new ArrayList<String>();
        end = Calendar.getInstance();
        start = Calendar.getInstance();
        start.set(2016,2-1,19,11,30);
        end.set(2016,2-1,19,16,00);
        tags.add("tag1");
        
        assertEquals("Display [message=added: \"Event3\", "
                + "events=["

                + "Description: Event2\r\n"
                + "Start Date: 19/02/16 11:00\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n"
                
                + ", Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL2\r\n"
                + "Deadline: 19/02/16 14:00\r\n"
                + "Location: JEM\r\n"
                + "Tags:\r\n\r\n, "

                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float2\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n"

                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddEvent("Event3", "", start, end, tags)).toString());
        
        //delete a task
        ArrayList<Integer> delNum = new ArrayList<Integer>();
        delNum.add(9);
        
        assertEquals("Display [message=deleted: \"Float2\", "
                + "events=["

                + "Description: Event2\r\n"
                + "Start Date: 19/02/16 11:00\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: jcube\r\n"
                + "Tags:\r\n\r\n"
                
                + ", Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL2\r\n"
                + "Deadline: 19/02/16 14:00\r\n"
                + "Location: JEM\r\n"
                + "Tags:\r\n\r\n, "

                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandDelete(delNum)).toString());
        
        //delete multiple tasks
        delNum = new ArrayList<Integer>();
        delNum.add(1);
        delNum.add(5);
        
        assertEquals("Display [message=deleted: \"DL2\", \"Event2\", "
                + "events=["
                
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandDelete(delNum)).toString());
        
        //delete invalid task
        delNum = new ArrayList<Integer>();
        delNum.add(0);
        
        assertEquals("Display [message=You have specified invalid task numbers: 0, "
                + "events=["
                
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandDelete(delNum)).toString());
        
        //delete multiple tasks with valid and invalid task numbers
        delNum = new ArrayList<Integer>();
        delNum.add(0);
        delNum.add(-1);
        delNum.add(3);
        
        assertEquals("Display [message=You have specified invalid task numbers: 0, -1, "
                + "events=["
                
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandDelete(delNum)).toString());
        
        //user enters invalid command
        assertEquals("Display [message=You have specified an invalid command, "
                + "events=["
                
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandInvalid()).toString());
        
        //user reserves time slot
        tags = new ArrayList<String>();
        ArrayList<Calendar> startDates = new ArrayList<Calendar>();
        ArrayList<Calendar> endDates = new ArrayList<Calendar>();
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.set(2016,2-1,19,11,30);
        end.set(2016,2-1,19,16,00);
        startDates.add(start);
        endDates.add(end);
        assertEquals("Display [message=Reserved: r1, "
                + "events=["
                
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddReserved("r1", "", startDates, endDates, tags)).toString());
        
        //user reserves multiple time slots with tags and location
        tags = new ArrayList<String>();
        startDates = new ArrayList<Calendar>();
        endDates = new ArrayList<Calendar>();
        Calendar start1 = Calendar.getInstance();
        Calendar end1 = Calendar.getInstance();
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.set(2016,2-1,19,12,00);
        end.set(2016,2-1,19,15,00);
        start1.set(2016,2-1,19,11,30);
        end1.set(2016,2-1,19,16,00);
        startDates.add(start);
        endDates.add(end);
        startDates.add(start1);
        endDates.add(end1);
        tags.add("tag1");
        tags.add("tag2");
        assertEquals("Display [message=Reserved: r2, "
                + "events=["
                
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandAddReserved("r2", "location", startDates, endDates, tags)).toString());
        
        //show tasks containing 'l'
        assertEquals("Display [message=Displaying tasks containing l, "
                + "events=[]"
                
                + ", deadlineTasks=["
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandShow("l")).toString());//show tasks containing 'l'
        
        //show tasks at specified location
        tags = new ArrayList<String>();
        assertEquals("Display [message=Displaying tasks at location, "
                + "events=[]"
                
                + ", deadlineTasks=[], "
                
                + "floatTasks=["
                + "Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandShow("", "location", null, null, tags)).toString());
        
        //show a time range
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.set(2016,2-1,19,12,00);
        end.set(2016,2-1,19,15,00);
        
        tags = new ArrayList<String>();
        assertEquals("Display [message=Displaying tasks from 19/02/16 12:00 to 19/02/16 15:00, "
                + "events=["
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandShow("", "", start, end, tags)).toString());
        
        //show a time range where time is equal to task start/end time
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.set(2016,2-1,19,11,30);
        end.set(2016,2-1,19,12,00);
        
        tags = new ArrayList<String>();
        assertEquals("Display [message=Displaying tasks from 19/02/16 11:30 to 19/02/16 12:00, "
                + "events=["
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=[], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandShow("", "", start, end, tags)).toString());
        
        //show a time range 
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.set(2016,2-1,19,11,30);
        end.set(2016,2-1,19,11,59);
        
        tags = new ArrayList<String>();
        assertEquals("Display [message=Displaying tasks from 19/02/16 11:30 to 19/02/16 11:59, "
                + "events=["
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n]"
                
                + ", deadlineTasks=[], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandShow("", "", start, end, tags)).toString());
        
        //show tag
        tags = new ArrayList<String>();
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        tags.add("tag1");
        assertEquals("Display [message=Displaying tasks tagged tag1, "
                + "events=["
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandShow("", "", null, null, tags)).toString());
        
        //show tag
        tags = new ArrayList<String>();
        tags.add("tag1");
        tags.add("tag2");
        assertEquals("Display [message=Displaying tasks tagged tag1, tag2, "
                + "events=[]"
                
                + ", deadlineTasks=["
                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandShow("", "", null, null, tags)).toString());
        
      //show tasks containing keyword, tags and is within time range
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.set(2016,2-1,19,11,30);
        end.set(2016,2-1,19,12,00);
        assertEquals("Display [message=Displaying tasks containing l at location from 19/02/16 11:30 to 19/02/16 12:00 tagged tag1, tag2, "
                + "events=[]"
                
                + ", deadlineTasks=[], "
                
                + "floatTasks=["
                + "Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=[], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandShow("l", "location", start, end, tags)).toString());
        
        //show all
        tags = new ArrayList<String>();
        assertEquals("Display [message=Displaying all tasks, "
                + "events=["
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Float4\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandShow("", "", null, null, tags)).toString());
        
        //update Float task description, add location, remove tags and add tags
        ArrayList<String> removedTags = new ArrayList<String>();
        removedTags.add("tag3");
        removedTags.add("tag4");
        tags = new ArrayList<String>();
        tags.add("tag10");
        assertEquals("Display [message=Edited : \"Float4\", "
                + "events=["
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Floatter\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag5 #tag10\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandUpdate(8, "Floatter", "location", null, null, tags, removedTags)).toString());
        
        //update Float task remove location, add due date
        removedTags = new ArrayList<String>();
        tags = new ArrayList<String>();
        end = Calendar.getInstance();
        end.set(2016,2-1,19,12,00);
        assertEquals("Display [message=Edited : \"Floatter\", "
                + "events=["
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: Floatter\r\n"
                + "Deadline: 19/02/16 12:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag5 #tag10\r\n\r\n, "
                
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"


                + ", Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandUpdate(8, "", "", null, end, tags, removedTags)).toString());
        
        //update Float task add start and end time
        tags = new ArrayList<String>();
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.set(2016,2-1,19,11,45);
        end.set(2016,2-1,19,13,00);
        assertEquals("Display [message=Edited : \"Float1\", "
                + "events=["
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: Float1\r\n"
                + "Start Date: 19/02/16 11:45\r\n"
                + "End Date: 19/02/16 13:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: Floatter\r\n"
                + "Deadline: 19/02/16 12:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1 #tag2 #tag5 #tag10\r\n\r\n, "
                
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandUpdate(7, null, null, start, end, tags, removedTags)).toString());
        
        //update Deadline task description, add location, remove tags and add tags
        removedTags = new ArrayList<String>();
        removedTags.add("tag1");
        tags = new ArrayList<String>();
        tags.add("tag11");
        
        assertEquals("Display [message=Edited : \"Floatter\", "
                + "events=["
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: Float1\r\n"
                + "Start Date: 19/02/16 11:45\r\n"
                + "End Date: 19/02/16 13:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DeadlineTask\r\n"
                + "Deadline: 19/02/16 12:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag2 #tag5 #tag10 #tag11\r\n\r\n, "
                
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandUpdate(1, "DeadlineTask", "location", null, null, tags, removedTags)).toString());
        
        //update Deadline due date and remove invalid task
        removedTags = new ArrayList<String>();
        removedTags.add("tag1");
        end = Calendar.getInstance();
        end.set(2016,2-1,19,13,00);
        
        assertEquals("Display [message=Edited : \"DeadlineTask\", "
                + "events=["
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: Float1\r\n"
                + "Start Date: 19/02/16 11:45\r\n"
                + "End Date: 19/02/16 13:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DeadlineTask\r\n"
                + "Deadline: 19/02/16 13:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag2 #tag5 #tag10 #tag11\r\n\r\n, "
                
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandUpdate(1, null, null, null, end, null, removedTags)).toString());
        
        //update Deadline- remove due date
        end = Calendar.getInstance();
        end.setTimeInMillis(0);
        assertEquals("Display [message=Edited : \"DeadlineTask\", "
                + "events=["
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: Float1\r\n"
                + "Start Date: 19/02/16 11:45\r\n"
                + "End Date: 19/02/16 13:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n"
                
                + ", Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                
                + "Description: DL3\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "

                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"
                
                + ", Description: DeadlineTask\r\n"
                + "Location: location\r\n"
                + "Tags: #tag2 #tag5 #tag10 #tag11\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandUpdate(1, null, null, null, end, null, null)).toString());
      
        //update Deadline- add start date and change end date
        tags = new ArrayList<String>();
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.set(2016,2-1,19,11,45);
        end.set(2016,2-1,19,14,00);
        
        assertEquals("Display [message=Edited : \"DL3\", "
                + "events=["
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 11:30\r\n"
                + "End Date: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: Float1\r\n"
                + "Start Date: 19/02/16 11:45\r\n"
                + "End Date: 19/02/16 13:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "

                + "Description: DL3\r\n"
                + "Start Date: 19/02/16 11:45\r\n"
                + "End Date: 19/02/16 14:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"
                
                + ", Description: DeadlineTask\r\n"
                + "Location: location\r\n"
                + "Tags: #tag2 #tag5 #tag10 #tag11\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandUpdate(1, null, null, start, end, null, null)).toString());
      
        //update event start and end dates
        tags = new ArrayList<String>();
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.set(2016,2-1,19,13,00);
        end.set(2016,2-1,19,18,00);
        
        assertEquals("Display [message=Edited : \"Event3\", "
                + "events=["
                + "Description: Float1\r\n"
                + "Start Date: 19/02/16 11:45\r\n"
                + "End Date: 19/02/16 13:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "

                + "Description: DL3\r\n"
                + "Start Date: 19/02/16 11:45\r\n"
                + "End Date: 19/02/16 14:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: Event3\r\n"
                + "Start Date: 19/02/16 13:00\r\n"
                + "End Date: 19/02/16 18:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n]"
                
                + ", deadlineTasks=["
                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"
                
                + ", Description: DeadlineTask\r\n"
                + "Location: location\r\n"
                + "Tags: #tag2 #tag5 #tag10 #tag11\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandUpdate(3, null, null, start, end, null, null)).toString());
      
        //update event - remove start date
        tags = new ArrayList<String>();
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.setTimeInMillis(0);
        assertEquals("Display [message=Edited : \"Event3\", "
                + "events=["
                + "Description: Float1\r\n"
                + "Start Date: 19/02/16 11:45\r\n"
                + "End Date: 19/02/16 13:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "

                + "Description: DL3\r\n"
                + "Start Date: 19/02/16 11:45\r\n"
                + "End Date: 19/02/16 14:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "deadlineTasks=["
                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: Event3\r\n"
                + "Deadline: 19/02/16 18:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"
                
                + ", Description: DeadlineTask\r\n"
                + "Location: location\r\n"
                + "Tags: #tag2 #tag5 #tag10 #tag11\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandUpdate(6, null, null, start, null, null, null)).toString());
      
        //update event - remove start and end date
        tags = new ArrayList<String>();
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.setTimeInMillis(0);
        end.setTimeInMillis(0);
        assertEquals("Display [message=Edited : \"Float1\", "
                + "events=["
                + "Description: DL3\r\n"
                + "Start Date: 19/02/16 11:45\r\n"
                + "End Date: 19/02/16 14:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "deadlineTasks=["
                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "
                
                + "Description: Event3\r\n"
                + "Deadline: 19/02/16 18:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"
                
                + ", Description: DeadlineTask\r\n"
                + "Location: location\r\n"
                + "Tags: #tag2 #tag5 #tag10 #tag11\r\n\r\n"
                
                + ", Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandUpdate(4, null, null, start, end, null, null)).toString());
      
        //mark a task as done
        
        delNum = new ArrayList<Integer>();
        delNum.add(1);
        assertEquals("Display [message=Completed: \"DL4\", "
                + "events=["
                + "Description: DL3\r\n"
                + "Start Date: 19/02/16 11:45\r\n"
                + "End Date: 19/02/16 14:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "deadlineTasks=["
                
                + "Description: Event3\r\n"
                + "Deadline: 19/02/16 18:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=["
                + "Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"
                
                + ", Description: DeadlineTask\r\n"
                + "Location: location\r\n"
                + "Tags: #tag2 #tag5 #tag10 #tag11\r\n\r\n"
                
                + ", Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandDone(delNum)).toString());
      
        //mark multiple tasks as done
        delNum = new ArrayList<Integer>();
        delNum.add(1);
        delNum.add(2);
        assertEquals("Display [message=Completed: \"Event3\", \"DL1\", "
                + "events=["
                + "Description: DL3\r\n"
                + "Start Date: 19/02/16 11:45\r\n"
                + "End Date: 19/02/16 14:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: Event1\r\n"
                + "Start Date: 19/02/16 12:00\r\n"
                + "End Date: 19/02/16 15:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "deadlineTasks=[], "
                
                + "floatTasks=["
                + "Description: Float3\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n"

                + ", Description: Float5\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2 #tag3 #tag4 #tag5\r\n\r\n"
                
                + ", Description: DeadlineTask\r\n"
                + "Location: location\r\n"
                + "Tags: #tag2 #tag5 #tag10 #tag11\r\n\r\n"
                
                + ", Description: Float1\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                
                + ", reservedTasks=["
                + "Description: r1\r\n"
                + "Start Dates: 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 16:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "
                
                + "Description: r2\r\n"
                + "Start Dates: 19/02/16 12:00, 19/02/16 11:30\r\n"
                + "End Dates: 19/02/16 15:00, 19/02/16 16:00\r\n"
                + "Location: location\r\n"
                + "Tags: #tag1 #tag2\r\n\r\n], "
                
                + "completedTasks=[]]", 
                Logic.executeCommand(new CommandDone(delNum)).toString());
      
        //show done
        assertEquals("Display [message=Showing done tasks, "
                + "events=[], "
                
                + "deadlineTasks=[], "
                
                + "floatTasks=[]"
                
                + ", reservedTasks=[], "
                
                + "completedTasks=["
                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n, "

                
                + "Description: Event3\r\n"
                + "Deadline: 19/02/16 18:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                + "]", 
                Logic.executeCommand(new CommandShow("done")).toString());
        
        //undone task
        delNum = new ArrayList<Integer>();
        delNum.add(1);
        assertEquals("Display [message=Undone task: \"DL4\", "
                + "events=[], "
                
                + "deadlineTasks=["
                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n"
                + "],"
                
                + " floatTasks=[]"
                
                + ", reservedTasks=[], "
                
                + "completedTasks=["
                + "Description: Event3\r\n"
                + "Deadline: 19/02/16 18:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n]"
                + "]", 
                Logic.executeCommand(new CommandUndone(delNum)).toString());
        
      //undone multiple task
        delNum = new ArrayList<Integer>();
        delNum.add(3);
        delNum.add(2);
        assertEquals("Display [message=Undone task: \"Event3\", \"DL1\", "
                + "events=[], "
                
                + "deadlineTasks=["
                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n"
                + ", "

                + "Description: Event3\r\n"
                + "Deadline: 19/02/16 18:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=[]"
                
                + ", reservedTasks=[], "
                
                + "completedTasks=["
                + "]]", 
                Logic.executeCommand(new CommandUndone(delNum)).toString());
        
      //done all
        assertEquals("Display [message=All shown tasks completed, "
                + "events=[], "
                
                + "deadlineTasks=[], "
                
                + "floatTasks=[]"
                
                + ", reservedTasks=[], "
                
                + "completedTasks=["
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n, "

                + "Description: Event3\r\n"
                + "Deadline: 19/02/16 18:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n"
                + "]]", 
                Logic.executeCommand(new CommandDone(null)).toString());
        
      //undone all
        assertEquals("Display [message=All tasks undone, "
                + "events=[], "
                
                + "deadlineTasks=["
                + "Description: DL4\r\n"
                + "Deadline: 19/02/16 15:00\r\n"
                + "Location: JEM\r\n"
                + "Tags: #tag1 #tag2 #tag3\r\n\r\n"
                + ", "

                + "Description: Event3\r\n"
                + "Deadline: 19/02/16 18:00\r\n"
                + "Location: \r\n"
                + "Tags: #tag1\r\n\r\n, "
                
                + "Description: DL1\r\n"
                + "Deadline: 19/02/16 19:00\r\n"
                + "Location: \r\n"
                + "Tags:\r\n\r\n], "
                
                + "floatTasks=[]"
                
                + ", reservedTasks=[], "
                
                + "completedTasks=["
                + "]]", 
                Logic.executeCommand(new CommandUndone(null)).toString());
    }

}
