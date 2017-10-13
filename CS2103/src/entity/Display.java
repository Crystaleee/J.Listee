/*
 * @@author A0139995E
 */
package entity;

/**
 * This class keeps track of all tasks and also the view state
 * of the program.
 * 
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("serial")
public class Display implements java.io.Serializable {

    private String _message;
    private String _commandType;
    private ArrayList<Integer> _taskIndices = new ArrayList<Integer>();
    private ArrayList<Integer> _conflictingTasksIndices = new ArrayList<Integer>();

    private ArrayList<TaskEvent> _events = new ArrayList<TaskEvent>();
    private ArrayList<TaskDeadline> _deadlineTasks = new ArrayList<TaskDeadline>();
    private ArrayList<TaskFloat> _floatTasks = new ArrayList<TaskFloat>();
    private ArrayList<TaskReserved> _reservedTasks = new ArrayList<TaskReserved>();
    private ArrayList<Task> _completedTasks = new ArrayList<Task>();

    private ArrayList<TaskEvent> _visibleEvents = _events;
    private ArrayList<TaskDeadline> _visibleDeadlineTasks = _deadlineTasks;
    private ArrayList<TaskFloat> _visibleFloatTasks = _floatTasks;
    private ArrayList<TaskReserved> _visibleReservedTasks = _reservedTasks;
    private ArrayList<Task> _visibleCompletedTasks = new ArrayList<Task>();

    public Display() {
        _message = GlobalConstants.EMPTY_STRING;
    }

    public Display(String message) {
        this._message = message;
    }

    public Display(String message, ArrayList<TaskEvent> events, ArrayList<TaskDeadline> deadlineTasks,
            ArrayList<TaskFloat> floatTasks, ArrayList<TaskReserved> reservedTasks,
            ArrayList<Task> completedTasks) {
        this._message = "";
        this._events = events;
        this._deadlineTasks = deadlineTasks;
        this._floatTasks = floatTasks;
        this._reservedTasks = reservedTasks;
        this._completedTasks = completedTasks;
        _visibleEvents = events;
        _visibleDeadlineTasks = deadlineTasks;
        _visibleFloatTasks = floatTasks;
        _visibleReservedTasks = reservedTasks;
    }

    public void setCommandType(String commandType) {
        this._commandType = commandType;
    }

    public String getCommandType() {
        return _commandType;
    }

    public void setConflictingTasksIndices(ArrayList<Integer> conflictingTasksIndices) {
        this._conflictingTasksIndices = conflictingTasksIndices;
    }

    public ArrayList<Integer> getConflictingTasksIndices() {
        return _conflictingTasksIndices;
    }

    public void setTaskIndices(ArrayList<Integer> taskIndices) {
        this._taskIndices = taskIndices;
    }

    public ArrayList<Integer> getTaskIndices() {
        return _taskIndices;
    }

    public void setVisibleReservedTasks(ArrayList<TaskReserved> reservedTasks) {
        this._visibleReservedTasks = reservedTasks;
    }

    public void setVisibleDeadlineTasks(ArrayList<TaskDeadline> deadlineTasks) {
        this._visibleDeadlineTasks = deadlineTasks;
    }

    public void setVisibleEvents(ArrayList<TaskEvent> events) {
        this._visibleEvents = events;
    }

    public void setVisibleFloatTasks(ArrayList<TaskFloat> floatTasks) {
        this._visibleFloatTasks = floatTasks;
    }

    public ArrayList<TaskFloat> getVisibleFloatTasks() {
        return _visibleFloatTasks;
    }

    public ArrayList<TaskEvent> getVisibleEvents() {
        return _visibleEvents;
    }

    public ArrayList<TaskDeadline> getVisibleDeadlineTasks() {
        return _visibleDeadlineTasks;
    }

    public ArrayList<TaskReserved> getVisibleReservedTasks() {
        return _visibleReservedTasks;
    }

    public void setMessage(String message) {
        this._message = message;
    }

    public int getNumberOfTasks() {
        return (_events.size() + _deadlineTasks.size() + _floatTasks.size() + _reservedTasks.size()
                + _completedTasks.size());
    }

    public String getMessage() {
        return _message;
    }

    public void setEvents(ArrayList<TaskEvent> events) {
        this._events = events;
    }

    public ArrayList<TaskEvent> getEventTasks() {
        return _events;
    }

    public void setDeadlineTasks(ArrayList<TaskDeadline> deadlineTasks) {
        this._deadlineTasks = deadlineTasks;
    }

    public ArrayList<TaskDeadline> getDeadlineTasks() {
        return _deadlineTasks;
    }

    public Display deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Display) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setFloatTasks(ArrayList<TaskFloat> floatTasks) {
        this._floatTasks = floatTasks;
    }

    public ArrayList<TaskFloat> getFloatTasks() {
        return _floatTasks;
    }

    public void setReservedTasks(ArrayList<TaskReserved> reservedTasks) {
        this._reservedTasks = reservedTasks;
    }

    public ArrayList<TaskReserved> getReservedTasks() {
        return _reservedTasks;
    }

    public ArrayList<Task> getCompletedTasks() {
        return _completedTasks;
    }

    public void setCompletedTasks(ArrayList<Task> completedTasks) {
        this._completedTasks = completedTasks;
    }

    public ArrayList<Task> getVisibleCompletedTasks() {
        return _visibleCompletedTasks;
    }

    public void setVisibleCompletedTasks(ArrayList<Task> completedTasks) {
        this._visibleCompletedTasks = completedTasks;
    }

    public boolean setOverdueTasks() {
        boolean changed = false;
        changed = setOverdueDeadlines(changed);
        changed = setOverdueEvents(changed);
        return changed;
    }

    private boolean setOverdueDeadlines(boolean changed) {
        if (this.getDeadlineTasks() != null) {
            for (int i = 0; i < this.getDeadlineTasks().size(); i++) {
                TaskDeadline task = this.getDeadlineTasks().get(i);
                if (task.getEndDate().before(Calendar.getInstance())) {
                    if (!task.isOverdue()) {
                        task.setIsOverdue(true);
                        changed = true;
                    }
                }

            }
        }
        return changed;
    }

    private boolean setOverdueEvents(boolean changed) {
        if (this.getEventTasks() != null) {
            for (int i = 0; i < this.getEventTasks().size(); i++) {
                TaskEvent task = this.getEventTasks().get(i);
                if (task.getEndDate().before(Calendar.getInstance())) {
                    if (!task.isOverdue()) {
                        task.setIsOverdue(true);
                        changed = true;
                    }
                }

            }
        }
        return changed;
    }

    // @@author A0149063E generated
    @Override
    public String toString() {
        return "Display [message=" + _message + ", events=" + _visibleEvents + ", deadlineTasks="
                + _visibleDeadlineTasks + ", floatTasks=" + _visibleFloatTasks + ", reservedTasks="
                + _visibleReservedTasks + ", completedTasks=" + _visibleCompletedTasks + "]";
    }
}
