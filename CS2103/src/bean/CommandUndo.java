/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 3:00am
 * CS2103
 */

package bean;

import History.History;

public class CommandUndo implements Command {
	private final String MESSAGE_UNDO = "Undid last command";
	private final String MESSAGE_ERROR_UNDO = "You have reached the earliest point possible";
	private boolean updateFile = true;
	private boolean saveHistory = false;

	public CommandUndo() {
	}

	public Display execute(Display display) {
		if (History.atFirstState()) {
			updateFile = false;
			display.setMessage(MESSAGE_ERROR_UNDO);
			return display;
			// return (new Display(MESSAGE_ERROR_UNDO));
		}

		Display prevDisplay = History.getDisplay(-1);
		display = prevDisplay.deepClone();
		
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
