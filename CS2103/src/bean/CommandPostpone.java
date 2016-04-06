/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

public class CommandPostpone implements Command {
    private int _taskNumber;
    private ArrayList<String> _parameters;
    private Calendar _time;
    private String _msg;
    private boolean _updateFile = true;;
    private boolean _saveHistory = true;

    public CommandPostpone() {
        this._taskNumber = -1;
    }

    public CommandPostpone(Integer taskNumber, Calendar time, ArrayList<String> parameters) {
        assert parameters != null;
        this._taskNumber = taskNumber - 1;
        this._time = time;
        this._parameters = parameters;
    }
    
    /*
     * Only deadline tasks and events can be postponed
     */
    public Display execute(Display display) {
        if (isInvalidCommand(display)) {
            setInvalidDisplay(display);
            return display;
        }
        postpone(display);
        display.setMessage(_msg);
        return display;
    }

    public void postpone(Display display) {
        if (_taskNumber < display.getVisibleDeadlineTasks().size()) {
            postponeDeadline(display);
        } else {
            _taskNumber -= display.getVisibleDeadlineTasks().size();
            postponeEvent(display);
        }
    }

    public void postponeEvent(Display display) {
        TaskEvent task = display.getVisibleEvents().remove(_taskNumber);
        display.getEventTasks().remove(task);
        for (int i = 0; i < _parameters.size(); i++) {
            String parameter = _parameters.get(i).trim().toLowerCase();
            switch (parameter) {
            case "year":
                addYear(task.getStartDate());
                addYear(task.getEndDate());
                break;
            case "month":
                addMonth(task.getStartDate());
                addMonth(task.getEndDate());
                break;
            case "day":
                addDay(task.getStartDate());
                addDay(task.getEndDate());
                break;
            case "hour":
                addHour(task.getStartDate());
                addHour(task.getEndDate());
                break;
            case "minute":
                addMinute(task.getStartDate());
                addMinute(task.getEndDate());
                break;
            }
        }
        new CommandAddEvent(task).execute(display);
        _msg = "Postponed: " + task.getDescription();
    }

    public void postponeDeadline(Display display) {
        TaskDeadline task = display.getVisibleDeadlineTasks().remove(_taskNumber);
        display.getDeadlineTasks().remove(task);
        for (int i = 0; i < _parameters.size(); i++) {
            String parameter = _parameters.get(i).trim().toLowerCase();
            switch (parameter) {
            case "year":
                addYear(task.getEndDate());
                break;
            case "month":
                addMonth(task.getEndDate());
                break;
            case "day":
                addDay(task.getEndDate());
                break;
            case "hour":
                addHour(task.getEndDate());
                break;
            case "minute":
                addMinute(task.getEndDate());
                break;
            }
        }
        new CommandAddDeadlineTask(task).execute(display);
        _msg = "Postponed: " + task.getDescription();
    }

    private void addMinute(Calendar oldTime) {
        oldTime.set(Calendar.MINUTE,
                oldTime.get(Calendar.MINUTE) + _time.get(Calendar.MINUTE));
    }

    private void addHour(Calendar oldTime) {
        oldTime.set(Calendar.HOUR_OF_DAY,
                oldTime.get(Calendar.HOUR_OF_DAY) + _time.get(Calendar.HOUR_OF_DAY));
    }

    private void addDay(Calendar oldTime) {
        oldTime.set(Calendar.DATE, oldTime.get(Calendar.DATE) + _time.get(Calendar.DATE));
    }

    private void addMonth(Calendar oldTime) {
        oldTime.set(Calendar.MONTH, oldTime.get(Calendar.MONTH) + _time.get(Calendar.MONTH));
    }

    private void addYear(Calendar oldTime) {
        oldTime.set(Calendar.YEAR, oldTime.get(Calendar.YEAR) + _time.get(Calendar.YEAR));
    }

    private void setInvalidDisplay(Display display) {
        _updateFile = false;
        _saveHistory = false;
        display.setMessage(_msg);
        display.setTaskIndices(null);
        display.setConflictingTasksIndices(null);
    }

    private boolean isInvalidCommand(Display display) {
        if (hasInvalidTaskNumber(display)) {
            return true;
        } else if (hasInvalidTime()) {
            return true;
        }
        return false;
    }

    private boolean hasInvalidTime() {
        return false;
    }

    private boolean hasInvalidTaskNumber(Display display) {
        if (_taskNumber < 0) {
            _msg = "Please specify a task number";
            return true;
        } else {
            int minIndex = 0;
            int maxIndex = display.getVisibleDeadlineTasks().size() + display.getVisibleEvents().size() - 1;
            if ((_taskNumber < minIndex) || (_taskNumber > maxIndex)) {
                _msg = "You can only pospone deadline tasks and events!";
                return true;
            }
        }
        return false;
    }

    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}
