// @@author A0139995E
package bean;

public class GlobalConstants {

    public static final String INVERTED_COMMAS = "\"";
    public static final String EMPTY_STRING = "";
    public static final String MINUTE = "minute";
    public static final String HOUR = "hour";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    
    public static final String GUI_ANIMATION_ADD = "Add";
    public static final String GUI_ANIMATION_DELETE = "Delete";
    public static final String GUI_ANIMATION_INVALID = "Invalid";
    
    public static final String MESSAGE_ADD_SUCCESS = "added: \"%1$s\"";
    public static final String MESSAGE_ALL_COMPLETED = "All shown tasks completed";
    public static final String MESSAGE_ALL_DELETED = "All tasks deleted";
    public static final String MESSAGE_ALL_UNDONE = "All tasks undone";
    public static final String MESSAGE_CHANGE_FILEPATH = "File path changed to: ";
    public static final String MESSAGE_FILE_CREATED = "File created and ready for use";
    public static final String MESSAGE_FILE_PATH_CHANGE = "File path changed to: ";
    public static final String MESSAGE_NATTY = "add this from tmr 3pm";
    public static final String MESSAGE_REDO = "Redid command(s)";
    public static final String MESSAGE_RESERVED = "Reserved: \"%1$s\"";
    public static final String MESSAGE_NO_TASKS = "No such tasks found";
    public static final String MESSAGE_POSTPONED = "Postponed: ";
    public static final String MESSAGE_START_UP = "Welcome! These are today's agenda and overdue tasks.";
    public static final String MESSAGE_SHOW_ALL = "Displaying all tasks";
    public static final String MESSAGE_UNDO = "Undid previous commands";
    
    public static final String MESSAGE_ERROR_ADD_START_DATE_ONLY = "Can't add just the start date!!";
    public static final String MESSAGE_ERROR_ADD_START_REMOVE_END = "Cant add start date and remove end date!";
    public static final String MESSAGE_ERROR_CANT_CHANGE_FILEPATH = "Can't change filePath";
    public static final String MESSAGE_ERROR_DATE_RANGE = "Please specify a valid date range";
    public static final String MESSAGE_ERROR_DESCRIPTION = "Please enter a description";
    public static final String MESSAGE_ERROR_FILE_EXISTS = "File already exists";
    public static final String MESSAGE_ERROR_CHANGE_FILE_PATH = "Can't change filePath";
    public static final String MESSAGE_ERROR_INVALID_COMMAND = "You have specified an invalid command";
    public static final String MESSAGE_ERROR_INVALID_INDEX = "Please specify a valid index";
    public static final String MESSAGE_ERROR_INVALID_TIMESLOT = "Please specify a valid time slot";
    public static final String MESSAGE_ERROR_NO_LOCATION = "No location to remove!";
    public static final String MESSAGE_ERROR_NO_NUMBER = "please specify a task index";
    public static final String MESSAGE_ERROR_NO_RESERVED_TASKS = "There are no reserved tasks";
    public static final String MESSAGE_ERROR_NO_TAGS = "No tags to remove!";
    public static final String MESSAGE_ERROR_NO_TIMESLOT = "Please specify a time slot";
    public static final String MESSAGE_ERROR_NO_VISIBLE_RESERVED_TASKS = "There are no shown reserved tasks";
    public static final String MESSAGE_ERROR_POSTPONE_INVALID_TASK_TYPES = "You can only pospone deadline tasks and events!";
    public static final String MESSAGE_ERROR_READING_FILE = "Error occured while reading file";
    public static final String MESSAGE_ERROR_REDO = "You have reached the latest point possible";
    public static final String MESSAGE_ERROR_REMOVE_END = "Can't remove end date!";
    public static final String MESSAGE_ERROR_REMOVE_END_DATE_ONLY = "Cant remove end date only!";
    public static final String MESSAGE_ERROR_REMOVE_START = "Can't remove start date!";
    public static final String MESSAGE_ERROR_START_AFTER_END = "Start date must be before end date!";
    public static final String MESSAGE_ERROR_TASK_NUMBER = "Please specify a valid task number";
    public static final String MESSAGE_ERROR_TIME_RANGE = "You have entered invalid time range(s)";
    public static final String MESSAGE_ERROR_UNDO = "You have reached the earliest point possible";
    public static final String MESSAGE_ERROR_UPDATE_FILE = "Error occured while updating to file";
    public static final String TASK_TYPE_COMPLETED = "done";
    public static final String TASK_TYPE_DEADLINE = "deadline";
    public static final String TASK_TYPE_FLOAT = "untimed";
    public static final String TASK_TYPE_EVENT = "event";
    public static final String TASK_TYPE_RESERVED = "reserved";

    public static final int TIMER_REMINDER_PERIOD = 60;
    public static final int TIMER_PERIOD = 1000;
    public static final int TIMER_DELAY = 0;
    public static final boolean IS_DAEMON_TASK = true;
}
