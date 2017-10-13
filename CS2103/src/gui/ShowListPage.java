package gui;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Display;
import entity.Task;
import entity.TaskDeadline;
import entity.TaskEvent;
import entity.TaskFloat;
import entity.TaskReserved;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import netscape.javascript.JSObject;

/**
 * @@author A0149527W
 */
public class ShowListPage extends AppPage {

    // These are the html path, file name, and bridge name for showList
    private static final String PATH_HTML = "/view/html/list.html";

    // This is the format of date and time displaying
    private static final String FORMAT_DATE = "EEE MM/dd HH:mm";

    // These are the JavaScript names of funcitons
    private static final String SCRIPT_RESET = "reset()";
    private static final String SCRIPT_SETFOCUS = "setFocus";
    private static final String SCRIPT_ANIMATION_CONFLICT = "showConflict";
    private static final String SCRIPT_ANIMATION_ADD = "addShowAnimation";
    private static final String SCRIPT_ADD_TASKS = "addTasks";
    private static final String SCRIPT_ADD_RESERVED = "addReservedTask";
    private static final String SCRIPT_ANIMATION_HIDE = "addHideAnimation";
    private static final String SCRIPT_CLEAR_CMD = "clearCommandLine";
    private static final String SCRIPT_HIDE_SUGGESTION = "hideSuggestion";
    private static final String SCRIPT_SHOW_FEEDBACK = "showFeedBack";

    // These are the name for different task types
    private static final String TASKTYPE_EVENT = "event";
    private static final String TASKTYPE_FLOAT = "floating";
    private static final String TASKTYPE_DEADLINE = "deadline";
    private static final String TASKTYPE_COMPLETED = "completed";

    // These are the JSON keys
    private static final String JSONKEY_STARTDATE = "startDate";
    private static final String JSONKEY_ENDDATE = "endDate";
    private static final String JSONKEY_STARTDATES = "startDates";
    private static final String JSONKEY_ENDDATES = "endDates";

    // These are command types used in animation
    private static final String CMDTYPE_ADD = "Add";
    private static final String CMDTYPE_DELETE = "Delete";

    // the display showing in page
    private Display display;

    // JSObeject to call Javascript
    private JSObject win;

    public ShowListPage(Display display) {
        // super class AppPage
        super(PATH_HTML);

        this.display = display;
        addLoadListener();
    }

    /**
     * add load listener to webengine
     */
    private void addLoadListener() {
        webEngine
                .getLoadWorker()
                .stateProperty()
                .addListener(
                        (ObservableValue<? extends State> ov, State oldState, State newState) -> {

                            // explicitly load web page again if loadworker
                            // failed
                            if (newState == Worker.State.FAILED) {
                                webEngine.load(WelcomePage.class.getResource(this.html)
                                        .toExternalForm());
                            }

                            // add content when load successful
                            if (newState == Worker.State.SUCCEEDED) {
                                setContent();
                            }
                        });
    }

    /**
     * set web page's content
     */
    private void setContent() {
        // conmunicate java and Javascript
        this.win = (JSObject) webEngine.executeScript(SCRIPT_WINDOW);
        win.setMember(NAME_BRIDGE, JSBridge.getInstance());

        resetTaskNumber();

        // construct JSON to pass to JS and call add
        // task
        addTasks();

        setMessageAndAnimation();
    }

    /**
     * set message and animation in display
     */
    private void setMessageAndAnimation() {
        setMessage();

        JSONArray jsonFocus = constructFocusArray();

        setAddAnimation(jsonFocus);
        setFocusAnimation(jsonFocus);
        setConflictAnimation();
    }

    /**
     * add different sorts of tasks
     */
    private void addTasks() {
        addCompleted();
        addDeadline();
        addEvent();
        addFloat();
        addReserved();
    }

    /**
     * reset task number
     */
    private void resetTaskNumber() {
        webEngine.executeScript(SCRIPT_RESET);
    }

    /**
     * set focus animation
     * 
     * @param jsonFocus
     */
    private void setFocusAnimation(JSONArray jsonFocus) {
        win.call(SCRIPT_SETFOCUS, jsonFocus);
    }

    /**
     * set conflict animation
     */
    private void setConflictAnimation() {
        JSONArray jsonConflict = new JSONArray();
        for (int i = 0; i < this.display.getConflictingTasksIndices().size(); i++) {
            jsonConflict.put(this.display.getConflictingTasksIndices().get(i));
        }
        win.call(SCRIPT_ANIMATION_CONFLICT, jsonConflict);
    }

    /**
     * set add animation
     * 
     * @param jsonFocus
     */
    private void setAddAnimation(JSONArray jsonFocus) {
        if (this.display.getCommandType() == CMDTYPE_ADD) {
            win.call(SCRIPT_ANIMATION_ADD, jsonFocus);
        }
    }

    /**
     * construct JSONArray for completed tasks and call js
     */
    private void addCompleted() {
        if (this.display.getVisibleCompletedTasks() != null
                && this.display.getVisibleDeadlineTasks().size() == 0
                && this.display.getVisibleEvents().size() == 0
                && this.display.getVisibleFloatTasks().size() == 0
                && this.display.getVisibleReservedTasks().size() == 0) {

            List<Task> completeds = this.display.getVisibleCompletedTasks();

            // construct JSONArray
            JSONArray jsonTask = new JSONArray();

            for (Task completed : completeds) {
                JSONObject task = constructJSON(completed);
                jsonTask.put(task);
            }

            // call Javascript
            win.call(SCRIPT_ADD_TASKS, jsonTask, TASKTYPE_COMPLETED);
        }
    }

    /**
     * construct JSONArray for reserved tasks and call js
     */
    private void addReserved() {

        if (this.display.getVisibleReservedTasks() != null) {

            List<TaskReserved> reservations = this.display.getVisibleReservedTasks();

            // construct JSONArray
            JSONArray jsonReserved = new JSONArray();

            for (TaskReserved reserved : reservations) {
                JSONObject task = new JSONObject(reserved);

                // remove original dates from JSON
                task.remove(JSONKEY_STARTDATES);
                task.remove(JSONKEY_ENDDATES);

                // add dates in format to JSON
                addDates(reserved, task);

                jsonReserved.put(task);
            }

            // call Javascript
            win.call(SCRIPT_ADD_RESERVED, jsonReserved);
        }
    }

    /**
     * add dates to JSONObject for a reserved task
     * 
     * @param reserved
     * @param task
     */
    private void addDates(TaskReserved reserved, JSONObject task) {
        for (int i = 0; i < reserved.getStartDates().size(); i++) {
            try {
                // append endDates
                task.append(
                        JSONKEY_ENDDATES,
                        new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH).format(reserved
                                .getEndDates().get(i).getTime()));

                // append startDates
                task.append(
                        JSONKEY_STARTDATES,
                        new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH).format(reserved
                                .getStartDates().get(i).getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * construct JSONArray for floating tasks and call js
     */
    private void addFloat() {
        if (this.display.getVisibleFloatTasks() != null) {
            List<TaskFloat> floatings = this.display.getVisibleFloatTasks();

            // construct JSONArray
            JSONArray jsonFloating = new JSONArray();

            for (Task floating : floatings) {
                JSONObject task = constructJSON(floating);
                jsonFloating.put(task);
            }

            // call Javascript
            win.call(SCRIPT_ADD_TASKS, jsonFloating, TASKTYPE_FLOAT);
        }
    }

    /**
     * construct JSONArray for event tasks and call js
     */
    private void addEvent() {
        if (this.display.getVisibleEvents() != null) {
            List<TaskEvent> events = this.display.getVisibleEvents();

            // construct JSONArray
            JSONArray jsonEvent = new JSONArray();

            for (TaskEvent event : events) {
                JSONObject task = constructJSON(event);
                jsonEvent.put(task);
            }

            // call Javascript
            win.call(SCRIPT_ADD_TASKS, jsonEvent, TASKTYPE_EVENT);
        }
    }

    /**
     * construct JSONArray for deadline tasks and call js
     */
    private void addDeadline() {
        if (this.display.getVisibleDeadlineTasks() != null) {

            List<TaskDeadline> deadlines = this.display.getVisibleDeadlineTasks();

            // construct JSONArray
            JSONArray jsonDeadline = new JSONArray();

            for (TaskDeadline deadline : deadlines) {
                JSONObject task = constructJSON(deadline);
                jsonDeadline.put(task);
            }

            // call Javascript
            win.call(SCRIPT_ADD_TASKS, jsonDeadline, TASKTYPE_DEADLINE);
        }
    }

    /**
     * construct single task json to pass to javascript
     * 
     * @param task
     * @return
     */
    private JSONObject constructJSON(Task completed) {
        JSONObject task = new JSONObject(completed);

        // remove original dates
        task.remove(JSONKEY_STARTDATE);
        task.remove(JSONKEY_ENDDATE);

        // add dates in format
        try {
            // add startDate if it's an event
            if (completed instanceof TaskEvent) {
                task.put(JSONKEY_STARTDATE, new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH)
                        .format(((TaskEvent) completed).getStartDate().getTime()));
            }

            // add endDates if it's a dealine or event
            if (completed instanceof TaskDeadline || completed instanceof TaskEvent) {
                task.put(JSONKEY_ENDDATE, new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH)
                        .format(((TaskDeadline) completed).getEndDate().getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    public void setList(Display display) {
        assert (display != null);

        this.display = display;

        // delete command does not need reloading
        if (this.display.getCommandType() == CMDTYPE_DELETE) {
            setDeleteAnimation();
        } else {
            webEngine.reload();
        }

    }

    /**
     * set delete animation
     */
    private void setDeleteAnimation() {
        // set message
        setMessage();

        // set focus animation
        JSONArray jsonFocus = constructFocusArray();
        setFocusAnimation(jsonFocus);

        // call Javascript
        win.call(SCRIPT_ANIMATION_HIDE, jsonFocus);
        win.call(SCRIPT_CLEAR_CMD);
        win.call(SCRIPT_HIDE_SUGGESTION);
    }

    /**
     * set message in display
     */
    private void setMessage() {
        if (this.display.getMessage() != null)
            win.call(SCRIPT_SHOW_FEEDBACK, this.display.getMessage());
    }

    /**
     * construct JSONArray from list of integers
     * 
     * @return
     */
    private JSONArray constructFocusArray() {
        JSONArray jsonFocus = new JSONArray();
        for (int i = 0; i < this.display.getTaskIndices().size(); i++) {
            jsonFocus.put(this.display.getTaskIndices().get(i));
        }
        return jsonFocus;
    }
}
