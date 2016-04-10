/*
 * @@author A0139995E
 */
package entity;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import history.History;

public class CommandRedo implements Command {
    private boolean _updateFile = true;
    private boolean _saveHistory = false;
    private int _count;
    private Logger logger = GlobalLogger.getLogger();

    public CommandRedo() {
        _count = 1;
    }

    public CommandRedo(int count) {
        this._count = count;
    }

    public Display execute(Display display) {
        assert display != null: "Redo: null display";
        Display nextDisplay = History.getDisplay(_count);
        if (nextDisplay == null) {
            logger.log(Level.INFO, "Redo: At last state");
            _updateFile = false;
            display.setMessage(GlobalConstants.MESSAGE_ERROR_REDO);
            return display;
        }
        display = setDisplay(nextDisplay);
        return display;
    }

    private Display setDisplay(Display nextDisplay) {
        Display display;
        display = nextDisplay.deepClone();
        display.setOverdueTasks();
        display.setTaskIndices(new ArrayList<Integer>());
        display.setConflictingTasksIndices(new ArrayList<Integer>());
        display.setMessage(GlobalConstants.MESSAGE_REDO);
        return display;
    }

    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}
