/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 31 Mar
 * CS2103
 */
package bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class Display implements java.io.Serializable{

	private String message;
	private ArrayList<TaskEvent> events = new ArrayList<TaskEvent>();
    private ArrayList<TaskDeadline> deadlineTasks = new ArrayList<TaskDeadline>();
    private ArrayList<TaskFloat> floatTasks = new ArrayList<TaskFloat>();
    private ArrayList<TaskReserved> reservedTasks = new ArrayList<TaskReserved>();
    private ArrayList<Task> completedTasks = new ArrayList<Task>();
    

    private ArrayList<TaskEvent> visibleEvents = events;
    private ArrayList<TaskDeadline> visibleDeadlineTasks = deadlineTasks;
    private ArrayList<TaskFloat> visibleFloatTasks = floatTasks;
    private ArrayList<TaskReserved> visibleReservedTasks = reservedTasks;
    private ArrayList<Task> visibleCompletedTasks = new ArrayList<Task>();

	public Display() {
		message = "";
	}

	public Display(String message) {
		this.message = message;
	}

	public Display(String message, ArrayList<TaskEvent> events, ArrayList<TaskDeadline> deadlineTasks,
			ArrayList<TaskFloat> floatTasks, ArrayList<TaskReserved> reservedTasks, ArrayList<Task> completedTasks) {
		this.message = "";
		this.events = events;
		this.deadlineTasks = deadlineTasks;
		this.floatTasks = floatTasks;
		this.reservedTasks = reservedTasks;
		this.completedTasks = completedTasks;
		visibleEvents = events;
		visibleDeadlineTasks = deadlineTasks;
		visibleFloatTasks = floatTasks;
		visibleReservedTasks = reservedTasks;
	}
    
    public void setVisibleReservedTasks(ArrayList<TaskReserved> reservedTasks){
        this.visibleReservedTasks = reservedTasks;
    }
    
    public void setVisibleDeadlineTasks(ArrayList<TaskDeadline> deadlineTasks){
        this.visibleDeadlineTasks = deadlineTasks;
    }
    
    public void setVisibleEvents(ArrayList<TaskEvent> events){
        this.visibleEvents = events;
    }
    
    public void setVisibleFloatTasks(ArrayList<TaskFloat> floatTasks){
        this.visibleFloatTasks = floatTasks;
    }

    public ArrayList<TaskFloat> getVisibleFloatTasks() {
        return visibleFloatTasks;
    }

    public ArrayList<TaskEvent> getVisibleEvents() {
        return visibleEvents;
    }

    public ArrayList<TaskDeadline> getVisibleDeadlineTasks() {
        return visibleDeadlineTasks;
    }

    public ArrayList<TaskReserved> getVisibleReservedTasks() {
        return visibleReservedTasks;
    }

	public void setMessage(String message) {
		this.message = message;
	}

	public int getNumberOfTasks() {
		return (events.size() + deadlineTasks.size() + floatTasks.size() + reservedTasks.size()
				+ completedTasks.size());
	}

	public String getMessage() {
		return message;
	}

	public void setEvents(ArrayList<TaskEvent> events) {
		this.events = events;
	}

	public ArrayList<TaskEvent> getEventTasks() {
		return events;
	}

	public void setDeadlineTasks(ArrayList<TaskDeadline> deadlineTasks) {
		this.deadlineTasks = deadlineTasks;
	}

	public ArrayList<TaskDeadline> getDeadlineTasks() {
		return deadlineTasks;
	}
	
	 public Display deepClone() {
	   try {
	     ByteArrayOutputStream baos = new ByteArrayOutputStream();
	     ObjectOutputStream oos = new ObjectOutputStream(baos);
	     oos.writeObject(this);
	     
	     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	     ObjectInputStream ois = new ObjectInputStream(bais);
	     return (Display)ois.readObject();
	   }
	   catch (Exception e) {
	     e.printStackTrace();
	     return null;
	   }
	 }

	public void setFloatTasks(ArrayList<TaskFloat> floatTasks) {
		this.floatTasks = floatTasks;
	}

	public ArrayList<TaskFloat> getFloatTasks() {
		return floatTasks;
	}

	public void setReservedTasks(ArrayList<TaskReserved> reservedTasks) {
		this.reservedTasks = reservedTasks;
	}

	public ArrayList<TaskReserved> getReservedTasks() {
		return reservedTasks;
	}

	public ArrayList<Task> getCompletedTasks() {
		return completedTasks;
	}

	public void setCompletedTasks(ArrayList<Task> completedTasks) {
		this.completedTasks = completedTasks;
	}

    public ArrayList<Task> getVisibleCompletedTasks() {
        return visibleCompletedTasks;
    }

    public void setVisibleCompletedTasks(ArrayList<Task> completedTasks) {
        this.visibleCompletedTasks = completedTasks;
    }
    
    public int setOverdueTasks( ) {
        int numOverdue = 0;
        if (this.getDeadlineTasks() != null) {
            for (int i = 0; i < this.getDeadlineTasks().size(); i++) {
                TaskDeadline task = this.getDeadlineTasks().get(i);
                if (task.getEndDate().before(Calendar.getInstance())) {
                    task.setIsOverdue(true);
                    numOverdue++;
                }

            }
        }
        if (this.getEventTasks() != null) {
            for (int i = 0; i < this.getEventTasks().size(); i++) {
                TaskEvent task = this.getEventTasks().get(i);
                if (task.getEndDate().before(Calendar.getInstance())) {
                    task.setIsOverdue(true);
                    numOverdue++;
                }

            }
        }
        return numOverdue;
    }
	

	// @@author Chloe Odquier Fortuna (A0149063E)
	@Override
	public String toString() {
		return "Display [message=" + message + ", events=" + visibleEvents + ", deadlineTasks=" + visibleDeadlineTasks
				+ ", floatTasks=" + visibleFloatTasks + ", reservedTasks=" + visibleReservedTasks + ", completedTasks="
				+ completedTasks + "]";
	}
}
