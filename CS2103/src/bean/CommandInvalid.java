/*
 * @@author A0139995E
 */

package bean;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandInvalid implements Command {
	private boolean updateFile = false;
	private boolean saveHistory = false;
    private Logger logger = GlobalLogger.getLogger();

	public CommandInvalid() {
	}

	public Display execute(Display display) {
        assert display != null: "Invalid: null display";
        logger.log(Level.INFO, "Invalid Command");
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
