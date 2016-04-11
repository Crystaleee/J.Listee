/*
 * @@author A0139995E
 */
package entity;

/**
 * This command is to edit any parameters
 * of a task
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class CommandUpdate extends TaskEvent implements Command {
    private Integer _reservedSlotIndex;
    private Integer _taskNumber;
    private ArrayList<String> _removeTags;
    private Display _display;
    private boolean _timeChanged = false;
    private boolean _updateFile = true;;
    private boolean _saveHistory = true;
    private String _msgEdit = "Edited : \"";
    private ArrayList<Integer> _removeReservedSlotIndex;
    private Logger _logger = GlobalLogger.getLogger();

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
        assert oldDisplay != null : "Update: null display";
        _display = oldDisplay;
        if (hasInvalidTaskNumber()) {
            _logger.log(Level.INFO, "Update: Invalid Indices");
            _msgEdit = GlobalConstants.MESSAGE_ERROR_TASK_NUMBER;
            setInvalidDisplay();
            return _display;
        }
        if (isInvalidDateRange()) {
            _logger.log(Level.INFO, "Update: Invalid dates");
            _msgEdit = GlobalConstants.MESSAGE_ERROR_DATE_RANGE;
            setInvalidDisplay();
            return oldDisplay;
        }
        editTask();
        _display.setMessage(_msgEdit);
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

    /**
     * sets up variables if command has invalid parameters
     */
    private void setInvalidDisplay() {
        _updateFile = false;
        _saveHistory = false;
        _display.setMessage(_msgEdit);
        _display.setTaskIndices(new ArrayList<Integer>());
        _display.setConflictingTasksIndices(new ArrayList<Integer>());
    }

    private boolean hasInvalidTaskNumber() {
        int numOfTasks = 0;
        numOfTasks += _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().size()
                + _display.getVisibleFloatTasks().size() + _display.getVisibleReservedTasks().size();
        return ((_taskNumber > numOfTasks) || (_taskNumber < 1));
    }

    /**
     * Maps task number to task types
     */
    private void editTask() {
        if (_taskNumber <= _display.getVisibleDeadlineTasks().size()) {
            _logger.log(Level.INFO, "Update: Deadline");
            editDeadline();
        } else {
            _taskNumber -= _display.getVisibleDeadlineTasks().size();
            if (_taskNumber <= _display.getVisibleEvents().size()) {
                _logger.log(Level.INFO, "Update: Event");
                editEvent();
            } else {
                _taskNumber -= _display.getVisibleEvents().size();
                if (_taskNumber <= _display.getVisibleFloatTasks().size()) {
                    _logger.log(Level.INFO, "Update: Float");
                    editFloat();
                } else {
                    _taskNumber -= _display.getVisibleFloatTasks().size();
                    _logger.log(Level.INFO, "Update: Reserved");
                    editReserved();
                }
            }
        }
    }

    private void editDeadline() {
        TaskDeadline task = _display.getVisibleDeadlineTasks().get(_taskNumber - 1);
        if (isInvalidEditDeadline(task)) {
            _logger.log(Level.INFO, "Update: Invalid deadline edit");
            setInvalidDisplay();
            return;
        }
        _msgEdit += task.getDescription() + GlobalConstants.INVERTED_COMMAS;
        editDescription(task);
        editLocation(task);
        editTags(task);
        editEndDate(task);
        changeDeadlineTaskType(task);

    }

    private void editEvent() {
        TaskEvent task = _display.getVisibleEvents().get(_taskNumber - 1);
        if (isInvalidEditEvent(task)) {
            _logger.log(Level.INFO, "Update: Invalid event edit");
            setInvalidDisplay();
            return;
        }
        _msgEdit += task.getDescription() + GlobalConstants.INVERTED_COMMAS;
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
        if (isInvalidEditFloat(task)) {
            _logger.log(Level.INFO, "Update: Invalid float edit");
            setInvalidDisplay();
            return;
        }
        _msgEdit += task.getDescription() + GlobalConstants.INVERTED_COMMAS;
        editDescription(task);
        editLocation(task);
        editTags(task);
        setTaskIndices(task);
        if (hasChangeFloatTaskType(task)) {
            _display.getVisibleFloatTasks().remove(_taskNumber - 1);
            _display.getFloatTasks().remove(task);
        }
    }

    private int getIndex(Task task) {
        int index = 0;
        if (task instanceof TaskEvent) {
            index = _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().indexOf(task) + 1;
        } else if (task instanceof TaskDeadline) {
            index = _display.getVisibleDeadlineTasks().indexOf(task) + 1;
        } else if (task instanceof TaskFloat) {
            index = _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().size()
                    + _display.getVisibleFloatTasks().indexOf(task) + 1;
        } else if (task instanceof TaskReserved) {
            index = _display.getVisibleDeadlineTasks().size() + _display.getVisibleEvents().size()
                    + _display.getVisibleFloatTasks().size()
                    + _display.getVisibleReservedTasks().indexOf(task) + 1;
        }
        return index;
    }

    private void editReserved() {
        TaskReserved task = _display.getReservedTasks().get(_taskNumber - 1);
        if (isInvalidEditReserved(task)) {
            _logger.log(Level.INFO, "Update: Invalid reserved edit");
            setInvalidDisplay();
            return;
        }
        _msgEdit += task.getDescription() + GlobalConstants.INVERTED_COMMAS;
        editDescription(task);
        editLocation(task);
        editTags(task);
        editTimeSlot(task);
        removeTimeSlot(task);
        setTaskIndices(task);
    }

    /**
     * this method removes time slots from a reserved task
     */
    private void removeTimeSlot(TaskReserved task) {
        if (_removeReservedSlotIndex != null) {
            Collections.sort(_removeReservedSlotIndex);
            for (int i = 0; i < _removeReservedSlotIndex.size(); i++) {
                int index = _removeReservedSlotIndex.get(i);
                task.getStartDates().remove(index - 1 - i);
                task.getEndDates().remove(index - 1 - i);
            }
        }
    }

    /**
     * this method checks if user wants to edit timeslot or add a timeslot
     */
    private void editTimeSlot(TaskReserved task) {
        if (_reservedSlotIndex != null) {
            editTime(task);
        } else {
            addTimeSlot(task);
        }
    }

    /**
     * this method adds a time slot to a reserved task
     */
    private void addTimeSlot(TaskReserved task) {
        if ((getStartDate() != null) && (getEndDate() != null)) {
            task.getStartDates().add(getStartDate());
            task.getEndDates().add(getEndDate());
        }else{
            _msgEdit = GlobalConstants.MESSAGE_ERROR_SPECIFY_BOTH_START_END;
        }
    }

    /**
     * this method edits a time slot of a reserved task
     */
    private void editTime(TaskReserved task) {
        editStart(task);
        editEnd(task);
    }

    private void editStart(TaskReserved task) {
        Calendar newTime = Calendar.getInstance();
        if (getStartDate() != null) {
            if (isChangeTimeOnly(getStartDate())) {
                newTime.setTimeInMillis(task.getStartDates().get(_reservedSlotIndex - 1).getTimeInMillis());
                newTime.set(Calendar.HOUR_OF_DAY, getStartDate().get(Calendar.HOUR_OF_DAY));
                newTime.set(Calendar.MINUTE, getStartDate().get(Calendar.MINUTE));
            } else {
                newTime.setTimeInMillis(getStartDate().getTimeInMillis());
            }

            if (newTime.before(task.getEndDates().get(_reservedSlotIndex - 1))) {
                Calendar start = task.getStartDates().get(_reservedSlotIndex - 1);
                start.setTimeInMillis(newTime.getTimeInMillis());
            } else {
                _msgEdit = GlobalConstants.MESSAGE_ERROR_START_AFTER_END;
            }
        }

    }

    private void editEnd(TaskReserved task) {
        Calendar newTime = Calendar.getInstance();
        if (getEndDate() != null) {
            if (isChangeTimeOnly(getEndDate())) {
                newTime.setTimeInMillis(task.getEndDates().get(_reservedSlotIndex - 1).getTimeInMillis());
                newTime.set(Calendar.HOUR_OF_DAY, getEndDate().get(Calendar.HOUR_OF_DAY));
                newTime.set(Calendar.MINUTE, getEndDate().get(Calendar.MINUTE));
            } else {
                newTime.setTimeInMillis(getEndDate().getTimeInMillis());
            }
            if (newTime.after(task.getStartDates().get(_reservedSlotIndex - 1))) {
                Calendar end = task.getEndDates().get(_reservedSlotIndex - 1);
                end.setTimeInMillis(newTime.getTimeInMillis());
            } else {
                _msgEdit = GlobalConstants.MESSAGE_ERROR_START_AFTER_END;
            }
        }
    }

    /**
     * this method checks for any invalid parameters when user has specified to
     * edit a reserved task
     */
    private boolean isInvalidEditReserved(TaskReserved task) {
        if (hasNoLocationToRemove(task)) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_NO_LOCATION;
            return true;
        }
        if (hasNoTagsToRemove(task)) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_NO_TAGS;
            return true;
        }
        if (hasRemoveEndDate()) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_REMOVE_END;
            return true;
        }
        if (hasRemoveStartDate()) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_REMOVE_START;
            return true;
        }
        if (_removeReservedSlotIndex != null) {
            if (!_removeReservedSlotIndex.isEmpty()) {
                if (hasInvalidIndices(task)) {
                    _msgEdit = GlobalConstants.MESSAGE_ERROR_INVALID_TIMESLOT;
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

    /**
     * this method checks for any invalid parameters when user has specified to
     * edit a floating task
     */
    private boolean isInvalidEditFloat(TaskFloat task) {
        if (hasNoLocationToRemove(task)) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_NO_LOCATION;
            return true;
        }
        if (hasNoTagsToRemove(task)) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_NO_TAGS;
            return true;
        }
        if ((hasAddStartDate()) && (!hasAddEndDate())) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_ADD_START_DATE_ONLY;
            return true;
        }

        return false;
    }

    /**
     * this method checks for any invalid parameters when user has specified to
     * edit an event task
     */
    private boolean isInvalidEditEvent(TaskEvent task) {
        if (hasNoLocationToRemove(task)) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_NO_LOCATION;
            return true;
        }
        if (hasNoTagsToRemove(task)) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_NO_TAGS;
            return true;
        }
        if ((hasRemoveEndDate()) && (!hasRemoveStartDate())) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_REMOVE_END_DATE_ONLY;
            return true;

        }
        if ((getStartDate() != null) && (getEndDate() != null)) {
            if ((getStartDate().getTimeInMillis() != 0) && (getEndDate().getTimeInMillis() != 0)) {
                if (getStartDate().after(getEndDate())) {
                    _msgEdit = GlobalConstants.MESSAGE_ERROR_START_AFTER_END;
                    return true;
                }
            }
        }
        if ((hasAddStartDate()) && (!hasAddEndDate())) {
            if (isStartAfterEnd(task)) {
                _msgEdit = GlobalConstants.MESSAGE_ERROR_START_AFTER_END;
                return true;
            }
        }

        return false;
    }

    private boolean isStartAfterEnd(TaskEvent task) {
        if (getStartDate().after(task.getEndDate())) {
            return true;
        } else if (isChangeTimeOnly(getStartDate())) {
            Calendar start = Calendar.getInstance();
            start.setTimeInMillis(task.getStartDate().getTimeInMillis());
            start.set(Calendar.HOUR_OF_DAY, getStartDate().get(Calendar.HOUR_OF_DAY));
            start.set(Calendar.MINUTE, getStartDate().get(Calendar.MINUTE));
            if (start.after(task.getEndDate())) {
                return true;
            }
        }
        return false;
    }

    private boolean isChangeTimeOnly(Calendar time) {
        return (time.get(Calendar.YEAR) == 1);
    }

    /**
     * this method checks for any invalid parameters when user has specified to
     * edit a deadline task
     */
    private boolean isInvalidEditDeadline(TaskDeadline task) {
        if (hasNoLocationToRemove(task)) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_NO_LOCATION;
            return true;
        }
        if (hasNoTagsToRemove(task)) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_NO_TAGS;
            return true;
        }
        if ((hasRemoveStartDate())) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_REMOVE_START;
            return true;
        }
        if ((hasRemoveEndDate()) && (hasAddStartDate())) {
            _msgEdit = GlobalConstants.MESSAGE_ERROR_ADD_START_REMOVE_END;
            return true;

        }

        return false;
    }

    @SuppressWarnings("unused")
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

    /**
     * checks if there is a conversion of task type from deadline to event/float
     * and converts accordingly
     */
    private boolean changeDeadlineTaskType(TaskDeadline task) {
        // assertFalse endDate==0 AND startDate != 0&null
        boolean hasTaskChanged = false;
        if (isConvertDeadlineToFloat()) {
            _logger.log(Level.INFO, "Update: Convert deadline to float");
            _display.getVisibleDeadlineTasks().remove(_taskNumber - 1);
            _display.getDeadlineTasks().remove(task);
            hasTaskChanged = convertDeadlineToFloat(task);
        }
        if (isConvertDeadlineToEvent(hasTaskChanged)) {
            _logger.log(Level.INFO, "Update: Convert deadline to event");
            _display.getVisibleDeadlineTasks().remove(_taskNumber - 1);
            _display.getDeadlineTasks().remove(task);
            convertDeadlineToEvent(task);
            hasTaskChanged = true;
        }
        if (!hasTaskChanged) {
            setTaskIndices(task);
        }
        return hasTaskChanged;
    }

    /**
     * sets the task indices array for UI to use in the animation
     */
    private void setTaskIndices(Task task) {
        ArrayList<Integer> indices = new ArrayList<Integer>();
        indices.add(getIndex(task));
        _display.setTaskIndices(indices);
        return;
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
                _display.getVisibleEvents().remove(task);
                _display.getEventTasks().remove(task);
                if (getStartDate().get(Calendar.YEAR) == 1) {
                    task.getStartDate().set(Calendar.HOUR_OF_DAY, getStartDate().get(Calendar.HOUR_OF_DAY));
                    task.getStartDate().set(Calendar.MINUTE, getStartDate().get(Calendar.MINUTE));
                    _timeChanged = true;
                } else {
                    task.setStartDate(getStartDate());
                    _timeChanged = true;
                }
                new CommandAddEvent(task).execute(_display);
            }
        }
        resetOverdueStatus(task);
        return;
    }

    private void resetOverdueStatus(TaskEvent task) {
        if (task.getEndDate().before(Calendar.getInstance())) {
            task.setIsOverdue(true);
        } else {
            task.setIsOverdue(false);
        }
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
        resetOverdueStatus(task);
        return;
    }

    private void editEndDate(TaskDeadline task) {
        if (getEndDate() != null) {
            if (getEndDate().getTimeInMillis() != 0) {
                _display.getVisibleDeadlineTasks().remove(task);
                _display.getDeadlineTasks().remove(task);
                if (getEndDate().get(Calendar.YEAR) == 1) {
                    task.getEndDate().set(Calendar.HOUR_OF_DAY, getEndDate().get(Calendar.HOUR_OF_DAY));
                    task.getEndDate().set(Calendar.MINUTE, getEndDate().get(Calendar.MINUTE));
                } else {
                    task.setEndDate(getEndDate());
                }
                new CommandAddDeadlineTask(task).execute(_display);
            }
        }
        resetOverdueStatus(task);
        return;
    }

    private void resetOverdueStatus(TaskDeadline task) {
        if (task.getEndDate().before(Calendar.getInstance())) {
            task.setIsOverdue(true);
        } else {
            task.setIsOverdue(false);
        }
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

    /**
     * checks if there is a conversion of task type from event to deadline/float
     * and converts accordingly
     */
    private void changeEventTaskType(TaskEvent task) {
        boolean hasTaskChanged = false;
        if (isConvertEventToFloat()) {
            _logger.log(Level.INFO, "Update: Convert event to float");
            _display.getVisibleEvents().remove(task);
            _display.getEventTasks().remove(task);
            convertEventToFloat(task);
            hasTaskChanged = true;
        }
        if (isConvertEventToDeadline(hasTaskChanged)) {
            _logger.log(Level.INFO, "Update: Convert event to deadline");
            _display.getVisibleEvents().remove(task);
            _display.getEventTasks().remove(task);
            convertEventToDeadline(task);
            hasTaskChanged = true;
        }
        if (!hasTaskChanged) {
            setTaskIndices(task);
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

    /**
     * checks if there is a conversion of task type from float to event/deadline
     * and converts accordingly
     */
    private boolean hasChangeFloatTaskType(TaskFloat task) {
        boolean hasTaskChanged = false;
        if (isChangeFloatToEvent()) {
            _logger.log(Level.INFO, "Update: Convert float to event");
            convertFloatToEvent(task);
            hasTaskChanged = true;
        } else {
            if (isChangeFloatToDeadline()) {
                _logger.log(Level.INFO, "Update: Convert float to deadline");
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
