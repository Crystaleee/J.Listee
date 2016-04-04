/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */

package bean;

import java.util.ArrayList;

public class CommandInvalid implements Command {
	private boolean updateFile = false;
	private boolean saveHistory = false;

	public CommandInvalid() {
	}

	public Display execute(Display display) {
	    setDisplay(display);
		return display;
	}

	private void setDisplay(Display display) {
        display.setMessage(GlobalConstants.MESSAGE_ERROR_INVALID_COMMAND);
        display.setCommandType(GlobalConstants.GUI_ANIMATION_INVALID);
        display.setTaskIndices(new ArrayList<Integer>());
        display.setConflictingTasksIndices(new ArrayList<Integer>());
    }
	
	public boolean requiresSaveHistory() {
		return saveHistory;
	}

	public boolean requiresUpdateFile() {
		return updateFile;
	}
}
