/*
 * @@author A0139995E
 */

package bean;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import History.History;

public class CommandUndo implements Command {
    private boolean updateFile = true;
    private boolean saveHistory = false;
    private int count;
    private Logger logger = GlobalLogger.getLogger();

    public CommandUndo() {
        count = -1;
    }

    public CommandUndo(int count) {
        this.count = -1 * count;
    }

    public Display execute(Display display) {
        assert display != null: "Undo: null display";
        Display prevDisplay = History.getDisplay(count);
        if (prevDisplay == null) {
            logger.log(Level.INFO, "Undo: At last state");
            updateFile = false;
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
        return saveHistory;
    }

    public boolean requiresUpdateFile() {
        return updateFile;
    }
}
