/*
 * @@author A0139995E
 */

package entity;

/**
 * This command is to undo previously entered
 * commands
 */
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import history.History;

public class CommandUndo implements Command {
    private boolean _updateFile = true;
    private boolean _saveHistory = false;
    private int _count;
    private Logger _logger = GlobalLogger.getLogger();

    public CommandUndo() {
        _count = -1;
    }

    public CommandUndo(int count) {
        this._count = -1 * count;
    }

    public Display execute(Display display) {
        assert display != null: "Undo: null display";
        Display prevDisplay = History.getDisplay(_count);
        if (prevDisplay == null) {
            _logger.log(Level.INFO, "Undo: At last state");
            _updateFile = false;
            display.setMessage(GlobalConstants.MESSAGE_ERROR_UNDO);
            return display;
        }
        display = setDisplay(prevDisplay);
        return display;
    }

    private Display setDisplay(Display prevDisplay) {
        Display display;
        display = prevDisplay.deepClone();
        display.setOverdueTasks();
        display.setTaskIndices(new ArrayList<Integer>());
        display.setConflictingTasksIndices(new ArrayList<Integer>());
        display.setMessage(GlobalConstants.MESSAGE_UNDO);
        return display;
    }

    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}
