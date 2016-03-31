/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 3:00am
 * CS2103
 */

package bean;

import java.util.Calendar;

import History.History;

public class CommandUndo implements Command {
	private final String MESSAGE_UNDO = "Undid previous commands";
	private final String MESSAGE_ERROR_UNDO = "You have reached the earliest point possible";
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
		if(prevDisplay == null){
            updateFile = false;
            display.setMessage(MESSAGE_ERROR_UNDO);
            return display;
		}
		display = prevDisplay.deepClone();
		display.setOverdueTasks();
		display.setMessage(MESSAGE_UNDO);
		return display;
	}

	public boolean getSaveHistory() {
		return saveHistory;
	}

	public boolean getUpdateFile() {
		return updateFile;
	}
}
