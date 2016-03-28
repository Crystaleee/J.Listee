/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 3:00am
 * CS2103
 */
package bean;

import History.History;

public class CommandRedo implements Command {
	private final String MESSAGE_REDO = "Redid last command";
	private final String MESSAGE_ERROR_REDO = "You have reached the latest point possible";
	private boolean updateFile = true;
	private boolean saveHistory = false;

	public CommandRedo() {
	}

	public Display execute(Display display) {
		if (History.atLastState()) {
			updateFile = false;
			display.setMessage(MESSAGE_ERROR_REDO);
			return display;
			// return (new Display(MESSAGE_ERROR_REDO));
		}

		display = History.getDisplay(1);
		display.setMessage(MESSAGE_REDO);
		return display;
	}

	public boolean getSaveHistory() {
		return saveHistory;
	}

	public boolean getUpdateFile() {
		return updateFile;
	}
}
