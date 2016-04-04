/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */

package bean;

import java.util.ArrayList;

import History.History;

public class CommandUndo implements Command {
    private boolean updateFile = true;
    private boolean saveHistory = false;
    private int count;

    public CommandUndo() {
        count = -1;
    }

    public CommandUndo(int count) {
        this.count = -1 * count;
    }

    public Display execute(Display display) {
        Display prevDisplay = History.getDisplay(count);
        if (prevDisplay == null) {
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
