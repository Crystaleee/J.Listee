/*
 * @@author A0139995E
 */

package entity;
/**
 * This class is for any invalid commands
 */
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandInvalid implements Command {
	private boolean _updateFile = false;
	private boolean _saveHistory = false;
    private Logger _logger = GlobalLogger.getLogger();

	public CommandInvalid() {
	}

	public Display execute(Display display) {
        assert display != null: "Invalid: null display";
        _logger.log(Level.INFO, "Invalid Command");
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
		return _saveHistory;
	}

	public boolean requiresUpdateFile() {
		return _updateFile;
	}
}
