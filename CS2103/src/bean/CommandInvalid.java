/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */

package bean;

import java.util.ArrayList;

public class CommandInvalid implements Command {
	private final String MESSAGE_INVALID_COMMAND = "You have specified an invalid command";
	private boolean updateFile = false;
	private boolean saveHistory = false;

	public CommandInvalid() {
	}

	public Display execute(Display display) {
	    setDisplay(display);
		return display;
	}

	private void setDisplay(Display display) {
        display.setMessage(MESSAGE_INVALID_COMMAND);
        display.setCommandType("Invalid");
        display.setTaskIndices(new ArrayList<Integer>());
        display.setConflictingTasksIndices(new ArrayList<Integer>());
    }
	
	public boolean getSaveHistory() {
		return saveHistory;
	}

	public boolean getUpdateFile() {
		return updateFile;
	}
}
