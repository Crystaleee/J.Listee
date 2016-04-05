/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class CommandUpdate extends TaskEvent implements Command {
    private Integer _reservedSlotIndex;
    private Integer _taskNumber;
    private ArrayList<String> _removeTags;
    private Display _display;
    private boolean _timeChanged = false;
    private boolean _updateFile = true;;
    private boolean _saveHistory = true;
    private String msgEdit = "Edited : \"";
    private ArrayList<Integer> _removeReservedSlotIndex;

    public CommandUpdate() {
        super();
        _removeTags = null;
        this._taskNumber = null;
    }

    public CommandUpdate(Integer taskNumber, String description, String location, Calendar startDate,
            Calendar endDate, ArrayList<String> addTags, ArrayList<String> removeTags) {
        super(description, location, startDate, endDate, addTags);
        this._removeTags = removeTags;
        _updateFile = true;
        this._taskNumber = taskNumber;
        this._reservedSlotIndex = null;
    }

    public CommandUpdate(Integer taskNumber, Integer reservedSlotIndex, String description, String location,
            Calendar startDate, Calendar endDate, ArrayList<String> addTags, ArrayList<String> removeTags,
            ArrayList<Integer> removeReservedSlotIndex) {
        super(description, location, startDate, endDate, addTags);
        this._removeTags = removeTags;
        _updateFile = true;
        this._taskNumber = taskNumber;
        this._reservedSlotIndex = reservedSlotIndex;
        this._removeReservedSlotIndex = removeReservedSlotIndex;
    }

    public Display execute(Display oldDisplay) {
        _display = oldDisplay;
        if (hasInvalidTaskNumber()) {
            msgEdit = GlobalConstants.MESSAGE_ERROR_TASK_NUMBER;
            setInvalidDisplay();
            return _display;
        }
        if (isInvalidDateRange()) {
            msgEdit = GlobalConstants.MESSAGE_ERROR_DATE_RANGE;
            setInvalidDisplay();
            return oldDisplay;
        }
        editTask();
        _display.setMessage(msgEdit);
        return _display;
    }

    // returns true if end date is before start date
    private boolean isInvalidDateRange() {
        if ((getStartDate() != null) && (getEndDate() != null)) {
            if (getStartDate().after(getEndDate())) {
                return true;
            }
        }
        return false;
    }

    private void setInvalidDisplay() {
        _updateFile = false;
        _saveHistory = false;
        _display.setMessage(msgEdit);
        _display.setTaskIndices(new ArrayList<Integer>());
        _display.setConflictingTasksIndices(new ArrayList<Integer>());
    }

    private boolean hasInvalidTaskNumber() {
        int numOfTasks = 0;
        numOfTasks += _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().size()
                + _display.getVisibleFloatTasks().size() + _display.getVisibleReservedTasks().size();
        return ((_taskNumber > numOfTasks) || (_taskNumber < 1));
    }

    private void editTask() {
        if (_taskNumber <= _display.getVisibleDeadlineTasks().size()) {
            editDeadline();
        } else {
            _taskNumber -= _display.getVisibleDeadlineTasks().size();
            if (_taskNumber <= _display.getVisibleEvents().size()) {
                editEvent();
            } else {
                _taskNumber -= _display.getVisibleEvents().size();
                if (_taskNumber <= _display.getVisibleFloatTasks().size()) {
                    editFloat();
                } else {
                    _taskNumber -= _display.getVisibleFloatTasks().size();
                    editReserved();
                }
            }
        }
    }

    private void editDeadline() {
        TaskDeadline task = _display.getVisibleDeadlineTasks().get(_taskNumber - 1);
        if (invalidEditDeadline(task)) {
            setInvalidDisplay();
            return;
        }
        msgEdit += task.getDescription() + GlobalConstants.INVERTED_COMMAS;
        editDescription(task);
        editLocation(task);
        editTags(task);
        editEndDate(task);
        changeDeadlineTaskType(task);
    }

    private void editEvent() {
        TaskEvent task = _display.getVisibleEvents().get(_taskNumber - 1);
        if (invalidEditEvent(task)) {
            setInvalidDisplay();
            return;
        }
        msgEdit += task.getDescription() + GlobalConstants.INVERTED_COMMAS;
        editDescription(task);
        editLocation(task);
        editTags(task);
        editEndDate(task);
        editStartDate(task);
        changeEventTaskType(task);
        if (_timeChanged) {
            _display.getEventTasks().remove(task);
            _display.getVisibleEvents().remove(task);
            Command add = new CommandAddEvent(task);
            add.execute(_display);
        }
    }

    private void editFloat() {
        TaskFloat task = _display.getVisibleFloatTasks().get(_taskNumber - 1);
        if (invalidEditFloat(task)) {
            setInvalidDisplay();
            return;
        }
        msgEdit += task.getDescription() + GlobalConstants.INVERTED_COMMAS;
        editDescription(task);
        editLocation(task);
        editTags(task);
        int index = getIndexOfFloat(task);
        _display.getTaskIndices().add(index);
        if (hasChangeFloatTaskType(task)) {
            _display.getVisibleFloatTasks().remove(_taskNumber - 1);
            _display.getFloatTasks().remove(task);
        }
    }

    private int getIndexOfFloat(TaskFloat task) {
        int index = _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().size()
                + _display.getVisibleFloatTasks().indexOf(task) + 1;
        return index;
    }

    private void editReserved() {
        TaskReserved task = _display.getReservedTasks().get(_taskNumber - 1);
        if (invalidEditReserved(task)) {
            setInvalidDisplay();
            return;
        }
        msgEdit += task.getDescription() + "\"";
        editDescription(task);
        editLocation(task);
        editTags(task);
        editTimeSlot(task);
        removeTimeSlot(task);
    }

    private void removeTimeSlot(TaskReserved task) {
        System.out.println("Enter");
        if (_removeReservedSlotIndex != null) {
            Collections.sort(_removeReservedSlotIndex);
            for (int i = 0; i < _removeReservedSlotIndex.size(); i++) {
                int index = _removeReservedSlotIndex.get(i);
                task.getStartDates().remove(index - 1 - i);
                task.getEndDates().remove(index - 1 - i);
            }
        }
    }

    private void editTimeSlot(TaskReserved task) {
        if (getStartDate() != null) {
            if (getStartDate().get(Calendar.YEAR) == 1) {
                Calendar start = task.getStartDates().get(_reservedSlotIndex - 1);
                start.set(Calendar.HOUR_OF_DAY, getStartDate().get(Calendar.HOUR_OF_DAY));
                start.set(Calendar.MINUTE, getStartDate().get(Calendar.MINUTE));
            } else {
                task.getStartDates().set(_reservedSlotIndex - 1, getStartDate());
            }
        }
        if (getEndDate() != null) {
            if (getEndDate().get(Calendar.YEAR) == 1) {
                Calendar end = task.getEndDates().get(_reservedSlotIndex - 1);
                end.set(Calendar.HOUR_OF_DAY, getEndDate().get(Calendar.HOUR_OF_DAY));
                end.set(Calendar.MINUTE, getEndDate().get(Calendar.MINUTE));
            } else {
                task.getEndDates().set(_reservedSlotIndex - 1, getEndDate());
            }
        }
    }

    private boolean invalidEditReserved(TaskReserved task) {
        if (hasNoLocationToRemove(task)) {
            msgEdit = "No location to remove!";
            return true;
        }
        if (hasNoTagsToRemove(task)) {
            msgEdit = "No tags to remove!";
            return true;
        }
        if ((getEndDate() != null) && (getStartDate() != null)) {
            if (_reservedSlotIndex == null) {
                msgEdit = "Please specify a timeslot";
                return true;
            }
        }
        if ((getEndDate() != null) && (getStartDate() != null)) {
            if (getEndDate().getTimeInMillis() == 0) {
                msgEdit = "Can't remove end date!";
                return true;
            } else if (getStartDate().getTimeInMillis() == 0) {
                msgEdit = "Can't remove start date!";
                return true;
            }
        }
        if (_removeReservedSlotIndex != null) {
            if (!_removeReservedSlotIndex.isEmpty()) {
                if (hasInvalidIndices(task)) {
                    msgEdit = "You have specified an invalid time slot(s) to be removed";
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasInvalidIndices(TaskReserved task) {
        for (int i = 0; i < _removeReservedSlotIndex.size(); i++) {
            int index = _removeReservedSlotIndex.get(i);
            if ((index < 1) || (index > task.getStartDates().size())) {
                return true;
            }
        }
        return false;
    }

    private boolean invalidEditFloat(TaskFloat task) {
        if (hasNoLocationToRemove(task)) {
            msgEdit = "No location to remove!";
            return true;
        }
        if (hasNoTagsToRemove(task)) {
            msgEdit = "No tags to remove!";
            return true;
        }
        if ((hasAddStartDate()) && (!hasAddEndDate())) {
            msgEdit = "Can't add just the start date!!";
            return true;
        }
        /*
         * if (_reservedSlotIndex != null) { msgEdit =
         * "Can't specify timeslot for non-reserved tasks!"; }
         */

        return false;
    }

    private boolean invalidEditEvent(TaskEvent task) {
        if (hasNoLocationToRemove(task)) {
            msgEdit = "No location to remove!";
            return true;
        }
        if (hasNoTagsToRemove(task)) {
            msgEdit = "No tags to remove!";
            return true;
        }
        if ((hasRemoveEndDate()) && (!hasRemoveStartDate())) {
            msgEdit = "Cant remove end date only!";
            return true;

        }
        /*
         * if (_reservedSlotIndex != null) { msgEdit =
         * "Can't specify timeslot for non-reserved tasks!"; }
         */

        return false;
    }

    private boolean invalidEditDeadline(TaskDeadline task) {
        if (hasNoLocationToRemove(task)) {
            msgEdit = "No location to remove!";
            return true;
        }
        if (hasNoTagsToRemove(task)) {
            msgEdit = "No tags to remove!";
            return true;
        }
        if ((hasRemoveStartDate())) {
            msgEdit = "No start date to remove!";
            return true;
        }
        if ((hasRemoveEndDate()) && (hasAddStartDate())) {
            msgEdit = "Cant add start date and remove end date!";
            return true;

        } /*
           * if (_reservedSlotIndex != null) { msgEdit =
           * "Can't specify timeslot for non-reserved tasks!"; }
           */

        return false;
    }

    private boolean hasRemoveTimeSlots() {
        if (_removeReservedSlotIndex != null) {
            if (!_removeReservedSlotIndex.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasRemoveEndDate() {
        if ((getEndDate() != null)) {
            if (getEndDate().getTimeInMillis() == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAddEndDate() {
        if ((getEndDate() != null)) {
            if (getEndDate().getTimeInMillis() != 0) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAddStartDate() {
        if ((getStartDate() != null)) {
            if (getStartDate().getTimeInMillis() != 0) {
                return true;
            }
        }
        return false;
    }

    private boolean hasRemoveStartDate() {
        if ((getStartDate() != null)) {
            if (getStartDate().getTimeInMillis() == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean hasNoTagsToRemove(Task task) {
        if (_removeTags == null) {
            return false;
        }
        if (_removeTags != null) {
            if ((!_removeTags.isEmpty()) && (task.getTags() == null)) {
                return true;
            } else if ((!_removeTags.isEmpty()) && (task.getTags().isEmpty())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasNoLocationToRemove(Task task) {
        if (getLocation() != null) {
            if ((getLocation().equals(GlobalConstants.EMPTY_STRING)) && (task.getLocation() == null)) {
                return true;
            }
        }
        return false;
    }

    private boolean changeDeadlineTaskType(TaskDeadline task) {
        // assertFalse endDate==0 AND startDate != 0&null
        boolean hasTaskChanged = false;
        if (isConvertDeadlineToFloat()) {
            _display.getVisibleDeadlineTasks().remove(_taskNumber - 1);
            _display.getDeadlineTasks().remove(task);
            hasTaskChanged = convertDeadlineToFloat(task);
        }
        if (isConvertDeadlineToEvent(hasTaskChanged)) {
            _display.getVisibleDeadlineTasks().remove(_taskNumber - 1);
            _display.getDeadlineTasks().remove(task);
            convertDeadlineToEvent(task);
            hasTaskChanged = true;
        }
        /*
         * if (!hasTaskChanged) { Command addCommand = new
         * CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
         * task.getEndDate(), task.getTags()); _display =
         * addCommand.execute(_display);
         * _display.setCommandType(GlobalConstants.GUI_ANIMATION_INVALID); }
         */
        return hasTaskChanged;
    }

    private boolean convertDeadlineToFloat(TaskDeadline task) {
        boolean hasTaskChanged;
        Command addCommand = new CommandAddFloatTask(task.getDescription(), task.getLocation(),
                task.getTags());
        _display = addCommand.execute(_display);
        hasTaskChanged = true;
        return hasTaskChanged;
    }

    private void convertDeadlineToEvent(TaskDeadline task) {
        Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(), getStartDate(),
                task.getEndDate(), task.getTags());
        _display = addCommand.execute(_display);
    }

    private boolean isConvertDeadlineToEvent(boolean hasTaskChanged) {
        if (getStartDate() != null) {
            if ((getStartDate().getTimeInMillis() != 0) && (!hasTaskChanged)) {
                return true;
            }
        }
        return false;
    }

    private boolean isConvertDeadlineToFloat() {
        if (getEndDate() != null) {
            if (getEndDate().getTimeInMillis() == 0) {
                return true;
            }
        }
        return false;
    }

    private void editTags(Task task) {
        if (_removeTags != null) {
            for (int i = 0; i < _removeTags.size(); i++) {
                String tag = _removeTags.get(i);
                task.getTags().remove(tag);
            }
        }

        if (getTags() != null) {
            for (int i = 0; i < getTags().size(); i++) {
                String tag = getTags().get(i);
                task.getTags().add(tag);
            }
        }
        return;
    }

    private void editStartDate(TaskEvent task) {
        if (getStartDate() != null) {
            if (getStartDate().getTimeInMillis() != 0) {
                if (getStartDate().get(Calendar.YEAR) == 1) {
                    task.getStartDate().set(Calendar.HOUR_OF_DAY, getStartDate().get(Calendar.HOUR_OF_DAY));
                    task.getStartDate().set(Calendar.MINUTE, getStartDate().get(Calendar.MINUTE));
                    _timeChanged = true;
                } else {
                    task.setStartDate(getStartDate());
                    _timeChanged = true;
                }
            }
        }
        return;
    }

    private void editEndDate(TaskEvent task) {
        if (getEndDate() != null) {
            if (getEndDate().getTimeInMillis() != 0) {
                if (getEndDate().get(Calendar.YEAR) == 1) {
                    task.getEndDate().set(Calendar.HOUR_OF_DAY, getEndDate().get(Calendar.HOUR_OF_DAY));
                    task.getEndDate().set(Calendar.MINUTE, getEndDate().get(Calendar.MINUTE));
                    _timeChanged = true;
                } else {
                    task.setEndDate(getEndDate());
                    _timeChanged = true;
                }
            }
        }
        if (task.getEndDate().before(Calendar.getInstance())) {
            task.setIsOverdue(true);
        } else {
            task.setIsOverdue(false);
        }
        return;
    }

    private void editEndDate(TaskDeadline task) {
        if (getEndDate() != null) {
            if (getEndDate().getTimeInMillis() != 0) {
                if (getEndDate().get(Calendar.YEAR) == 1) {
                    task.getEndDate().set(Calendar.HOUR_OF_DAY, getEndDate().get(Calendar.HOUR_OF_DAY));
                    task.getEndDate().set(Calendar.MINUTE, getEndDate().get(Calendar.MINUTE));
                } else {
                    task.setEndDate(getEndDate());
                }
            }
        }
        if (task.getEndDate().before(Calendar.getInstance())) {
            task.setIsOverdue(true);
        } else {
            task.setIsOverdue(false);
        }
        return;
    }

    private void editLocation(Task task) {
        if (getLocation() != null) {
            if (getLocation().trim().isEmpty()) {
                task.setLocation(null);
            } else {
                task.setLocation(getLocation().trim());
            }
        }
        return;
    }

    private void editDescription(Task task) {
        if (getDescription() != null) {
            if (!getDescription().trim().isEmpty()) {
                task.setDescription(getDescription().trim());
            }
        }
        return;
    }

    private void changeEventTaskType(TaskEvent task) {
        boolean hasTaskChanged = false;
        if (isConvertEventToFloat()) {
            _display.getVisibleEvents().remove(task);
            _display.getEventTasks().remove(task);
            convertEventToFloat(task);
            hasTaskChanged = true;
        }
        if (isConvertEventToDeadline(hasTaskChanged)) {
            _display.getVisibleEvents().remove(task);
            _display.getEventTasks().remove(task);
            convertEventToDeadline(task);
            hasTaskChanged = true;
        }
        return;
    }

    private void convertEventToFloat(TaskEvent task) {
        Command addCommand = new CommandAddFloatTask(task.getDescription(), task.getLocation(),
                task.getTags());
        _display = addCommand.execute(_display);
    }

    private void convertEventToDeadline(TaskEvent task) {
        Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                task.getEndDate(), task.getTags());
        _display = addCommand.execute(_display);
    }

    private boolean isConvertEventToFloat() {
        if ((getStartDate() != null) && (getEndDate() != null)) {
            if ((getStartDate().getTimeInMillis() == 0) && (getEndDate().getTimeInMillis() == 0)) {
                return true;
            }
        }
        return false;
    }

    private boolean isConvertEventToDeadline(boolean hasTaskChanged) {
        if (getStartDate() != null) {
            if ((getStartDate().getTimeInMillis() == 0) && (!hasTaskChanged)) {
                return true;
            }
        }
        return false;
    }

    private void addEventBack(TaskEvent task) {
        Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(),
                task.getStartDate(), task.getEndDate(), task.getTags());
        _display = addCommand.execute(_display);
        if (!_timeChanged) {
            _display.setConflictingTasksIndices(new ArrayList<Integer>());
        }
        _display.setCommandType(GlobalConstants.GUI_ANIMATION_INVALID);
    }

    private boolean hasChangeFloatTaskType(TaskFloat task) {
        boolean hasTaskChanged = false;
        if (isChangeFloatToEvent()) {
            convertFloatToEvent(task);
            hasTaskChanged = true;
        } else {
            if (isChangeFloatToDeadline()) {
                convertFloatToDeadline(task);
                hasTaskChanged = true;
            }
        }
        return hasTaskChanged;
    }

    private void convertFloatToDeadline(TaskFloat task) {
        Command addCommand = new CommandAddDeadlineTask(task.getDescription(), task.getLocation(),
                getEndDate(), task.getTags());
        _display = addCommand.execute(_display);
    }

    private boolean isChangeFloatToDeadline() {
        if (getEndDate() != null) {
            if (getEndDate().getTimeInMillis() != 0) {
                return true;
            }
        }
        return false;
    }

    private void convertFloatToEvent(TaskFloat task) {
        Command addCommand = new CommandAddEvent(task.getDescription(), task.getLocation(), getStartDate(),
                getEndDate(), task.getTags());
        _display = addCommand.execute(_display);
    }

    private boolean isChangeFloatToEvent() {
        if ((getStartDate() != null) && (getEndDate() != null)) {
            if ((getStartDate().getTimeInMillis() != 0) && (getEndDate().getTimeInMillis() != 0)) {
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
